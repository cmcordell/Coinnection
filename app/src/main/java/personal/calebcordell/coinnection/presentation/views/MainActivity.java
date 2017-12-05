package personal.calebcordell.coinnection.presentation.views;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.views.allassets.AllAssetsFragment;
import personal.calebcordell.coinnection.presentation.views.portfolio.PortfolioFragment;
import personal.calebcordell.coinnection.presentation.views.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements BaseFragment.BackHandlerInterface {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler = new Handler();
    private BaseFragment mSelectedFragment;
    private Fragment mNextFragment;
    private boolean mSelectedFragmentIsSettings = false;
    private boolean mChangeContentFragment = false;

    @BindView(R.id.nav_view) protected NavigationView mNavigationView;
    @BindView(R.id.drawer_layout) protected DrawerLayout mDrawer;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    private DrawerArrowDrawable mHomeDrawable;
    private boolean mIsHomeAsUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(App.getApp().getAppTheme());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mHomeDrawable = new DrawerArrowDrawable(mToolbar.getContext());
        mToolbar.setNavigationIcon(mHomeDrawable);
        mDrawer.addDrawerListener(mDrawerListener);

        mNavigationView.setNavigationItemSelectedListener(new DrawerItemSelectedListener());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(GravityCompat.START)){
                    mDrawer.closeDrawer(GravityCompat.START);
                } else if(mIsHomeAsUp){
                    onBackPressed();
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, PortfolioFragment.newInstance(), PortfolioFragment.class.getName())
                    .commit();
            mNavigationView.setCheckedItem(R.id.nav_portfolio);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if(mSelectedFragment != null && !mSelectedFragment.onBackPressed()){
            super.onBackPressed();
        } else if(mSelectedFragmentIsSettings) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.slide_right_enter, R.animator.slide_right_exit)
                    .replace(R.id.content_frame, PortfolioFragment.newInstance(), PortfolioFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mSelectedFragment = selectedFragment;

        if(selectedFragment instanceof PortfolioFragment) {
            mNavigationView.setCheckedItem(R.id.nav_portfolio);
        }

        if(selectedFragment != null) {
            mSelectedFragmentIsSettings = false;
        } else {
            mSelectedFragmentIsSettings = true;
        }
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setActionBarElevation(int elevationDP) {
        mToolbar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevationDP, getResources().getDisplayMetrics()));
    }
    public void setHomeAsUp(boolean isHomeAsUp) {
        if(mIsHomeAsUp != isHomeAsUp) {
            mIsHomeAsUp = isHomeAsUp;
            ValueAnimator anim;
            if(isHomeAsUp) {
                anim = ValueAnimator.ofFloat(0, 1);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                anim = ValueAnimator.ofFloat(1, 0);
            }
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHomeDrawable.setProgress((Float) valueAnimator.getAnimatedValue());
                }
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(400);
            anim.start();
        }
    }
    public View getRootLayout() {
        return mDrawer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private FragmentTransaction setupAnimationsForFragmentTransaction(FragmentTransaction fragmentTransaction) {
        int enter;
        int exit;

        if(mSelectedFragment instanceof PortfolioFragment) {
            enter = R.animator.slide_left_enter;
            exit = R.animator.slide_left_exit;
        } else {
            if(mNextFragment instanceof PortfolioFragment) {
                enter = R.animator.slide_right_enter;
                exit = R.animator.slide_right_exit;
            } else {
                enter = R.animator.slide_left_enter;
                exit = R.animator.slide_right_exit;
            }
        }

        fragmentTransaction.setCustomAnimations(enter, exit);
        return fragmentTransaction;
    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
        @Override public void onDrawerOpened(View drawerView) {}
        @Override
        public void onDrawerClosed(View drawerView) {
            if(mChangeContentFragment) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction = setupAnimationsForFragmentTransaction(fragmentTransaction);
                fragmentTransaction
                        .replace(R.id.content_frame, mNextFragment, mNextFragment.getClass().getName())
                        .commit();

                mChangeContentFragment = false;
                mNextFragment = null;
            }
        }
        @Override public void onDrawerStateChanged(int newState) {}
    };
    private class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            mNavigationView.setCheckedItem(id);

            if(id == R.id.nav_portfolio) {
                if(!(mSelectedFragment instanceof PortfolioFragment)) {
                    mNextFragment = PortfolioFragment.newInstance();
                    mChangeContentFragment = true;
                }
            } else if(id == R.id.nav_all_assets) {
                if(!(mSelectedFragment instanceof AllAssetsFragment)) {
                    mNextFragment = AllAssetsFragment.newInstance();
                    mChangeContentFragment = true;
                }
            } else if(id == R.id.nav_settings) {
                if(mSelectedFragment != null) {
                    mNextFragment = SettingsFragment.newInstance();
                    mChangeContentFragment = true;
                }

            } else if(id == R.id.nav_about) {
                if(!(mSelectedFragment instanceof AboutFragment)) {
                    mNextFragment = AboutFragment.newInstance();
                    mChangeContentFragment = true;
                }
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawer.closeDrawer(GravityCompat.START);
                }
            }, 0);

            return true;
        }
    }
}