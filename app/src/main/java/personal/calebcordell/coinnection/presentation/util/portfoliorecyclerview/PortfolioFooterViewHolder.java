package personal.calebcordell.coinnection.presentation.util.portfoliorecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;


public class PortfolioFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.last_update_text)
    TextView mLastUpdateTextView;
    @BindString(R.string.last_updated_label)
    String mLastUpdateLabel;

    PortfolioFooterViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(long lastUpdated) {
        String lastUpdateString = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(lastUpdated));
        mLastUpdateTextView.setText(String.format("%s %s", mLastUpdateLabel, lastUpdateString));
    }
}