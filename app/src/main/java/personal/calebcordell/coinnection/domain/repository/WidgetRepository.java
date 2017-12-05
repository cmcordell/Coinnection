package personal.calebcordell.coinnection.domain.repository;

import java.util.List;
import personal.calebcordell.coinnection.domain.model.Widget;


public interface WidgetRepository {
    List<Integer> getAllWidgetIds();

    List<Integer> getCryptocurrencyInfoWidgetIds();

    Widget getWidget(int id);

    List<Widget> getWidgets(int[] ids);

    void updateWidget(Widget widget);

    void addWidget(Widget widget);

    void deleteWidget(int id);

    void clearWidgets();
}
