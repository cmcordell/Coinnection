package personal.calebcordell.coinnection.presentation.views.assetsearch;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.animation.RevealAnimationSetting;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.MaterialPaddingItemDecoration;
import personal.calebcordell.coinnection.presentation.util.OnPopUpFragmentClosedListener;
import personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview.AssetSearchItemListener;
import personal.calebcordell.coinnection.presentation.util.assetsearchrecyclerview.AssetSearchRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AssetSearchFragment extends BaseFragment implements AssetSearchContract.View {
    private static final String TAG = AssetSearchFragment.class.getSimpleName();

    @Inject protected AssetSearchContract.Presenter mPresenter;
    @Inject protected MainActivity mActivity;

    @BindView(R.id.layout) ConstraintLayout mLayout;
    @BindView(R.id.asset_search_view) protected SearchView mAssetSearchView;
    @BindView(R.id.asset_search_results_recycler_view) protected RecyclerView mAssetSearchRecyclerView;
    @Inject protected AssetSearchRecyclerViewAdapter mAssetSearchRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.search_hint) protected String mQueryHintString;

    private OnPopUpFragmentClosedListener mOnPopUpFragmentClosedListener;
//    private RevealAnimationSetting mRevealAnimationSettings;

    private Unbinder mUnbinder;

    public AssetSearchFragment() {}
    public static Fragment newInstance(@NonNull RevealAnimationSetting revealAnimationSetting, OnPopUpFragmentClosedListener listener) {
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

        Bundle args = getArguments();
        if (args != null) {
//            mRevealAnimationSettings = args.getParcelable(Constants.EXTRA_ANIMATION_SETTINGS);
            mOnPopUpFragmentClosedListener = args.getParcelable(Constants.EXTRA_ON_CLOSE_LISTENER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_search, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mAssetSearchView.setIconified(false);
        mAssetSearchView.setQueryHint(mQueryHintString);
        mAssetSearchView.setOnClickListener((v) -> mAssetSearchView.setIconified(false));
        mAssetSearchView.setOnCloseListener(() -> {
            ViewCompat.postOnAnimationDelayed(view, this::onBackPressed, Constants.SELECTABLE_VIEW_ANIMATION_DELAY);
            return true;
        });
        mAssetSearchView.setOnQueryTextListener(mOnQueryTextListener);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mAssetSearchRecyclerViewAdapter.setAssetSearchItemListener(mAssetSearchItemListener);
        mAssetSearchRecyclerView.setAdapter(mAssetSearchRecyclerViewAdapter);
        mAssetSearchRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAssetSearchRecyclerView.addItemDecoration(new MaterialPaddingItemDecoration(mActivity));
        ((SimpleItemAnimator) mAssetSearchRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter.setView(this);

        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(mKeyboardChangeListener);

//        AnimationUtils.registerCircularRevealAnimation(mActivity, view, mRevealAnimationSettings);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.hideKeyboard();
        mPresenter.destroy();

        mAssetSearchView.setOnQueryTextListener(null);
        mAssetSearchView.setOnClickListener(null);
        mAssetSearchView.setOnCloseListener(null);

        mAssetSearchRecyclerViewAdapter.setAssetSearchItemListener(null);

        mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mKeyboardChangeListener);

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
//        AnimationUtils.startCircularExitAnimation(mActivity, getView(), mRevealAnimationSettings,
//                () -> {
                    mFragmentManager.beginTransaction()
                            .remove(this)
                            .commit();
//                            .commitAllowingStateLoss();
                    mOnPopUpFragmentClosedListener.popUpFragmentClosed();
//                });
        return true;
    }

    public void setAssets(@NonNull List<Asset> assets) {
        mAssetSearchRecyclerViewAdapter.replaceAll(assets);
    }

    public void setPortfolioAssetIds(@NonNull List<String> portfolioAssetIds) {
        mAssetSearchRecyclerViewAdapter.setPortfolioAssetIds(portfolioAssetIds);
    }

    public void setWatchlistAssetIds(@NonNull List<String> watchlistAssetIds) {
        mAssetSearchRecyclerViewAdapter.setWatchlistAssetIds(watchlistAssetIds);
    }

    public void openAssetDetailUI(@NonNull Asset asset) {
        mFragmentManager.beginTransaction()
                .remove(this)
                .commit();

        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
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

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
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
    };

    private ViewTreeObserver.OnGlobalLayoutListener mKeyboardChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            if (mLayout != null) {
                mLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = mLayout.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int newScreenHeight = screenHeight - (screenHeight - r.bottom);

                if (newScreenHeight != screenHeight) {
                    ViewGroup.LayoutParams params = mLayout.getLayoutParams();
                    params.height = newScreenHeight;
                    mLayout.setLayoutParams(params);
                }
            }
        }
    };
}