package personal.calebcordell.coinnection.presentation.views.mainactivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.Keyboard;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.views.about.AboutFragment;
import personal.calebcordell.coinnection.presentation.views.allassets.AllAssetsFragment;
import personal.calebcordell.coinnection.presentation.views.assetpairlist.AssetPairListFragment;
import personal.calebcordell.coinnection.presentation.views.base.BaseFragment;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioFragment;
import personal.calebcordell.coinnection.presentation.views.settings.SettingsFragment;


public class MainActivity extends DaggerAppCompatActivity implements BaseFragment.BackHandlerInterface, BaseFragment.ParentActivityInterface {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler = new Handler();
    private BaseFragment mSelectedFragment;
    private Fragment mNextFragment;
    private boolean mSelectedFragmentIsSettings = false;
    private boolean mChangeContentFragment = false;

    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager mFragmentManager;

    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    private DrawerArrowDrawable mHomeDrawable;
    private boolean mIsHomeAsUp = false;

    @Inject
    protected Preferences mPreferences;
    @Inject
    protected Keyboard mKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(mPreferences.getAppThemeStyleAttr());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mHomeDrawable = new DrawerArrowDrawable(mToolbar.getContext());
        mToolbar.setNavigationIcon(mHomeDrawable);
        mDrawer.addDrawerListener(mDrawerListener);

        mNavigationView.setNavigationItemSelectedListener(new DrawerItemSelectedListener());
        mToolbar.setNavigationOnClickListener((view) ->
            ViewCompat.postOnAnimationDelayed(view, this::onHomeClicked, Constants.SELECTABLE_VIEW_ANIMATION_DELAY));

        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.content_frame, PortfolioFragment.newInstance(), PortfolioFragment.class.getName())
                    .addToBackStack(Constants.MAIN_FRAGMENT)
                    .commit();
            mNavigationView.setCheckedItem(R.id.nav_portfolio);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDrawer.removeDrawerListener(mDrawerListener);

        mNavigationView.setNavigationItemSelectedListener(null);

        mToolbar.setNavigationOnClickListener(null);
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if(mSelectedFragment instanceof PortfolioFragment) {
            mFragmentManager.beginTransaction()
                    .remove(mSelectedFragment)
                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_NONE)
                    .commit();
            finishAfterTransition();
        } else if(mSelectedFragment != null && !mSelectedFragment.onBackPressed()) {
            super.onBackPressed();
        } else if(mSelectedFragmentIsSettings) {
            mFragmentManager.popBackStack(Constants.MAIN_FRAGMENT, 0);
        }
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mSelectedFragment = selectedFragment;

        if (selectedFragment != null) {
            mSelectedFragmentIsSettings = false;
            if (mSelectedFragment instanceof PortfolioFragment) {
                mNavigationView.setCheckedItem(R.id.nav_portfolio);
            } else if (mSelectedFragment instanceof AllAssetsFragment) {
                mNavigationView.setCheckedItem(R.id.nav_all_assets);
            } else if (mSelectedFragment instanceof AssetPairListFragment) {
                mNavigationView.setCheckedItem(R.id.nav_asset_pairs);
            } else if (mSelectedFragment instanceof AboutFragment) {
                mNavigationView.setCheckedItem(R.id.nav_about);
            }
        } else {
            mSelectedFragmentIsSettings = true;
            mNavigationView.setCheckedItem(R.id.nav_settings);
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public void setActionBarElevation(int elevationDP) {
        mToolbar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevationDP, getResources().getDisplayMetrics()));
    }

    @Override
    public void setHomeAsUp(boolean isHomeAsUp) {
        if (mIsHomeAsUp != isHomeAsUp) {
            mIsHomeAsUp = isHomeAsUp;
            ValueAnimator anim;
            if (isHomeAsUp) {
                anim = ValueAnimator.ofFloat(0, 1);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                anim = ValueAnimator.ofFloat(1, 0);
            }
            anim.addUpdateListener((valueAnimator) ->
                    mHomeDrawable.setProgress((Float) valueAnimator.getAnimatedValue()));
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(400);
            anim.start();
        }
    }

    @Override
    public View getRootLayout() {
        return mDrawer;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideKeyboard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onHomeClicked() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (mIsHomeAsUp) {
            onBackPressed();
        } else {
            hideKeyboard();
            mDrawer.openDrawer(GravityCompat.START);
        }
    }

    public void hideKeyboard() {
        mKeyboard.hide(getRootLayout());
    }
    public void showKeyboard() {
        mKeyboard.show(getRootLayout());
    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {}

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            if (mChangeContentFragment) {
                mFragmentManager.popBackStack(Constants.NAV_START_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit,
                                R.anim.slide_right_enter, R.anim.slide_right_exit)
                        .replace(R.id.content_frame, mNextFragment, mNextFragment.getClass().getName())
                        .addToBackStack(Constants.NAV_START_FRAGMENT)
                        .commit();

                mChangeContentFragment = false;
                mNextFragment = null;
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {}
    };

    private class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
//            mNavigationView.setCheckedItem(id);

            if (id == R.id.nav_portfolio) {
                if (!(mSelectedFragment instanceof PortfolioFragment)) {
                    mFragmentManager.popBackStack(Constants.MAIN_FRAGMENT, 0);
                    mChangeContentFragment = false;
                }
            } else if (id == R.id.nav_all_assets) {
                if (!(mSelectedFragment instanceof AllAssetsFragment)) {
                    mNextFragment = AllAssetsFragment.newInstance();
                    mChangeContentFragment = true;
                }
            } else if (id == R.id.nav_asset_pairs) {
                if (!(mSelectedFragment instanceof AssetPairListFragment)) {
                    mNextFragment = AssetPairListFragment.newInstance();
                    mChangeContentFragment = true;
                }
            } else if (id == R.id.nav_settings) {
                if (mSelectedFragment != null) {
                    mNextFragment = SettingsFragment.newInstance();
                    mChangeContentFragment = true;
                }
            } else if (id == R.id.nav_about) {
                if (!(mSelectedFragment instanceof AboutFragment)) {
                    mNextFragment = AboutFragment.newInstance();
                    mChangeContentFragment = true;
                }
            }

            mHandler.postDelayed(() -> mDrawer.closeDrawer(GravityCompat.START), 0);

            return true;
        }
    }
}