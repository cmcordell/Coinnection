package personal.calebcordell.coinnection.presentation.views.base;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import dagger.android.support.DaggerAppCompatDialogFragment;


public class BaseDialogFragment extends DaggerAppCompatDialogFragment {

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation;

        if(nextAnim == 0) {
            animation = super.onCreateAnimation(transit, enter, nextAnim);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            animation.setAnimationListener(mAnimationListener);
        }

        return animation;
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