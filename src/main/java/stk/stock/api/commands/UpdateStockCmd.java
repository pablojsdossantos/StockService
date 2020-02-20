package stk.stock.api.commands;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Pablo JS dos Santos
 */
public class UpdateStockCmd {
    @NotBlank
    private String cmdId;

    @NotBlank
    private String stockCode;

    @DecimalMin("0.01")
    @DecimalMax("99999.99")
    private double currentPrice;

    @Min(0)
    @Max(9_999_999)
    private int dailyLiquidity;

    @DecimalMin("0.00")
    @DecimalMax("99999.99")
    private double dividend;

    @DecimalMin("0.01")
    @DecimalMax("99999.99")
    private double dividendYield;

    @Min(0)
    @Max(1_000)
    private int realEstateAssetCount;

    @NotBlank
    private String category;

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

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getDailyLiquidity() {
        return dailyLiquidity;
    }

    public void setDailyLiquidity(int dailyLiquidity) {
        this.dailyLiquidity = dailyLiquidity;
    }

    public double getDividend() {
        return dividend;
    }

    public void setDividend(double dividend) {
        this.dividend = dividend;
    }

    public double getDividendYield() {
        return dividendYield;
    }

    public void setDividendYield(double dividendYield) {
        this.dividendYield = dividendYield;
    }

    public int getRealEstateAssetCount() {
        return realEstateAssetCount;
    }

    public void setRealEstateAssetCount(int realEstateAssetCount) {
        this.realEstateAssetCount = realEstateAssetCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(String referenceDate) {
        this.referenceDate = referenceDate;
    }
}
