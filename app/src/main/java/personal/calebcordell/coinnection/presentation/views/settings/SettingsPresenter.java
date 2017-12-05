package personal.calebcordell.coinnection.presentation.views.settings;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.domain.interactor.impl.CurrencyChangedInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.FetchAllAssetsInteractor;


class SettingsPresenter implements SettingsContract.Presenter {
    private static final String TAG = SettingsPresenter.class.getSimpleName();

    private SettingsContract.View mView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CurrencyChangedInteractor mCurrencyChangedInteractor = new CurrencyChangedInteractor();
    private FetchAllAssetsInteractor mFetchAllAssetsInteractor = new FetchAllAssetsInteractor();

    SettingsPresenter(SettingsContract.View view) {
        if(view != null) {
            this.mView = view;
        }
    }

    public void onCurrencyPreferenceChanged() {
        mCompositeDisposable.add(mCurrencyChangedInteractor
                .execute(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    public void onForceUpdatePreferenceClicked() {
        mView.showForceUpdateDialog();
    }

    public void forceUpdate() {
        App.showToast("Starting data refresh...");
        mCompositeDisposable.add(mFetchAllAssetsInteractor
                .execute(true, new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        App.showToast("Data refresh complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    public void destroy() {
        mCompositeDisposable.dispose();
    }
}
