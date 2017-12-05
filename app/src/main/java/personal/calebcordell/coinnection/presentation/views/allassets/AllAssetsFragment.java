package personal.calebcordell.coinnection.presentation.views.allassets;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;
import personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview.AllAssetsRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.util.SimpleSpinnerAdapter;
import personal.calebcordell.coinnection.presentation.views.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;
import personal.calebcordell.coinnection.presentation.views.assetdetail.AssetDetailFragment;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioFragment;


public class AllAssetsFragment extends BaseFragment implements AllAssetsContract.View {
    private static final String TAG = AllAssetsFragment.class.getSimpleName();

    private AllAssetsContract.Presenter mPresenter;
    private MainActivity mActivity;

    private List<Asset> mAssets;

    @BindView(R.id.search_view) protected  SearchView mAllAssetSearchView;
    @BindView(R.id.sort_spinner) protected Spinner mAllAssetSortSpinner;
    @BindView(R.id.sort_direction_button) protected ImageButton mAllAssetSortDirectionButton;
    @BindView(R.id.all_assets_recycler_view) protected RecyclerView mAllAssetsRecyclerView;
    private AllAssetsRecyclerViewAdapter mAllAssetsRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.search_hint) String mSearchHint;
    private int mSortDirection = Constants.SORT_DIRECTION_DESCENDING;

    private Unbinder mUnbinder;

    public AllAssetsFragment() {}
    public static Fragment newInstance() {
        return new AllAssetsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_assets, container, false);

        mUnbinder = ButterKnife.bind(this, view);

//        setHasOptionsMenu(true);

        mAllAssetSearchView.setIconifiedByDefault(false);
        mAllAssetSearchView.setQueryHint(mSearchHint);
        mAllAssetSearchView.setOnQueryTextListener(mOnQueryTextListener);

        mAllAssetSortSpinner.setOnItemSelectedListener(mOnSortItemSelectedListener);
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(mActivity,
                R.array.spinner_all_assets_info, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAllAssetSortSpinner.setAdapter(adapter);
        mAllAssetSortSpinner.setSelection(Constants.ALL_ASSETS_LIST_ITEM_SHOW_MARKET_CAP);

        if(mSortDirection == Constants.SORT_DIRECTION_DESCENDING) {
            mAllAssetSortDirectionButton.setRotation(180);
        }
        mAllAssetSortDirectionButton.setOnClickListener(mOnSortDirectionButtonClickListener);

        mAllAssetsRecyclerViewAdapter = new AllAssetsRecyclerViewAdapter();
        mAllAssetsRecyclerViewAdapter.setOnAssetItemClickListener(mOnAssetItemClickListener);

        mAllAssetsRecyclerView.setAdapter(mAllAssetsRecyclerViewAdapter);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mAllAssetsRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAllAssetsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new AllAssetsPresenter(this);
        mPresenter.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setTitle(getString(R.string.title_all_assets));
        mActivity.setHomeAsUp(false);
        mActivity.setActionBarElevation(0);
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        mPresenter.pause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mActivity.setActionBarElevation(4);
        mPresenter.destroy();
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

    public void showAssets(List<Asset> assets) {
        mAssets = assets;
        mAllAssetsRecyclerViewAdapter.setAssets(assets);
        mAllAssetsRecyclerView.setVisibility(View.VISIBLE);
    }
    public void updateAssets(List<Asset> assets) {
        mAssets = assets;
        mAllAssetsRecyclerViewAdapter.replaceAssets(mAssets);
    }


    public void openAssetDetailViewUI(Asset asset) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        //TODO change animation, expand listitem to fragment
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_right_exit,
                        R.animator.slide_right_enter, R.animator.slide_left_exit)
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
    }

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override public boolean onQueryTextSubmit(String query) {
            setQuery(query);
            return true;
        }
        @Override public boolean onQueryTextChange(String newText) {
            setQuery(newText);
            return true;
        }
    };

    private AdapterView.OnItemSelectedListener mOnSortItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setAssetInfoShown(position);
        }
        @Override public void onNothingSelected(AdapterView<?> parent) {}
    };

    private View.OnClickListener mOnSortDirectionButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int rotation;
            if(mSortDirection == Constants.SORT_DIRECTION_DESCENDING) {
                mSortDirection = Constants.SORT_DIRECTION_ASCENDING;
                rotation = 360;
            } else {
                mSortDirection = Constants.SORT_DIRECTION_DESCENDING;
                rotation = 180;
            }
            setSortDirection(mSortDirection);

            mAllAssetSortDirectionButton.animate()
                    .rotation(rotation)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    };

    private OnObjectItemClickListener<Asset> mOnAssetItemClickListener = this::openAssetDetailViewUI;
}