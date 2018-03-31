package personal.calebcordell.coinnection.presentation.views.startupscreenactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.FetchAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.globalmarketdatainteractors.FetchGlobalMarketDataInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.portfoliointeractors.UpdatePortfolioInteractor;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.presentation.Preferences;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;


public class StartUpScreenActivity extends DaggerAppCompatActivity {
    private static final String TAG = StartUpScreenActivity.class.getSimpleName();

    private static final long SPLASH_DELAY = 500;

    @BindView(R.id.initialize_progress_bar)
    protected ProgressBar mInitializingProgressBar;
    @BindView(R.id.initialize_text_view)
    protected TextView mInitializingTextView;

    @BindString(R.string.initial_load_network_error_text)
    protected String mInitialLoadNetworkErrorText;

    @Inject
    protected UpdatePortfolioInteractor mUpdatePortfolioInteractor;
    @Inject
    protected FetchAllAssetsInteractor mFetchAllAssetsInteractor;
    @Inject
    protected FetchGlobalMarketDataInteractor mFetchGlobalMarketDataInteractor;

    @Inject
    protected Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(mPreferences.getAppThemeStyleAttr());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());

        setContentView(R.layout.activity_start_up_screen);

        ButterKnife.bind(this);

        if (mPreferences.isFirstRun()) {
            Portfolio portfolio = new Portfolio();
            portfolio.setName("My Portfolio");
            mUpdatePortfolioInteractor.execute(portfolio, new DisposableCompletableObserver() {
                @Override public void onComplete() {}
                @Override public void onError(Throwable e) {}
            });

            startInitialLoad();
        } else {
            hideLoading();
            startBackgroundUpdate();
            Handler handler = new Handler();
            handler.postDelayed(this::navigateToMainActivity, SPLASH_DELAY);
        }
    }

    private void startInitialLoad() {
        showLoading();
        mFetchAllAssetsInteractor.execute(true, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                mFetchGlobalMarketDataInteractor.execute(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        hideLoading();
                        mPreferences.finishFirstRun();
                        navigateToMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                showError();
            }
        });
    }

    private void startBackgroundUpdate() {
        mFetchAllAssetsInteractor.execute(false, new DisposableCompletableObserver() {
            @Override public void onComplete() {}
            @Override public void onError(Throwable e) {}
        });
    }

    private void navigateToMainActivity() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent, bundle);

        finish();
    }

    private void showLoading() {
        mInitializingTextView.setVisibility(View.VISIBLE);
        mInitializingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mInitializingTextView.setVisibility(View.INVISIBLE);
        mInitializingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mInitializingTextView.setText(mInitialLoadNetworkErrorText);
        mInitializingProgressBar.setVisibility(View.GONE);
    }
}