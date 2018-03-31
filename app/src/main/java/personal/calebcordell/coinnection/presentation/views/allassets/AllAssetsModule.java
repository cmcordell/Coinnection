package personal.calebcordell.coinnection.presentation.views.allassets;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class AllAssetsModule {
    @Binds
    @PerFragment
    abstract AllAssetsContract.Presenter
    provideAllAssetsPresenter(AllAssetsPresenter allAssetsPresenter);
}