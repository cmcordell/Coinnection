package personal.calebcordell.coinnection.domain.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import personal.calebcordell.coinnection.domain.model.Widget;


public interface WidgetRepository<W extends Widget> {

    Single<W> getWidget(int id);
    Single<List<W>> getAllWidgets();

    Completable insertWidget(W widget);
    Completable insertWidgets(List<W> widgets);

    Completable deleteWidget(int id);
    Completable deleteWidgets(int[] ids);

    Completable clearWidgets();
}