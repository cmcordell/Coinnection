package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;


public abstract class CompletableInteractor<REQUEST_DATA> {


    protected abstract Completable buildCompletable(REQUEST_DATA requestData);

    public Disposable execute(REQUEST_DATA requestData, DisposableCompletableObserver interactorObserver) {
        if(interactorObserver == null) {
            return this.buildCompletable(requestData).subscribe();
        }
        return this.buildCompletable(requestData).subscribeWith(interactorObserver);
    }
}