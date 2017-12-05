package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;


public abstract class SingleInteractor<REQUEST_DATA, RESPONSE_DATA> {

    protected abstract Single<RESPONSE_DATA> buildSingle(REQUEST_DATA requestData);

    public Disposable execute(REQUEST_DATA requestData, DisposableSingleObserver<RESPONSE_DATA> interactorObserver) {
        if(interactorObserver == null) {
            return this.buildSingle(requestData).subscribe();
        }
        return this.buildSingle(requestData).subscribeWith(interactorObserver);
    }
}