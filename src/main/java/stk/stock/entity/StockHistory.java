package stk.stock.entity;

import java.time.LocalDate;

/**
 *
 * @author Pablo JS dos Santos
 */
public class StockHistory {
    private String code;
    private LocalDate referenceDate;
    private double price;
    private int dailyLiquidity;
    private double dividend;
    private double dividendYield;
    private int realEstateAssetCount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
}
