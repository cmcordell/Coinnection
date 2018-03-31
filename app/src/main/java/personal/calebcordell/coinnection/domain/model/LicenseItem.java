package personal.calebcordell.coinnection.domain.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;


public class LicenseItem implements Parcelable {

    private String title;
    private String text;

    public LicenseItem(@NonNull final String title, @NonNull final String text) {
        this.title = title;
        this.text = text;
    }

    public LicenseItem setTitle(final String title) {
        this.title = title;
        return this;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public LicenseItem setText(final String text) {
        this.text = text;
        return this;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public static LicenseItem Empty() {
        return new LicenseItem("", "");
    }

    /**
     * Parcelable implementation.  Allows assets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(text);
    }

    protected LicenseItem(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<LicenseItem> CREATOR = new Parcelable.Creator<LicenseItem>() {
        @Override
        public LicenseItem createFromParcel(Parcel in) {
            return new LicenseItem(in);
        }

        @Override
        public LicenseItem[] newArray(int size) {
            return new LicenseItem[size];
        }
    };
}