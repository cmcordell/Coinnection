package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.views.assetdetailtab.AssetDetailTabFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class PortfolioDetailFragment extends BaseFragment implements PortfolioDetailContract.View {
    private static final String TAG = PortfolioDetailFragment.class.getSimpleName();

    @Inject
    protected MainActivity mActivity;
    @Inject
    protected PortfolioDetailContract.Presenter mPresenter;

    @BindView(R.id.tab_layout) protected TabLayout mTabLayout;
    @BindView(R.id.view_pager) protected ViewPager mViewPager;
    private PortfolioDetailViewPagerAdapter mViewPagerAdapter;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp)
    Drawable mFavoriteBorderDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp)
    Drawable mFavoriteDrawable;
    private boolean mCurrentAssetIsInPortfolio = true;
    private boolean mCurrentAssetIsOnWatchlist = false;

    private Unbinder mUnbinder;

    public PortfolioDetailFragment() {}
    public static Fragment newInstance(@NonNull ArrayList<PortfolioAsset> portfolioAssets,
                                       @NonNull ArrayList<WatchlistAsset> watchlistAssets,
                                       int startPosition) {
        Fragment fragment = new PortfolioDetailFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.EXTRA_PORTFOLIO_ASSETS, portfolioAssets);
        args.putParcelableArrayList(Constants.EXTRA_WATCHLIST_ASSETS, watchlistAssets);
        args.putInt(Constants.EXTRA_START_POSITION, startPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPresenter.setPortfolioAssets(bundle.getParcelableArrayList(Constants.EXTRA_PORTFOLIO_ASSETS));
            mPresenter.setWatchlistAssets(bundle.getParcelableArrayList(Constants.EXTRA_WATCHLIST_ASSETS));
            mPresenter.setInitialPosition(bundle.getInt(Constants.EXTRA_START_POSITION));

            mViewPagerAdapter = new PortfolioDetailViewPagerAdapter(
                    bundle.getParcelableArrayList(Constants.EXTRA_PORTFOLIO_ASSETS),
                    bundle.getParcelableArrayList(Constants.EXTRA_WATCHLIST_ASSETS),
                    getChildFragmentManager());
        } else {
            mViewPagerAdapter = new PortfolioDetailViewPagerAdapter(getChildFragmentManager());
        }

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            mPortfolioAssets = bundle.getParcelableArrayList(Constants.EXTRA_PORTFOLIO_ASSETS);
//            mWatchlistAssets = bundle.getParcelableArrayList(Constants.EXTRA_WATCHLIST_ASSETS);
//            mInitialPosition = bundle.getInt(Constants.EXTRA_START_POSITION);
//        }
//        mPresenter.setPortfolioAssets(mPortfolioAssets);
//        mPresenter.setWatchlistAssets(mWatchlistAssets);
//        mPresenter.setInitialPosition(mInitialPosition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter.setView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setActionBarElevation(0);
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.setActionBarElevation(4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();

        mViewPager.clearOnPageChangeListeners();

        mUnbinder.unbind();
    }

    @Override
    public boolean onBackPressed() {
        mFragmentManager.popBackStack();
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem watchlistActionItem = menu.findItem(R.id.action_favorite);
        if (mCurrentAssetIsInPortfolio) {
            watchlistActionItem.setVisible(false);
        } else {
            watchlistActionItem.setVisible(true);
            if (mCurrentAssetIsOnWatchlist) {
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
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                if (mCurrentAssetIsOnWatchlist) {
                    mPresenter.removeAssetFromWatchlist();
                } else {
                    mPresenter.addAssetToWatchlist();
                }
                updateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setInitialPosition(final int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void setPortfolioAssets(@NonNull List<PortfolioAsset> portfolioAssets) {
        mViewPagerAdapter.setPortfolioAssets(portfolioAssets);
    }

    @Override
    public void setWatchlistAssets(@NonNull List<WatchlistAsset> watchlistAssets) {
        mViewPagerAdapter.setWatchlistAssets(watchlistAssets);
    }

    @Override
    public void setCurrentAsset(Asset asset, boolean assetOnWatchlist) {
        mActivity.setTitle(asset.getName());
        if (asset instanceof PortfolioAsset) {
            mCurrentAssetIsInPortfolio = true;
            mCurrentAssetIsOnWatchlist = false;
        } else {
            mCurrentAssetIsOnWatchlist = assetOnWatchlist;
            mCurrentAssetIsInPortfolio = false;
        }
        updateOptionsMenu();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    public void updateOptionsMenu() {
        mActivity.invalidateOptionsMenu();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
            mPresenter.setCurrentAssetPosition(position);
        }
        @Override public void onPageScrollStateChanged(int state) {}
    };


    public class PortfolioDetailViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<PortfolioAsset> mPortfolioAssets = new ArrayList<>();
        private List<WatchlistAsset> mWatchlistAssets = new ArrayList<>();
        private int mPortfolioAssetsSize = 0;
        private int mTotalSize = 0;

        public PortfolioDetailViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        public PortfolioDetailViewPagerAdapter(List<PortfolioAsset> portfolioAssets, List<WatchlistAsset> watchlistAssets, FragmentManager manager) {
            super(manager);
            mPortfolioAssets.addAll(portfolioAssets);
            mWatchlistAssets.addAll(watchlistAssets);
            updateTotalSize();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (mTotalSize != 0) {
                if (position >= mTotalSize) {
                    position = mTotalSize - 1;
                }

                if (position < mPortfolioAssetsSize) {
                    fragment = AssetDetailTabFragment.newInstance(mPortfolioAssets.get(position));
                } else if (position < mTotalSize) {
                    fragment = AssetDetailTabFragment.newInstance(mWatchlistAssets.get(position - mPortfolioAssetsSize));
                }
            }

            return fragment;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return FragmentPagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mTotalSize;
        }

        public void setPortfolioAssets(@NonNull List<PortfolioAsset> portfolioAssets) {
            mPortfolioAssets = portfolioAssets;
            updateTotalSize();
            notifyDataSetChanged();
        }

        public void setWatchlistAssets(@NonNull List<WatchlistAsset> watchlistAssets) {
            mWatchlistAssets = watchlistAssets;
            updateTotalSize();
            notifyDataSetChanged();
        }

        private void updateTotalSize() {
            mPortfolioAssetsSize = mPortfolioAssets.size();
            mTotalSize = mPortfolioAssetsSize + mWatchlistAssets.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < mPortfolioAssets.size()) {
                return mPortfolioAssets.get(position).getSymbol();
            } else if (position < mTotalSize) {
                return mWatchlistAssets.get(position - mPortfolioAssetsSize).getSymbol();
            }

            return null;
        }
    }
}