package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

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


public class AssetDetailTabFragment extends BaseFragment implements AssetDetailTabContract.View {
    private static final int TARGET_ADD = 0;
    private static final int TARGET_EDIT = 1;

    @Inject
    protected AssetDetailTabContract.Presenter mPresenter;
    @Inject
    protected MainActivity mActivity;
    @Inject
    protected Preferences mPreferences;

    @BindView(R.id.asset_recycler_view)
    protected RecyclerView mAssetRecyclerView;
    @Inject
    protected AssetRecyclerViewAdapter mAssetRecyclerViewAdapter;

    @BindString(R.string.remove_portfolio_asset_text)
    protected String mRemoveAssetTextString;

    private Unbinder mUnbinder;

    private boolean mIsInPortfolio = true;

    public AssetDetailTabFragment() {}
    public static Fragment newInstance(@NonNull final Asset asset) {
        Fragment fragment = new AssetDetailTabFragment();

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
                mPresenter.setAsset(asset);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mAssetRecyclerViewAdapter.setOnClickListener(mOnClickListener);
        mAssetRecyclerViewAdapter.setOnTabSelectedListener(mOnTabSelectedListener);

        mAssetRecyclerView.setAdapter(mAssetRecyclerViewAdapter);
        mAssetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) mAssetRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//        mAssetRecyclerView.addOnScrollListener(mOnScrollListener);

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

        mAssetRecyclerViewAdapter.setOnClickListener(null);
        mAssetRecyclerViewAdapter.setOnTabSelectedListener(null);

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public void showAsset(@NonNull Asset asset) {
        mIsInPortfolio = (asset instanceof PortfolioAsset);
        mAssetRecyclerViewAdapter.setAsset(asset);
        mAssetRecyclerView.setVisibility(View.VISIBLE);
    }

    public void openEditPortfolioAssetUI(@NonNull Asset asset) {
        DialogFragment editBalanceFragment = EditBalanceDialogFragment.newInstance(asset);
        editBalanceFragment.setTargetFragment(this, TARGET_EDIT);
        if (getParentFragment() != null) {
            getParentFragment().getChildFragmentManager().beginTransaction()
                    .add(editBalanceFragment, EditBalanceDialogFragment.class.getSimpleName())
                    .commit();
        }
    }


    public void openAddPortfolioAssetUI(@NonNull Asset asset) {
        DialogFragment editBalanceFragment = EditBalanceDialogFragment.newInstance(asset);
        editBalanceFragment.setTargetFragment(this, TARGET_EDIT);
        if (getParentFragment() != null) {
            getParentFragment().getChildFragmentManager().beginTransaction()
                    .add(editBalanceFragment, EditBalanceDialogFragment.class.getSimpleName())
                    .commit();
        }
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

    /**
     * Show a dialog to confirm removing the asset from portfolio
     */
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


    /**
     * Callback for button clicks, either Add, Edit, of Remove
     */
    private View.OnClickListener mOnClickListener = (view) -> {
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
    };

    /**
     * Callback for when user selects a Timeframe Tab on the price graph
     */
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
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };
}