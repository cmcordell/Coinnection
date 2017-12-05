package personal.calebcordell.coinnection.presentation.views.allassets;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Asset;


public interface AllAssetsContract {
    interface View {
        void showAssets(List<Asset> assets);
        void updateAssets(List<Asset> assets);

        void openAssetDetailViewUI(Asset asset);
    }

    interface Presenter {
        void start();

        void resume();

        void pause();

        void destroy();
    }
}