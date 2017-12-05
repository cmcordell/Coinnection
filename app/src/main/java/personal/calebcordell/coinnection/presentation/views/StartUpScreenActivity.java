package personal.calebcordell.coinnection.presentation.views;

import android.content.Intent;
import android.os.Handler;
import android.transition.Fade;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.data.PreferencesRepositoryImpl;
import personal.calebcordell.coinnection.domain.interactor.impl.UpdatePortfolioInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.FetchAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.model.Portfolio;
import personal.calebcordell.coinnection.presentation.App;


public class StartUpScreenActivity extends AppCompatActivity {
    private static final String TAG = StartUpScreenActivity.class.getSimpleName();

    @BindView(R.id.initialize_progress_bar) protected ProgressBar mInitializingProgressBar;
    @BindView(R.id.initialize_text_view) protected TextView mInitializingTextView;

    @BindString(R.string.initial_load_network_error_text) protected String mInitialLoadNetworkErrorText;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private UpdatePortfolioInteractor mUpdatePortfolioInteractor = new UpdatePortfolioInteractor();
    private FetchAllAssetsInteractor mFetchAllAssetsInteractor = new FetchAllAssetsInteractor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(App.getApp().getAppTheme());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());

        setContentView(R.layout.activity_start_up_screen);

        ButterKnife.bind(this);

        if(PreferencesRepositoryImpl.getInstance().isFirstRun()) {
            Portfolio portfolio = new Portfolio();
            portfolio.setName("My Portfolio");
            mCompositeDisposable.add(mUpdatePortfolioInteractor.execute(portfolio, null));

            startInitialLoad();
        } else {
            hideLoading();
            startBackgroundUpdate();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    navigateToMainActivity();
                }
            }, 500);
        }
    }

    private void startInitialLoad() {
        showLoading();
        mCompositeDisposable.add(mFetchAllAssetsInteractor.execute(true, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                hideLoading();
                PreferencesRepositoryImpl.getInstance().finishFirstRun();
                navigateToMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showError();
            }
        }));
    }

    private void startBackgroundUpdate() {
        //Show persistent loading?
        mCompositeDisposable.add(mFetchAllAssetsInteractor.execute(false, null));
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mCompositeDisposable.dispose();
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