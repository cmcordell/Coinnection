package personal.calebcordell.coinnection.domain.repository;


public interface PreferencesRepository {
    String getCurrencyCode();
    void setCurrencyCode(String currency);

    String getAppTheme();
    void setAppTheme(String appTheme);

    int getDataRefresh();
    void setDataRefresh(String dataRefresh);

    boolean isFirstRun();
    void finishFirstRun();

    boolean needsFullUpdate();
    long getLastFullUpdate();
    void setLastFullUpdate(long timeInMilliseconds);
}