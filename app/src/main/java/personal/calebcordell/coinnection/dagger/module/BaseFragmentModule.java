package personal.calebcordell.coinnection.dagger.module;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import personal.calebcordell.coinnection.dagger.PerFragment;


@Module
public abstract class BaseFragmentModule {
    public static final String FRAGMENT_LAYOUT_MANAGER = "BaseFragmentModule.fragmentLayoutManager";
    public static final String CHILD_FRAGMENT_MANAGER = "BaseFragmentModule.childFragmentManager";

    @Provides
    @PerFragment
    @Named(CHILD_FRAGMENT_MANAGER)
    static FragmentManager providesChildFragmentManager(Fragment fragment) {
        return fragment.getChildFragmentManager();
    }

    @Provides
    @PerFragment
    @Named(FRAGMENT_LAYOUT_MANAGER)
    static LinearLayoutManager providesLayoutManager(@Named(BaseActivityModule.ACTIVITY_CONTEXT) Context context) {
        return new LinearLayoutManager(context);
    }
}