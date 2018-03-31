package personal.calebcordell.coinnection.presentation.views.assetpairdetail;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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
import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.util.assetrecyclerview.AssetRecyclerViewAdapter;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class AssetPairDetailFragment extends BaseFragment implements AssetPairDetailContract.View {
    private static final String TAG = AssetPairDetailFragment.class.getSimpleName();

    @Inject
    protected MainActivity mActivity;
    @Inject
    protected AssetPairDetailContract.Presenter mPresenter;
    @Inject
    protected Preferences mPreferences;

    @BindView(R.id.asset_pair_recycler_view)
    protected RecyclerView mAssetPairRecyclerView;
    @Inject
    protected AssetRecyclerViewAdapter mAssetRecyclerViewAdapter;
    protected LinearLayoutManager mLinearLayoutManager;

    @BindString(R.string.title_remove_asset_pair)
    String mRemoveAssetPairTitleString;
    @BindString(R.string.remove_asset_pair_text)
    String mRemoveAssetPairTextString;


    private Unbinder mUnbinder;

    public AssetPairDetailFragment() {}
    public static Fragment newInstance(@NonNull AssetPair assetPair) {
        Fragment fragment = new AssetPairDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.EXTRA_ASSET_PAIR, assetPair);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            AssetPair assetPair = bundle.getParcelable(Constants.EXTRA_ASSET_PAIR);
            if (assetPair != null) {
                setTitle(String.format("%s/%s", assetPair.getSymbol(), assetPair.getQuoteCurrencySymbol()));
                mPresenter.setAssetPair(assetPair);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_pair_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        mAssetRecyclerViewAdapter.setOnClickListener(mOnClickListener);
        mAssetRecyclerViewAdapter.setOnTabSelectedListener(mOnTabSelectedListener);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mAssetPairRecyclerView.setAdapter(mAssetRecyclerViewAdapter);
        mAssetPairRecyclerView.setLayoutManager(mLinearLayoutManager);
        ((SimpleItemAnimator) mAssetPairRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

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

    @Override
    public boolean onBackPressed() {
        mFragmentManager.popBackStack();
        return true;
    }

    public void showAssetPair(@NonNull AssetPair assetPair) {
        setTitle(String.format("%s/%s", assetPair.getSymbol(), assetPair.getQuoteCurrencySymbol()));
        mAssetRecyclerViewAdapter.setAsset(assetPair);
        mAssetPairRecyclerView.setVisibility(View.VISIBLE);
    }

    public void openRemoveAssetPairUI() {
        int[] attrs = {android.R.attr.alertDialogTheme};
        TypedArray styles = mActivity.getApplication().obtainStyledAttributes(mPreferences.getAppThemeStyleAttr(), attrs);
        int alertDialogTheme = styles.getResourceId(0, R.style.CoinnectionTheme_Light_Dialog_Alert);
        styles.recycle();

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, alertDialogTheme);

        builder.setTitle(mRemoveAssetPairTitleString)
                .setMessage(mRemoveAssetPairTextString)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    mPresenter.removeAssetPair();
                    onBackPressed();
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                })
                .setCancelable(true)
                .show();
    }

    private void setTitle(@NonNull final String title) {
        mActivity.setTitle(title);
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

    private View.OnClickListener mOnClickListener = (view) -> mPresenter.onRemoveAssetPairClicked();

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