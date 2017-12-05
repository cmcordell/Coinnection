package personal.calebcordell.coinnection.domain.interactor.base;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;


public abstract class FlowableInteractor<REQUEST_DATA, RESPONSE_DATA> {

    protected abstract Flowable<RESPONSE_DATA> buildFlowable(REQUEST_DATA requestData);

    public Disposable execute(REQUEST_DATA requestData, DisposableSubscriber<RESPONSE_DATA> interactorSubscriber) {
        if(interactorSubscriber == null) {
            return this.buildFlowable(requestData).subscribe();
        }
        return this.buildFlowable(requestData).subscribeWith(interactorSubscriber);
    }
}