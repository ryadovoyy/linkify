package com.ryadovoy.linkify.util;

import com.ryadovoy.linkify.exception.PageArgumentNotValidException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestFactory {
    private static final int MAX_PAGE_SIZE = 50;

    public static PageRequest createRequest(int page, int size, Sort sort) {
        if (size > MAX_PAGE_SIZE) {
            throw new PageArgumentNotValidException("Page size must not be larger than " + MAX_PAGE_SIZE);
        }

        try {
            return PageRequest.of(page, size, sort);
        } catch (IllegalArgumentException ex) {
            throw new PageArgumentNotValidException(ex.getMessage());
        }
    }
}
