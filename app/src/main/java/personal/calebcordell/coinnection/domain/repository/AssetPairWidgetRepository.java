package personal.calebcordell.coinnection.domain.repository;

import io.reactivex.Completable;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;


public interface AssetPairWidgetRepository extends WidgetRepository<AssetPairWidget> {
    Completable fetchAllWidgetAssetPairs();
    Completable fetchWidgetAssetPair(int widgetId);
}