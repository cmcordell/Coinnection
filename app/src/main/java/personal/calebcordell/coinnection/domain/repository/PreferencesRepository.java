package personal.calebcordell.coinnection.domain.repository;


public interface PreferencesRepository {
    String getCurrencyCode();
    void setCurrencyCode(String currency);

    String getAppTheme();
    void setAppTheme(String appTheme);

    boolean getIsFirstRun();
    void setIsFirstRun(boolean isFirstRun);

    long getLastFullUpdate();
    void setLastFullUpdate(long timeInMilliseconds);
}