package personal.calebcordell.coinnection.data;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;
import personal.calebcordell.coinnection.presentation.Constants;


@Singleton
public class PreferencesRepositoryImpl implements PreferencesRepository {

    private static final String DEFAULT_CURRENCY = "usd";
    private static final String DEFAULT_APP_THEME = "light";
    private static final boolean DEFAULT_FIRST_RUN = true;
    private static final long DEFAULT_LAST_FULL_UPDATE = 0L;

    private SharedPreferences mPreferences;

    @Inject
    public PreferencesRepositoryImpl(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    public String getCurrencyCode() {
        return mPreferences.getString(Constants.KEY_CURRENCY, DEFAULT_CURRENCY);
    }
    public void setCurrencyCode(@Constants.CurrencyCode String currencyCode) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_CURRENCY, currencyCode);
        editor.apply();
    }

    public String getAppTheme() {
        return mPreferences.getString(Constants.KEY_APP_THEME, DEFAULT_APP_THEME);
    }
    public void setAppTheme(@Constants.AppTheme String appTheme) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(Constants.KEY_APP_THEME, appTheme);
        editor.apply();
    }

    public boolean getIsFirstRun() {
        return mPreferences.getBoolean(Constants.KEY_FIRST_RUN, DEFAULT_FIRST_RUN);
    }
    public void setIsFirstRun(final boolean isFirstRun) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(Constants.KEY_FIRST_RUN, isFirstRun);
        editor.apply();
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