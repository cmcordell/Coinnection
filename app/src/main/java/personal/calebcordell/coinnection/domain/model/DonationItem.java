package personal.calebcordell.coinnection.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;


public class DonationItem implements Parcelable {

    private String name;
    private String address;
    private int iconDrawableRes;
    private int qrcodeDrawableRes;

    public DonationItem(@DrawableRes final int iconDrawableRes,
                        @DrawableRes final int qrcodeDrawableRes,
                        @NonNull final String name,
                        @NonNull final String address) {
        this.iconDrawableRes = iconDrawableRes;
        this.qrcodeDrawableRes = qrcodeDrawableRes;
        this.name = name;
        this.address = address;
    }

    public DonationItem setIconDrawableRes(@DrawableRes final int iconDrawableRes) {
        this.iconDrawableRes = iconDrawableRes;
        return this;
    }

    public @DrawableRes
    int getIconDrawableRes() {
        return iconDrawableRes;
    }

    public DonationItem setQRCodeDrawableRes(@DrawableRes final int qrcodeDrawableRes) {
        this.qrcodeDrawableRes = qrcodeDrawableRes;
        return this;
    }

    public @DrawableRes
    int getQRCodeDrawableRes() {
        return qrcodeDrawableRes;
    }

    public DonationItem setName(final String name) {
        this.name = name;
        return this;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public DonationItem setAddress(final String address) {
        this.address = address;
        return this;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public static DonationItem Empty() {
        return new DonationItem(-1, -1, "", "");
    }

    /**
     * Parcelable implementation.  Allows assets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(address);
        out.writeInt(iconDrawableRes);
        out.writeInt(qrcodeDrawableRes);
    }

    protected DonationItem(Parcel in) {
        name = in.readString();
        address = in.readString();
        iconDrawableRes = in.readInt();
        qrcodeDrawableRes = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DonationItem> CREATOR = new Parcelable.Creator<DonationItem>() {
        @Override
        public DonationItem createFromParcel(Parcel in) {
            return new DonationItem(in);
        }

        @Override
        public DonationItem[] newArray(int size) {
            return new DonationItem[size];
        }
    };
}