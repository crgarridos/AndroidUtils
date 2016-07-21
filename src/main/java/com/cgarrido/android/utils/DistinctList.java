package com.ylly.android.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by cristian on 03/09/2015.
 */
public class DistinctList<T> extends ArrayList<T> {
    public DistinctList() {}

    public DistinctList(List<T> list) {
        this.addAll(list);
    }

    /**
     * Only add the object if there is not
     * another copy of it in the list
     */
    public boolean add(T obj) {
        return !this.contains(obj) && super.add(obj);
    }

    public boolean addAll(Collection<? extends T> c) {
        boolean result = false;
        if (c != null)
        for (T t : c) {
            if (add(t)) {
                result = true;
            }
        }
        return result;
    }
}
