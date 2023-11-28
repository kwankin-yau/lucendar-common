package com.lucendar.common.types.rest;

import info.gratour.common.error.ErrorWithCode;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_LIMIT = 20;

    public static final Pagination DEFAULT = new Pagination(DEFAULT_LIMIT, DEFAULT_PAGE);
    public static final Pagination FIRST_ONE = new Pagination(1, 1);
    public static final int MAX_LIMIT = 2000;

    public static void check(int page, int limit) {
        if (page <= 0)
            throw ErrorWithCode.invalidParam("__page");

        if (limit <= 0 || limit > MAX_LIMIT)
            throw ErrorWithCode.invalidParam("__limit");
    }

    public static void checkLimitOffset(int limit, int offset) {
        if (limit <= 0 || limit > MAX_LIMIT)
            throw ErrorWithCode.invalidParam("limit");

        if (offset < 0)
            throw ErrorWithCode.invalidParam("offset");
    }

    public static int pageOf(int limit, int offset) {
        return (offset + limit - 1) / limit;
    }

    public static int offsetOf(int limit, int page) {
        return limit * (page - 1);
    }

    public static long pageCount(long totalRecordCount, int pageSize) {
        return (totalRecordCount + (pageSize - 1)) / pageSize;
    }

    public final int limit;
    public final int page;

    public Pagination(int limit, int page) {
        this.limit = limit;
        this.page = page;
    }

    public int offset() {
        return (page - 1) * limit;
    }

    public Pagination nextPage() {
        return new Pagination(limit, page + 1);
    }

    public Pagination nextPage(int totalRecordCount) {
        int nextOffset = offset() + limit;
        if (nextOffset < totalRecordCount)
            return new Pagination(limit, page + 1);
        else
            return null;
    }

    public <T> List<T> listPaging(List<T> list) {
        int ofs = offset();
        if (ofs >= list.size())
            return new ArrayList<>();
        else
            return list.subList(ofs, ofs + limit);
    }
}
