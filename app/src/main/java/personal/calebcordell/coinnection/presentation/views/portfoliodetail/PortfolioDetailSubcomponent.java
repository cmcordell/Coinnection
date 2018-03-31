package personal.calebcordell.coinnection.presentation.views.portfoliodetail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = PortfolioDetailModule.class)
public interface PortfolioDetailSubcomponent extends AndroidInjector<PortfolioDetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<PortfolioDetailFragment> {}
}