package personal.calebcordell.coinnection.domain.model;


import android.support.annotation.NonNull;

public class Currency {
    @NonNull
    private String code;
    @NonNull
    private String name;
    private int logo;

    public Currency(@NonNull final String code, @NonNull final String name) {
        this.code = code;
        this.name = name;
        logo = LogoUtil.getFiatLogo(code);
    }

    @NonNull
    public String getCode() {
        return code;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }
}