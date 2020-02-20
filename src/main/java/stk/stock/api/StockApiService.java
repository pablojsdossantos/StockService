package stk.stock.api;

import cqk.api.ApiRequest;
import cqk.api.ApiResponse;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import stk.stock.entity.StockController;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
public class StockApiService {
    private StockController controller;

    public CompletionStage<ApiResponse> updateStock(ApiRequest request) {

        return null;
    }
}
