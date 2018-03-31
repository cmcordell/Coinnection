package personal.calebcordell.coinnection.presentation.views.portfolio;

import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.domain.model.animation.RevealAnimationSetting;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.OnPopUpFragmentClosedListener;
import personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview.OnPortfolioItemDragFinishedListener;
import personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview.PortfolioItemTouchHelperCallback;
import personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview.PortfolioRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.assetsearch.AssetSearchFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;
import personal.calebcordell.coinnection.presentation.views.portfoliodetail.PortfolioDetailFragment;


public class PortfolioFragment extends BaseFragment implements PortfolioContract.View {
    private static final String TAG = PortfolioFragment.class.getSimpleName();

    @Inject
    protected PortfolioContract.Presenter mPresenter;

    private List<PortfolioAsset> mPortfolioAssets = new ArrayList<>();
    private List<WatchlistAsset> mWatchlistAssets = new ArrayList<>();

    @BindView(R.id.portfolio_recycler_view)
    protected RecyclerView mPortfolioRecyclerView;
    @Inject
    protected PortfolioRecyclerViewAdapter mPortfolioRecyclerViewAdapter;
    @Inject
    protected MainActivity mActivity;
    @Inject
    protected PortfolioItemTouchHelperCallback mCallback;
    private ItemTouchHelper mHelper;

    private Unbinder mUnbinder;
    private boolean mAnimating = false;

    public PortfolioFragment() {}
    public static Fragment newInstance() {
        return new PortfolioFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mPortfolioRecyclerViewAdapter.setOnAssetItemClickListener(mOnAssetItemClickListener);
        mPortfolioRecyclerViewAdapter.setOnAssetDragFinishedListener(mOnAssetDragFinishedListener);
        mPortfolioRecyclerViewAdapter.setOnTimeframeTabSelectedListener(mOnTimeframeTabSelectedListener);
        mPortfolioRecyclerViewAdapter.setOnAssetInfoSelectedListener(mOnAssetInfoSelectedListener);

        mCallback.setItemTouchHelperAdapter(mPortfolioRecyclerViewAdapter);
        mHelper = new ItemTouchHelper(mCallback);
        mHelper.attachToRecyclerView(mPortfolioRecyclerView);

        mPortfolioRecyclerView.setAdapter(mPortfolioRecyclerViewAdapter);
        mPortfolioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) mPortfolioRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        mPortfolioRecyclerView.addOnScrollListener(mOnScrollListener);
        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivity.setTitle(getString(R.string.title_portfolio));
        mActivity.setHomeAsUp(false);

//        int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
//        if (pastVisibleItems == 0) {
//            mActivity.setActionBarElevation(0);
//        } else if (pastVisibleItems > 0) {
//            mActivity.setActionBarElevation(4);
//        }

        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

        mPortfolioRecyclerViewAdapter.setOnAssetItemClickListener(null);
        mPortfolioRecyclerViewAdapter.setOnAssetDragFinishedListener(null);
        mPortfolioRecyclerViewAdapter.setOnTimeframeTabSelectedListener(null);
        mPortfolioRecyclerViewAdapter.setOnAssetInfoSelectedListener(null);

