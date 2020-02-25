package stk.stock.api;

import cqk.api.ApiRequest;
import cqk.api.ApiResponse;
import etk.exceptions.ExceptionUtils;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import stk.stock.api.commands.HistoryVarCmd;
import stk.stock.api.commands.UpdateStockCmd;
import stk.stock.entity.DuplicatedCommandException;
import stk.stock.entity.HistoryVarController;
import stk.stock.entity.StockController;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockApiService {
    private StockController stockController;
    private HistoryVarController historyVarController;

    public StockApiService(StockController stockController, HistoryVarController historyVarController) {
        this.stockController = stockController;
        this.historyVarController = historyVarController;
    }

    public CompletionStage<ApiResponse> updateStock(ApiRequest request) {
        UpdateStockCmd command = request.getRequestBody();
        this.stockController.readUpdateCommand(command);

        return this.stockController.saveUpdate()
            .thenAccept(aVoid -> stockController.requestStockHistoryVarComputation())
            .exceptionally(this::handlingException)
            .thenApply(ApiResponse::success);
    }

    private Void handlingException(Throwable throwable) {
        if (!ExceptionUtils.isCausedBy(throwable, DuplicatedCommandException.class)) {
            ExceptionUtils.throwAgain(throwable);
        }

        return null;
    }

    public CompletionStage<ApiResponse> computeHistoryVar(ApiRequest request) {
        HistoryVarCmd command = request.getRequestBody();

        return this.historyVarController.saveCommand(command)
            .thenCompose(aVoid -> this.historyVarController.loadHistoryVarComputationContext())
            .thenAccept(aVoid -> this.historyVarController.computeHistoryVar())
            .thenCompose(aVoid -> this.historyVarController.saveHistoryVar())
            .thenAccept(aVoid -> this.historyVarController.requestNextHistoryVarComputation())
            .exceptionally(this::handlingException)
            .thenApply(ApiResponse::success);
    }
}
