package personal.calebcordell.coinnection.presentation;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;
import personal.calebcordell.coinnection.dagger.component.DaggerAppComponent;


public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(e -> {});
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AndroidInjector<App> appAndroidInjector = DaggerAppComponent.builder().create(this);
        appAndroidInjector.inject(this);
        return appAndroidInjector;
    }
}