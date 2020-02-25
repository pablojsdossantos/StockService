package stk.stock.api.commands;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author Pablo JS dos Santos
 */
public class HistoryVarCmd {
    @NotBlank
    private String cmdId;

    @NotBlank
    private String stockCode;

    @NotBlank
    private String referenceDate;

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

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
