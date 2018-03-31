package personal.calebcordell.coinnection.presentation.views.licenses;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.LicenseItem;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.views.base.BaseDialogFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class LicenseDialogFragment extends BaseDialogFragment {
    private static final String TAG = LicenseDialogFragment.class.getSimpleName();

    @BindView(R.id.license_text)
    protected TextView mLicenseText;

    @Inject
    protected Preferences mPreferences;
    @Inject
    protected MainActivity mActivity;

    private LicenseItem mLicenseItem;

    private Unbinder mUnbinder;

    public LicenseDialogFragment() {}
    public static LicenseDialogFragment newInstance(LicenseItem licenseItem) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_LICENSE_ITEM, licenseItem);

        LicenseDialogFragment fragment = new LicenseDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override @NonNull
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_license, null);

        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mLicenseItem = bundle.getParcelable(Constants.EXTRA_LICENSE_ITEM);
        }

        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AppCompatDialog dialog = new AlertDialog.Builder(mActivity, alertDialogTheme)
                .setView(view)
                .setTitle(mLicenseItem.getTitle())
                .setPositiveButton(R.string.okay, null)
                .create();

        mLicenseText.setText(mLicenseItem.getText());

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}