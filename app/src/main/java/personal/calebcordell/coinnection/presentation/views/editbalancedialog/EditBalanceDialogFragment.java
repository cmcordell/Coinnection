package personal.calebcordell.coinnection.presentation.views.editbalancedialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.views.base.BaseDialogFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class EditBalanceDialogFragment extends BaseDialogFragment {

    @BindView(R.id.balance_edit) protected EditText mBalanceEditText;
    @BindView(R.id.asset_symbol) protected TextView mAssetSymbolTextView;

    @BindString(R.string.edit) protected String mEditButtonText;
    @BindString(R.string.add) protected String mAddButtonText;
    @BindString(R.string.edit_balance) protected String mEditBalanceTitle;
    @BindString(R.string.add_balance) protected String mAddBalanceTitle;

    @Inject protected MainActivity mActivity;
    @Inject protected Preferences mPreferences;

    private Asset mAsset;
    private double mBalance = 0.0;

    private Unbinder mUnbinder;

    public EditBalanceDialogFragment() {}

    public static EditBalanceDialogFragment newInstance(Asset asset) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET, asset);

        EditBalanceDialogFragment fragment = new EditBalanceDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override @NonNull
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_balance, null);

        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            mAsset = bundle.getParcelable(Constants.EXTRA_ASSET);
            if (mAsset instanceof PortfolioAsset) {
                mBalance = ((PortfolioAsset) mAsset).getBalance();
            }
        }

        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        String positiveButtonStr = mAddButtonText;
        String titleStr = mAddBalanceTitle;
        mBalanceEditText.setHint(String.valueOf(StringUtils.getFormattedNumberString(0.0)));

        if (mBalance > 0) {
            positiveButtonStr = mEditButtonText;
            titleStr = mEditBalanceTitle;
            mBalanceEditText.setHint(String.valueOf(StringUtils.getFormattedNumberString(mBalance)));
        }

        AppCompatDialog dialog = new AlertDialog.Builder(mActivity, alertDialogTheme)
                .setView(view)
                .setTitle(titleStr)
                .setPositiveButton(positiveButtonStr, (d, w) -> positiveButtonClick())
                .setNegativeButton(R.string.cancel, (d, w) -> negativeButtonClick())
                .create();

        mAssetSymbolTextView.setText(mAsset.getSymbol());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setTargetFragment(null, -1);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        mActivity.hideKeyboard();
    }
    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mActivity.hideKeyboard();
    }

    private void positiveButtonClick() {
        String newBalanceStr = mBalanceEditText.getText().toString();
        if (!newBalanceStr.equals("")) {
            double newBalance = Double.parseDouble(newBalanceStr);
            if (newBalance > 0) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_ASSET_BALANCE, newBalance);
                if(getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }
            }
        } else {
            if(getTargetFragment() != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
            }
        }
        dismiss();
    }
    private void negativeButtonClick() {
        if(getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
        dismiss();
    }
}