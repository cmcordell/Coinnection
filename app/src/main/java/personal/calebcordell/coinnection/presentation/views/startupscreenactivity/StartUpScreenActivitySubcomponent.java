package personal.calebcordell.coinnection.presentation.views.startupscreenactivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerActivity;


@PerActivity
@Subcomponent(modules = StartUpScreenActivityModule.class)
public interface StartUpScreenActivitySubcomponent extends AndroidInjector<StartUpScreenActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<StartUpScreenActivity> {
    }
}