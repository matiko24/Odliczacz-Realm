package com.matekome.odliczacz.data;

import org.junit.Test;

import java.util.Date;

public class MyPeriodTest {
    @Test
    public void getPeriodToDisplay() throws Exception {
        MyPeriod.getPeriodToDisplay(new Date());
    }

}