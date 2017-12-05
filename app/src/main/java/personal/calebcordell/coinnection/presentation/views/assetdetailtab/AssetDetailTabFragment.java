package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.FragmentVisiblilityListener;
import personal.calebcordell.coinnection.presentation.util.assetrecyclerview.AssetRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.EditBalanceDialogFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;
import personal.calebcordell.coinnection.presentation.views.portfoliodetail.PortfolioDetailFragment;


public class AssetDetailTabFragment extends Fragment implements AssetDetailTabContract.View, FragmentVisiblilityListener {
    private static final String TAG = AssetDetailTabFragment.class.getSimpleName();

    private static final int TARGET_EDIT_ADD = 0;
    private static final long UPDATE_INTERVAL = 100;

    private MainActivity mActivity;
    private PortfolioDetailFragment mParentFragment;
    private AssetDetailTabContract.Presenter mPresenter;

    @BindView(R.id.asset_recycler_view) protected RecyclerView mAssetRecyclerView;
    private AssetRecyclerViewAdapter mAssetRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.remove_portfolio_asset_text) String mRemoveAssetTextString;

    private boolean mFragmentVisible;
    private Handler handler = new Handler();
    private Timer mTimer;

    private Asset mAsset;

    private Unbinder mUnbinder;

    public AssetDetailTabFragment() {}
    public static Fragment newInstance(Asset asset) {
        Fragment fragment = new AssetDetailTabFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET, asset);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mParentFragment = (PortfolioDetailFragment) getParentFragment();

        mFragmentVisible = false;

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mAsset = bundle.getParcelable(Constants.EXTRA_ASSET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        mAssetRecyclerViewAdapter = new AssetRecyclerViewAdapter(mAsset, mOnClickListener, mOnTabSelectedListener);

        mAssetRecyclerView.setAdapter(mAssetRecyclerViewAdapter);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mAssetRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAssetRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAssetRecyclerView.addOnScrollListener(mOnScrollListener);

        showAsset(mAsset);

        mPresenter = new AssetDetailTabPresenter(this);
        mPresenter.start(mAsset.getId());

        return view;
    }

    @Override
    public void onPause() {
        if(mTimer != null) {
            mTimer.cancel();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        mUnbinder.unbind();
        super.onDestroyView();
    }

    public void showAsset(Asset asset) {
        mAssetRecyclerViewAdapter.setAsset(asset);
        mAssetRecyclerView.setVisibility(View.VISIBLE);
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
                if (resultCode == Activity.RESULT_OK) {
                    PortfolioAsset portfolioAsset = data.getParcelableExtra(Constants.EXTRA_ASSET);
                    mAsset = portfolioAsset;
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
                        mParentFragment.removeCurrentAsset();
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

    @Override
    public void fragmentBecameVisible() {
        if(mAsset != null) {
            fragmentBecameVisibleAsync();
        } else {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate( new TimerTask() {
                public void run() {
                    if(mAsset != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                fragmentBecameVisibleAsync();
                            }
                        });
                        cancel();
                    }
                }
            }, 10, UPDATE_INTERVAL);
        }
    }
    private void fragmentBecameVisibleAsync() {
        mActivity.setTitle(mAsset.getName());

        mFragmentVisible = true;
        int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pastVisibleItems == 0) {
            mParentFragment.setActionBarElevation(0);
        } else if(pastVisibleItems > 0) {
            mParentFragment.setActionBarElevation(4);
        }
    }
    @Override
    public void fragmentBecameInvisible() {
        mFragmentVisible = false;
        if(mPresenter != null) {
            mPresenter.fragmentBecameInvisible();
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(mFragmentVisible) {
                int pastVisibleItems = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (pastVisibleItems == 0) {
                    mParentFragment.setActionBarElevation(0);
                } else if(pastVisibleItems > 0) {
                    mParentFragment.setActionBarElevation(4);
                }
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mAsset instanceof PortfolioAsset) {
                if (v.getId() == R.id.edit_add_asset_button) {
                    openEditPortfolioAssetUI();
                } else if (v.getId() == R.id.remove_asset_button) {
                    showRemoveAssetDialog();
                }
            } else {
                if (v.getId() == R.id.edit_add_asset_button) {
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