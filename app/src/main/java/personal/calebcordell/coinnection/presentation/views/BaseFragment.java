package personal.calebcordell.coinnection.presentation.views;

import android.os.Bundle;
import android.app.Fragment;


public abstract class BaseFragment extends Fragment {

    protected BackHandlerInterface backHandlerInterface;

    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Mark this fragment as the selected Fragment.
        backHandlerInterface.setSelectedFragment(this);
    }

    //TODO setTitle is not part of backhandlerinterface, create a seperate interface
    public interface BackHandlerInterface {
        void setSelectedFragment(BaseFragment baseFragment);

        void setTitle(String title);
    }
}