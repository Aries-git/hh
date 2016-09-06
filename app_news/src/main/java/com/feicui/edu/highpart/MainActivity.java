package com.feicui.edu.highpart;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.edu.highpart.asyntask.HttpAsyncTask;
import com.feicui.edu.highpart.asyntask.LoadCallbackListener;
import com.feicui.edu.highpart.bean.News;
import com.feicui.edu.highpart.bean.NewsGroup;
import com.feicui.edu.highpart.util.GsonParseUtil;
import com.feicui.edu.highpart.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public TextView textView;
    private RecyclerView rv;
    private MyRecycleViewAdapter adapter;
    String url = "http://192.168.2.35:8080/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";

    public void changetext(View view) {
        textView.setText("MainActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<News> newses = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);
        assert rv != null;
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecycleViewAdapter(newses, this);
        rv.setAdapter(adapter);

        textView = (TextView) findViewById(R.id.tv);


         okhttpAsyncLoad();
//        myAsyncLoad();


    }

    private void okhttpAsyncLoad() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                /*.cacheControl(CacheControl.FORCE_CACHE)//为什么加缓存就出不来*/
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "onResponse: "+string);
                ArrayList<News> newses = GsonParseUtil.parseNewJsonString(string);
                adapter.setNewses(newses);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //自己写的框架
    private void myAsyncLoad() {
        //        //在UI线程，创建AsyncTask子类的对象
        final HttpAsyncTask task = new HttpAsyncTask(this);
        //开启异步任务
        task.setListener(new LoadCallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<News> newses = GsonParseUtil.parseNewJsonString(s);
                Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                adapter.setNewses(newses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        task.execute(url);
    }

    public void pullParseAssetXmlFile() {
        //解析assets目录的xml文件
        XmlPullParser xmlPullParser = Xml.newPullParser();
        try {
            ArrayList<Person> persons = new ArrayList<>();
            Person p = null;
            InputStream stream = getAssets().open("person.xml");
            xmlPullParser.setInput(stream, "utf-8");
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //初始化动作

                        break;
                    case XmlPullParser.START_TAG:
                        //开始标签对应的事件
                        String name = xmlPullParser.getName();
                        if (name.equals("person")) {
                            p = new Person();
                            String id = xmlPullParser.getAttributeValue(null, "id");
                            p.id = Integer.parseInt(id);
                            Log.d(TAG, "id: " + id);
                        } else if (name.equals("name")) {
                            String text = xmlPullParser.nextText();
                            p.name = text;
                            Log.d(TAG, "name: " + text);

                        } else if (name.equals("age")) {
                            String age = xmlPullParser.nextText();
                            p.age = Integer.parseInt(age);
                            persons.add(p);
                            Log.d(TAG, "age: " + age);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //结束标签对应的事件
                        break;
                }
                eventType = xmlPullParser.next();
            }
            for (Person person : persons) {
                Toast.makeText(MainActivity.this, "" + person.age + person.name + person.id, Toast.LENGTH_SHORT).show();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    //解析带namespace的xml文件
    public void pullParse() {
        //解析assets目录的xml文件
        try {
//            XmlPullParser xmlPullParser = Xml.newPullParser();
//            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            XmlResourceParser xmlPullParser = getResources().getXml(R.xml.aa);
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //初始化动作
                        break;
                    case XmlPullParser.START_TAG:
                        //开始标签对应的事件
                        String name = xmlPullParser.getName();
                        if (name.equals("TextView")) {
                            //String tag = xmlPullParser.getAttributeName(2);
                            //int attributeResourceValue = xmlPullParser.getAttributeResourceValue("android", tag, 0);

                            //Toast.makeText(MainActivity.this, attributeResourceValue + "", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //结束标签对应的事件
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

    }

    //解析json数据
    public void parseNewsGroupJsonString() {
        String url = "http://192.168.2.35:8080/newsClient/news_sort?ver=1&imei=1";
        String data = HttpUtil.getJsonString(url);
        Log.d(TAG, "onCreate: " + data);

        //解析数据 json
        try {
            JSONObject j = new JSONObject(data);
            NewsGroup ng = new NewsGroup();

            String message = j.getString("message");
            int status = j.getInt("status");

            ng.setMessage(message);
            ng.setStatus(status);
            Log.d(TAG, "message: " + message + "status:" + status);

            JSONArray array = j.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                int gid = o.getInt("gid");
                String group = o.getString("group");
                NewsGroup.DataBean dataBean = new NewsGroup.DataBean();
                dataBean.setGid(gid);
                dataBean.setGroup(group);
                Log.d(TAG, "gid: " + gid + "group:" + group);
                JSONArray array1 = o.getJSONArray("subgrp");
                for (int k = 0; k < array1.length(); k++) {

                    JSONObject o1 = array1.getJSONObject(k);
                    int subid = o1.getInt("subid");
                    String subgroup = o1.getString("subgroup");
                    Log.d(TAG, "subid: " + subid + "subgroup:" + subgroup);
                    NewsGroup.DataBean.SubgrpBean bean = new NewsGroup.DataBean.SubgrpBean();
                    bean.setSubgroup(subgroup);
                    bean.setSubid(subid);
                    dataBean.getSubgrp().add(bean);
                }
                ng.getData().add(dataBean);
            }

        } catch (JSONException e) {
            Log.d(TAG, "run: json 字符串数据格式有问题");
        }
    }


}


