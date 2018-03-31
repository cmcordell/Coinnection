package personal.calebcordell.coinnection.domain.model.animation;

import android.os.Parcel;
import android.os.Parcelable;


public class RevealAnimationSetting implements Parcelable {

    private int mCenterX;
    private int mCenterY;
    private int mWidth;
    private int mHeight;

    public RevealAnimationSetting(int centerX, int centerY, int width, int height) {
        mCenterX = centerX;
        mCenterY = centerY;
        mWidth = width;
        mHeight = height;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }


    /**
     * Parcelable implementation.  Allows assets to be moved easily between fragments/activities
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mCenterX);
        out.writeInt(mCenterY);
        out.writeInt(mWidth);
        out.writeInt(mHeight);
    }

    protected RevealAnimationSetting(Parcel in) {
        mCenterX = in.readInt();
        mCenterY = in.readInt();
        mWidth = in.readInt();
        mHeight = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RevealAnimationSetting> CREATOR = new Parcelable.Creator<RevealAnimationSetting>() {
        @Override
        public RevealAnimationSetting createFromParcel(Parcel in) {
            return new RevealAnimationSetting(in);
        }

        @Override
        public RevealAnimationSetting[] newArray(int size) {
            return new RevealAnimationSetting[size];
        }
    };
}