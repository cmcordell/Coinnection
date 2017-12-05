package personal.calebcordell.coinnection.presentation.views.portfolio;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcel;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

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
import personal.calebcordell.coinnection.presentation.views.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;
import personal.calebcordell.coinnection.presentation.views.assetsearch.AssetSearchFragment;
import personal.calebcordell.coinnection.presentation.views.portfoliodetail.PortfolioDetailFragment;


public class PortfolioFragment extends BaseFragment implements PortfolioContract.View {
    private static final String TAG = PortfolioFragment.class.getSimpleName();

    private PortfolioContract.Presenter mPresenter;
    private MainActivity mActivity;

    private List<PortfolioAsset> mPortfolioAssets;
    private List<WatchlistAsset> mWatchlistAssets;

    @BindView(R.id.portfolio_recycler_view) protected RecyclerView mPortfolioRecyclerView;
    private PortfolioRecyclerViewAdapter mPortfolioRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private Unbinder mUnbinder;

    public PortfolioFragment() {}
    public static Fragment newInstance() {
        return new PortfolioFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mPortfolioRecyclerViewAdapter = new PortfolioRecyclerViewAdapter(mOnObjectItemClickListener,
                mOnItemSelectedListener, mOnDragFinishedListener, mOnTabSelectedListener);

        mPortfolioRecyclerView.setAdapter(mPortfolioRecyclerViewAdapter);
        ItemTouchHelper.Callback callback = new PortfolioItemTouchHelperCallback(mPortfolioRecyclerViewAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mPortfolioRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mPortfolioRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mPortfolioRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mPortfolioRecyclerView.addOnScrollListener(mOnScrollListener);

        mPresenter = new PortfolioPresenter(this);
        mPresenter.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_portfolio));
        mActivity.setHomeAsUp(false);

        int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pastVisibleItems == 0) {
            mActivity.setActionBarElevation(0);
        } else if (pastVisibleItems > 0) {
            mActivity.setActionBarElevation(4);
        }

        mPresenter.resume();
    }

    @Override
    public void onPause() {
        mPresenter.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mActivity.setActionBarElevation(4);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options_portfolio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                openAssetSearchUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPortfolioAssets(List<PortfolioAsset> portfolioAssets) {
        mPortfolioAssets = portfolioAssets;
        mPortfolioRecyclerViewAdapter.setPortfolioAssets(mPortfolioAssets);
        mPortfolioRecyclerView.setVisibility(View.VISIBLE);
    }
    public void updatePortfolioAssets(List<PortfolioAsset> portfolioAssets) {
        mPortfolioAssets = portfolioAssets;
        mPortfolioRecyclerViewAdapter.replacePortfolioAssets(mPortfolioAssets);
    }

    public void showWatchlistAssets(List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets = watchlistAssets;
        mPortfolioRecyclerViewAdapter.setWatchlistAssets(mWatchlistAssets);
        mPortfolioRecyclerView.setVisibility(View.VISIBLE);
    }
    public void updateWatchlistAssets(List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets = watchlistAssets;
        mPortfolioRecyclerViewAdapter.replaceWatchlistAssets(mWatchlistAssets);
    }

    public void showLastUpdated(long lastUpdated) {
        mPortfolioRecyclerViewAdapter.setLastUpdated(lastUpdated);
    }

    public void openAssetSearchUI() {
        Fragment fragment = AssetSearchFragment.newInstance(constructRevealSettings(), mOnPopUpFragmentClosedListener);

        getFragmentManager().beginTransaction()
                .add(android.R.id.content, fragment, AssetSearchFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
    public void openPortfolioDetailViewUI(Asset asset) {
        int assetPosition = -1;

        //Determine the position of the clicked asset so we can open it in portfoliodetailfragment
        if(asset instanceof PortfolioAsset) {
            int size = mPortfolioAssets.size();
            for(int i=0; i < size; i++) {
                if(mPortfolioAssets.get(i).getId().equals(asset.getId())) {
                    assetPosition = i;
                }
            }
        } else {
            int size = mWatchlistAssets.size();
            for(int i=0; i < size; i++) {
                if(mWatchlistAssets.get(i).getId().equals(asset.getId())) {
                    assetPosition = i + mPortfolioAssets.size();
                }
            }
        }

        if(assetPosition >= 0) {
            ArrayList<Asset> compositeList = new ArrayList<>(10);
            if(mPortfolioAssets != null) {
                compositeList.addAll(mPortfolioAssets);
            }
            if(mWatchlistAssets != null) {
                compositeList.addAll(mWatchlistAssets);
            }
            //TODO change animation, expand listitem to fragment
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_right_exit,
                            R.animator.slide_right_enter, R.animator.slide_left_exit)
                    .replace(R.id.content_frame, PortfolioDetailFragment.newInstance(assetPosition,
                            compositeList), PortfolioDetailFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void assetSearchClosed() {
        mActivity.setSelectedFragment(this);
    }

    private RevealAnimationSetting constructRevealSettings() {
        View view = mActivity.getRootLayout();
        return new RevealAnimationSetting(view.getRight(), view.getTop(), view.getWidth(), view.getHeight());
    }


    /**
     * App bar will not have a shadow when when recyclerview is scrolled to the top
     */
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

    /**
     * Used to determine when an asset is clicked
     */
    private OnObjectItemClickListener<Asset> mOnObjectItemClickListener = this::openPortfolioDetailViewUI;

    /**
     * Used to determine when a spinner item has been selected
     */
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
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
        @Override public int describeContents() {return 0;}
        @Override public void writeToParcel(Parcel dest, int flags) {}
    };

    private OnPortfolioItemDragFinishedListener mOnDragFinishedListener = new OnPortfolioItemDragFinishedListener() {
        @Override
        public void onDragFinished(List<PortfolioAsset> portfolioAssets, List<WatchlistAsset> watchlistAssets) {
            if(!mPortfolioAssets.equals(portfolioAssets)) {
                mPresenter.reorderPortfolioAssets(portfolioAssets);
            }
            if(!mWatchlistAssets.equals(watchlistAssets)) {
                mPresenter.reorderWatchlistAssets(watchlistAssets);
            }
        }
    };

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
            switch(tab.getPosition()) {
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
        @Override public void onTabUnselected(TabLayout.Tab tab) {

        }
        @Override public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}