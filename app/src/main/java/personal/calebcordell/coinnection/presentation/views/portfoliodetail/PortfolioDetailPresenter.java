package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import personal.calebcordell.coinnection.domain.interactor.impl.portfolioassetinteractors.GetAllPortfolioAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.GetAllWatchlistAssetsInteractor;
import personal.calebcordell.coinnection.domain.interactor.impl.watchlistassetinteractors.RemoveAssetsFromWatchlistInteractor;
import personal.calebcordell.coinnection.domain.model.PortfolioAsset;
import personal.calebcordell.coinnection.domain.model.WatchlistAsset;


public class PortfolioDetailPresenter extends PortfolioDetailContract.Presenter {
    private static final String TAG = PortfolioDetailPresenter.class.getSimpleName();

    private List<PortfolioAsset> mPortfolioAssets = new ArrayList<>();
    private List<WatchlistAsset> mWatchlistAssets = new ArrayList<>();
    private Map<String, Boolean> mWatchlistAssetsOnWatchlist = new HashMap<>();
    private int mCurrentAssetPosition = 0;
    private List<String> mWatchlistAssetIdsToRemove = new ArrayList<>();
    private boolean mInitialPortfolioAssetsLoaded = false;
    private boolean mInitialWatchlistAssetsLoaded = false;

    private GetAllPortfolioAssetsInteractor mGetAllPortfolioAssetsInteractor;
    private GetAllWatchlistAssetsInteractor mGetAllWatchlistAssetsInteractor;
    private RemoveAssetsFromWatchlistInteractor mRemoveAssetsFromWatchlistInteractor;

    private CompositeDisposable mCompositeDisposable;

    @Inject
    public PortfolioDetailPresenter(GetAllPortfolioAssetsInteractor getAllPortfolioAssetsInteractor,
                                    GetAllWatchlistAssetsInteractor getAllWatchlistAssetsInteractor,
                                    RemoveAssetsFromWatchlistInteractor removeAssetsFromWatchlistInteractor) {
        mGetAllPortfolioAssetsInteractor = getAllPortfolioAssetsInteractor;
        mGetAllWatchlistAssetsInteractor = getAllWatchlistAssetsInteractor;
        mRemoveAssetsFromWatchlistInteractor = removeAssetsFromWatchlistInteractor;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setPortfolioAssets(@NonNull final List<PortfolioAsset> portfolioAssets) {
        mPortfolioAssets = portfolioAssets;
    }

    @Override
    public void setWatchlistAssets(@NonNull final List<WatchlistAsset> watchlistAssets) {
        mWatchlistAssets = watchlistAssets;
        for (WatchlistAsset asset : mWatchlistAssets) {
            if (mWatchlistAssetsOnWatchlist.get(asset.getId()) == null) {
                mWatchlistAssetsOnWatchlist.put(asset.getId(), true);
            }
        }
    }

    @Override
    public void setInitialPosition(final int position) {
        mCurrentAssetPosition = position;
    }

    @Override
    public void setCurrentAssetPosition(int position) {
        mCurrentAssetPosition = position;
        if(position < mPortfolioAssets.size()) {
            mView.setCurrentAsset(mPortfolioAssets.get(position), false);
        } else if(position < mPortfolioAssets.size() + mWatchlistAssets.size()) {
            position -= mPortfolioAssets.size();
            mView.setCurrentAsset(mWatchlistAssets.get(position),
                    mWatchlistAssetsOnWatchlist.get(mWatchlistAssets.get(position).getId()));
        }
    }

    @Override
    public void initialize() {
        mView.setInitialPosition(mCurrentAssetPosition);
        setCurrentAssetPosition(mCurrentAssetPosition);

        getPortfolioAssets();
        getWatchlistAssets();
    }

    private void getPortfolioAssets() {
        mCompositeDisposable.add(mGetAllPortfolioAssetsInteractor.execute(new DisposableSubscriber<List<PortfolioAsset>>() {
            @Override
            public void onNext(List<PortfolioAsset> portfolioAssets) {
                if (mInitialPortfolioAssetsLoaded) {
                    setPortfolioAssets(portfolioAssets);
                    if (mWatchlistAssets.size() == 0 && mPortfolioAssets.size() == 0) {
                        mView.goBack();
                    } else {
                        mView.setPortfolioAssets(portfolioAssets);
                        setCurrentAssetPosition(mCurrentAssetPosition);
                    }
                }
                mInitialPortfolioAssetsLoaded = true;
            }

            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        }));
    }
    private void getWatchlistAssets() {
        mCompositeDisposable.add(mGetAllWatchlistAssetsInteractor.execute(new DisposableSubscriber<List<WatchlistAsset>>() {
            @Override
            public void onNext(List<WatchlistAsset> watchlistAssets) {
                if (mInitialWatchlistAssetsLoaded) {
                    setWatchlistAssets(watchlistAssets);
                    mView.setWatchlistAssets(watchlistAssets);
                    setCurrentAssetPosition(mCurrentAssetPosition);
                }
                mInitialWatchlistAssetsLoaded = true;
            }
            @Override public void onError(Throwable t) {}
            @Override public void onComplete() {}
        }));
    }

    @Override
    public void addAssetToWatchlist() {
        String id = mWatchlistAssets.get(mCurrentAssetPosition - mPortfolioAssets.size()).getId();
        mWatchlistAssetsOnWatchlist.put(id, true);
        mWatchlistAssetIdsToRemove.remove(id);
        mView.setCurrentAsset(mWatchlistAssets.get(mCurrentAssetPosition - mPortfolioAssets.size()), true);
    }
    @Override
    public void removeAssetFromWatchlist() {
        String id = mWatchlistAssets.get(mCurrentAssetPosition - mPortfolioAssets.size()).getId();
        mWatchlistAssetsOnWatchlist.put(id, false);
        if (!mWatchlistAssetIdsToRemove.contains(id)) {
            mWatchlistAssetIdsToRemove.add(id);
        }
        mView.setCurrentAsset(mWatchlistAssets.get(mCurrentAssetPosition - mPortfolioAssets.size()), false);
    }

    @Override
    public void destroy() {
        mCompositeDisposable.clear();

        removeAssetsFromWatchlist();

        super.destroy();
    }

    private void removeAssetsFromWatchlist() {
        mRemoveAssetsFromWatchlistInteractor.execute(mWatchlistAssetIdsToRemove,
                new DisposableCompletableObserver() {
                    @Override public void onComplete() {}
                    @Override public void onError(@NonNull Throwable e) {}
                });
    }
}