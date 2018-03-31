package personal.calebcordell.coinnection.presentation.util.licenserecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.LicenseItem;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class LicenseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LicenseItem> mItems = new ArrayList<>();
    private OnObjectItemClickListener<LicenseItem> mOnLicenseItemClickListener;

    @Inject
    public LicenseRecyclerViewAdapter() {}

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
            return new LicenseItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_license_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LicenseItemViewHolder) {
            ((LicenseItemViewHolder) viewHolder).bind(mItems.get(position), mOnLicenseItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(@NonNull final List<LicenseItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void setOnLicenseItemClickListener(final OnObjectItemClickListener<LicenseItem> onLicenseItemClickListener) {
        mOnLicenseItemClickListener = onLicenseItemClickListener;
    }
}