package personal.calebcordell.coinnection.dagger.module;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import personal.calebcordell.coinnection.dagger.PerActivity;


@Module
public abstract class BaseActivityModule {
    public static final String ACTIVITY_FRAGMENT_MANAGER = "BaseActivityModule.activityFragmentManager";
    public static final String ACTIVITY_CONTEXT = "BaseActivityModule.activityContext";

    @Binds
    @Named(ACTIVITY_CONTEXT)
    @PerActivity
    abstract Context provideActivityContext(Activity activity);

    @Provides
    @PerActivity
    static Resources provideActivityResources(Activity activity) {
        return activity.getResources();
    }

    @Provides
    @PerActivity
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

    @Provides
    @PerActivity
    static InputMethodManager provideInputMethodManager(Activity activity) {
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    @Named(ACTIVITY_FRAGMENT_MANAGER)
    @PerActivity
    static FragmentManager provideActivityFragmentManager(Activity activity) {
        return ((AppCompatActivity) activity).getSupportFragmentManager();
    }
}