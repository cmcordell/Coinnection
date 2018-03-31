package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor1;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class FetchAndGetAllAssetPairWidgetsInteractor extends SingleInteractor1<List<AssetPairWidget>> {
    private static final String TAG = FetchAndGetAllAssetPairWidgetsInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mAssetPairWidgetRepository;

    @Inject
    public FetchAndGetAllAssetPairWidgetsInteractor(AssetPairWidgetRepository assetPairWidgetRepository) {
        mAssetPairWidgetRepository = assetPairWidgetRepository;
    }

    protected Single<List<AssetPairWidget>> buildSingle() {
        return mAssetPairWidgetRepository.fetchAllWidgetAssetPairs()
                .andThen(mAssetPairWidgetRepository.getAllWidgets())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
