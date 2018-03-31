package personal.calebcordell.coinnection.data.globalmarketdata;

import com.google.gson.annotations.SerializedName;


public class GlobalMarketDataEntity {
    @SerializedName(value = "total_market_cap_usd", alternate = {"total_market_cap_aud", "total_market_cap_brl",
            "total_market_cap_cad", "total_market_cap_chf", "total_market_cap_cny", "total_market_cap_eur",
            "total_market_cap_gbp", "total_market_cap_hkd", "total_market_cap_idr", "total_market_cap_inr",
            "total_market_cap_jpy", "total_market_cap_krw", "total_market_cap_mxn", "total_market_cap_rub"})
    private double totalMarketCap;

    @SerializedName(value = "total_24h_volume_usd", alternate = {"total_24h_volume_aud", "total_24h_volume_brl",
            "total_24h_volume_cad", "total_24h_volume_chf", "total_24h_volume_cny", "total_24h_volume_eur",
            "total_24h_volume_gbp", "total_24h_volume_hkd", "total_24h_volume_idr", "total_24h_volume_inr",
            "total_24h_volume_jpy", "total_24h_volume_krw", "total_24h_volume_mxn", "total_24h_volume_rub"})
    private double totalVolume24hour;

    private double bitcoinPercentageOfMarketCap;

    private int activeCurrencies;

    private int activeAssets;

    private int activeMarkets;


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

    public void setBitcoinPercentageOfMarketCap(double bitcoinPercentageOfMarketCap) {
        this.bitcoinPercentageOfMarketCap = bitcoinPercentageOfMarketCap;
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
}