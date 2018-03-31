package personal.calebcordell.coinnection.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 *
 */
@Entity(tableName = "global_market_data")
public class GlobalMarketData {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "total_market_cap")
    private double totalMarketCap; //total market capitalization of all active assets

    @ColumnInfo(name = "total_volume_24h")
    private double totalVolume24hour; //total

    @ColumnInfo(name = "bitcoin_percentage_of_market_cap")
    private double bitcoinPercentageOfMarketCap; //bitcoin's market cap/total market cap

    @ColumnInfo(name = "altcoin_percentage_of_market_cap")
    private double altcoinPercentageOfMarketCap; //total of all other assets market caps/total market cap

    @ColumnInfo(name = "active_currencies")
    private int activeCurrencies; //

    @ColumnInfo(name = "active_assets")
    private int activeAssets; //

    @ColumnInfo(name = "active_markets")
    private int activeMarkets; //

    @ColumnInfo(name = "last_update")
    private long lastUpdated;

    public GlobalMarketData() {
        this.id = "1";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public double getTotalMarketCap() {
        return totalMarketCap;
    }

    public void setTotalMarketCap(double totalMarketCap) {
        this.totalMarketCap = totalMarketCap;
    }

    public double getTotalVolume24hour() {
        return totalVolume24hour;
    }

    public void setTotalVolume24hour(double totalVolume24hour) {
        this.totalVolume24hour = totalVolume24hour;
    }

    public double getBitcoinPercentageOfMarketCap() {
        return bitcoinPercentageOfMarketCap;
    }

    public void setBitcoinPercentageOfMarketCap(double percentOfMarketCapBitcoin) {
        this.bitcoinPercentageOfMarketCap = percentOfMarketCapBitcoin;
        this.altcoinPercentageOfMarketCap = 100.0 - percentOfMarketCapBitcoin;
    }

    public double getAltcoinPercentageOfMarketCap() {
        return altcoinPercentageOfMarketCap;
    }

    public void setAltcoinPercentageOfMarketCap(double percentOfMarketCapAlts) {
        this.altcoinPercentageOfMarketCap = percentOfMarketCapAlts;
    }

    public int getActiveCurrencies() {
        return activeCurrencies;
    }

    public void setActiveCurrencies(int activeCurrencies) {
        this.activeCurrencies = activeCurrencies;
    }

    public int getActiveAssets() {
        return activeAssets;
    }

    public void setActiveAssets(int activeAssets) {
        this.activeAssets = activeAssets;
    }

    public int getActiveMarkets() {
        return activeMarkets;
    }

    public void setActiveMarkets(int activeMarkets) {
        this.activeMarkets = activeMarkets;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}