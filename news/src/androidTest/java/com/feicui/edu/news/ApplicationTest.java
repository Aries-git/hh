package com.feicui.edu.news;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void runTest() throws Throwable {
        super.runTest();
        String url = "http://192.168.2.35:8080/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
        assertNotNull(OkHttpUtil.getJsonString(url));
    }
}