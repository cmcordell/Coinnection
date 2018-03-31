package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.domain.model.Asset;


@Singleton
public class AssetDiskDataStore {
    private static final String TAG = AssetDiskDataStore.class.getSimpleName();

    private Flowable<List<Asset>> mAllProcessor;
    private final Map<String, Flowable<Asset>> mProcessorMap = new HashMap<>();

    private final AssetDao mAssetDao;


    @Inject
    public AssetDiskDataStore(AssetDao assetDao) {
        mAssetDao = assetDao;
    }


    public void storeSingular(@NonNull final Asset asset) {
        Completable.fromRunnable(() -> mAssetDao.insert(asset))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void storeAll(@NonNull final List<Asset> assets) {
        Completable.fromRunnable(() -> mAssetDao.insertAll(assets))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void replaceAll(@NonNull final List<Asset> assets) {
        Completable.fromRunnable(() -> {
            mAssetDao.clear();
            mAssetDao.insertAll(assets);
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Completable removeSingular(@NonNull final String id) {
        removeFlowableForKey(id);
        return Completable.fromRunnable(() -> mAssetDao.remove(id))
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    public Flowable<Asset> getSingular(@NonNull final String id) {
        Flowable<Asset> flowable = getFlowableForKey(id);
        if (flowable == null) {
            flowable = mAssetDao.get(id)
                    .subscribeOn(Schedulers.io());

            addFlowableWithKey(id, flowable);
        }

        return flowable;
    }

    @NonNull
    public Flowable<List<Asset>> getAll() {
        if (mAllProcessor == null) {
            mAllProcessor = mAssetDao.getAll()
                    .subscribeOn(Schedulers.io());
        }
        return mAllProcessor;
    }


    private Flowable<Asset> getFlowableForKey(String key) {
        synchronized (mProcessorMap) {
            if (mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }

    private void addFlowableWithKey(String key, Flowable<Asset> flowable) {
        synchronized (mProcessorMap) {
            if (!mProcessorMap.containsKey(key)) {
                mProcessorMap.put(key, flowable);
            }
        }
    }

    private void removeFlowableForKey(@NonNull final String key) {
        synchronized (mProcessorMap) {
            mProcessorMap.remove(key);
        }
    }
}