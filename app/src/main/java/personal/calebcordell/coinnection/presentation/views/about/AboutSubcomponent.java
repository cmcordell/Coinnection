package personal.calebcordell.coinnection.presentation.views.about;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = AboutModule.class)
public interface AboutSubcomponent extends AndroidInjector<AboutFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AboutFragment> {
    }
}