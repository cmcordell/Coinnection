package personal.calebcordell.coinnection.presentation.views.startupscreenactivity;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import personal.calebcordell.coinnection.dagger.PerActivity;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;


@Module(includes = BaseActivityModule.class)
public abstract class StartUpScreenActivityModule {
    @Binds
    @PerActivity
    abstract Activity provideActivity(StartUpScreenActivity startUpScreenActivity);
}