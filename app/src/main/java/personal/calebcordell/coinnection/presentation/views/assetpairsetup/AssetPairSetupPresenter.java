package personal.calebcordell.coinnection.presentation.views.assetpairsetup;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.assetinteractors.GetAllAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.AddAssetPairInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.assetpairinteractors.GetAssetPairByIdInteractor;
import personal.calebcordell.coinnection.domain.model.Asset;
import personal.calebcordell.coinnection.domain.model.AssetPair;


public class AssetPairSetupPresenter extends AssetPairSetupContract.Presenter {
    private static final String TAG = AssetPairSetupPresenter.class.getSimpleName();

    private GetAllAssetsInteractor mGetAllAssetsInteractor;
    private AddAssetPairInteractor mAddAssetPairInteractor;
    private GetAssetPairByIdInteractor mGetAssetPairByIdInteractor;

    private CompositeDisposable mCompositeDisposable;

    private boolean mIsWidgetSetup = false;

    @Inject
    AssetPairSetupPresenter(GetAllAssetsInteractor getAllAssetsInteractor,
                            AddAssetPairInteractor addAssetPairInteractor,
                            GetAssetPairByIdInteractor getAssetPairByIdInteractor) {
        mGetAllAssetsInteractor = getAllAssetsInteractor;
        mAddAssetPairInteractor = addAssetPairInteractor;
        mGetAssetPairByIdInteractor = getAssetPairByIdInteractor;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setIsWidgetSetup() {
        mIsWidgetSetup = true;
    }

    @Override
    public void initialize() {
        getAllAssets();
    }

    private void getAllAssets() {
        mCompositeDisposable.add(mGetAllAssetsInteractor.execute(new DisposableSubscriber<List<Asset>>() {
            @Override
            public void onNext(List<Asset> assets) {
                if (assets.size() == 0) {
                    mView.showMessage("Error getting assets, please reload");
                }
                mView.setAssets(assets);
            }

            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        }));
    }

    @Override
    public void onAssetPairSelected(@NonNull AssetPair assetPair) {
        if(mIsWidgetSetup) {
            mView.assetPairLoaded(assetPair);
        } else {
            mAddAssetPairInteractor.execute(assetPair, new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    getAssetPair(assetPair);
                }
                @Override
                public void onError(Throwable e) {
                    if (e instanceof SQLiteConstraintException) {
                        mView.showMessage("Asset Pair " + assetPair.getSymbol() + "/" + assetPair.getQuoteCurrencySymbol() + " already exists");
                    }
                }
            });
        }
    }

    private void getAssetPair(@NonNull final AssetPair assetPair) {
        mCompositeDisposable.add(mGetAssetPairByIdInteractor.execute(new Pair<>(
                        assetPair.getId(),
                        assetPair.getQuoteCurrencySymbol()),
                new DisposableSubscriber<AssetPair>() {
                    @Override
                    public void onNext(AssetPair assetPair) {
                        mView.assetPairLoaded(assetPair);
                    }
                    @Override public void onError(Throwable t) {}
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void destroy() {
        mCompositeDisposable.dispose();

        super.destroy();
    }
}