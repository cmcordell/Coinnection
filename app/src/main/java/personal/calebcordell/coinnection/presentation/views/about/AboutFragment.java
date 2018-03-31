package personal.calebcordell.coinnection.presentation.views.about;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AboutItem;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.util.aboutrecyclerview.AboutRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.donations.DonationsFragment;
import personal.calebcordell.coinnection.presentation.views.licenses.LicensesFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AboutFragment extends BaseFragment {
    private static final String TAG = AboutFragment.class.getSimpleName();

    @Inject
    protected MainActivity mActivity;
    @Inject
    protected AboutRecyclerViewAdapter mAdapter;
    @Inject
    protected Preferences mPreferences;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.about_recycler_view)
    RecyclerView mAboutRecyclerView;

    @BindString(R.string.app_version)
    protected String mAppVersionString;
    @BindString(R.string.support_email_text)
    protected String mSupportEmailTextString;
    @BindString(R.string.app_email)
    protected String mAppEmailString;
    @BindString(R.string.email_subject)
    protected String mEmailSubjectString;
    @BindString(R.string.coin_market_cap_text)
    protected String mCoinMarketCapTextString;
    @BindString(R.string.coin_market_cap_url)
    protected String mCoinMarketCapUrlString;
    @BindString(R.string.app_review_text)
    protected String mPlayStoreTitleString;
    @BindString(R.string.title_donate)
    protected String mDonateTitleString;
    @BindString(R.string.title_licenses)
    protected String mLicensesTitleString;
    @BindString(R.string.email_device_info)
    protected String mSupportEmailDeviceInfoString;

    private Unbinder mUnbinder;

    public AboutFragment() {}
    public static Fragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        int[] attrs = {R.attr.drawableDonationOverBackground, R.attr.drawableInfoOverBackground,
                R.attr.drawableLicenseOverBackground, R.attr.drawableLinkOverBackground,
                R.attr.drawableMailOverBackground, R.attr.drawableStarOverBackground};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        Drawable donateDrawable = styles.getDrawable(0);
        @SuppressLint("ResourceType") Drawable versionDrawable = styles.getDrawable(1);
        @SuppressLint("ResourceType") Drawable licenseDrawable = styles.getDrawable(2);
        @SuppressLint("ResourceType") Drawable coinMarketCapDrawable = styles.getDrawable(3);
        @SuppressLint("ResourceType") Drawable emailDrawable = styles.getDrawable(4);
        @SuppressLint("ResourceType") Drawable playStoreDrawable = styles.getDrawable(5);
        styles.recycle();

        String versionTitle;
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            versionTitle = String.format("%s %s", mAppVersionString, packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionTitle = "1.0.0";
        }

        List<AboutItem> items = new ArrayList<>(7);
        items.add(AboutItem.Empty());

        items.add(new AboutItem(donateDrawable, mDonateTitleString)
                .setOnClickListener((v) -> openDonationsUI()));

        items.add(new AboutItem(emailDrawable, mSupportEmailTextString)
                .setOnClickListener((v) -> {
                    String deviceInfo = String.format(mSupportEmailDeviceInfoString,
                            System.getProperty("os.version"),
                            android.os.Build.VERSION.INCREMENTAL,
                            android.os.Build.VERSION.SDK_INT,
                            android.os.Build.DEVICE,
                            android.os.Build.MODEL,
                            android.os.Build.PRODUCT);

                    openEmail(new String[]{mAppEmailString}, mEmailSubjectString, deviceInfo);
                }));

        items.add(new AboutItem(playStoreDrawable, mPlayStoreTitleString)
                .setOnClickListener((v) -> openPlayStore(mActivity.getPackageName())));

        items.add(new AboutItem(coinMarketCapDrawable, mCoinMarketCapTextString)
                .setOnClickListener((v) -> openWebsite(mCoinMarketCapUrlString)));

        items.add(new AboutItem(licenseDrawable, mLicensesTitleString)
                .setOnClickListener((v) -> openLicensesUI()));

        items.add(new AboutItem(versionDrawable, versionTitle)
                .setOnClickListener((v) -> doSomethingCool()));

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mAdapter.setItems(items);
        mAboutRecyclerView.setAdapter(mAdapter);
        mAboutRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAboutRecyclerView.addOnScrollListener(mOnScrollListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_about));
        mActivity.setHomeAsUp(false);

        int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pastVisibleItems == 0) {
            mActivity.setActionBarElevation(0);
        } else if (pastVisibleItems > 0) {
            mActivity.setActionBarElevation(4);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.setActionBarElevation(4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAboutRecyclerView.removeOnScrollListener(mOnScrollListener);
        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        mFragmentManager.popBackStack(Constants.MAIN_FRAGMENT, 0);
        return true;
    }


    private void openDonationsUI() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.content_frame, DonationsFragment.newInstance(), DonationsFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openEmail(@NonNull String[] emails, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);

        if (subject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (body != null) {
            intent.putExtra(Intent.EXTRA_TEXT, body);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openPlayStore(@NonNull String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openWebsite(@NonNull String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openLicensesUI() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.content_frame, LicensesFragment.newInstance(), LicensesFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void doSomethingCool() {

    }

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (pastVisibleItems == 0) {
                mActivity.setActionBarElevation(0);
            } else if (pastVisibleItems > 0) {
                mActivity.setActionBarElevation(4);
            }
        }
    };
}
