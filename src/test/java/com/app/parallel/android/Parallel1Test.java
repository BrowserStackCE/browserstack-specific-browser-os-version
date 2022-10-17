package com.app.parallel.android;

import org.testng.annotations.Test;

public class Parallel1Test extends BaseTest {

    @Test
    public void searchWikipedia1() {
        super.searchWikipedia();
    }

    @Test
    public void searchWikipedia2() {
        super.searchWikipedia();
    }

    @Test
    public void searchWikipedia3() {
        super.searchWikipedia();
    }

}
