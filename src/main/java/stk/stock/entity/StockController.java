package stk.stock.entity;

import cqk.api.parsers.GsonFactory;
import cqk.api.parsers.ParseUtils;
import io.vertx.axle.core.eventbus.EventBus;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import stk.stock.api.commands.UpdateStockCmd;
import stk.stock.messaging.StockOutputChannel;
import stk.stock.messaging.commands.ComputeHistoryVarCmd;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockController {
    private StockPersistenceManager persistenceManager;
    private EventBus eventBus;

    private Stock stock;
    private StockHistory historyItem;
    private UpdateStockCommand updateStockCommand;

    public StockController(StockPersistenceManager persistenceManager, EventBus eventBus) {
        this.persistenceManager = persistenceManager;
        this.eventBus = eventBus;
    }

    public void readUpdateCommand(UpdateStockCmd updateCommand) {
        this.instantiateStockFromCommand(updateCommand);
        this.instantiateHistoryFromCommand(updateCommand);
        this.instantiateCommandFromCommand(updateCommand);
    }

    private void instantiateStockFromCommand(UpdateStockCmd updateCommand) {
        this.stock = new Stock();
        this.stock.setCode(updateCommand.getStockCode());
        this.stock.setCategory(updateCommand.getCategory());
        this.stock.setDailyLiquidity(updateCommand.getDailyLiquidity());
        this.stock.setDividend(updateCommand.getDividend());
        this.stock.setDividendYield(updateCommand.getDividendYield());
        this.stock.setCurrentPrice(updateCommand.getCurrentPrice());
        this.stock.setRealEstateAssetCount(updateCommand.getRealEstateAssetCount());
        this.stock.setLastUpdate(ParseUtils.parseDate(updateCommand.getReferenceDate()));
    }

    private void instantiateHistoryFromCommand(UpdateStockCmd updateCommand) {
        this.historyItem = new StockHistory();
        this.historyItem.setCode(updateCommand.getStockCode());
        this.historyItem.setDailyLiquidity(updateCommand.getDailyLiquidity());
        this.historyItem.setDividend(updateCommand.getDividend());
        this.historyItem.setDividendYield(updateCommand.getDividendYield());
        this.historyItem.setPrice(updateCommand.getCurrentPrice());
        this.historyItem.setRealEstateAssetCount(updateCommand.getRealEstateAssetCount());
        this.historyItem.setReferenceDate(ParseUtils.parseDate(updateCommand.getReferenceDate()));
    }

    private void instantiateCommandFromCommand(UpdateStockCmd updateCommand) {
        this.updateStockCommand = new UpdateStockCommand();
        this.updateStockCommand.setId(updateCommand.getCmdId());
        this.updateStockCommand.setPayload(GsonFactory.createGson().toJson(updateCommand));
    }

    public CompletionStage<Void> saveUpdate() {
        return this.persistenceManager.save(this.stock, this.historyItem, this.updateStockCommand);
    }

    public void requestStockHistoryVarComputation() {
        ComputeHistoryVarCmd command = new ComputeHistoryVarCmd();
        command.setStockCode(this.stock.getCode());
        command.setReferenceDate(ParseUtils.parseDate(this.historyItem.getReferenceDate()));

        this.eventBus.publish(StockOutputChannel.COMPUTE_HISTORY_VAR, command);
    }
}
