package stk.stock.entity;

import etk.jdbc.connection.ExConnection;
import etk.jdbc.connection.Session;
import etk.jdbc.mapping.Insert;
import etk.jdbc.mapping.RowMapper;
import etk.jdbc.mapping.Sql;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockPersistenceManager {
    private static final String SELECT_STOCK_HISTORY
        = "SELECT h.stock_code,"
        + "       h.reference_date,"
        + "       h.price,"
        + "       h.daily_liquidity,"
        + "       h.dividend,"
        + "       h.dividend_yield,"
        + "       h.real_estate_asset_count"
        + "  FROM stock_history h";

    private static final RowMapper<StockHistory> STOCK_HISTORY_MAPPER = rs -> {
        StockHistory history = new StockHistory();
        history.setCode(rs.getString("stock_code"));
        history.setReferenceDate(rs.getLocalDate("reference_date"));
        history.setDailyLiquidity(rs.getInt("daily_liquidity"));
        history.setPrice(rs.getDouble("price"));
        history.setDividend(rs.getDouble("dividend"));
        history.setDividendYield(rs.getDouble("dividend_yield"));
        history.setRealEstateAssetCount(rs.getInt("real_estate_asset_count"));

        return history;
    };

    private ExConnection connection;

    public StockPersistenceManager(ExConnection connection) {
        this.connection = connection;
    }

    public CompletionStage<Void> save(Stock stock, StockHistory historyItem, UpdateStockCommand command) {
        return this.connection.writeAsync(session -> {
            this.insertCommand(session, command);
            this.insertHistory(session, historyItem);
            this.updateStock(session, stock);

            return null;
        });
    }

    private void insertCommand(Session session, UpdateStockCommand command) {
        Sql sql = Insert.ignoreInto("update_stock_cmd")
            .value("id", command.getId())
            .value("payload", command.getPayload())
            .build();

        long updatedRowsCount = session.executeUpdate(sql, false);

        if (updatedRowsCount == 0) {
            throw new DuplicatedCommandException("Dupliicated command with ID = " + command.getId());
        }
    }

    private void insertHistory(Session session, StockHistory historyItem) {
        Sql sql = Insert.into("stock_history")
            .value("stock_code", historyItem.getCode())
            .value("reference_date", historyItem.getReferenceDate())
            .value("price", historyItem.getPrice())
            .value("daily_liquidity", historyItem.getDailyLiquidity())
            .value("dividend", historyItem.getDividend())
            .value("dividend_yield", historyItem.getDividendYield())
            .value("real_estate_asset_count", historyItem.getRealEstateAssetCount())
            .onDuplicate()
            .update("price")
            .update("daily_liquidity")
            .update("dividend")
            .update("dividend_yield")
            .update("real_estate_asset_count")
            .build();

        session.executeUpdate(sql, false);
    }

    private void updateStock(Session session, Stock stock) {
        long updatedRowsCount = session.createStatement(""
            + "UPDATE stock"
            + "   SET current_price = ${price},"
            + "       daily_liquidity = ${liquidity},"
            + "       dividend = ${dividend},"
            + "       dividend_yield = ${dividendYield},"
            + "       real_estate_asset_count = ${assetCount},"
            + "       category = ${category},"
            + "       last_update = ${lastUpdate}"
            + " WHERE stock_code = ${stockCode}"
            + "   AND last_update <= ${referenceDate}")
            .bind("price", stock.getCurrentPrice())
            .bind("liquidity", stock.getDailyLiquidity())
            .bind("dividend", stock.getDividend())
            .bind("dividendYield", stock.getDividendYield())
            .bind("assetCount", stock.getRealEstateAssetCount())
            .bind("category", stock.getCategory())
            .bind("lastUpdate", stock.getLastUpdate())
            .bind("stockCode", stock.getCode())
            .bind("referenceDate", stock.getLastUpdate())
            .execute();

        if (updatedRowsCount == 0) {
            this.insertIgnoreStock(session, stock);
        }
    }

    private void insertIgnoreStock(Session session, Stock stock) {
        Sql sql = Insert.ignoreInto("stock")
            .value("stock_code", stock.getCode())
            .value("category", stock.getCategory())
            .value("current_price", stock.getCurrentPrice())
            .value("daily_liquidity", stock.getDailyLiquidity())
            .value("dividend", stock.getDividend())
            .value("dividend_yield", stock.getDividendYield())
            .value("real_estate_asset_count", stock.getRealEstateAssetCount())
            .value("last_update", stock.getLastUpdate())
            .build();

        session.executeUpdate(sql, false);
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory(String stockCode, LocalDate referenceDate) {
        return this.connection.readAsync(session ->
            session.<StockHistory>createQuery(SELECT_STOCK_HISTORY
                + " WHERE h.stock_code = ${code}"
                + "   AND h.reference_date = ${date}")
                .bind("code", stockCode)
                .bind("date", referenceDate)
                .map(STOCK_HISTORY_MAPPER)
                .findFirst());
    }

    public CompletionStage<Optional<StockHistory>> findNextStockHistory(String stockCode, LocalDate referenceDate) {
        return this.connection.readAsync(session ->
            session.<StockHistory>createQuery(SELECT_STOCK_HISTORY
                + " WHERE h.stock_code = ${code}"
                + "   AND h.reference_date = (SELECT MIN(s.reference_date)"
                + "                             FROM stock_history s"
                + "                            WHERE s.stock_code = h.stock_code "
                + "                              AND s.reference_date > ${date})")
                .bind("code", stockCode)
                .bind("date", referenceDate)
                .map(STOCK_HISTORY_MAPPER)
                .findFirst());
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory1d(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusDays(1));
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory1m(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusMonths(1));
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory3m(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusMonths(3));
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory6m(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusMonths(6));
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory1y(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusYears(1));
    }

    public CompletionStage<Optional<StockHistory>> findStockHistory5y(String stockCode, LocalDate referenceDate) {
        return this.findPastStockHistory(stockCode, referenceDate.minusYears(5));
    }

    public CompletionStage<Optional<StockHistory>> findPastStockHistory(String stockCode, LocalDate date) {
        return this.connection.readAsync(session ->
            session.<StockHistory>createQuery(SELECT_STOCK_HISTORY
                + " WHERE h.stock_code = ${code}"
                + "   AND h.reference_date = (SELECT MAX(s.reference_date)"
                + "                             FROM stock_history s"
                + "                            WHERE s.stock_code = h.stock_code "
                + "                              AND s.reference_date <= ${date})")
                .bind("code", stockCode)
                .bind("date", date)
                .map(STOCK_HISTORY_MAPPER)
                .findFirst());
    }

    public CompletionStage<Void> save(ComputeHistoryVarCommand command) {
        return this.connection.writeAsync(session -> {
            Sql sql = Insert.ignoreInto("compute_hisstory_cmd")
                .value("id", command.getId())
                .value("stock_code", command.getStockCode())
                .value("reference_date", command.getReferenceDate())
                .build();

            long updatedRowsCount = session.executeUpdate(sql, false);

            if (updatedRowsCount == 0) {
                throw new DuplicatedCommandException("Duplicated command with ID = " + command.getId());
            }

            return null;
        });
    }

    public CompletionStage<Void> save(StockHistoryVar entity) {
        return this.connection.writeAsync(session -> {
            Sql sql = Insert.into("stock_history_var")
                .value("stock_code", entity.getStockCode())
                .value("reference_date", entity.getReferenceDate())
                .value("price_var_1d", entity.getPriceVar1d())
                .value("price_var_1d_ref", entity.getPriceVar1dRef())
                .value("price_var_1m", entity.getPriceVar1m())
                .value("price_var_1m_ref", entity.getPriceVar1mRef())
                .value("price_var_3m", entity.getPriceVar3m())
                .value("price_var_3m_ref", entity.getPriceVar3mRef())
                .value("price_var_6m", entity.getPriceVar6m())
                .value("price_var_6m_ref", entity.getPriceVar6mRef())
                .value("price_var_1y", entity.getPriceVar1y())
                .value("price_var_1y_ref", entity.getPriceVar1yRef())
                .value("price_var_5y", entity.getPriceVar5y())
                .value("price_var_5y_ref", entity.getPriceVar5yRef())
                .value("liquidity_var_1d", entity.getLiquidityVar1d())
                .value("liquidity_var_1d_ref", entity.getLiquidityVar1dRef())
                .value("liquidity_var_1m", entity.getLiquidityVar1m())
                .value("liquidity_var_1m_ref", entity.getLiquidityVar1mRef())
                .value("liquidity_var_3m", entity.getLiquidityVar3m())
                .value("liquidity_var_3m_ref", entity.getLiquidityVar3mRef())
                .value("liquidity_var_6m", entity.getLiquidityVar6m())
                .value("liquidity_var_6m_ref", entity.getLiquidityVar6mRef())
                .value("liquidity_var_1y", entity.getLiquidityVar1y())
                .value("liquidity_var_1y_ref", entity.getLiquidityVar1yRef())
                .value("liquidity_var_5y", entity.getLiquidityVar5y())
                .value("liquidity_var_5y_ref", entity.getLiquidityVar5yRef())
                .value("dividend_var_1d", entity.getDividendVar1d())
                .value("dividend_var_1d_ref", entity.getDividendVar1dRef())
                .value("dividend_var_1m", entity.getDividendVar1m())
                .value("dividend_var_1m_ref", entity.getDividendVar1mRef())
                .value("dividend_var_3m", entity.getDividendVar3m())
                .value("dividend_var_3m_ref", entity.getDividendVar3mRef())
                .value("dividend_var_6m", entity.getDividendVar6m())
                .value("dividend_var_6m_ref", entity.getDividendVar6mRef())
                .value("dividend_var_1y", entity.getDividendVar1y())
                .value("dividend_var_1y_ref", entity.getDividendVar1yRef())
                .value("dividend_var_5y", entity.getDividendVar5y())
                .value("dividend_var_5y_ref", entity.getDividendVar5yRef())
                .value("div_yield_var_1d", entity.getDivYieldVar1d())
                .value("div_yield_var_1d_ref", entity.getDivYieldVar1dRef())
                .value("div_yield_var_1m", entity.getDivYieldVar1m())
                .value("div_yield_var_1m_ref", entity.getDivYieldVar1mRef())
                .value("div_yield_var_3m", entity.getDivYieldVar3m())
                .value("div_yield_var_3m_ref", entity.getDivYieldVar3mRef())
                .value("div_yield_var_6m", entity.getDivYieldVar6m())
                .value("div_yield_var_6m_ref", entity.getDivYieldVar6mRef())
                .value("div_yield_var_1y", entity.getDivYieldVar1y())
                .value("div_yield_var_1y_ref", entity.getDivYieldVar1yRef())
                .value("div_yield_var_5y", entity.getDivYieldVar5y())
                .value("div_yield_var_5y_ref", entity.getDivYieldVar5yRef())
                .value("estate_var_1d", entity.getEstateVar1d())
                .value("estate_var_1d_ref", entity.getEstateVar1dRef())
                .value("estate_var_1m", entity.getEstateVar1m())
                .value("estate_var_1m_ref", entity.getEstateVar1mRef())
                .value("estate_var_3m", entity.getEstateVar3m())
                .value("estate_var_3m_ref", entity.getEstateVar3mRef())
                .value("estate_var_6m", entity.getEstateVar6m())
                .value("estate_var_6m_ref", entity.getEstateVar6mRef())
                .value("estate_var_1y", entity.getEstateVar1y())
                .value("estate_var_1y_ref", entity.getEstateVar1yRef())
                .value("estate_var_5y", entity.getEstateVar5y())
                .value("estate_var_5y_ref", entity.getEstateVar5yRef())
                .onDuplicate()
                .update("price_var_1d")
                .update("price_var_1d_ref")
                .update("price_var_1m")
                .update("price_var_1m_ref")
                .update("price_var_3m")
                .update("price_var_3m_ref")
                .update("price_var_6m")
                .update("price_var_6m_ref")
                .update("price_var_1y")
                .update("price_var_1y_ref")
                .update("price_var_5y")
                .update("price_var_5y_ref")
                .update("liquidity_var_1d")
                .update("liquidity_var_1d_ref")
                .update("liquidity_var_1m")
                .update("liquidity_var_1m_ref")
                .update("liquidity_var_3m")
                .update("liquidity_var_3m_ref")
                .update("liquidity_var_6m")
                .update("liquidity_var_6m_ref")
                .update("liquidity_var_1y")
                .update("liquidity_var_1y_ref")
                .update("liquidity_var_5y")
                .update("liquidity_var_5y_ref")
                .update("dividend_var_1d")
                .update("dividend_var_1d_ref")
                .update("dividend_var_1m")
                .update("dividend_var_1m_ref")
                .update("dividend_var_3m")
                .update("dividend_var_3m_ref")
                .update("dividend_var_6m")
                .update("dividend_var_6m_ref")
                .update("dividend_var_1y")
                .update("dividend_var_1y_ref")
                .update("dividend_var_5y")
                .update("dividend_var_5y_ref")
                .update("div_yield_var_1d")
                .update("div_yield_var_1d_ref")
                .update("div_yield_var_1m")
                .update("div_yield_var_1m_ref")
                .update("div_yield_var_3m")
                .update("div_yield_var_3m_ref")
                .update("div_yield_var_6m")
                .update("div_yield_var_6m_ref")
                .update("div_yield_var_1y")
                .update("div_yield_var_1y_ref")
                .update("div_yield_var_5y")
                .update("div_yield_var_5y_ref")
                .update("estate_var_1d")
                .update("estate_var_1d_ref")
                .update("estate_var_1m")
                .update("estate_var_1m_ref")
                .update("estate_var_3m")
                .update("estate_var_3m_ref")
                .update("estate_var_6m")
                .update("estate_var_6m_ref")
                .update("estate_var_1y")
                .update("estate_var_1y_ref")
                .update("estate_var_5y")
                .update("estate_var_5y_ref")
                .build();

            session.executeUpdate(sql, false);

            return null;
        });
    }
}
