package personal.calebcordell.coinnection.presentation.views.assetdetailtab;

import personal.calebcordell.coinnection.domain.model.Asset;


interface AssetDetailTabContract {
    interface View {
        void showAsset(Asset asset);
    }

    interface Presenter {
        void start(String assetId);

        void addAssetToPortfolio(Asset asset, double balance);
        void removeAssetFromPortfolio(String assetId);

        void fragmentBecameInvisible();

        void destroy();
    }
}