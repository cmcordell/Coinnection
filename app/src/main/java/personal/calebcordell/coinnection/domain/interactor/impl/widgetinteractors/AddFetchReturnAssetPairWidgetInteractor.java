package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.SingleInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class AddFetchReturnAssetPairWidgetInteractor extends SingleInteractor<AssetPairWidget, AssetPairWidget> {
    private static final String TAG = AddAssetPairWidgetInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public AddFetchReturnAssetPairWidgetInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Single<AssetPairWidget> buildSingle(@NonNull final AssetPairWidget widget) {
        return mWidgetRepository.insertWidget(widget)
                .andThen(mWidgetRepository.fetchWidgetAssetPair(widget.getId()))
                .andThen(mWidgetRepository.getWidget(widget.getId()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}