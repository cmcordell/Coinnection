package personal.calebcordell.coinnection.data.portfolioassetdata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public interface PortfolioAssetDao {
    @Query("SELECT * FROM portfolio_assets WHERE id = :id LIMIT 1")
    Flowable<List<PortfolioAssetEntity>> get(String id);

    @Query("SELECT * FROM portfolio_assets ORDER BY position")
    Flowable<List<PortfolioAssetEntity>> getAll();

    @Query("SELECT id FROM portfolio_assets")
    Single<List<String>> getAllIds();

    @Query("SELECT COUNT(1) FROM portfolio_assets")
    int getCount();

    @Query("SELECT position FROM portfolio_assets WHERE id = :id LIMIT 1")
    int getPosition(String id);
    @Query("UPDATE portfolio_assets SET position = position - 1 WHERE position > :position")
    void updatePositions(int position);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PortfolioAssetEntity portfolioAssetEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PortfolioAssetEntity> portfolioAssetEntities);

    @Query("DELETE FROM portfolio_assets WHERE id = :id")
    void remove(String id);

    @Query("DELETE FROM portfolio_assets")
    void clear();
}