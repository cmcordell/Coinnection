package personal.calebcordell.coinnection.presentation.util.licenserecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.LicenseItem;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class LicenseItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.license_item_title)
    TextView mLicenseItemTitle;

    LicenseItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final LicenseItem item, final OnObjectItemClickListener<LicenseItem> onObjectItemClickListener) {
        mLicenseItemTitle.setText(item.getTitle());

        if (onObjectItemClickListener != null) {
            itemView.setOnClickListener((view) -> ViewCompat.postOnAnimationDelayed(view,
                    () -> onObjectItemClickListener.onObjectItemClick(item), Constants.SELECTABLE_VIEW_ANIMATION_DELAY)
            );
        }
    }
}