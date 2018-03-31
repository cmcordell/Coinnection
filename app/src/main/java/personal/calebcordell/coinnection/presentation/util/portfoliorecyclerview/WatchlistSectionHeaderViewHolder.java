package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.presentation.util.SimpleSpinnerAdapter;


public class WatchlistSectionHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_empty_text)
    TextView mListEmptyTextView;
    @BindView(R.id.section_title)
    TextView mSectionTitleTextView;
    @BindView(R.id.asset_info_spinner)
    Spinner mAssetInfoSpinner;

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    WatchlistSectionHeaderViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(itemView.getContext(),
                R.array.spinner_asset_info, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAssetInfoSpinner.setAdapter(adapter);
    }

    public void bind(final int watchlistListSize, final AdapterView.OnItemSelectedListener onItemSelectedListener, final int assetInfoShown) {
        if (watchlistListSize > 0) {
            mListEmptyTextView.setVisibility(View.GONE);
            mSectionTitleTextView.setVisibility(View.VISIBLE);
            mAssetInfoSpinner.setVisibility(View.VISIBLE);

            if (mOnItemSelectedListener == null) {
                mAssetInfoSpinner.setOnItemSelectedListener(onItemSelectedListener);
                mOnItemSelectedListener = onItemSelectedListener;
            }

            mAssetInfoSpinner.setSelection(assetInfoShown);
        } else {
            mListEmptyTextView.setVisibility(View.VISIBLE);
            mSectionTitleTextView.setVisibility(View.GONE);
            mAssetInfoSpinner.setVisibility(View.GONE);
        }
    }
}
