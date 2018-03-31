package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor1;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class GetAllAssetPairWidgetsInteractor extends SingleInteractor1<List<AssetPairWidget>> {
    private static final String TAG = GetAllAssetPairWidgetsInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public GetAllAssetPairWidgetsInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Single<List<AssetPairWidget>> buildSingle() {
        return mWidgetRepository.getAllWidgets()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
