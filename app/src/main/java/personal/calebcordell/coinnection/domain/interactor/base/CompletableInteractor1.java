package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;


public abstract class CompletableInteractor1 extends CompletableInteractor<Void> {

    protected Completable buildCompletable(Void aVoid) {
        return buildCompletable();
    }

    protected abstract Completable buildCompletable();

    public Disposable execute(DisposableCompletableObserver interactorObserver) {
        return super.execute(null, interactorObserver);
    }
}