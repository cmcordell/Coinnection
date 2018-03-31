package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public abstract class PortfolioAssetDao {
    @Query("SELECT * FROM portfolio_assets WHERE id = :id LIMIT 1")
    abstract Flowable<List<PortfolioAssetEntity>> get(String id);

    @Query("SELECT * FROM portfolio_assets ORDER BY position")
    abstract Flowable<List<PortfolioAssetEntity>> getAll();

    @Query("SELECT id FROM portfolio_assets")
    abstract Single<List<String>> getAllIds();

    @Query("SELECT COUNT(1) FROM portfolio_assets")
    abstract int getCount();

    @Query("SELECT IFNULL((SELECT position FROM portfolio_assets WHERE id = :id LIMIT 1), -1)")
    abstract int getPosition(String id);

    @Query("UPDATE portfolio_assets SET position = position - 1 WHERE position > :position")
    abstract void updatePositions(int position);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertInternal(PortfolioAssetEntity portfolioAssetEntity);
    @Query("DELETE FROM watchlist_assets WHERE id = :id")
    abstract void insertRemovalFromWatchlistCondition(String id);
    @Transaction
    void insert(PortfolioAssetEntity portfolioAssetEntity) {
        insertInternal(portfolioAssetEntity);
        insertRemovalFromWatchlistCondition(portfolioAssetEntity.getId());
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<PortfolioAssetEntity> portfolioAssetEntities);

    @Query("DELETE FROM portfolio_assets WHERE id = :id")
    abstract void remove(String id);

    @Query("DELETE FROM portfolio_assets")
    abstract void clear();
}