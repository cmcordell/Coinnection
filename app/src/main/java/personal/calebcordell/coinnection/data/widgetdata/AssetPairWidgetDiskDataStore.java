package personal.calebcordell.coinnection.data.widgetdata;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetpairdata.AssetPairEntity;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;


@Singleton
public class AssetPairWidgetDiskDataStore {
    private static final String TAG = AssetPairWidgetDiskDataStore.class.getSimpleName();

    private final AssetPairWidgetDao mAssetPairWidgetDao;
    private final AssetPairWidgetMapper mAssetPairWidgetMapper;

    @Inject
    public AssetPairWidgetDiskDataStore(AssetPairWidgetDao assetPairWidgetDao,
                                        AssetPairWidgetMapper assetPairWidgetMapper) {
        mAssetPairWidgetDao = assetPairWidgetDao;
        mAssetPairWidgetMapper = assetPairWidgetMapper;
    }

    public Single<AssetPairWidget> getWidget(final int id) {
        return mAssetPairWidgetDao.get(id)
                .subscribeOn(Schedulers.computation())
                .map(mAssetPairWidgetMapper::mapUp);
    }
    public Single<List<AssetPairWidget>> getAllWidgets() {
        return mAssetPairWidgetDao.getAll()
                .subscribeOn(Schedulers.computation())
                .map(mAssetPairWidgetMapper::mapUp);
    }

    public Completable insertWidget(@NonNull final AssetPairWidget widget) {
        return Completable.fromRunnable(() -> mAssetPairWidgetDao.insert(mAssetPairWidgetMapper.mapDown(widget)))
                .subscribeOn(Schedulers.computation());
    }
    public Completable insertWidgets(@NonNull final List<AssetPairWidget> widgets) {
        return Completable.fromRunnable(() -> mAssetPairWidgetDao.insert(mAssetPairWidgetMapper.mapDown(widgets)))
                .subscribeOn(Schedulers.computation());
    }

    public Completable updateWidget(@NonNull final AssetPairEntity entity) {
        return Completable.fromRunnable(() -> {
            List<AssetPairWidgetEntity> widgetEntities = mAssetPairWidgetDao.get(entity.getId(), entity.getQuoteCurrencySymbol());

            for(AssetPairWidgetEntity widgetEntity : widgetEntities) {
                AssetPairWidgetEntity newEntity = new AssetPairWidgetEntity(
                        widgetEntity.getId(),
                        widgetEntity.getAssetId(),
                        widgetEntity.getQuoteCurrencySymbol(),
                        entity.getName(),
                        entity.getSymbol(),
                        entity.getRank(),
                        entity.getPrice(),
                        entity.getVolume24Hour(),
                        entity.getMarketCap(),
                        entity.getAvailableSupply(),
                        entity.getTotalSupply(),
                        entity.getMaxSupply(),
                        entity.getPercentChange1h(),
                        entity.getPercentChange24h(),
                        entity.getPercentChange7d(),
                        entity.getLastUpdated());

                mAssetPairWidgetDao.insert(newEntity);
            }
        })
                .subscribeOn(Schedulers.computation());
    }
    public Completable updateWidgets(@NonNull final List<AssetPairEntity> entities) {
        return Flowable.fromIterable(entities)
                .subscribeOn(Schedulers.computation())
                .flatMapCompletable(this::updateWidget);
    }

    public Completable deleteWidget(final int id) {
        return Completable.fromRunnable(() -> mAssetPairWidgetDao.remove(id))
                .subscribeOn(Schedulers.computation());
    }
    public Completable deleteWidgets(final int[] ids) {
        return Completable.fromRunnable(() -> mAssetPairWidgetDao.remove(ids))
                .subscribeOn(Schedulers.computation());
    }
    public Completable clearWidgets() {
        return Completable.fromRunnable(mAssetPairWidgetDao::clear)
                .subscribeOn(Schedulers.computation());
    }
}