package stk.stock.entity;

import etk.jdbc.connection.ExConnection;
import etk.jdbc.connection.Session;
import etk.jdbc.mapping.Insert;
import etk.jdbc.mapping.Sql;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockPersistenceManager {
    private ExConnection connection;

    public StockPersistenceManager(ExConnection connection) {
        this.connection = connection;
    }

    public CompletionStage<Void> save(Stock stock, StockHistory historyItem, UpdateStockCommand command) {
        return this.connection.writeAsync(session -> {
            this.insertCommand(session, command);
            this.insertHistory(session, historyItem);
            this.updateStock(session, stock, historyItem.getReferenceDate());

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

    private void updateStock(Session session, Stock stock, LocalDate referenceDate) {
        session.createStatement(""
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
            .bind("referenceDate", referenceDate)
            .execute();
    }
}
