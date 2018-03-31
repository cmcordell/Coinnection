package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class RemoveAssetPairWidgetsInteractor extends CompletableInteractor<int[]> {
    private static final String TAG = RemoveAssetPairWidgetsInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public RemoveAssetPairWidgetsInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Completable buildCompletable(final int[] widgetIds) {
        return mWidgetRepository.deleteWidgets(widgetIds)
                .observeOn(AndroidSchedulers.mainThread());
    }
}