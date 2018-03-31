package personal.calebcordell.coinnection.data.assetpairdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public abstract class AssetPairDao {

    @Query("SELECT * FROM asset_pairs WHERE id = :baseAssetId AND quote_currency_symbol = :quoteCurrencySymbol LIMIT 1")
    abstract Flowable<AssetPairEntity> get(String baseAssetId, String quoteCurrencySymbol);
    @Query("SELECT * FROM asset_pairs ORDER BY position")
    abstract Flowable<List<AssetPairEntity>> getAll();

    @Query("SELECT * FROM asset_pairs")
    abstract Single<List<AssetPairEntity>> getAllSingle();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract void insert(AssetPairEntity assetPairEntity);

    @Insert
    abstract void insertAll(List<AssetPairEntity> assetPairEntities);
    @Transaction
    void saveAll(List<AssetPairEntity> assetPairEntities) {
        removeAll();
        insertAll(assetPairEntities);
    }

    @Update
    abstract void update(AssetPairEntity assetPairEntity);
    @Update
    abstract void updateAll(List<AssetPairEntity> assetPairEntities);

    @Query("DELETE FROM asset_pairs WHERE id = :id AND quote_currency_symbol = :quoteCurrencySymbol")
    abstract void remove(String id, String quoteCurrencySymbol);
    @Query("DELETE FROM asset_pairs")
    abstract void removeAll();

    @Query("SELECT COUNT(1) FROM asset_pairs")
    abstract int getCount();
}