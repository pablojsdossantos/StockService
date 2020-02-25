package stk.stock.messaging.commands;

/**
 *
 * @author Pablo JS dos Santos
 */
public class ComputeHistoryVarCmd {
    private String stockCode;
    private String referenceDate;

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }
}
