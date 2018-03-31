package personal.calebcordell.coinnection.data.widgetdata;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.assetdata.AssetNetworkDataStore;
import personal.calebcordell.coinnection.domain.model.AssetPairWidget;
import personal.calebcordell.coinnection.domain.repository.AssetPairWidgetRepository;


@Singleton
public class AssetPairWidgetRepositoryImpl implements AssetPairWidgetRepository {
    private static final String TAG = AssetPairWidgetRepositoryImpl.class.getSimpleName();

    private final AssetPairWidgetDiskDataStore mAssetPairWidgetDiskDataStore;
    private final AssetNetworkDataStore mAssetNetworkDataStore;

    @Inject
    public AssetPairWidgetRepositoryImpl(AssetPairWidgetDiskDataStore assetPairWidgetDiskDataStore, AssetNetworkDataStore assetNetworkDataStore) {
        mAssetPairWidgetDiskDataStore = assetPairWidgetDiskDataStore;
        mAssetNetworkDataStore = assetNetworkDataStore;
    }

    public Single<AssetPairWidget> getWidget(final int id) {
        return mAssetPairWidgetDiskDataStore.getWidget(id);
    }
    public Single<List<AssetPairWidget>> getAllWidgets() {
        return mAssetPairWidgetDiskDataStore.getAllWidgets();
    }

    public Completable insertWidget(@NonNull final AssetPairWidget widget) {
        return mAssetPairWidgetDiskDataStore.insertWidget(widget);
    }
    public Completable insertWidgets(@NonNull final List<AssetPairWidget> widgets) {
        return mAssetPairWidgetDiskDataStore.insertWidgets(widgets);
    }

    public Completable deleteWidget(final int id) {
        return mAssetPairWidgetDiskDataStore.deleteWidget(id);
    }
    public Completable deleteWidgets(final int[] ids) {
        return mAssetPairWidgetDiskDataStore.deleteWidgets(ids);
    }

    public Completable clearWidgets() {
        return mAssetPairWidgetDiskDataStore.clearWidgets();
    }

    public Completable fetchAllWidgetAssetPairs() {
        return mAssetPairWidgetDiskDataStore.getAllWidgets()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::mapWidgetsToPairs)
                .flatMap(mAssetNetworkDataStore::assetPairs)
                .flatMapCompletable(mAssetPairWidgetDiskDataStore::updateWidgets);
    }
    public Completable fetchWidgetAssetPair(final int widgetId) {
        return mAssetPairWidgetDiskDataStore.getWidget(widgetId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::mapWidgetToPair)
                .flatMap(mAssetNetworkDataStore::assetPair)
                .flatMapCompletable(mAssetPairWidgetDiskDataStore::updateWidget);
    }

    private Pair<String, String> mapWidgetToPair(@NonNull final AssetPairWidget widget) {
        return new Pair<>(widget.getAssetPair().getId(), widget.getAssetPair().getQuoteCurrencySymbol());
    }
    private List<Pair<String, String>> mapWidgetsToPairs(@NonNull final List<AssetPairWidget> widgets) {
        List<Pair<String, String>> pairs = new ArrayList<>(widgets.size());

        for(AssetPairWidget widget : widgets) {
            pairs.add(mapWidgetToPair(widget));
        }

        return pairs;
    }
}