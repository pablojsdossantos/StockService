package stk.stock.entity;

import cqk.api.parsers.ParseUtils;
import etk.number.Calculator;
import io.vertx.axle.core.eventbus.EventBus;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import stk.stock.api.commands.HistoryVarCmd;
import stk.stock.messaging.StockOutputChannel;
import stk.stock.messaging.commands.ComputeHistoryVarCmd;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class HistoryVarController {
    private StockPersistenceManager persistenceManager;
    private EventBus eventBus;

    private ComputeHistoryVarCommand command;

    private Optional<StockHistory> currentHistory;
    private StockHistoryVar historyVar;

    private Optional<StockHistory> nextHistory;
    private Optional<StockHistory> history1d;
    private Optional<StockHistory> history1m;
    private Optional<StockHistory> history3m;
    private Optional<StockHistory> history6m;
    private Optional<StockHistory> history1y;
    private Optional<StockHistory> history5y;

    public HistoryVarController(StockPersistenceManager persistenceManager, EventBus eventBus) {
        this.persistenceManager = persistenceManager;
        this.eventBus = eventBus;
    }

    public CompletionStage<Void> saveCommand(HistoryVarCmd command) {
        this.command = new ComputeHistoryVarCommand();
        this.command.setId(command.getCmdId());
        this.command.setStockCode(command.getStockCode());
        this.command.setReferenceDate(ParseUtils.parseDate(command.getReferenceDate()));

        return this.persistenceManager.save(this.command);
    }

    public CompletionStage<Void> loadHistoryVarComputationContext() {
        String stockCode = this.command.getStockCode();
        LocalDate referenceDate = this.command.getReferenceDate();

        return this.persistenceManager.findStockHistory(stockCode, referenceDate)
            .thenAccept(history -> this.currentHistory = history)
            .thenCompose(aVoid -> this.persistenceManager.findNextStockHistory(stockCode, referenceDate))
            .thenAccept(history -> this.nextHistory = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory1d(stockCode, referenceDate))
            .thenAccept(history -> this.history1d = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory1m(stockCode, referenceDate))
            .thenAccept(history -> this.history1m = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory3m(stockCode, referenceDate))
            .thenAccept(history -> this.history3m = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory6m(stockCode, referenceDate))
            .thenAccept(history -> this.history6m = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory1y(stockCode, referenceDate))
            .thenAccept(history -> this.history1y = history)
            .thenCompose(aVoid -> this.persistenceManager.findStockHistory5y(stockCode, referenceDate))
            .thenAccept(history -> this.history5y = history);
    }

    public void computeHistoryVar() {
        this.historyVar = new StockHistoryVar();
        this.historyVar.setStockCode(this.command.getStockCode());
        this.historyVar.setReferenceDate(this.command.getReferenceDate());

        if (this.currentHistory.isPresent()) {
            this.history1d.ifPresent(this::compute1d);
            this.history1m.ifPresent(this::compute1m);
            this.history3m.ifPresent(this::compute3m);
            this.history6m.ifPresent(this::compute6m);
            this.history1y.ifPresent(this::compute1y);
            this.history5y.ifPresent(this::compute5y);
        }
    }

    private void compute1d(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar1d(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar1dRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar1d(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar1dRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar1d(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar1dRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar1d(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar1dRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar1d(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar1dRef(pastHistory.getReferenceDate());
    }

    private void compute1m(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar1m(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar1mRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar1m(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar1mRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar1m(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar1mRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar1m(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar1mRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar1m(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar1mRef(pastHistory.getReferenceDate());
    }

    private void compute3m(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar3m(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar3mRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar3m(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar3mRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar3m(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar3mRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar3m(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar3mRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar3m(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar3mRef(pastHistory.getReferenceDate());
    }

    private void compute6m(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar6m(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar6mRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar6m(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar6mRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar6m(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar6mRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar6m(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar6mRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar6m(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar6mRef(pastHistory.getReferenceDate());
    }

    private void compute1y(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar1y(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar1yRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar1y(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar1yRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar1y(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar1yRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar1y(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar1yRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar1y(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar1yRef(pastHistory.getReferenceDate());
    }

    private void compute5y(StockHistory pastHistory) {
        StockHistory currentHistory = this.currentHistory.get();

        this.historyVar.setPriceVar5y(this.computeVar(currentHistory.getPrice(), pastHistory.getPrice()));
        this.historyVar.setPriceVar5yRef(pastHistory.getReferenceDate());

        this.historyVar.setDividendVar5y(this.computeVar(currentHistory.getDividend(), pastHistory.getDividend()));
        this.historyVar.setDividendVar5yRef(pastHistory.getReferenceDate());

        this.historyVar.setDivYieldVar5y(this.computeVar(currentHistory.getDividendYield(), pastHistory.getDividendYield()));
        this.historyVar.setDivYieldVar5yRef(pastHistory.getReferenceDate());

        this.historyVar.setLiquidityVar5y(this.computeVar(currentHistory.getDailyLiquidity(), pastHistory.getDailyLiquidity()));
        this.historyVar.setLiquidityVar5yRef(pastHistory.getReferenceDate());

        this.historyVar.setEstateVar5y(this.computeVar(currentHistory.getRealEstateAssetCount(), pastHistory.getRealEstateAssetCount()));
        this.historyVar.setEstateVar5yRef(pastHistory.getReferenceDate());
    }

    private Double computeVar(double current, double past) {
        double diff = Calculator.set(current)
            .minus(past)
            .getValue(5, RoundingMode.DOWN);

        double percent = Calculator.set(past)
            .multiply(diff)
            .divide(100)
            .getValue(5, RoundingMode.DOWN);

        if (Calculator.isInvalid(percent)) {
            return null;
        }

        return percent;
    }

    public CompletionStage<Void> saveHistoryVar() {
        return this.persistenceManager.save(this.historyVar);
    }

    public void requestNextHistoryVarComputation() {
        this.nextHistory.ifPresent(nextVar -> {
            ComputeHistoryVarCmd command = new ComputeHistoryVarCmd();
            command.setStockCode(nextVar.getCode());
            command.setReferenceDate(ParseUtils.parseDate(nextVar.getReferenceDate()));

            this.eventBus.publish(StockOutputChannel.COMPUTE_HISTORY_VAR, command);
        });
    }
}
