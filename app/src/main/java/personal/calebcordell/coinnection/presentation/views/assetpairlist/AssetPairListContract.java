package personal.calebcordell.coinnection.presentation.views.assetpairlist;

import android.support.annotation.NonNull;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.AssetPair;
import personal.calebcordell.coinnection.presentation.views.base.BasePresenter;


public interface AssetPairListContract {
    interface View {
        void showEmptyText();

        void hideEmptyText();

        void showAssetPairs(@NonNull List<AssetPair> assetPairs);

        void openAssetPairDetailViewUI(@NonNull AssetPair assetPair);

        void openAssetPairSetupUI();

        void showMessage(@NonNull String message);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void resume();

        abstract void pause();

        abstract void getAssetPairs();

        abstract void onAssetPairClicked(@NonNull AssetPair assetPair);

        abstract void onAddAssetPairClicked();

        abstract void onAssetPairMoved(@NonNull List<AssetPair> assetPairs);
    }
}