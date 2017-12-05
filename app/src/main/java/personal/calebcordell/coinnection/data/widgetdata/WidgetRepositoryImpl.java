package personal.calebcordell.coinnection.data.widgetdata;

import java.util.List;

import personal.calebcordell.coinnection.domain.model.Widget;
import personal.calebcordell.coinnection.domain.repository.WidgetRepository;


public class WidgetRepositoryImpl implements WidgetRepository {
    private DiskWidgetDataStore diskWidgetDataStore;

    public WidgetRepositoryImpl() {
        diskWidgetDataStore = DiskWidgetDataStore.getInstance();
    }

    public List<Integer> getAllWidgetIds() {
        return diskWidgetDataStore.getAllWidgetIds();
    }

    public List<Integer> getCryptocurrencyInfoWidgetIds() {
        return diskWidgetDataStore.getCryptocurrencyInfoWidgetIds();
    }

    public Widget getWidget(int id) {
        return diskWidgetDataStore.getWidget(id);
    }

    public List<Widget> getWidgets(int[] ids) {
        return diskWidgetDataStore.getWidgets(ids);
    }

    public void updateWidget(Widget widget) {
        diskWidgetDataStore.updateWidget(widget);
    }

    public void addWidget(Widget widget) {
        diskWidgetDataStore.addWidget(widget);
    }

    public void deleteWidget(int id) {
        diskWidgetDataStore.deleteWidget(id);
    }

    public void clearWidgets() {
        diskWidgetDataStore.clearWidgets();
    }
}
