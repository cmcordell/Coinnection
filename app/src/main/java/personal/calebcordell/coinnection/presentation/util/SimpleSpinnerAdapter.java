package personal.calebcordell.coinnection.presentation.util;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;


public class SimpleSpinnerAdapter extends ArrayAdapter<CharSequence> {

    public SimpleSpinnerAdapter(@NonNull Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
        super(context, textViewResId, 0, Arrays.asList(context.getResources().getTextArray(textArrayResId)));
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(((Spinner) parent).getSelectedItemPosition(), convertView, parent);
    }
}