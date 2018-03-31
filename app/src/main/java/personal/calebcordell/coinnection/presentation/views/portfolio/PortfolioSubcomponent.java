package personal.calebcordell.coinnection.presentation.views.portfolio;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = PortfolioModule.class)
public interface PortfolioSubcomponent extends AndroidInjector<PortfolioFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PortfolioFragment> {
    }
}