package personal.calebcordell.coinnection.presentation;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


public class StringUtils {

    public static String getFormattedNumberString(final double number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(number);
    }

    public static String getFormattedDecimalString(final double amount) {
        NumberFormat decimalFormat = DecimalFormat.getInstance();

        double absAmount = Math.abs(amount);
        if (absAmount < 1.00 && absAmount > 0.0) {
            decimalFormat.setMaximumFractionDigits(4);
            decimalFormat.setMinimumFractionDigits(4);
        } else if (absAmount < 10000.0) {
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
        } else {
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setMinimumFractionDigits(0);
        }

        return decimalFormat.format(amount);
    }

    public static String getFormattedCurrencyString(final double amount, final String currencyCode) {
        // This formats currency values as the user expects to read them (default locale).
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // This specifies the actual currency that the value is in, and provides the currency symbol.
        Currency currency = Currency.getInstance(currencyCode);
        currencyFormat.setCurrency(currency);

        // Our fix is to use the US locale as default for the symbol, unless the currency is USD
        // and the locale is NOT the US, in which case we know it should be US$.
        String symbol;
        if (currencyCode.equalsIgnoreCase("usd") && !Locale.getDefault().equals(Locale.US)) {
            symbol = "US$";
        } else {
            symbol = currency.getSymbol();
        }

        //This switch statement is a quick fix for any isoCurrencyCode specific things
        switch (currencyCode.toLowerCase()) {
            case "idr":
                symbol = "Rp";
                break;
            case "rub":
                symbol = "\u20BD";
                break;
        }

        // We then tell our formatter to use this symbol.
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol(symbol);
        ((DecimalFormat) currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

        //Application specific fraction decimal formatting
        double absAmount = Math.abs(amount);
        if (absAmount < 1.00 && absAmount > 0.0) {
            currencyFormat.setMaximumFractionDigits(4);
            currencyFormat.setMinimumFractionDigits(4);
        } else if (absAmount < 10000.0) {
            currencyFormat.setMaximumFractionDigits(2);
            currencyFormat.setMinimumFractionDigits(2);
        } else {
            currencyFormat.setMaximumFractionDigits(0);
            currencyFormat.setMinimumFractionDigits(0);
        }

        return currencyFormat.format(amount);
    }

    public static String getFormattedPercentString(final double percent) {
        NumberFormat percentFormat = DecimalFormat.getPercentInstance();

        String pattern = ((DecimalFormat) percentFormat).toPattern();
        if (percent < .10) {
            pattern = pattern.replace("0", "0.00");
        } else if (percent < 1.00) {
            pattern = pattern.replace("0", "0.0");
        }
        ((DecimalFormat) percentFormat).applyPattern(pattern);

        return percentFormat.format(percent);
    }

    public static void makeTextViewHyperlink(@NonNull final TextView tv) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(tv.getText());
        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}