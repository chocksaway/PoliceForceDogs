package com.chocksaway.util;

import java.util.Map;

public class Page {
    private final int pageNumber;
    private final int onAPage;
    private final int totalDogs;

    public Page(int pageNumber, int onAPage, int totalDogs) {
        this.pageNumber = pageNumber;
        this.onAPage = onAPage;
        this.totalDogs = totalDogs;
    }

    public Map<String, Integer> offset() {
        // Normalize inputs
        int pn = pageNumber <= 0 ? 1 : pageNumber;
        int per = onAPage <= 0 ? 1 : onAPage;
        int total = Math.max(0, totalDogs);

        int startIndex;
        int endIndex;
        if (total == 0) {
            startIndex = 0;
            endIndex = 0;
            return Map.of("start", startIndex, "finish", endIndex);
        }

        int pageIndex = pn - 1; // zero-based
        int start0 = pageIndex * per;

        if (start0 >= total) {
            // out of range -> no items on this page
            startIndex = 0;
            endIndex = 0;
            return Map.of("start", startIndex, "finish", endIndex);
        }

        int endExclusive = Math.min(start0 + per, total);

        // store 1-based start and finish indices for human-friendly values
        startIndex = start0 + 1;
        endIndex = endExclusive;

        return Map.of("start", startIndex, "finish", endIndex);
    }
}
