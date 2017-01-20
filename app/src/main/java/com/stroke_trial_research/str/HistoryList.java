package com.stroke_trial_research.str;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class HistoryList extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Intent i = getIntent();
        List<String> l = (List<String>) i.getSerializableExtra("Quest");

        final ListView list = (ListView) findViewById(R.id.QuestionList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, l);
        list.setAdapter(adapter);
    }
}
