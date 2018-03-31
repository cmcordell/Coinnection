package personal.calebcordell.coinnection.data.assetdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Asset;


@Dao
public interface AssetDao {
    @Query("SELECT * FROM assets WHERE id = :id")
    Flowable<Asset> get(String id);

    @Query("SELECT * FROM assets ORDER BY rank")
    Flowable<List<Asset>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Asset asset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Asset> assets);

    @Query("DELETE FROM assets WHERE id = :id")
    void remove(String id);

    @Query("DELETE FROM assets")
    void clear();
}