package personal.calebcordell.coinnection.presentation.views.settings;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.domain.repository.PreferencesRepository;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;
import personal.calebcordell.coinnection.presentation.views.MainActivity;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SettingsFragment extends PreferenceFragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    private MainActivity mActivity;

    @BindString(R.string.title_settings) protected String mSettingTitleString;
    @BindString(R.string.title_force_update) protected String mForceUpdateTitleString;
    @BindString(R.string.force_update_text) protected String mForceUpdateTextString;

    private Unbinder mUnbinder;

    public SettingsFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(mSettingTitleString);
        mActivity.setSelectedFragment(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mPresenter = new SettingsPresenter(this);

        addPreferencesFromResource(R.xml.app_preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        final PreferencesRepository preferencesRepository = PreferencesRepositoryImpl.getInstance();

        ListPreference currencyPreference = (ListPreference) findPreference(Constants.KEY_CURRENCY);
        currencyPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preferencesRepository.setCurrencyCode(newValue.toString());
                Utils.updateCurrency();
                mPresenter.onCurrencyPreferenceChanged();
                return true;
            }
        });
        currencyPreference.setValue(preferencesRepository.getCurrencyCode());


        ListPreference appThemePreference = (ListPreference) findPreference(Constants.KEY_APP_THEME);
        appThemePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preferencesRepository.setAppTheme(newValue.toString());
                mActivity.recreate();
                return true;
            }
        });
        appThemePreference.setValue(preferencesRepository.getAppTheme());


        ListPreference dataRefreshPreference = (ListPreference) findPreference(Constants.KEY_DATA_UPDATE);
        dataRefreshPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preferencesRepository.setDataRefresh(newValue.toString());
                return true;
            }
        });
        dataRefreshPreference.setValue(String.valueOf(preferencesRepository.getDataRefresh()));


        Preference forceUpdatePreference = findPreference(Constants.KEY_FORCE_UPDATE);
        forceUpdatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mPresenter.onForceUpdatePreferenceClicked();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void showForceUpdateDialog() {
        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, alertDialogTheme);

        builder.setTitle(mForceUpdateTitleString)
                .setMessage(mForceUpdateTextString)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPresenter.forceUpdate();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setCancelable(true)
                .show();
    }
}