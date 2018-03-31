package personal.calebcordell.coinnection.presentation.util.aboutrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AboutItem;
import personal.calebcordell.coinnection.presentation.Constants;


public class AboutItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    CardView mCardView;
    @BindView(R.id.about_item_icon)
    ImageView mAboutItemIcon;
    @BindView(R.id.about_item_title)
    TextView mAboutItemTitle;

    AboutItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(AboutItem item) {
        if (item.getIconDrawable() != null) {
            Glide.with(itemView.getContext())
                    .load(item.getIconDrawable())
                    .into(mAboutItemIcon);
        }

        if (item.getTitle() != null) {
            mAboutItemTitle.setText(item.getTitle());
        }
        if (item.getOnClickListener() != null) {
            mCardView.setOnClickListener((view) ->
                    ViewCompat.postOnAnimationDelayed(view,
                            () -> item.getOnClickListener().onClick(view), Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
        }

    }
}