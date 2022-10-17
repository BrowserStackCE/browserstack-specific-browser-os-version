package com.app.parallel.ios;

import org.testng.annotations.Test;

public class Parallel1Test extends BaseTest {

    @Test
    public void printText1() {
        super.printText();
    }

    @Test
    public void printText2() {
        super.printText();
    }

    @Test
    public void printText3() {
        super.printText();
    }

}
