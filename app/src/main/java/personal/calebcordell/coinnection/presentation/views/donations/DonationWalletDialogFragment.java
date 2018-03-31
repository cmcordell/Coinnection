package personal.calebcordell.coinnection.presentation.views.donations;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.dagger.module.AppModule;
import personal.calebcordell.coinnection.domain.model.DonationItem;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.StringUtils;
import personal.calebcordell.coinnection.presentation.views.base.BaseDialogFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class DonationWalletDialogFragment extends BaseDialogFragment {
    private static final String TAG = DonationWalletDialogFragment.class.getSimpleName();

    @BindView(R.id.donation_wallet_qrcode)
    protected ImageView mQRCode;
    @BindView(R.id.donation_wallet_address)
    protected TextView mAddress;
    @BindView(R.id.erc20_link)
    protected TextView mErc20Link;

    @BindString(R.string.wallet_name_ethereum)
    protected String mEthereumWalletStr;
    @BindString(R.string.erc20_link_url)
    protected String mErc20Url;
    @BindString(R.string.donate)
    protected String mDonateString;

    @Inject @Named(AppModule.APP_CONTEXT)
    protected Context mContext;
    @Inject
    protected Preferences mPreferences;
    @Inject
    protected MainActivity mActivity;

    private Toast mToast;

    private DonationItem mDonationItem;

    private Unbinder mUnbinder;

    public DonationWalletDialogFragment() {}
    public static DonationWalletDialogFragment newInstance(DonationItem donationItem) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_DONATION_ITEM, donationItem);

        DonationWalletDialogFragment fragment = new DonationWalletDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override @NonNull
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donation, null);

        mUnbinder = ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDonationItem = bundle.getParcelable(Constants.EXTRA_DONATION_ITEM);
        }

        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AppCompatDialog dialog = new AlertDialog.Builder(mActivity, alertDialogTheme)
                .setView(view)
                .setTitle(mDonateString + " " + mDonationItem.getName())
                .setPositiveButton(R.string.copy, null)
                .setNeutralButton(R.string.email, null)
                .setNegativeButton(R.string.cancel, (d, w) -> {})
                .create();

        dialog.setOnShowListener((dialogShown) -> {
            ((AlertDialog) dialogShown).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v) -> copyAddress());
            ((AlertDialog) dialogShown).getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener((v) -> emailAddress());
        });

        Glide.with(view).load(mDonationItem.getQRCodeDrawableRes()).into(mQRCode);
        mAddress.setText(mDonationItem.getAddress());

        if (mDonationItem.getName().equals(mEthereumWalletStr)) {
            StringUtils.makeTextViewHyperlink(mErc20Link);
            mErc20Link.setOnClickListener((v) -> erc20LinkClicked());
            mErc20Link.setVisibility(View.VISIBLE);
        }

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void copyAddress() {
        ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(mDonationItem.getName() + " wallet address", mDonationItem.getAddress());

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            showToast(mDonationItem.getName() + " wallet address copied to clipboard");
        }
    }

    private void emailAddress() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_SUBJECT, mDonateString + " " + mDonationItem.getName());
        intent.putExtra(Intent.EXTRA_TEXT, "Donation Type:  " + mDonationItem.getName() + "\n\nDonation Address:  " + mDonationItem.getAddress());

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void erc20LinkClicked() {
        Uri uri = Uri.parse(mErc20Url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showToast(@NonNull final String message) {
        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}