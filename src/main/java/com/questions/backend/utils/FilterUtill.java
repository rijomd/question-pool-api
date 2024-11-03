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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
public class FilterUtill {

    @ToString
    @Data
    @Setter
    @Getter
    @NoArgsConstructor
    public class FilterUtilDTO {
        private Map<FilterType, String> filterMap;
        private List<Filter> filters;
        private int offset;
        private int limit;
        private Map<FilterType, String> likeMap;
        private Boolean requirePrefix;
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

    public String generateWhereClause(FilterUtilDTO dto) {

        var whereClause = "";
        var prefix = " and ";
        boolean requirePrefix = dto.getRequirePrefix();
        List<Filter> filters = dto.getFilters();
        Map<FilterType, String> filterMap = dto.getFilterMap();
        Map<FilterType, String> likeMap = dto.getLikeMap();

        if (requirePrefix) {
            prefix = " where ";
        }

        if (!filters.isEmpty()) {
            whereClause = filters.stream()
                    .map(filter -> {
                        String filterString = filterMap.getOrDefault(filter.getFilterType(), "");
                        if (likeMap.containsKey(filter.getFilterType())) {
                            String newValue = "%" + filter.getValue() + "%";
                            likeMap.put(filter.getFilterType(), newValue);
                        }
                        return Filter.convertFilterString(filter.getValue(), filterString);
                    })
                    .collect(Collectors.joining(" and ", prefix, ""));
        }

        if (whereClause.equals(" where ")) {
            whereClause = "";
        }

        if (dto.getOffset() < 0 && dto.getLimit() < 0) {
            return whereClause;
        }

        if (whereClause.equals(" where ")) {
            whereClause = """
                        OFFSET ?
                        LIMIT ?
                    """;
        } else {
            whereClause = whereClause + """
                        OFFSET ?
                        LIMIT ?
                    """;
        }

        return whereClause;
    }

    public void injectPreparedStatementValues(List<Filter> filters, int offset, int limit, PreparedStatement ps)
            throws SQLException {
        Map<FilterType, String> likeMap = new HashMap<>(); // LIKE search

        FilterUtilDTO dto = new FilterUtilDTO();
        dto.setFilters(filters);
        dto.setLimit(limit);
        dto.setOffset(offset);
        dto.setLikeMap(likeMap);
        injectPreparedStatementValues(dto, ps);
    }

    public void injectPreparedStatementValues(FilterUtilDTO dto, PreparedStatement ps)
            throws SQLException {
        var position = 1;
        Map<FilterType, String> likeMap = dto.getLikeMap();
        List<Filter> filters = dto.getFilters();

        for (int i = 0; i < filters.size(); i++) {
            var values = filters.get(i).getValue().split(",");
            for (var singleValue : values) {
                if (likeMap.get(filters.get(i).getFilterType()) != null) {
                    ps.setString(position, likeMap.get(filters.get(i).getFilterType()));
                } else {
                    ps.setString(position, singleValue);
                }
                position++;
            }
        }
        if (dto.getOffset() > -1 && dto.getLimit() > -1) {
            ps.setInt(position, dto.getOffset());
            position++;
            ps.setInt(position, dto.getLimit());
        }
    }

}
