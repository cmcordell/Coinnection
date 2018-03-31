package personal.calebcordell.coinnection.data.watchlistassetdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface WatchlistAssetDao {
    @Query("SELECT * FROM watchlist_assets WHERE id = :id LIMIT 1")
    Flowable<List<WatchlistAssetEntity>> get(String id);

    @Query("SELECT * FROM watchlist_assets ORDER BY position")
    Flowable<List<WatchlistAssetEntity>> getAll();

    @Query("SELECT id FROM watchlist_assets")
    Single<List<String>> getAllIds();

    @Query("SELECT COUNT(1) FROM watchlist_assets")
    int getCount();

    @Query("SELECT position FROM watchlist_assets WHERE id = :id LIMIT 1")
    int getPosition(String id);

    @Query("UPDATE watchlist_assets SET position = position - 1 WHERE position > :position")
    void updatePositions(int position);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WatchlistAssetEntity watchlistAssetEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WatchlistAssetEntity> watchlistAssetsEntities);

    @Query("DELETE FROM watchlist_assets WHERE id = :id")
    void remove(String id);

    @Query("DELETE FROM watchlist_assets")
    void clear();
}
