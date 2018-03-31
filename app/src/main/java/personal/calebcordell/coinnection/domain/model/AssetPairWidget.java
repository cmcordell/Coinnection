package personal.calebcordell.coinnection.domain.model;

import android.support.annotation.NonNull;


public class AssetPairWidget extends Widget {

    @NonNull
    private AssetPair assetPair;

    public AssetPairWidget(int id, @NonNull AssetPair assetPair) {
        super(id);
        this.assetPair = assetPair;
    }

    @NonNull
    public AssetPair getAssetPair() {
        return assetPair;
    }

    public void setAssetPair(@NonNull AssetPair assetPair) {
        this.assetPair = assetPair;
    }
}