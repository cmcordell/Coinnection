package personal.calebcordell.coinnection.presentation.util.allassetsrecyclerview;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class AllAssetSearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.search_view) SearchView mAssetSearchView;
    @BindView(R.id.sort_spinner) Spinner mAssetSortSpinner;
    @BindView(R.id.sort_direction_button) ImageButton mAssetSortDirectionButton;

    @BindString(R.string.search_hint) String mSearchHint;

    @BindDrawable(R.drawable.ic_arrow_upward_black) Drawable mSortDirectionDrawable;

    private int mSortDirection;

    AllAssetSearchViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final SearchView.OnQueryTextListener onQueryTextListener,
                     final AdapterView.OnItemSelectedListener onItemSelectedListener,
                     final OnObjectItemClickListener<Integer> onObjectItemClickListener,
                     final int infoShown, final int sortDirection) {

        mAssetSearchView.setIconified(false);
        mAssetSearchView.setQueryHint(mSearchHint);
        mAssetSearchView.setOnQueryTextListener(onQueryTextListener);

        mAssetSortSpinner.setSelection(infoShown);
        mAssetSortSpinner.setOnItemSelectedListener(onItemSelectedListener);

        mSortDirection = sortDirection;
        mAssetSortDirectionButton.setImageDrawable(mSortDirectionDrawable);
        if(mSortDirection == Constants.SORT_DIRECTION_ASCENDING) {
            mAssetSortDirectionButton.setRotation(180);
        }
        mAssetSortDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSortDirection == Constants.SORT_DIRECTION_DESCENDING) {
                    mSortDirection = Constants.SORT_DIRECTION_ASCENDING;
                } else {
                    mSortDirection = Constants.SORT_DIRECTION_DESCENDING;
                }
                onObjectItemClickListener.onObjectItemClick(mSortDirection);

                mAssetSortDirectionButton.animate()
                        .rotation(180)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        });
    }
}