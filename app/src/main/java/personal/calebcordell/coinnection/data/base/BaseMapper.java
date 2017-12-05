package personal.calebcordell.coinnection.data.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapper<TOP, BOTTOM> {
    public abstract TOP mapUp(BOTTOM from);
    public abstract BOTTOM mapDown(TOP from);

    public List<TOP> mapUp(List<BOTTOM> bottomList) {
        List<TOP> topList = null;
        if (bottomList != null) {
            topList = new ArrayList<>(bottomList.size());
            for(BOTTOM bottom : bottomList) {
                topList.add(mapUp(bottom));
            }
        }
        return topList;
    }

    public List<BOTTOM> mapDown(List<TOP> topList) {
        List<BOTTOM> bottomList = null;
        if (topList != null) {
            bottomList = new ArrayList<>(topList.size());
            for (TOP top : topList) {
                bottomList.add(mapDown(top));
            }
        }
        return bottomList;
    }
}