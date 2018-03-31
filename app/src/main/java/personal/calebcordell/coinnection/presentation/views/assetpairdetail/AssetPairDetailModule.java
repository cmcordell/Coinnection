package personal.calebcordell.coinnection.presentation.views.assetpairdetail;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AssetPairDetailModule {
    @Binds
    @PerFragment
    abstract AssetPairDetailContract.Presenter
    provideAssetPairDetailPresenter(AssetPairDetailPresenter assetPairDetailPresenter);
}