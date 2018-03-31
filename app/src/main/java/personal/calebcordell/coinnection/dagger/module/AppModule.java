package personal.calebcordell.coinnection.dagger.module;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.BroadcastReceiverKey;
import dagger.android.ServiceKey;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.IntoMap;
import personal.calebcordell.coinnection.presentation.App;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivity;
import personal.calebcordell.coinnection.presentation.views.mainactivity.MainActivitySubcomponent;
import personal.calebcordell.coinnection.presentation.views.startupscreenactivity.StartUpScreenActivity;
import personal.calebcordell.coinnection.presentation.views.startupscreenactivity.StartUpScreenActivitySubcomponent;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetProvider;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetProviderSubcomponent;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetSetupActivity;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetSetupActivitySubcomponent;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetUpdateService;
import personal.calebcordell.coinnection.widgets.assetpair.AssetPairWidgetUpdateServiceSubcomponent;


@Module(includes = AndroidSupportInjectionModule.class,
        subcomponents = {
                StartUpScreenActivitySubcomponent.class,
                MainActivitySubcomponent.class,
                AssetPairWidgetSetupActivitySubcomponent.class,
                AssetPairWidgetProviderSubcomponent.class,
                AssetPairWidgetUpdateServiceSubcomponent.class})
public abstract class AppModule {
    public static final String APP_CONTEXT = "AppModule.appContext";

    @Binds
    @IntoMap
    @ActivityKey(StartUpScreenActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindStartUpScreenActivity(StartUpScreenActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindMainActivity(MainActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(AssetPairWidgetSetupActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindAssetPairWidgetSetupActivity(AssetPairWidgetSetupActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @BroadcastReceiverKey(AssetPairWidgetProvider.class)
    abstract AndroidInjector.Factory<? extends BroadcastReceiver>
    bindAssetPairWidgetProvider(AssetPairWidgetProviderSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ServiceKey(AssetPairWidgetUpdateService.class)
    abstract AndroidInjector.Factory<? extends Service>
    bindAssetPairWidgetUpdateService(AssetPairWidgetUpdateServiceSubcomponent.Builder builder);

    @Binds
    @Named(APP_CONTEXT)
    @Singleton
    abstract Context bindContext(App app);

    @Provides
    @Singleton
    static SharedPreferences provideSharedPreferences(@Named(APP_CONTEXT) Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}