package personal.calebcordell.coinnection.presentation.views.settings;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseFragmentModule;


@Module(includes = BaseFragmentModule.class)
public abstract class SettingsModule {
    @Binds
    @PerFragment
    abstract SettingsContract.Presenter
    provideSettingsPresenter(SettingsPresenter settingsPresenter);
}