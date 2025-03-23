package com.questions.backend.filters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.questions.backend.utils.FilterUtill;

public class Filter {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private String value;
    private FilterType filterType;

    public Filter(String value, FilterType filterType) {
        this.filterType = filterType;
        this.value = value;
    }

    public Filter(int value, FilterType filterType) {
        this.filterType = filterType;
        this.value = Integer.toString(value);
    }

    public Filter(boolean value, FilterType filterType) {
        this.filterType = filterType;
        this.value = Boolean.toString(value);
    }

    public Filter(List<String> value, FilterType filterType) {
        this.value = value.stream().collect(Collectors.joining(","));
        this.filterType = filterType;
    }

    public Filter(LocalDateTime value, FilterType filterType) {
        this.value = value.format(dateTimeFormatter);
        this.filterType = filterType;
    }

    public Filter(String[] value, FilterType filterType) {
        this.value = (value == null || value.length == 0) ? "" : Arrays.stream(value).collect(Collectors.joining(","));
        this.filterType = filterType;
    }

    @Override
    public String toString() {
        return value + '-' + filterType.toString();
    }

    public String getValue() {
        return value;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public static String convertFilterString(String value, String filterString) {
        if (value == null) {
            return filterString;
        }

        if (value.contains(",")) {
            long occurrences = FilterUtill.countOccurrences(value, ',') + 1;
            var replacement = FilterUtill.repeatAndJoin("?", occurrences);
            return filterString.replace("?", replacement);
        } else {
            return filterString;
        }
    }
}
