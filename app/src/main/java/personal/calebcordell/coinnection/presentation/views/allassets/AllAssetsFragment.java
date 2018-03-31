package personal.calebcordell.coinnection.presentation.views.allassets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.GlobalMarketData;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.SimpleSpinnerAdapter;
import personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview.AllAssetsRecyclerView;
import personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview.AllAssetsRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AllAssetsFragment extends BaseFragment implements AllAssetsContract.View {
    private static final String TAG = AllAssetsFragment.class.getSimpleName();

    @Inject
    protected AllAssetsContract.Presenter mPresenter;
    @Inject
    protected MainActivity mActivity;

    @BindView(R.id.search_view)
    protected SearchView mAllAssetSearchView;
    @BindView(R.id.sort_spinner)
    protected Spinner mAllAssetSortSpinner;
    @BindView(R.id.sort_direction_button)
    protected ImageButton mAllAssetSortDirectionButton;
    @BindView(R.id.scroll_to_top_fab)
    protected FloatingActionButton mScrollToTopButton;
    @BindView(R.id.all_assets_recycler_view)
    protected AllAssetsRecyclerView mAllAssetsRecyclerView;
    @Inject
    protected AllAssetsRecyclerViewAdapter mAllAssetsRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.search_hint)
    protected String mSearchHint;
    private int mSortDirection = Constants.SORT_DIRECTION_DESCENDING;

    private Unbinder mUnbinder;

    public AllAssetsFragment() {
    }

    public static Fragment newInstance() {
        return new AllAssetsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assets, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(false);

        mAllAssetSearchView.setIconifiedByDefault(false);
        mAllAssetSearchView.setQueryHint(mSearchHint);
        mAllAssetSearchView.setOnQueryTextListener(mOnQueryTextListener);

        mAllAssetSortSpinner.setOnItemSelectedListener(mOnSortItemSelectedListener);
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(mActivity,
                R.array.spinner_all_assets_info, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAllAssetSortSpinner.setAdapter(adapter);
        mAllAssetSortSpinner.setSelection(Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP);

        mScrollToTopButton.setOnClickListener((v) -> mAllAssetsRecyclerView.scrollToPosition(0));

        mAllAssetSortDirectionButton.setOnClickListener(mOnSortDirectionButtonClickListener);

        mAllAssetsRecyclerViewAdapter.setOnAssetItemClickListener(mOnAssetItemClickListener);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mAllAssetsRecyclerView.setAdapter(mAllAssetsRecyclerViewAdapter);
        mAllAssetsRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAllAssetsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAllAssetsRecyclerView.addOnScrollListener(mOnScrollListener);

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_all_assets));
        mActivity.setHomeAsUp(false);
        mActivity.setActionBarElevation(0);
        mPresenter.resume();
        setSortDirection(mSortDirection);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
        mActivity.setActionBarElevation(4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mActivity.hideKeyboard();

        mPresenter.destroy();

        mAllAssetSearchView.setOnQueryTextListener(null);

        mAllAssetSortSpinner.setOnItemSelectedListener(null);

        mScrollToTopButton.setOnClickListener(null);

        mAllAssetSortDirectionButton.setOnClickListener(null);

        mAllAssetsRecyclerViewAdapter.setOnAssetItemClickListener(null);

        mAllAssetsRecyclerView.removeOnScrollListener(mOnScrollListener);

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        mActivity.hideKeyboard();
        mFragmentManager.popBackStack(Constants.MAIN_FRAGMENT, 0);
        return true;
    }

    /**
     * Show the initial list of assets
     *
     * @param assets initial list of assets
     */
    public void showAssets(List<Asset> assets) {
        mAllAssetsRecyclerViewAdapter.setAssets(assets);
        mAllAssetsRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showGlobalMarketData(GlobalMarketData globalMarketData) {
        mAllAssetsRecyclerViewAdapter.setGlobalMarketData(globalMarketData);
    }

    public void openAssetDetailViewUI(Asset asset) {
        mActivity.hideKeyboard();

        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                        R.anim.slide_right_enter, R.anim.slide_right_exit)
                .replace(R.id.content_frame, AssetDetailFragment.newInstance(asset), AssetDetailFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    public void setQuery(String query) {
        mAllAssetsRecyclerViewAdapter.filter(query);
    }

    public void setAssetInfoShown(int position) {
        mAllAssetsRecyclerViewAdapter.sort(position);
    }

    public void setSortDirection(int direction) {
        mAllAssetsRecyclerViewAdapter.setSortDirection(direction);

        int rotation = 180;
        if (direction == Constants.SORT_DIRECTION_ASCENDING) {
            rotation = 360;
        }

        mAllAssetSortDirectionButton.animate()
                .rotation(rotation)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        mSortDirection = direction;
    }

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (pastVisibleItems > 10) {
                mScrollToTopButton.show();
            } else {
                mScrollToTopButton.hide();
            }
        }
    };

    private final SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            setQuery(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            setQuery(newText);
            return true;
        }
    };

    private final AdapterView.OnItemSelectedListener mOnSortItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setAssetInfoShown(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener mOnSortDirectionButtonClickListener = (View v) -> {
        if (mSortDirection == Constants.SORT_DIRECTION_DESCENDING) {
            setSortDirection(Constants.SORT_DIRECTION_ASCENDING);
        } else {
            setSortDirection(Constants.SORT_DIRECTION_DESCENDING);
        }
    };

    private final OnObjectItemClickListener<Asset> mOnAssetItemClickListener = this::openAssetDetailViewUI;
}