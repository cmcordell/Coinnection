package personal.calebcordell.coinnection.presentation.views.assetpairdetail;

import android.support.annotation.NonNull;
import android.util.Pair;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.GetAssetPairByIdInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.RemoveAssetPairInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPair;


public class AssetPairDetailPresenter extends AssetPairDetailContract.Presenter {
    private static final String TAG = AssetPairDetailPresenter.class.getSimpleName();

    private GetAssetPairByIdInteractor mGetAssetPairByIdInteractor;
    private RemoveAssetPairInteractor mRemoveAssetPairInteractor;

    private CompositeDisposable mCompositeDisposable;

    private AssetPair mAssetPair;

    @Inject
    AssetPairDetailPresenter(GetAssetPairByIdInteractor getAssetPairByIdInteractor,
                             RemoveAssetPairInteractor removeAssetPairInteractor) {
        mGetAssetPairByIdInteractor = getAssetPairByIdInteractor;
        mRemoveAssetPairInteractor = removeAssetPairInteractor;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setAssetPair(@NonNull AssetPair assetPair) {
        mAssetPair = assetPair;
    }

    @Override
    public void initialize() {
        if (mAssetPair == null) {
            throw new RuntimeException("AssetPair must be set before calling start()");
        } else {
            mView.showAssetPair(mAssetPair);
        }
    }

    private void getAssetPair() {
        mCompositeDisposable.add(mGetAssetPairByIdInteractor.execute(
                new Pair<>(mAssetPair.getId(), mAssetPair.getQuoteCurrencySymbol()),
                new DisposableSubscriber<AssetPair>() {
                    @Override
                    public void onNext(AssetPair assetPair) {
                        mAssetPair = assetPair;
                        mView.showAssetPair(assetPair);
                    }

                    @Override
                    public void onError(Throwable t) {}
                    @Override
                    public void onComplete() {}
                }));
    }

    @Override
    public void onRemoveAssetPairClicked() {
        mView.openRemoveAssetPairUI();
    }

    @Override
    public void removeAssetPair() {
        mRemoveAssetPairInteractor.execute(
                new Pair<>(mAssetPair.getId(), mAssetPair.getQuoteCurrencySymbol()),
                new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    @Override
    public void destroy() {
        mCompositeDisposable.dispose();

        super.destroy();
    }
}