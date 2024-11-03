package com.questions.backend.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.questions.backend.exceptions.MissingFilterImplementationException;

public abstract class Query<E, T extends Repository<E>> {

    private T repository;
    private Set<FilterType> implemented;
    private String label;
    private List<Filter> filterList = new ArrayList<>();

    public Query(T repository, String label, FilterType... implementedFilters) {
        this.repository = repository;
        this.label = label;
        implemented = new HashSet<>(Arrays.asList(implementedFilters));
    }

    public T getRepository() {
        return this.repository;
    }

    public Query<E, T> filters(List<Filter> filters) {
        if (filters.stream().allMatch(filter -> implemented.contains(filter.getFilterType()))) {
            filterList.addAll(filters);
            return this;
        } else {
            var missingType = filters.stream().filter(filter -> !implemented.contains(filter.getFilterType()))
                    .findFirst();
            if (missingType.isPresent()) {
                throw new MissingFilterImplementationException(missingType.get().getFilterType(), label);
            }
            throw new MissingFilterImplementationException(null, label);
        }
    }

    public PaginationList<E> findAllPagination(int offset, int limit) {
        return repository.findByFilters(filterList, offset, limit);
    }
}
