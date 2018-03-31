package personal.calebcordell.coinnection.domain.model;


public class WatchlistAsset extends Asset {

    private int position;

    public WatchlistAsset() {

    }

    public WatchlistAsset(Asset asset) {
        super(asset);
        position = -1;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}