        mCallback.setItemTouchHelperAdapter(null);
        mHelper.attachToRecyclerView(null);

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options_portfolio, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mAnimating = true;
                ViewCompat.postOnAnimationDelayed(getView(), this::openAssetSearchUI, Constants.SELECTABLE_VIEW_ANIMATION_DELAY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPortfolioAssets(@NonNull List<PortfolioAsset> portfolioAssets) {
        mPortfolioAssets = portfolioAssets;
        mPortfolioRecyclerViewAdapter.setPortfolioAssets(mPortfolioAssets);
        mPortfolioRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showWatchlistAssets(@NonNull List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets = watchlistAssets;
        mPortfolioRecyclerViewAdapter.setWatchlistAssets(mWatchlistAssets);
        mPortfolioRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showLastUpdated(final long lastUpdated) {
        mPortfolioRecyclerViewAdapter.setLastUpdated(lastUpdated);
    }

    public void openAssetSearchUI() {
        Fragment fragment = AssetSearchFragment.newInstance(constructRevealSettings(), mOnPopUpFragmentClosedListener);

        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, FragmentTransaction.TRANSIT_NONE)
                .add(android.R.id.content, fragment, AssetSearchFragment.class.getSimpleName())
                .commit();

        mAnimating = false;
    }

    public void openPortfolioDetailViewUI(Asset asset) {
        int assetPosition = -1;

        //Determine the position of the clicked asset so we can open it in portfoliodetailfragment
        if (asset instanceof PortfolioAsset) {
            int size = mPortfolioAssets.size();
            for (int i = 0; i < size; i++) {
                if (mPortfolioAssets.get(i).getId().equals(asset.getId())) {
                    assetPosition = i;
                }
            }
        } else {
            int size = mWatchlistAssets.size();
            for (int i = 0; i < size; i++) {
                if (mWatchlistAssets.get(i).getId().equals(asset.getId())) {
                    assetPosition = i + mPortfolioAssets.size();
                }
            }
        }

        if (assetPosition >= 0) {
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                            R.anim.slide_right_enter, R.anim.slide_right_exit)
                    .replace(R.id.content_frame, PortfolioDetailFragment.newInstance(new ArrayList<>(mPortfolioAssets), new ArrayList<>(mWatchlistAssets), assetPosition), PortfolioDetailFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void assetSearchClosed() {
        mBackHandlerInterface.setSelectedFragment(this);
    }

    private RevealAnimationSetting constructRevealSettings() {
        View view = mActivity.getRootLayout();
        return new RevealAnimationSetting(view.getRight(), view.getTop(), view.getWidth(), view.getHeight());
    }

//    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
//            if (pastVisibleItems == 0) {
//                mActivity.setActionBarElevation(0);
//            } else if (pastVisibleItems > 0) {
//                mActivity.setActionBarElevation(4);
//            }
//        }
//    };

    /**
     * Used to determine when an asset is clicked
     */
    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener = this::openPortfolioDetailViewUI;

    /**
     * Used to determine when a spinner item has been selected
     */
    private AdapterView.OnItemSelectedListener mOnAssetInfoSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPortfolioRecyclerViewAdapter.setAssetInfoShown(position);
        }
        @Override public void onNothingSelected(AdapterView<?> parent) {}
    };

    /**
     * Used to determine when AssetSearchFragment is closed
     */
    private OnPopUpFragmentClosedListener mOnPopUpFragmentClosedListener = new OnPopUpFragmentClosedListener() {
        @Override
        public void popUpFragmentClosed() {
            assetSearchClosed();
        }
        @Override public int describeContents() {
            return 0;
        }
        @Override public void writeToParcel(Parcel dest, int flags) {}
    };

    private OnPortfolioItemDragFinishedListener mOnAssetDragFinishedListener = new OnPortfolioItemDragFinishedListener() {
        @Override
        public void onDragFinished(List<PortfolioAsset> portfolioAssets, List<WatchlistAsset> watchlistAssets) {
            if (!mPortfolioAssets.equals(portfolioAssets)) {
                mPresenter.reorderPortfolioAssets(portfolioAssets);
            }
            if (!mWatchlistAssets.equals(watchlistAssets)) {
                mPresenter.reorderWatchlistAssets(watchlistAssets);
            }
        }
    };

    private TabLayout.OnTabSelectedListener mOnTimeframeTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_HOUR);
                    break;
                case 1:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_DAY);
                    break;
                case 2:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_WEEK);
                    break;
                case 3:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_MONTH);
                    break;
                case 4:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_YEAR);
                    break;
                default:
                    mPortfolioRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_HOUR);
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };
}