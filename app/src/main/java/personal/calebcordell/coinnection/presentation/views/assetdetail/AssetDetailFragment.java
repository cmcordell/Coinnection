package personal.calebcordell.coinnection.presentation.views.assetdetail;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.assetrecyclerview.AssetRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.EditBalanceDialogFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;


public class AssetDetailFragment extends BaseFragment implements AssetDetailContract.View {
    private static final String TAG = AssetDetailFragment.class.getSimpleName();

    private static final int TARGET_EDIT_ADD = 0;

    private MainActivity mActivity;
    private AssetDetailContract.Presenter mPresenter;

    @BindView(R.id.asset_recycler_view) protected RecyclerView mAssetRecyclerView;
    private AssetRecyclerViewAdapter mAssetRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.title_remove_portfolio_asset) String mRemoveAssetTitleString;
    @BindString(R.string.remove_portfolio_asset_text) String mRemoveAssetTextString;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable mFavoriteBorderDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable mFavoriteDrawable;
    private boolean mIsInPortfolio = false;
    private boolean mIsOnWatchlist = false;

    private Asset mAsset;

    private Unbinder mUnbinder;

    public AssetDetailFragment() {}
    public static Fragment newInstance(Asset asset) {
        Fragment fragment = new AssetDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET, asset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mAsset = bundle.getParcelable(Constants.EXTRA_ASSET);
            mActivity.setTitle(mAsset.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mAssetRecyclerViewAdapter = new AssetRecyclerViewAdapter(mAsset, mOnClickListener, mOnTabSelectedListener);

        mAssetRecyclerView.setAdapter(mAssetRecyclerViewAdapter);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mAssetRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAssetRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAssetRecyclerView.addOnScrollListener(mOnScrollListener);

        showAsset(mAsset);

        mPresenter = new AssetDetailPresenter(this);
        mPresenter.start(mAsset.getId());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem watchlistActionItem = menu.findItem(R.id.action_favorite);
        if(mIsInPortfolio) {
            watchlistActionItem.setVisible(false);
        } else {
            watchlistActionItem.setVisible(true);
            if (mIsOnWatchlist) {
                watchlistActionItem.setIcon(mFavoriteDrawable);
            } else {
                watchlistActionItem.setIcon(mFavoriteBorderDrawable);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options_portfolio_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(mIsOnWatchlist) {
                    mIsOnWatchlist = false;
                    mPresenter.removeAssetFromWatchlist(mAsset.getId());
                } else {
                    mIsOnWatchlist = true;
                    mPresenter.addAssetToWatchlist(mAsset);
                }
                mActivity.invalidateOptionsMenu();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStackImmediate();
        return true;
    }

    public void showAsset(Asset asset) {
        mAsset = asset;
        mAssetRecyclerViewAdapter.setAsset(asset);
        mAssetRecyclerView.setVisibility(View.VISIBLE);
    }
    public void showAssetInPortfolio(boolean isInPortfolio) {
        mIsInPortfolio = isInPortfolio;
        mActivity.invalidateOptionsMenu();
    }
    public void showAssetOnWatchlist(boolean isOnWatchlist) {
        mIsOnWatchlist = isOnWatchlist;
        mActivity.invalidateOptionsMenu();
    }

    public void removePortfolioAsset() {
        mPresenter.removeAssetFromPortfolio(mAsset.getId());
    }

    public void openEditPortfolioAssetUI() {
        Fragment editBalanceFragment = EditBalanceDialogFragment.newInstance(mAsset);
        editBalanceFragment.setTargetFragment(this, TARGET_EDIT_ADD);
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(editBalanceFragment, TAG)
                .commit();
    }
    public void openAddPortfolioAssetUI() {
        Fragment editBalanceFragment = EditBalanceDialogFragment.newInstance(mAsset);
        editBalanceFragment.setTargetFragment(this, TARGET_EDIT_ADD);
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(editBalanceFragment, TAG)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case TARGET_EDIT_ADD:
                if(resultCode == Activity.RESULT_OK) {
                    PortfolioAsset portfolioAsset = data.getParcelableExtra(Constants.EXTRA_ASSET);
                    mAsset = portfolioAsset;
                    showAssetInPortfolio(true);
                    showAsset(mAsset);
                    mPresenter.addAssetToPortfolio(portfolioAsset, portfolioAsset.getBalance());
                }
                break;
        }
    }

    private void showRemoveAssetDialog() {
        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = App.getAppContext().obtainStyledAttributes(App.getApp().getAppTheme(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, alertDialogTheme);

        builder.setMessage(mRemoveAssetTextString)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removePortfolioAsset();
                        mPresenter.removeAssetFromPortfolio(mAsset.getId());
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (pastVisibleItems == 0) {
                mActivity.setActionBarElevation(0);
            } else if(pastVisibleItems > 0) {
                mActivity.setActionBarElevation(4);
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mAsset instanceof PortfolioAsset) {
                if (view.getId() == R.id.edit_add_asset_button) {
                    openEditPortfolioAssetUI();
                } else if (view.getId() == R.id.remove_asset_button) {
                    showRemoveAssetDialog();
                }
            } else {
                if (view.getId() == R.id.edit_add_asset_button) {
                    openAddPortfolioAssetUI();
                }
            }
        }
    };
    
    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override public void onTabSelected(TabLayout.Tab tab) {
            switch(tab.getPosition()) {
                case 0:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_HOUR);
                    break;
                case 1:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_DAY);
                    break;
                case 2:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_WEEK);
                    break;
                case 3:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_MONTH);
                    break;
                case 4:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_YEAR);
                    break;
                default:
                    mAssetRecyclerViewAdapter.setTimeframe(Constants.TIMEFRAME_HOUR);
                    break;
            }
        }
        @Override public void onTabUnselected(TabLayout.Tab tab) {}
        @Override public void onTabReselected(TabLayout.Tab tab) {}
    };
}