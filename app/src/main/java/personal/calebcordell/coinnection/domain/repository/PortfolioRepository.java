package personal.calebcordell.coinnection.domain.repository;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import personal.calebcordell.coinnection.domain.model.Portfolio;


public interface PortfolioRepository {

    Flowable<Portfolio> getPortfolio(String id);

    Completable createOrUpdatePortfolio(Portfolio portfolio);
}