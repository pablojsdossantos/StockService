package stk.stock.entity;

import cqk.api.parsers.GsonFactory;
import cqk.api.parsers.ParseUtils;
import java.time.Instant;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import stk.stock.api.commands.UpdateStockCmd;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockController {
    private StockPersistenceManager persistenceManager;
    private Stock stock;
    private StockHistory historyItem;
    private UpdateStockCommand command;

    public StockController(StockPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
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
        this.stock.setLastUpdate(Instant.now());
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
        this.command = new UpdateStockCommand();
        this.command.setId(updateCommand.getCmdId());
        this.command.setPayload(GsonFactory.createGson().toJson(updateCommand));
    }

    public CompletionStage<Void> saveUpdate() {
        return this.persistenceManager.save(this.stock, this.historyItem, this.command);
    }
}
