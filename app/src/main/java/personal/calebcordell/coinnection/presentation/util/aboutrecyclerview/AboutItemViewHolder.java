package personal.calebcordell.coinnection.presentation.util.aboutrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.AboutItem;


public class AboutItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view) CardView mCardView;
    @BindView(R.id.about_item_icon) ImageView mAboutItemIcon;
    @BindView(R.id.about_item_title) TextView mAboutItemTitle;

    AboutItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(AboutItem item) {
        if(item.getIconDrawable() != null) {
            mAboutItemIcon.setImageDrawable(item.getIconDrawable());
        }
        if(item.getTitle() != null) {
            mAboutItemTitle.setText(item.getTitle());
        }
        if(item.getOnClickListener() != null) {
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewCompat.postOnAnimationDelayed(view, new Runnable() {
                        @Override
                        public void run() {
                            item.getOnClickListener().onClick(view);
                        }
                    }, 100);
                }
            });
        }

    }
}