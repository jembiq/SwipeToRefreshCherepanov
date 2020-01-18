package com.example.onrefreshlistcherepanov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SwipeRefreshLayout refreshLayout;
    SimpleAdapter adapter;
    private static List<Map<String, String>> viewList = new ArrayList<>();
    private static List<String> list = new ArrayList<>();
    private static final String TEXT = "text";
    private SharedPreferences save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadPrefs();
    }

    private void loadPrefs() {
        String[] items = save
                .getString("list", "")
                .split(",");

        viewList.clear();

        for(int i = 0; i < 50; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(TEXT, items[i]);
            viewList.add(map);
            list.add(getString(R.string.line) + (i+1));
        }
    }

    private void init() {
        listView = findViewById(R.id.list_view);
        refreshLayout = findViewById(R.id.refreshLayout);

        save = getSharedPreferences("shared", MODE_PRIVATE);

        prepareContent();

        adapter = createAdapter(viewList);
        listView.setAdapter(adapter);
        listOnItemClickAction();
        swipeOnRefreshAction();
    }

    private void listOnItemClickAction() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void swipeOnRefreshAction() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                loadPrefs();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void prepareContent(){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < 50; i++) {
            list.add(getString(R.string.line) + (i+1));
        }

        for (String s : list) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        SharedPreferences.Editor editor = save.edit();
        editor.putString("list", stringBuilder.toString());
        editor.apply();
    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> viewList) {
        return new SimpleAdapter(this
                , viewList
                , R.layout.views
                , new String[] {TEXT}
                , new int[] {R.id.text});
    }
}
