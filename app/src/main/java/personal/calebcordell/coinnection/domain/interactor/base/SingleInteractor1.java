package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;


public abstract class SingleInteractor1<RESPONSE_DATA> extends SingleInteractor<Void, RESPONSE_DATA> {

    protected Single<RESPONSE_DATA> buildSingle(Void aVoid) {
        return buildSingle();
    }

    protected abstract Single<RESPONSE_DATA> buildSingle();

    public Disposable execute(DisposableSingleObserver<RESPONSE_DATA> interactorObserver) {
        return super.execute(null, interactorObserver);
    }
}