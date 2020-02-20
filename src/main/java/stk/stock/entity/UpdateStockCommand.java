package stk.stock.entity;

/**
 *
 * @author Pablo JS dos Santos
 */
public class UpdateStockCommand {
    private String id;
    private String payload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
