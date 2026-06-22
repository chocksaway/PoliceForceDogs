package com.chocksaway.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PageTest {

    @Test
    void firstPage_singlePerPage() {
        Page p = new Page(1, 1, 5);
        Map<String, Integer> off = p.offset();
        assertEquals(1, off.get("start"));
        assertEquals(1, off.get("finish"));
    }

    @Test
    void secondPage_singlePerPage() {
        Page p = new Page(2, 1, 5);
        Map<String, Integer> off = p.offset();
        assertEquals(2, off.get("start"));
        assertEquals(2, off.get("finish"));
    }

    @Test
    void outOfRangePage_returnsZeros() {
        Page p = new Page(6, 1, 5);
        Map<String, Integer> off = p.offset();
        assertEquals(0, off.get("start"));
        assertEquals(0, off.get("finish"));
    }

    @Test
    void partialLastPage() {
        // 7 total, 3 per page -> pages: [1-3],[4-6],[7]
        Page p = new Page(3, 3, 7);
        Map<String, Integer> off = p.offset();
        assertEquals(7, off.get("start"));
        assertEquals(7, off.get("finish"));
    }

    @Test
    void multiPerPage_middle() {
        // 5 total, 2 per page -> pages: [1-2],[3-4],[5]
        Page p = new Page(2, 2, 5);
        Map<String, Integer> off = p.offset();
        assertEquals(3, off.get("start"));
        assertEquals(4, off.get("finish"));
    }

    @Test
    void zeroTotal_returnsZeros() {
        Page p = new Page(1, 1, 0);
        Map<String, Integer> off = p.offset();
        assertEquals(0, off.get("start"));
        assertEquals(0, off.get("finish"));
    }

    @Test
    void negativeInputs_normalized() {
        // page and onAPage normalized to 1; total negative becomes 0
        Page p = new Page(0, 0, -5);
        Map<String, Integer> off = p.offset();
        assertEquals(0, off.get("start"));
        assertEquals(0, off.get("finish"));
    }
}

