package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.FragmentVisiblilityListener;
import personal.calebcordell.coinnection.presentation.views.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.MainActivity;
import personal.calebcordell.coinnection.presentation.views.assetdetailtab.AssetDetailTabFragment;


public class PortfolioDetailFragment extends BaseFragment implements PortfolioDetailContract.View {
    private static final String TAG = PortfolioDetailFragment.class.getSimpleName();

    private MainActivity mActivity;
    private PortfolioDetailContract.Presenter mPresenter;

    private List<Asset> mAssets;
    private List<String> mWatchlistAssetIds = new ArrayList<>();
    private int mCurrentTabPosition;
    @BindView(R.id.tab_layout) protected TabLayout mTabLayout;
    @BindView(R.id.view_pager) protected ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable mFavoriteBorderDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable mFavoriteDrawable;
    private boolean mCurrentAssetIsInPortfolio = true;
    private boolean mCurrentAssetIsOnWatchlist = false;

    private Unbinder mUnbinder;

    public PortfolioDetailFragment() {}

    public static Fragment newInstance(int startPosition, ArrayList<Asset> assets) {
        Fragment fragment = new PortfolioDetailFragment();

        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_START_POSITION, startPosition);
        //TODO we dont actually need the assets, we can get them ourself.  Quicker to pass them though...
        args.putParcelableArrayList(Constants.EXTRA_ALL_ASSETS, assets);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mCurrentTabPosition = bundle.getInt(Constants.EXTRA_START_POSITION);
            mAssets = bundle.getParcelableArrayList(Constants.EXTRA_ALL_ASSETS);

            mCurrentAssetIsInPortfolio = (mAssets.get(mCurrentTabPosition) instanceof PortfolioAsset);

            int size = mAssets.size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio_detail, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mAssets);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setCurrentItem(mCurrentTabPosition);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setScrollPosition(mCurrentTabPosition, 0f, true);

        mViewPager.post(new Runnable()
        {
            @Override
            public void run()
            {
                mOnPageChangeListener .onPageSelected(mViewPager.getCurrentItem());
            }
        });

        mPresenter = new PortfolioDetailPresenter(this);
        mPresenter.start();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setActionBarElevation(0);
        mActivity.setHomeAsUp(true);
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroy();
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        getFragmentManager().popBackStackImmediate();
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem watchlistActionItem = menu.findItem(R.id.action_favorite);
        if(mCurrentAssetIsInPortfolio) {
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
                String id = mAssets.get(mCurrentTabPosition).getId();
                if(mCurrentAssetIsOnWatchlist) {
                    mPresenter.removeAssetFromWatchlist(id);
                    mWatchlistAssetIds.remove(id);
                } else {
                    mPresenter.addAssetToWatchlist(mAssets.get(mCurrentTabPosition));
                    mWatchlistAssetIds.add(id);
                }
                updateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setWatchlistAssetIds(List<String> watchlistAssetIds) {
        mWatchlistAssetIds.clear();
        mWatchlistAssetIds.addAll(watchlistAssetIds);
    }

    public void setActionBarElevation(int elevationDP) {
        mTabLayout.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevationDP, getResources().getDisplayMetrics()));
    }

    public void removeCurrentAsset() {
        mPresenter.removeAsset(mAssets.remove(mCurrentTabPosition).getId());

        if(mAssets.size() == 0) {
            onBackPressed();
        } else {
            mViewPagerAdapter.removeAsset(mCurrentTabPosition);
        }
    }

    public void updateOptionsMenu() {
        Asset asset = mAssets.get(mCurrentTabPosition);
        if(asset instanceof PortfolioAsset) {
            mCurrentAssetIsInPortfolio = true;
        } else {
            mCurrentAssetIsInPortfolio = false;
            if(mWatchlistAssetIds.contains(asset.getId())) {
                mCurrentAssetIsOnWatchlist = true;
            } else {
                mCurrentAssetIsOnWatchlist = false;
            }
        }
        mActivity.invalidateOptionsMenu();
    }


    /**
     *
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Asset> mAssets;

        ViewPagerAdapter(FragmentManager manager, List<Asset> assets) {
            super(manager);
            this.mAssets = new ArrayList<>(assets.size());
            this.mAssets.addAll(assets);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= this.mAssets.size()) {
                position = this.mAssets.size() - 1;
            }

            return AssetDetailTabFragment.newInstance(this.mAssets.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return this.mAssets.size();
        }

        void removeAsset(int position) {
            int nextPosition = position;
            if(position == this.mAssets.size() - 1) {
                nextPosition = position - 1;
            }

            Log.d(TAG, "position = " + position + ";  nextPosition = " + nextPosition);
            this.mAssets.remove(position);
            notifyDataSetChanged();
            mOnPageChangeListener.onPageSelected(nextPosition);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mAssets.get(position).getSymbol();
        }
    }


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            FragmentVisiblilityListener oldFragment =
                    (FragmentVisiblilityListener) mViewPagerAdapter.instantiateItem(mViewPager, mCurrentTabPosition);
            FragmentVisiblilityListener newFragment =
                    (FragmentVisiblilityListener) mViewPagerAdapter.instantiateItem(mViewPager, position);
            if (oldFragment != null) {
                oldFragment.fragmentBecameInvisible();
            }
            if (newFragment != null) {
                newFragment.fragmentBecameVisible();
                mCurrentTabPosition = position;
            }

            updateOptionsMenu();
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}