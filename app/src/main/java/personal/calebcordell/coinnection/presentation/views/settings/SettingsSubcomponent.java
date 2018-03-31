package personal.calebcordell.coinnection.presentation.views.settings;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.PerFragment;


@PerFragment
@Subcomponent(modules = SettingsModule.class)
public interface SettingsSubcomponent extends AndroidInjector<SettingsFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SettingsFragment> {
    }
}