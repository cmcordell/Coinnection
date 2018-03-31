package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import android.util.SparseIntArray;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class RestoreAssetPairWidgetsInteractor extends CompletableInteractor<SparseIntArray> {
    private static final String TAG = RestoreAssetPairWidgetsInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public RestoreAssetPairWidgetsInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Completable buildCompletable(SparseIntArray idsArray) {
        return mWidgetRepository.getAllWidgets()
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .map((assetPairWidget) -> {
                    int newId = idsArray.get(assetPairWidget.getId());
                    if(newId != 0) {
                        assetPairWidget.setId(newId);
                    }
                    return assetPairWidget;
                })
                .toList()
                .flatMapCompletable((widgets) ->
                        mWidgetRepository.clearWidgets()
                                .andThen(mWidgetRepository.insertWidgets(widgets))
                )
                .observeOn(AndroidSchedulers.mainThread());
    }
}