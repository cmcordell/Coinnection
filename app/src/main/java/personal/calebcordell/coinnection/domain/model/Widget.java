package personal.calebcordell.coinnection.domain.model;


public class Widget {
    private int mAppWidgetId;

    public Widget(int appWidgetId) {
        mAppWidgetId = appWidgetId;
    }

    public int getAppWidgetId() {
        return mAppWidgetId;
    }
    public void setAppWidgetId(int appWidgetId) {
        mAppWidgetId = appWidgetId;
    }
}