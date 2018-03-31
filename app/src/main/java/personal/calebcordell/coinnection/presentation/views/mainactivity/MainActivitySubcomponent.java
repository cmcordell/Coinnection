package personal.calebcordell.coinnection.presentation.views.mainactivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerActivity;


@PerActivity
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }
}