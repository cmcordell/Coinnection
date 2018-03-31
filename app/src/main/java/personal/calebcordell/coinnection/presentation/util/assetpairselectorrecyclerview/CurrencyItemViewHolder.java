package personal.calebcordell.coinnection.presentation.util.assetpairselectorrecyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import personal.calebcordell.coinnection.R;
import personal.calebcordell.coinnection.domain.model.Currency;
import personal.calebcordell.coinnection.presentation.Constants;
import personal.calebcordell.coinnection.presentation.util.OnObjectItemClickListener;


public class CurrencyItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.currency_logo)
    ImageView mCurrencyLogo;
    @BindView(R.id.currency_name)
    TextView mCurrencyName;
    @BindView(R.id.currency_symbol)
    TextView mCurrencySymbol;

    CurrencyItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Currency currency, final OnObjectItemClickListener<Currency> onObjectItemClickListener) {

        Glide.with(itemView.getContext())
                .load(currency.getLogo())
                .into(mCurrencyLogo);
        mCurrencyName.setText(currency.getName());
        mCurrencySymbol.setText(currency.getCode());

        if (onObjectItemClickListener != null) {
            itemView.setOnClickListener((view) ->
                    ViewCompat.postOnAnimationDelayed(view, () ->
                            onObjectItemClickListener.onObjectItemClick(currency), Constants.SELECTABLE_VIEW_ANIMATION_DELAY));
        }
    }
}