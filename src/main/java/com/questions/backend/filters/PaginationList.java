package com.questions.backend.filters;

import java.util.ArrayList;
import java.util.List;

public class PaginationList<E> {

    private List<E> list;
    private int total;

    public PaginationList(List<E> list, int total) {
        this.list = (list != null) ? list : new ArrayList<>();
        this.total = total;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
       this.list = (list != null) ? list : new ArrayList<>(); 
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
