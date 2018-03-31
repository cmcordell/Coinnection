package personal.calebcordell.coinnection.domain.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Comparator;


@Entity(tableName = "assets")
public class Asset implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id; //internal id of asset ie ethereum, this is needed because names and symbols are not necessarily unique
    private String name; //name of asset ie Ethereum
    private String symbol; //3+ character symbol, ie ETH for Ethereum
    private int rank; //Current rank by comparison of market capitalizations
    private int logo; //Image resource id of asset logo
    private double price; //current market price
    @ColumnInfo(name = "volume_24h")
    private double volume24Hour; //total fiat amount of all transactions in the past 24 hours
    @ColumnInfo(name = "market_cap")
    private double marketCap; //price multiplied by available supply
    @ColumnInfo(name = "available_supply")
    private double availableSupply; //units of currency in circulation
    @ColumnInfo(name = "total_supply")
    private double totalSupply; //total units of currency that are currently in existence, this may change
    @ColumnInfo(name = "max_supply")
    private double maxSupply; //total units of currency that will ever be in existence
    @ColumnInfo(name = "percent_change_1h")
    private double percentChange1Hour; //percent change of value since exactly 1 hour ago
    @ColumnInfo(name = "percent_change_24h")
    private double percentChange24Hour; //percent change of value since exactly 24 hours ago
    @ColumnInfo(name = "percent_change_7d")
    private double percentChange7Day; //percent change of value since exactly 7 days ago
    @ColumnInfo(name = "last_update")
    private long lastUpdated; //time of last update in ms

    @Ignore
    public Asset() {
        this("");
    }

    public Asset(@NonNull String id) {
        this.id = id;
        this.name = "";
        this.symbol = "";
        this.rank = -1;
        this.logo = LogoUtil.getLogo("");
        this.price = 0;
        this.volume24Hour = 0;
        this.marketCap = 0;
        this.availableSupply = 0;
        this.totalSupply = 0;
        this.maxSupply = 0;
        this.percentChange1Hour = 0;
        this.percentChange24Hour = 0;
        this.percentChange7Day = 0;
        this.lastUpdated = 0;
    }

    public Asset(Asset asset) {
        id = asset.getId();
        name = asset.getName();
        symbol = asset.getSymbol();
        rank = asset.getRank();
        logo = LogoUtil.getLogo(id);
        price = asset.getPrice();
        volume24Hour = asset.getVolume24Hour();
        marketCap = asset.getMarketCap();
        availableSupply = asset.getAvailableSupply();
        totalSupply = asset.getTotalSupply();
        maxSupply = asset.getMaxSupply();
        percentChange1Hour = asset.getPercentChange1Hour();
        percentChange24Hour = asset.getPercentChange24Hour();
        percentChange7Day = asset.getPercentChange7Day();
        lastUpdated = asset.getLastUpdated();
    }

    //Getters and Setters
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(@IntRange(from = 0, to = Integer.MAX_VALUE) int rank) {
        this.rank = rank;
    }

    public void setLogo(int logoId) {
        logo = logoId;
    }

    public int getLogo() {
        return logo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double price) {
        this.price = price;
    }

    public double getVolume24Hour() {
        return volume24Hour;
    }

    public void setVolume24Hour(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double volume24Hour) {
        this.volume24Hour = volume24Hour;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double marketCap) {
        this.marketCap = marketCap;
    }

    public double getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double availableSupply) {
        this.availableSupply = availableSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(@FloatRange(from = 0.0, to = Double.MAX_VALUE) double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public double getPercentChange1Hour() {
        return percentChange1Hour;
    }

    public void setPercentChange1Hour(double percentChange1Hour) {
        this.percentChange1Hour = percentChange1Hour;
    }

    public double getPercentChange24Hour() {
        return percentChange24Hour;
    }

    public void setPercentChange24Hour(double percentChange24Hour) {
        this.percentChange24Hour = percentChange24Hour;
    }

    public double getPercentChange7Day() {
        return percentChange7Day;
    }

    public void setPercentChange7Day(double percentChange7Day) {
        this.percentChange7Day = percentChange7Day;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdate) {
        this.lastUpdated = lastUpdate;
    }

    public boolean isUpToDate(long expirationTime) {
        return (System.currentTimeMillis() - lastUpdated) < expirationTime;
    }

    /**
     * Parcelable implementation.  Allows assets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(symbol);
        out.writeInt(rank);
        out.writeInt(logo);
        out.writeDouble(price);
        out.writeDouble(volume24Hour);
        out.writeDouble(marketCap);
        out.writeDouble(availableSupply);
        out.writeDouble(totalSupply);
        out.writeDouble(maxSupply);
        out.writeDouble(percentChange1Hour);
        out.writeDouble(percentChange24Hour);
        out.writeDouble(percentChange7Day);
        out.writeLong(lastUpdated);
    }

    protected Asset(Parcel in) {
        id = in.readString();
        name = in.readString();
        symbol = in.readString();
        rank = in.readInt();
        logo = in.readInt();
        price = in.readDouble();
        volume24Hour = in.readDouble();
        marketCap = in.readDouble();
        availableSupply = in.readDouble();
        totalSupply = in.readDouble();
        maxSupply = in.readDouble();
        percentChange1Hour = in.readDouble();
        percentChange24Hour = in.readDouble();
        percentChange7Day = in.readDouble();
        lastUpdated = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Asset> CREATOR = new Parcelable.Creator<Asset>() {

        @Override
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };

    public static class IdComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            String a1 = c1.getId();
            String a2 = c2.getId();
            return a1.compareTo(a2);
        }
    }

    public static class NameComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            String a1 = c1.getName().toLowerCase();
            String a2 = c2.getName().toLowerCase();
            return a1.compareTo(a2);
        }
    }

    public static class SymbolComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            String a1 = c1.getSymbol();
            String a2 = c2.getSymbol();
            return a1.compareTo(a2);
        }
    }

    public static class RankComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Integer a1 = c1.getRank();
            Integer a2 = c2.getRank();
            return a1.compareTo(a2);
        }
    }

    public static class PriceComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getPrice();
            Double a2 = c2.getPrice();
            return a1.compareTo(a2);
        }
    }

    public static class Volume24HourComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getVolume24Hour();
            Double a2 = c2.getVolume24Hour();
            return a1.compareTo(a2);
        }
    }

    public static class MarketCapComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getMarketCap();
            Double a2 = c2.getMarketCap();
            return a1.compareTo(a2);
        }
    }

    public static class AvailableSupplyComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getAvailableSupply();
            Double a2 = c2.getAvailableSupply();
            return a1.compareTo(a2);
        }
    }

    public static class TotalSupplyComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getTotalSupply();
            Double a2 = c2.getTotalSupply();
            return a1.compareTo(a2);
        }
    }

    public static class MaxSupplyComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getMaxSupply();
            Double a2 = c2.getMaxSupply();
            return a1.compareTo(a2);
        }
    }

    public static class PercentChangeHourComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getPercentChange1Hour();
            Double a2 = c2.getPercentChange1Hour();
            return a1.compareTo(a2);
        }
    }

    public static class PercentChangeDayComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getPercentChange24Hour();
            Double a2 = c2.getPercentChange24Hour();
            return a1.compareTo(a2);
        }
    }

    public static class PercentChangeWeekComparator implements Comparator<Asset> {
        public int compare(Asset c1, Asset c2) {
            Double a1 = c1.getPercentChange7Day();
            Double a2 = c2.getPercentChange7Day();
            return a1.compareTo(a2);
        }
    }
}