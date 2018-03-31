package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor1;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class ClearAssetPairWidgetsInteractor extends CompletableInteractor1 {
    private static final String TAG = ClearAssetPairWidgetsInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public ClearAssetPairWidgetsInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Completable buildCompletable() {
        return mWidgetRepository.clearWidgets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}