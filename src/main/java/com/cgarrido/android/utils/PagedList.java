package com.ylly.android.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created for valueOf list's object those have "datas" keys.
 * @author Created by cristian on 20/11/2015
 */
public class PagedList<T> extends ArrayList<T> {


    private static final int DEFAULT_PAGE_SIZE = 50;
    private int mPageSize;

    public PagedList(){
        this(DEFAULT_PAGE_SIZE,null);
    }

    public PagedList(List<T> list) {
        this(DEFAULT_PAGE_SIZE,list);
    }

    public PagedList(int pageSize,List<T> list){
        super(list != null ? list : new ArrayList<T>());
        mPageSize = pageSize;
    }

    public PagedList<T> getPage(int page){
        int start = (page-1) * mPageSize > size() ? size() : (page-1) * mPageSize;
        int end   =  page    * mPageSize > size() ? size() :  page    * mPageSize;
        return new PagedList<>(this.subList(start,end));
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public List<T> subList(Integer limit) {
        return limit > 0 && limit < size() ? subList(0, limit) : this;
    }
}
