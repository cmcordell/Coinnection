package personal.calebcordell.coinnection.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import personal.calebcordell.coinnection.dagger.module.AppModule;
import personal.calebcordell.coinnection.dagger.module.DiskModule;
import personal.calebcordell.coinnection.dagger.module.NetworkModule;
import personal.calebcordell.coinnection.dagger.module.RepositoryModule;
import personal.calebcordell.coinnection.presentation.App;


@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        DiskModule.class,
        RepositoryModule.class})
public interface AppComponent extends AndroidInjector<App> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {}
}