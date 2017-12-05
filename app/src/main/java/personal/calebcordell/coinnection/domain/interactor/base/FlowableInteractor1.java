package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;


public abstract class FlowableInteractor1<RESPONSE_DATA> extends FlowableInteractor<Void, RESPONSE_DATA> {

    protected Flowable<RESPONSE_DATA> buildFlowable(Void aVoid) {
        return buildFlowable();
    }

    protected abstract Flowable<RESPONSE_DATA> buildFlowable();

    public Disposable execute(DisposableSubscriber<RESPONSE_DATA> interactorSubscriber) {
        return super.execute(null, interactorSubscriber);
    }
}
