package com.libcommons.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResult<T> {
    private int totalRecords;

    private int pageSize;

    private int page;

    private int lastPage;

    private boolean hasNext;

    private List<T> data;

    public static <T> PaginatedResult<T> paginatedListFrom(List<T> data, int pageSize, int page, int count) {
        int lastPage = (int) Math.ceil((double) count / pageSize);
        boolean hasNext = page < lastPage;

        return new PaginatedResult<>(
                count,
                pageSize,
                page,
                lastPage,
                hasNext,
                data
        );
    }
}
