package personal.calebcordell.coinnection.data.assetdata;

import com.google.gson.annotations.SerializedName;

/**
 * Asset Entity used to pull/store data through Retrofit and Room
 * See Asset.class for more info
 */
public class AssetEntity {
    private String id;
    private String name;
    private String symbol;
    private int rank;
    @SerializedName(value="price_usd", alternate={"price_aud", "price_brl", "price_cad", "price_chf",
            "price_cny", "price_eur", "price_gbp", "price_hkd", "price_idr", "price_inr", "price_jpy",
            "price_krw", "price_mxn", "price_rub"})
    private double price;
    @SerializedName(value="24h_volume_usd", alternate={"24h_volume_aud", "24h_volume_brl",
            "24h_volume_cad", "24h_volume_chf", "24h_volume_cny", "24h_volume_eur", "24h_volume_gbp",
            "24h_volume_hkd", "24h_volume_idr", "24h_volume_inr", "24h_volume_jpy", "24h_volume_krw",
            "24h_volume_mxn", "24h_volume_rub"})
    private double volume24Hour;
    @SerializedName(value="market_cap_usd", alternate={"market_cap_aud", "market_cap_brl",
            "market_cap_cad", "market_cap_chf", "market_cap_cny", "market_cap_eur", "market_cap_gbp",
            "market_cap_hkd", "market_cap_idr", "market_cap_inr", "market_cap_jpy", "market_cap_krw",
            "market_cap_mxn", "market_cap_rub"})
    private double marketCap;
    private double availableSupply;
    private double totalSupply;
    @SerializedName("percent_change_1h")
    private double percentChange1h;
    @SerializedName("percent_change_24h")
    private double percentChange24h;
    @SerializedName("percent_change_7d")
    private double percentChange7d;
    private long lastUpdated;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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