package personal.calebcordell.coinnection.data.assetdata;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import personal.calebcordell.coinnection.data.base.AppDatabase;
import personal.calebcordell.coinnection.domain.model.Asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import personal.calebcordell.coinnection.presentation.App;


public class AssetDiskDataStore {
    private static final String TAG = AssetDiskDataStore.class.getSimpleName();
    //Used to calculate milliseconds from days
    private static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
    //We need to update info about each crypto every so often, 5 minutes (This is how often coinmarketcap updates their endpoints)
    private static final long INDIVIDUAL_EXPIRATION_TIME = 5 * 60 * 1000;

    private static AssetDiskDataStore INSTANCE;
    
    private Flowable<List<Asset>> mAllProcessor;
    @NonNull
    private final Map<String, Flowable<Asset>> mProcessorMap = new HashMap<>();

    private AssetDao mAssetDao;

    public AssetDiskDataStore() {
        mAssetDao = AppDatabase.getDatabase(App.getAppContext()).assetDao();
    }
    public static AssetDiskDataStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AssetDiskDataStore();
        }
        return INSTANCE;
    }

    public void storeSingular(@NonNull final Asset asset) {
        Completable.fromRunnable(() -> mAssetDao.insert(asset))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    public void storeAll(@NonNull final List<Asset> assets) {
        Completable.fromRunnable(() -> mAssetDao.insertAll(assets))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    public void replaceAll(@NonNull final List<Asset> assets) {
        Completable.fromRunnable(() -> {
            mAssetDao.clear();
            mAssetDao.insertAll(assets);

        })
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    @NonNull
    public Flowable<Asset> getSingular(@NonNull final String id) {
        Flowable<Asset> flowable = getFlowableForKey(id);
        if(flowable == null) {
            flowable = mAssetDao.get(id)
                    .subscribeOn(Schedulers.computation());

            addFlowableWithKey(id, flowable);
        }

        return flowable;
    }

    @NonNull
    public Flowable<List<Asset>> getAll() {
        if(mAllProcessor == null) {
            mAllProcessor = mAssetDao.getAll()
                    .subscribeOn(Schedulers.computation());
        }
        return mAllProcessor;
    }

    
    private Flowable<Asset> getFlowableForKey(String key) {
        synchronized (mProcessorMap) {
            if(mProcessorMap.containsKey(key)) {
                return mProcessorMap.get(key);
            }
            return null;
        }
    }
    private void addFlowableWithKey(String key, Flowable<Asset> flowable) {
        synchronized (mProcessorMap) {
            if(!mProcessorMap.containsKey(key)) {
                mProcessorMap.put(key, flowable);
            }
        }
    }
}