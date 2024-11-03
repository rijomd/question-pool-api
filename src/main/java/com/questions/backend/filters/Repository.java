package com.questions.backend.filters;

import java.util.List;

public interface Repository<T> {

    public PaginationList<T> findByFilters(List<Filter> filters, int offset, int limit);

    public boolean deleteByFilters(List<Filter> filters);

}
