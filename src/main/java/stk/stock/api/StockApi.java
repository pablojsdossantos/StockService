package stk.stock.api;

import java.util.concurrent.CompletionStage;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import stk.core.api.StkRestApi;
import stk.stock.api.commands.UpdateStockCmd;

/**
 *
 * @author Pablo JS dos Santos
 */
@RequestScoped
@Path("stock")
public class StockApi extends StkRestApi {
    private StockApiService service;

    public StockApi(StockApiService service) {
        this.service = service;
    }

    @PUT
    public CompletionStage<Response> updateStock() {
        return this.actionFor(this.service::updateStock)
            .authenticated()
            .expectingJsonBody(UpdateStockCmd.class)
            .invoke();
    }
}
