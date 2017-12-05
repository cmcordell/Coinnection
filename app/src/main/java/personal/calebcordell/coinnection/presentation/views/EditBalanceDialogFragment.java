package personal.calebcordell.coinnection.presentation.views;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Utils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class EditBalanceDialogFragment extends DialogFragment {
    private static final String TAG = EditBalanceDialogFragment.class.getSimpleName();

    @BindView(R.id.balance_edit) protected EditText mBalanceEditText;
    @BindView(R.id.asset_symbol) protected TextView mAssetSymbolTextView;

    @BindString(R.string.edit) protected String mEditButtonText;
    @BindString(R.string.add) protected String mAddButtonText;
    @BindString(R.string.edit_balance) protected String mEditBalanceTitle;
    @BindString(R.string.add_balance) protected String mAddBalanceTitle;

    private Asset mAsset;
    private double mBalance = 0.0;

    private Unbinder mUnbinder;

    public EditBalanceDialogFragment() {

    }

    public static EditBalanceDialogFragment newInstance(Asset asset) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET, asset);

        EditBalanceDialogFragment fragment = new EditBalanceDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_balance, null);

        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            mAsset = bundle.getParcelable(Constants.EXTRA_ASSET);
            if(mAsset instanceof PortfolioAsset) {
                mBalance = ((PortfolioAsset) mAsset).getBalance();
            }
        }

        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        String positiveButtonStr = mAddButtonText;
        String titleStr = mAddBalanceTitle;
        mBalanceEditText.setHint(String.valueOf(Utils.getFormattedNumberString(0.0)));

        if(mBalance > 0) {
            positiveButtonStr = mEditButtonText;
            titleStr = mEditBalanceTitle;
            mBalanceEditText.setHint(String.valueOf(Utils.getFormattedNumberString(mBalance)));
        }

        AppCompatDialog dialog = new AlertDialog.Builder(getActivity(), alertDialogTheme)
                .setView(view)
                .setTitle(titleStr)
                .setPositiveButton(positiveButtonStr, (d, w) -> positiveButtonClick())
                .setNegativeButton(R.string.cancel, (d, w) -> negativeButtonClick())
                .create();

        mAssetSymbolTextView.setText(mAsset.getSymbol());

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void positiveButtonClick() {
        String newBalanceStr = mBalanceEditText.getText().toString();
        if (!newBalanceStr.equals("")) {
            double newBalance = Double.parseDouble(newBalanceStr);
            if(newBalance > 0) {
                Intent intent = new Intent();
                mAsset = new PortfolioAsset(mAsset, newBalance);
                intent.putExtra(Constants.EXTRA_ASSET, mAsset);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
        } else {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        }
    }
    private void negativeButtonClick() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
    }
}