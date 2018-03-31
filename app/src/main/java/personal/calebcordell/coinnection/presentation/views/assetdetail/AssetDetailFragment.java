package personal.calebcordell.coinnection.presentation.views.assetdetail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.util.assetrecyclerview.AssetRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.editbalancedialog.EditBalanceDialogFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AssetDetailFragment extends BaseFragment implements AssetDetailContract.View {
    private static final String TAG = AssetDetailFragment.class.getSimpleName();

    private static final int TARGET_ADD = 0;
    private static final int TARGET_EDIT = 1;

    @Inject
    protected MainActivity mActivity;
    @Inject
    protected AssetDetailContract.Presenter mPresenter;
    @Inject
    protected Preferences mPreferences;

    @BindView(R.id.asset_recycler_view)
    protected RecyclerView mAssetRecyclerView;
    @Inject
    protected AssetRecyclerViewAdapter mAssetRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.title_remove_portfolio_asset)
    String mRemoveAssetTitleString;
    @BindString(R.string.remove_portfolio_asset_text)
    String mRemoveAssetTextString;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp)
    Drawable mFavoriteBorderDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp)
    Drawable mFavoriteDrawable;
    private boolean mAssetLoaded = false;
    private boolean mIsInPortfolio = false;
    private boolean mIsOnWatchlist = false;

    private Unbinder mUnbinder;

    public AssetDetailFragment() {}
    public static Fragment newInstance(@NonNull Asset asset) {
        Fragment fragment = new AssetDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET, asset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Asset asset = bundle.getParcelable(Constants.EXTRA_ASSET);
            if (asset != null) {
                mActivity.setTitle(asset.getName());
            }
            mPresenter.setAsset(asset);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mAssetRecyclerViewAdapter.setOnClickListener(mOnClickListener);
        mAssetRecyclerViewAdapter.setOnTabSelectedListener(mOnTabSelectedListener);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mAssetRecyclerView.setAdapter(mAssetRecyclerViewAdapter);
        mAssetRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAssetRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

        mAssetRecyclerViewAdapter.setOnClickListener(null);
        mAssetRecyclerViewAdapter.setOnTabSelectedListener(null);

        mUnbinder.unbind();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem watchlistActionItem = menu.findItem(R.id.action_favorite);
        if (!mAssetLoaded || mIsInPortfolio) {
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
        inflater.inflate(R.menu.menu_options_asset_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (mIsOnWatchlist) {
                    mPresenter.removeAssetFromWatchlist();
                } else {
                    mPresenter.addAssetToWatchlist();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onBackPressed() {
        mFragmentManager.popBackStack();
        return true;
    }

    public void showAsset(@NonNull Asset asset) {
        mActivity.setTitle(asset.getName());
        if (asset instanceof PortfolioAsset) {
            mIsInPortfolio = true;
        }
        mAssetRecyclerViewAdapter.setAsset(asset);
        mAssetRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showAssetInPortfolio(boolean isInPortfolio) {
        mAssetLoaded = true;
        mIsInPortfolio = isInPortfolio;
        mActivity.invalidateOptionsMenu();
    }
    public void showAssetOnWatchlist(boolean isOnWatchlist) {
        mAssetLoaded = true;
        mIsOnWatchlist = isOnWatchlist;
        mActivity.invalidateOptionsMenu();
    }

    public void openEditPortfolioAssetUI(@NonNull Asset asset) {
        DialogFragment editBalanceFragment = EditBalanceDialogFragment.newInstance(asset);
        editBalanceFragment.setTargetFragment(this, TARGET_EDIT);
        editBalanceFragment.show(mFragmentManager, TAG);
    }

    public void openAddPortfolioAssetUI(@NonNull Asset asset) {
        DialogFragment editBalanceFragment = EditBalanceDialogFragment.newInstance(asset);
        editBalanceFragment.setTargetFragment(this, TARGET_ADD);
        editBalanceFragment.show(mFragmentManager, TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TARGET_ADD:
                if (resultCode == Activity.RESULT_OK) {
                    double balance = data.getDoubleExtra(Constants.EXTRA_ASSET_BALANCE, -1);
                    mPresenter.addAssetToPortfolio(balance);
                }
                break;
            case TARGET_EDIT:
                if (resultCode == Activity.RESULT_OK) {
                    double balance = data.getDoubleExtra(Constants.EXTRA_ASSET_BALANCE, -1);
                    mPresenter.editAssetBalance(balance);
                }
                break;
        }
    }

    public void openRemovePortfolioAssetUI() {
        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, alertDialogTheme);

        builder.setMessage(mRemoveAssetTextString)
                .setPositiveButton(R.string.yes, (dialog, id) -> mPresenter.removeAssetFromPortfolio())
                .setNegativeButton(R.string.no, (dialog, id) -> {})
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
            } else if (pastVisibleItems > 0) {
                mActivity.setActionBarElevation(4);
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsInPortfolio) {
                if (view.getId() == R.id.edit_add_asset_button) {
                    mPresenter.onEditPortfolioAssetClicked();
                } else if (view.getId() == R.id.remove_asset_button) {
                    mPresenter.onRemoveAssetFromPortfolioClicked();
                }
            } else {
                if (view.getId() == R.id.edit_add_asset_button) {
                    mPresenter.onAddAssetToPortfolioClicked();
                }
            }
        }
    };

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
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