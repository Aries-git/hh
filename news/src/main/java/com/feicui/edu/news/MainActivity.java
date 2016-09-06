package com.feicui.edu.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getComponentName().getClassName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "http://192.168.2.35:8080/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
        String jsonString = OkHttpUtil.getJsonString(url);
        Log.d(TAG, "onCreate: "+jsonString);
    }
}
