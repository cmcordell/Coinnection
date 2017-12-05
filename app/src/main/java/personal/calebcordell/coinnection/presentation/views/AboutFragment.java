package personal.calebcordell.coinnection.presentation.views;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.App;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.domain.model.AboutItem;
import personal.calebcordell.coinnection.presentation.util.aboutrecyclerview.AboutRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioFragment;


public class AboutFragment extends BaseFragment {
    private static final String TAG = AboutFragment.class.getSimpleName();

    private MainActivity mActivity;
    @BindView(R.id.about_recycler_view) RecyclerView mAboutRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.app_version) protected String mAppVersionString;
    @BindString(R.string.support_email_text) protected String mSupportEmailTextString;
    @BindString(R.string.app_email) protected String mAppEmailString;
    @BindString(R.string.email_subject) protected String mEmailSubjectString;
    @BindString(R.string.coin_market_cap_text) protected String mCoinMarketCapTextString;
    @BindString(R.string.coin_market_cap_url) protected String mCoinMarketCapUrlString;
    @BindString(R.string.app_review_text) protected String mPlayStoreTitleString;
    @BindString(R.string.title_donate) protected String mDonateTitleString;
    @BindString(R.string.title_licenses) protected String mLicensesTitleString;

    private Unbinder mUnbinder;

    public AboutFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        int[] attrs = {R.attr.drawableInfoOverBackground, R.attr.drawableLinkOverBackground,
                R.attr.drawableMailOverBackground, R.attr.drawableStarOverBackground,
                R.attr.drawableDocumentOverBackground, R.attr.drawableDonationOverBackground};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);
        Drawable versionDrawable = styles.getDrawable(0);
        Drawable coinMarketCapDrawable = styles.getDrawable(1);
        Drawable emailDrawable = styles.getDrawable(2);
        Drawable playStoreDrawable = styles.getDrawable(3);
        styles.recycle();

        String versionTitle;
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            versionTitle = String.format("%s %s", mAppVersionString, packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionTitle = "1.0.0";
        }
        AboutItem versionItem = new AboutItem(versionDrawable, versionTitle)
                .setOnClickListener((v) -> doSomethingCool());

        AboutItem emailItem = new AboutItem(emailDrawable, mSupportEmailTextString)
                .setOnClickListener((v) -> openEmail(new String[]{mAppEmailString}, mEmailSubjectString, null));

        AboutItem coinMarketCapItem = new AboutItem(coinMarketCapDrawable, mCoinMarketCapTextString)
                .setOnClickListener((v) -> openWebsite(mCoinMarketCapUrlString));

        AboutItem playStoreItem = new AboutItem(playStoreDrawable, mPlayStoreTitleString)
                .setOnClickListener((v) -> openPlayStore(mActivity.getPackageName()));

        AboutItem donationsItem = new AboutItem(null, mDonateTitleString)
                .setOnClickListener((v) -> openDonationsUI());

        AboutItem licensesItem = new AboutItem(null, mLicensesTitleString)
                .setOnClickListener((v) -> openLicensesUI());

        List<AboutItem> items = new ArrayList<>(6);
        items.add(donationsItem);
        items.add(emailItem);
        items.add(playStoreItem);
        items.add(coinMarketCapItem);
        items.add(licensesItem);
        items.add(versionItem);

        AboutRecyclerViewAdapter adapter = new AboutRecyclerViewAdapter(items);
        mAboutRecyclerView.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mAboutRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAboutRecyclerView.addOnScrollListener(mOnScrollListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_about));

        int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pastVisibleItems == 0) {
            mActivity.setActionBarElevation(0);
        } else if (pastVisibleItems > 0) {
            mActivity.setActionBarElevation(4);
        }
    }

    @Override
    public void onStop() {
        mActivity.setActionBarElevation(4);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.content_frame, PortfolioFragment.newInstance(), PortfolioFragment.class.getName())
                .commit();
        return true;
    }


    private void openDonationsUI() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit,
                        R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.content_frame, DonationsFragment.newInstance(), DonationsFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }
    private void openEmail(@NonNull String[] emails, String subject, String body) {
        //TODO Notify user we will be leaving app
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);

        if(subject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if(body != null) {
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
        //TODO Notify user we will be leaving app
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
        //TODO Notify user we will be leaving app
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
//        getFragmentManager().beginTransaction()
//                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit,
//                        R.animator.slide_right_enter, R.animator.slide_right_exit)
//                .replace(R.id.content_frame, DonationsFragment.newInstance(), DonationsFragment.class.getName())
//                .commit();
    }
    private void doSomethingCool() {
        //TODO Do something cool
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if(pastVisibleItems  == 0) {
                mActivity.setActionBarElevation(0);
            } else if(pastVisibleItems > 0) {
                mActivity.setActionBarElevation(4);
            }
        }
    };
}
