package stk.stock.entity;

import java.time.LocalDate;

/**
 *
 * @author Pablo JS dos Santos
 */
public class Stock {
    private String code;
    private double currentPrice;
    private int dailyLiquidity;
    private double dividend;
    private double dividendYield;
    private int realEstateAssetCount;
    private String category;
    private LocalDate lastUpdate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
