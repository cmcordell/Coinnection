package personal.calebcordell.coinnection.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;


public class PreferencesRepositoryImpl implements PreferencesRepository {

    //TODO Locale based default currency
    private static final String DEFAULT_CURRENCY = "usd";
    private static final String DEFAULT_APP_THEME = "light";
    private static final String DEFAULT_DATA_UPDATE = "2";
    private static final boolean DEFAULT_FIRST_RUN = true;
    private static final long DEFAULT_LAST_FULL_UPDATE = 0L;

    private static PreferencesRepositoryImpl INSTANCE;
    private SharedPreferences mPreferences;


    private PreferencesRepositoryImpl(@NonNull Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static PreferencesRepositoryImpl getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PreferencesRepositoryImpl(App.getAppContext());
        }
        return INSTANCE;
    }

    public SharedPreferences getDefaultSharedPreferences() {
        return mPreferences;
    }

    public String getCurrencyCode() {
        return mPreferences.getString(Constants.KEY_CURRENCY, DEFAULT_CURRENCY);
    }
    public void setCurrencyCode(@Constants.CurrencyCode String currencyCode) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_CURRENCY, currencyCode);
        editor.apply();

        App.getApp().setCurrencyCode(currencyCode);
    }

    public String getAppTheme() {
        return mPreferences.getString(Constants.KEY_APP_THEME, DEFAULT_APP_THEME);
    }
    public void setAppTheme(@Constants.AppTheme String appTheme) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_APP_THEME, appTheme);
        editor.apply();

        App.getApp().setAppTheme(appTheme);
    }

    public int getDataRefresh() {
        String  dataRefreshStr = mPreferences.getString(Constants.KEY_DATA_UPDATE, DEFAULT_DATA_UPDATE);

        return Integer.parseInt(dataRefreshStr);
    }
    public void setDataRefresh(@Constants.DataRefresh String dataRefresh) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_DATA_UPDATE, dataRefresh);
        editor.apply();
    }

    public boolean isFirstRun() {
        return mPreferences.getBoolean(Constants.KEY_FIRST_RUN, DEFAULT_FIRST_RUN);
    }
    public void finishFirstRun() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(Constants.KEY_FIRST_RUN, false);
        editor.apply();
    }

    public boolean needsFullUpdate() {
        String  dataRefreshStr = mPreferences.getString(Constants.KEY_DATA_UPDATE, DEFAULT_DATA_UPDATE);
        long dataRefreshTime = Integer.parseInt(dataRefreshStr) * 24 * 60 * 60 * 1000;
        long timeSinceLastUpdate = System.currentTimeMillis() - mPreferences.getLong(Constants.KEY_LAST_FULL_UPDATE, DEFAULT_LAST_FULL_UPDATE);
        return (timeSinceLastUpdate > dataRefreshTime);
    }
    public long getLastFullUpdate() {
        return mPreferences.getLong(Constants.KEY_LAST_FULL_UPDATE, DEFAULT_LAST_FULL_UPDATE);
    }
    public void setLastFullUpdate(long timeInMilliseconds) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(Constants.KEY_LAST_FULL_UPDATE, System.currentTimeMillis());
        editor.apply();
    }
}