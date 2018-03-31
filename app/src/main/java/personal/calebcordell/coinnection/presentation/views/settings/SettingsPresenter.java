package personal.calebcordell.coinnection.presentation.views.settings;

import javax.inject.Inject;

import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.domain.interactor.impl.CurrencyChangedInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.FetchAllAssetsInteractor;


class SettingsPresenter extends SettingsContract.Presenter {
    private static final String TAG = SettingsPresenter.class.getSimpleName();

    private CurrencyChangedInteractor mCurrencyChangedInteractor;
    private FetchAllAssetsInteractor mFetchAllAssetsInteractor;

    @Inject
    SettingsPresenter(CurrencyChangedInteractor currencyChangedInteractor,
                      FetchAllAssetsInteractor fetchAllAssetsInteractor) {
        mCurrencyChangedInteractor = currencyChangedInteractor;
        mFetchAllAssetsInteractor = fetchAllAssetsInteractor;
    }

    @Override
    public void initialize() {}

    @Override
    public void onCurrencyPreferenceChanged() {
        mCurrencyChangedInteractor.execute(new DisposableCompletableObserver() {
                    @Override public void onComplete() {}
                    @Override public void onError(Throwable e) {}
                });
    }

    @Override
    public void onForceUpdatePreferenceClicked() {
        mView.showForceUpdateDialog();
    }

    @Override
    public void forceUpdate() {
        mView.showMessage("Starting data refresh...");
        mFetchAllAssetsInteractor
                .execute(true, new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mView.showMessage("Data refresh complete");
                    }
                    @Override
                    public void onError(Throwable e) {}
                });
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
