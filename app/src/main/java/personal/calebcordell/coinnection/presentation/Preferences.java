package personal.calebcordell.coinnection.presentation;

import javax.inject.Inject;
import javax.inject.Singleton;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;


@Singleton
public class Preferences {
    private PreferencesRepository mPreferencesRepository;
    private String mCurrencyCode;
    private String mAppTheme;
    private boolean mIsFirstRun;
    private boolean mNeedsFullUpdate;

    @Inject
    Preferences(PreferencesRepository preferencesRepository) {
        mPreferencesRepository = preferencesRepository;

        mCurrencyCode = mPreferencesRepository.getCurrencyCode();
        mAppTheme = mPreferencesRepository.getAppTheme();
        mIsFirstRun = mPreferencesRepository.getIsFirstRun();
        mNeedsFullUpdate = (System.currentTimeMillis() - mPreferencesRepository.getLastFullUpdate())
                > Constants.RELOAD_TIME_FULL_UPDATE;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }
    public void setCurrencyCode(@Constants.CurrencyCode final String currencyCode) {
        mPreferencesRepository.setCurrencyCode(currencyCode);
        mCurrencyCode = currencyCode;
    }

    public int getAppThemeStyleAttr() {
        switch(mAppTheme) {
            case Constants.APP_THEME_LIGHT:
                return R.style.CoinnectionTheme_Light;
            case Constants.APP_THEME_DARK:
                return R.style.CoinnectionTheme_Dark;
            default:
                return R.style.CoinnectionTheme_Light;
        }
    }
    public String getAppTheme() {
        return mAppTheme;
    }
    public void setAppTheme(@Constants.AppTheme final String appTheme) {
        mPreferencesRepository.setAppTheme(appTheme);
        mAppTheme = appTheme;
    }

    public boolean isFirstRun() {
        return mIsFirstRun;
    }
    public void finishFirstRun() {
        mPreferencesRepository.setIsFirstRun(false);
        mIsFirstRun = false;
    }

    public boolean needsFullUpdate() {
        return mNeedsFullUpdate;
    }
    public void setLastFullUpdateTime(final long lastFullUpdateTime) {
        mPreferencesRepository.setLastFullUpdate(lastFullUpdateTime);
        mNeedsFullUpdate = false;
    }
}