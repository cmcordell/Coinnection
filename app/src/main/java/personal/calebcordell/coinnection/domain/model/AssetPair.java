package personal.calebcordell.coinnection.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


public class AssetPair extends Asset {

    private int position;
    @NonNull
    private String quoteCurrencySymbol;

    public AssetPair() {
        this(-1, "");
    }

    public AssetPair(int position, @NonNull String quoteCurrencySymbol) {
        this(new Asset(), position, quoteCurrencySymbol);
    }

    public AssetPair(@NonNull Asset asset, int position, @NonNull String quoteCurrencySymbol) {
        super(asset);
        this.position = position;
        this.quoteCurrencySymbol = quoteCurrencySymbol;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    public String getQuoteCurrencySymbol() {
        return quoteCurrencySymbol;
    }

    public void setQuoteCurrencySymbol(@NonNull String quoteCurrencySymbol) {
        this.quoteCurrencySymbol = quoteCurrencySymbol;
    }

    /**
     * Parcelable implementation.  Allows PortfolioAssets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(position);
        out.writeString(quoteCurrencySymbol);
    }

    protected AssetPair(Parcel in) {
        super(in);
        position = in.readInt();
        quoteCurrencySymbol = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AssetPair> CREATOR = new Parcelable.Creator<AssetPair>() {
        @Override
        public AssetPair createFromParcel(Parcel in) {
            return new AssetPair(in);
        }

        @Override
        public AssetPair[] newArray(int size) {
            return new AssetPair[size];
        }
    };
}