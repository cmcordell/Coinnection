package personal.calebcordell.coinnection.domain.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class AssetWidget extends Widget {

    private String mAssetId;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TIMEFRAME_NONE, TIMEFRAME_HOUR, TIMEFRAME_DAY, TIMEFRAME_WEEK})
    public @interface Timeframe{}
    public static final int TIMEFRAME_NONE = -1;
    public static final int TIMEFRAME_HOUR = 0;
    public static final int TIMEFRAME_DAY = 1;
    public static final int TIMEFRAME_WEEK = 2;
    private int mTimeframe;

    public AssetWidget(int appWidgetId, String assetId, int timeframe) {
        super(appWidgetId);
        mAssetId = assetId;
        mTimeframe = timeframe;
    }

    public String getAssetId() {
        return mAssetId;
    }
    public void setAssetId(String assetId) {
        mAssetId = assetId;
    }

    @Timeframe
    public int getTimeframe() {
        return mTimeframe;
    }
    public void setTimeframe(@Timeframe int timeframe) {
        mTimeframe = timeframe;
    }
    public void toggleTimeframe() {
        switch(mTimeframe) {
            case TIMEFRAME_NONE:
                return;
            case TIMEFRAME_HOUR:
                mTimeframe = TIMEFRAME_DAY;
                break;
            case TIMEFRAME_DAY:
                mTimeframe = TIMEFRAME_WEEK;
                break;
            case TIMEFRAME_WEEK:
                mTimeframe = TIMEFRAME_HOUR;
                break;
        }
    }
}
