package personal.calebcordell.coinnection.presentation.views.settings;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {

    @Inject
    protected SettingsContract.Presenter mPresenter;
    @Inject
    protected MainActivity mActivity;
    @Inject
    protected Preferences mPreferences;

    private Toast mToast;

    public SettingsFragment() {}
    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here before M, L (API 22) and below because onAttach(Context)
            // is not yet available at L.
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(activity);
    }
    @Override
    public void onAttach(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
            AndroidSupportInjection.inject(this);
        }
        super.onAttach(context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.app_preferences, rootKey);

        mPresenter.setView(this);

        setupPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_settings));
        mActivity.setSelectedFragment(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
    }

    public void showForceUpdateDialog() {
        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, alertDialogTheme);

        builder.setTitle(getString(R.string.title_force_update))
                .setMessage(R.string.force_update_text)
                .setPositiveButton(R.string.yes, (dialog, id) -> mPresenter.forceUpdate())
                .setNegativeButton(R.string.no, (dialog, id) -> {
                })
                .setCancelable(true)
                .show();
    }

    public void showMessage(@NonNull final String message) {
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mActivity.getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void setupPreferences() {
        ListPreference currencyPreference = (ListPreference) findPreference(Constants.KEY_CURRENCY);
        currencyPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPreferences.setCurrencyCode(newValue.toString());
            mPresenter.onCurrencyPreferenceChanged();
            return true;
        });
        currencyPreference.setValue(mPreferences.getCurrencyCode());


        ListPreference appThemePreference = (ListPreference) findPreference(Constants.KEY_APP_THEME);
        appThemePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            mPreferences.setAppTheme(newValue.toString());
            mActivity.recreate();
            return true;
        });
        appThemePreference.setValue(mPreferences.getAppTheme());


        Preference forceUpdatePreference = findPreference(Constants.KEY_FORCE_UPDATE);
        forceUpdatePreference.setOnPreferenceClickListener((preference) -> {
            mPresenter.onForceUpdatePreferenceClicked();
            return true;
        });
    }
}