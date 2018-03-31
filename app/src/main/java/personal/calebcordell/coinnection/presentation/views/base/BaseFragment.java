package personal.calebcordell.coinnection.presentation.views.base;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;
import personal.calebcordell.coinnection.dagger.module.BaseActivityModule;


public abstract class BaseFragment extends DaggerFragment {
    private static final int DEFAULT_CHILD_ANIMATION_DURATION = 250;

    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager mFragmentManager;

    @Inject
    protected BackHandlerInterface mBackHandlerInterface;

    public abstract boolean onBackPressed();

    @Override
    public void onStart() {
        super.onStart();
        mBackHandlerInterface.setSelectedFragment(this);
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        final Fragment parent = getParentFragment();

        Animation animation;
        // Apply the workaround only if this is a child fragment, and the parent
        // is being removed.
        if(nextAnim == 0) {
            animation = super.onCreateAnimation(transit, enter, nextAnim);
        } else  if (!enter && parent != null && parent.isRemoving()) {
            // Apply the workaround only if this is a child fragment, and the parent
            // is being removed.

            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            animation = new AlphaAnimation(1, 1);
            animation.setDuration(getNextAnimationDuration(parent, nextAnim, DEFAULT_CHILD_ANIMATION_DURATION));
            animation.setAnimationListener(mAnimationListener);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            animation.setAnimationListener(mAnimationListener);
        }

        return animation;
    }

    private static long getNextAnimationDuration(Fragment fragment, int nextAnim, long defValue) {
        try {
            Animation animation = AnimationUtils.loadAnimation(fragment.getActivity(), nextAnim);

            //If it can be loaded, return that animation's duration
            return (animation == null) ? defValue : animation.getDuration();
        } catch(Resources.NotFoundException ex) {
            return defValue;
        }
    }

    public interface BackHandlerInterface {
        void setSelectedFragment(BaseFragment baseFragment);
    }

    public interface ParentActivityInterface {
        void setActionBarElevation(final int elevationInDp);

        void setHomeAsUp(final boolean isHomeAsUp);

        View getRootLayout();
    }

    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationStart(Animation animation) {
            if(getActivity() != null) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        public void onAnimationRepeat(Animation animation) {}
        public void onAnimationEnd(Animation animation) {
            if(getActivity() != null) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    };
}