package personal.calebcordell.coinnection.domain.model;

import android.os.Parcel;
import android.os.Parcelable;


public class PortfolioAsset extends Asset {
    private double balance;
    private int position;

    public PortfolioAsset(Asset asset, double balance) {
        super(asset);
        this.balance = balance;
        position = -1;
    }
    public PortfolioAsset(Asset asset) {
        this(asset, 0.0);
    }
    public PortfolioAsset() {
        this(new Asset());
    }

    public double getBalance() {
        return this.balance;
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

    /**
     * Parcelable implementation.  Allows PortfolioAssets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeDouble(balance);
    }
    protected PortfolioAsset(Parcel in) {
        super(in);
        balance = in.readDouble();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<PortfolioAsset> CREATOR = new Parcelable.Creator<PortfolioAsset>() {
        @Override
        public PortfolioAsset createFromParcel(Parcel in) {
            return new PortfolioAsset(in);
        }

        @Override
        public PortfolioAsset[] newArray(int size) {
            return new PortfolioAsset[size];
        }
    };
}