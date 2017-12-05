package personal.calebcordell.coinnection.presentation;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;


public class App extends Application {
    //TODO Cannot have a static reference to context, it is a memory leak
    private static Context mContext;
    private static Toast mAppToast;

    private int mAppTheme;
    private String mCurrencyCode;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PreferencesRepository preferencesRepository = PreferencesRepositoryImpl.getInstance();
        setAppTheme(preferencesRepository.getAppTheme());
        mCurrencyCode = preferencesRepository.getCurrencyCode();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static App getApp() {
        return (App) mContext;
    }

    /**
     * display toast message
     *
     * @param data text to be displayed
     */
    public static void showToast(@NonNull String data) {
        if(mAppToast != null) {
            mAppToast.cancel();
        }

        mAppToast = Toast.makeText(mContext, data, Toast.LENGTH_SHORT);
        mAppToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) Utils.convertDpToPixels(20));
        mAppToast.show();
    }

    public void setAppTheme(String appThemePref) {
        switch(appThemePref) {
            case Constants.APP_THEME_LIGHT:
                mAppTheme = R.style.CoinnectionTheme_Light;
                break;
            case Constants.APP_THEME_DARK:
                mAppTheme = R.style.CoinnectionTheme_Dark;
                break;
        }
    }
    public int getAppTheme() {
        return mAppTheme;
    }

    public void setCurrencyCode(String currencyCode) {
        mCurrencyCode = currencyCode;
    }
    public String getCurrencyCode() {
        return mCurrencyCode;
    }


}