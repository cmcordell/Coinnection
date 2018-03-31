package personal.calebcordell.coinnection.presentation.views.portfolio;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class PortfolioModule {
    @Binds
    @PerFragment
    abstract PortfolioContract.Presenter
    providePortfolioPresenter(PortfolioPresenter portfolioPresenter);
}