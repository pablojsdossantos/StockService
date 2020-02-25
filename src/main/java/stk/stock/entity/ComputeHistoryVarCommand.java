package stk.stock.entity;

import java.time.LocalDate;

/**
 *
 * @author Pablo JS dos Santos
 */
public class ComputeHistoryVarCommand {
    private String id;
    private String stockCode;
    private LocalDate referenceDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }
}
