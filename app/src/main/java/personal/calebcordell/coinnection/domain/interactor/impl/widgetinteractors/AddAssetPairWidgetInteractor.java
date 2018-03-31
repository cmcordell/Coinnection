package personal.calebcordell.coinnection.domain.interactor.impl.widgetinteractors;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import personal.calebcordell.coinnection.domain.interactor.base.CompletableInteractor;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


public class AddAssetPairWidgetInteractor extends CompletableInteractor<AssetPairWidget> {
    private static final String TAG = AddAssetPairWidgetInteractor.class.getSimpleName();

    private final AssetPairWidgetRepository mWidgetRepository;

    @Inject
    public AddAssetPairWidgetInteractor(AssetPairWidgetRepository widgetRepository) {
        mWidgetRepository = widgetRepository;
    }

    protected Completable buildCompletable(@NonNull final AssetPairWidget widget) {
        return mWidgetRepository.insertWidget(widget)
                .observeOn(AndroidSchedulers.mainThread());
    }
}