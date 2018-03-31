package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "portfolio_assets")
public class PortfolioAssetEntity {
    @PrimaryKey
    @NonNull
    private String id;

    private double balance;

    private int position;

    private String name;

    private String symbol;

    private int rank;

    private double price;

    @ColumnInfo(name = "volume_24h")
    private double volume24Hour;

    @ColumnInfo(name = "market_cap")
    private double marketCap;

    @ColumnInfo(name = "available_supply")
    private double availableSupply;

    @ColumnInfo(name = "total_supply")
    private double totalSupply;

    @ColumnInfo(name = "percent_change_1h")
    private double percentChange1h;

    @ColumnInfo(name = "percent_change_24h")
    private double percentChange24h;

    @ColumnInfo(name = "percent_change_7d")
    private double percentChange7d;

    @ColumnInfo(name = "last_update")
    private long lastUpdated;

    PortfolioAssetEntity(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume24Hour() {
        return volume24Hour;
    }

    public void setVolume24Hour(double volume24Hour) {
        this.volume24Hour = volume24Hour;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(double availableSupply) {
        this.availableSupply = availableSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(double percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(double percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}