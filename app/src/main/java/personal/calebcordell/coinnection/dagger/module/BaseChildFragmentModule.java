package personal.calebcordell.coinnection.dagger.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import personal.calebcordell.coinnection.dagger.PerChildFragment;


@Module
public abstract class BaseChildFragmentModule {
    public static final String CHILD_FRAGMENT = "BaseChildFragmentModule.childFragment";
    public static final String CHILD_FRAGMENT_LAYOUT_MANAGER = "BaseChildFragmentModule.childFragmentLayoutManager";

    @Provides
    @PerChildFragment
    @Named(CHILD_FRAGMENT_LAYOUT_MANAGER)
    static LinearLayoutManager providesLayoutManager(@Named(BaseActivityModule.ACTIVITY_CONTEXT) Context context) {
        return new LinearLayoutManager(context);
    }
}