package personal.calebcordell.coinnection.presentation.views.assetsearch;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.animation.RevealAnimationSetting;
import personal.calebcordell.coinnection.presentation.AnimationUtils;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.FragmentVisiblilityListener;
import personal.calebcordell.coinnection.presentation.util.OnDismissedListener;
import personal.calebcordell.coinnection.presentation.util.OnPopUpFragmentClosedListener;
import personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview.AssetSearchItemListener;
import personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview.AssetSearchRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;
import personal.calebcordell.coinnection.presentation.util.MaterialPaddingItemDecoration;

import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailFragment;
import personal.calebcordell.coinnection.presentation.views.assetdetailtab.AssetDetailTabFragment;


public class AssetSearchFragment extends BaseFragment implements AssetSearchContract.View {
    private static final String TAG = AssetSearchFragment.class.getSimpleName();

    private AssetSearchContract.Presenter mPresenter;
    private MainActivity mActivity;

    @BindView(R.id.layout) RelativeLayout mLayout;
    @BindView(R.id.asset_search_view) protected SearchView mAssetSearchView;
    @BindView(R.id.asset_search_results_recycler_view) protected RecyclerView mAssetSearchRecyclerView;
    private AssetSearchRecyclerViewAdapter mAssetSearchRecyclerViewAdapter;

    @BindString(R.string.search_hint) protected String mQueryHintString;

    private OnPopUpFragmentClosedListener mOnPopUpFragmentClosedListener;
    private RevealAnimationSetting mRevealAnimationSettings;

    private Unbinder mUnbinder;

    public AssetSearchFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance(@Nonnull RevealAnimationSetting revealAnimationSetting, OnPopUpFragmentClosedListener listener) {
        Fragment fragment = new AssetSearchFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ANIMATION_SETTINGS, revealAnimationSetting);
        args.putParcelable(Constants.EXTRA_ON_CLOSE_LISTENER, listener);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        Bundle args = getArguments();
        if(args != null) {
            mRevealAnimationSettings = args.getParcelable(Constants.EXTRA_ANIMATION_SETTINGS);
            mOnPopUpFragmentClosedListener = args.getParcelable(Constants.EXTRA_ON_CLOSE_LISTENER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_search, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAssetSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAssetSearchRecyclerViewAdapter.filter(query);
                mAssetSearchRecyclerView.scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAssetSearchRecyclerViewAdapter.filter(newText);
                mAssetSearchRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        mAssetSearchRecyclerViewAdapter = new AssetSearchRecyclerViewAdapter(mAssetSearchItemListener);
        mAssetSearchRecyclerView.setAdapter(mAssetSearchRecyclerViewAdapter);
        mAssetSearchRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAssetSearchRecyclerView.addItemDecoration(new MaterialPaddingItemDecoration(mActivity));
        ((SimpleItemAnimator) mAssetSearchRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new AssetSearchPresenter(this);
        mPresenter.start();

        mAssetSearchView.setIconified(false);
        mAssetSearchView.setQueryHint(mQueryHintString);
        mAssetSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssetSearchView.setIconified(false);
            }
        });
        mAssetSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 50);
                return false;
            }
        });

        AnimationUtils.registerCircularRevealAnimation(mActivity, view, mRevealAnimationSettings);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        AnimationUtils.startCircularExitAnimation(mActivity, getView(), mRevealAnimationSettings,
                new OnDismissedListener() {
            @Override
            public void onDismissed() {
                getFragmentManager().popBackStackImmediate();
                mOnPopUpFragmentClosedListener.popUpFragmentClosed();
            }
        });
        return true;
    }

    public void setAssets(List<Asset> assets) {
        Log.d(TAG, "setAssets()");
        mAssetSearchRecyclerViewAdapter.replaceAll(assets);
    }

    public void setPortfolioAssetIds(List<String> portfolioAssetIds) {
        mAssetSearchRecyclerViewAdapter.setPortfolioAssetIds(portfolioAssetIds);
    }

    public void setWatchlistAssetIds(List<String> watchlistAssetIds) {
        mAssetSearchRecyclerViewAdapter.setWatchlistAssetIds(watchlistAssetIds);
    }

    public void openAssetDetailUI(Asset asset) {
        Log.d(TAG, "Open Asset Detail UI");

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_right_exit,
                        R.animator.slide_right_enter, R.animator.slide_left_exit)
                .replace(R.id.content_frame, AssetDetailFragment.newInstance(asset), AssetDetailFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private AssetSearchItemListener mAssetSearchItemListener = new AssetSearchItemListener() {
        @Override
        public void onAssetItemClick(Asset asset) {
            mPresenter.onAssetSelected(asset);
        }
        @Override
        public void onFavoriteClick(Asset asset, boolean isOnWatchlist) {
            mPresenter.onAssetFavoriteClicked(asset, isOnWatchlist);
            mAssetSearchRecyclerViewAdapter.setAssetOnWatchlist(asset.getId(), isOnWatchlist);
        }
    };
}