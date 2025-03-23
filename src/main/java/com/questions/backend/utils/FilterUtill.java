package com.questions.backend.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.questions.backend.filters.Filter;
import com.questions.backend.filters.FilterType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
public class FilterUtill {

    @ToString
    @Data
    @Setter
    @Getter
    public class FilterUtilDTO {
        private Map<FilterType, String> filterMap;
        private List<Filter> filters;
        private int offset;
        private int limit;
        private Map<FilterType, String> likeMap;
        private Boolean requirePrefix;

        public FilterUtilDTO() {
            this.likeMap = new HashMap<>();
        }

        public Map<FilterType, String> getLikeMap() {
            if (likeMap == null) {
                likeMap = new HashMap<>();
            }
            return likeMap;
        }

    }

    public static long countOccurrences(String value, char occurrence) {
        return value.chars().filter(ch -> ch == occurrence).count();
    }

    public static String repeatAndJoin(String value, long repeat) {
        var result = "";
        for (int i = 0; i < repeat; i++) {
            result = result + value;
            if (i < repeat - 1) {
                result = result + ", ";
            }
        }
        return result;
    }

    // old ones
    public String generateWhereClause(Map<FilterType, String> filterMap, List<Filter> filters, int offset, int limit) {
        Map<FilterType, String> likeMap = new HashMap<>(); // LIKE search

        FilterUtilDTO dto = new FilterUtilDTO();
        dto.setFilterMap(filterMap);
        dto.setFilters(filters);
        dto.setLikeMap(likeMap);
        dto.setLimit(limit);
        dto.setOffset(offset);
        dto.setRequirePrefix(true);

        return generateWhereClause(dto);
    }

    // new ones
    public String generateWhereClause(FilterUtilDTO dto) {
        String whereClause = "";
        String prefix = dto.getRequirePrefix() ? " where " : " and ";
        List<Filter> filters = dto.getFilters();
        Map<FilterType, String> filterMap = dto.getFilterMap();
        Map<FilterType, String> likeMap = dto.getLikeMap();

        if (!filters.isEmpty()) {
            whereClause = filters.stream()
                    .map(filter -> {
                        String filterString = filterMap.getOrDefault(filter.getFilterType(), "");
                        if (likeMap.containsKey(filter.getFilterType())) {
                            likeMap.put(filter.getFilterType(), "%" + filter.getValue() + "%");
                        }
                        return Filter.convertFilterString(filter.getValue(), filterString);
                    })
                    .collect(Collectors.joining(" and ", prefix, ""));
        }

        if (dto.getOffset() < 0 && dto.getLimit() < 0) {
            return whereClause;
        }

        if (whereClause.equals(" where ")) {
            whereClause = """
                        LIMIT ?
                        OFFSET ?
                    """;
        } else {
            whereClause = whereClause + """
                        LIMIT ?
                        OFFSET ?
                    """;
        }

        return whereClause;
    }

    // for old ones
    public void injectPreparedStatementValues(List<Filter> filters, int offset, int limit, PreparedStatement ps)
            throws SQLException {
        Map<FilterType, String> likeMap = new HashMap<>(); // LIKE search

        FilterUtilDTO dto = new FilterUtilDTO();
        dto.setFilters(filters);
        dto.setLimit(limit);
        dto.setOffset(offset);
        dto.setLikeMap(likeMap);
        FilterType type = null;
        if (filters.size() > 0) {
            type = filters.get(0).getFilterType();
        }
        if (type != FilterType.ID_LIST) {
            injectPreparedStatementValues(dto, ps);
        }
    }

    // use this one for new
    public void injectPreparedStatementValues(FilterUtilDTO dto, PreparedStatement ps)
            throws SQLException {
        int position = 1;
        Map<FilterType, String> likeMap = dto.getLikeMap();
        List<Filter> filters = dto.getFilters();

        for (Filter filter : filters) {
            String[] values = filter.getValue().split(",");
            for (String value : values) {
                if (likeMap.containsKey(filter.getFilterType())) {
                    ps.setString(position++, likeMap.get(filter.getFilterType()));
                } else {
                    if (filter.getFilterType() == FilterType.ID_LIST) {
                        int intValue = Integer.parseInt(value);
                        ps.setInt(position++, intValue);
                    } else {
                        ps.setString(position++, value);
                    }
                }
            }
        }
        if (dto.getLimit() > -1) {
            ps.setInt(position++, dto.getLimit());
        }
        if (dto.getOffset() > -1) {
            ps.setInt(position, dto.getOffset());
        }
    }

}
