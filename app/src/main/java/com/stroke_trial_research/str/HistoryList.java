package com.stroke_trial_research.str;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class HistoryList extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Intent i = getIntent();
        List<Node> nodeList = (List<Node>) i.getExtras().get("Quest");
        List<String> printStrings = new LinkedList<>();

        for (Node n : nodeList) {
            printStrings.add(n.toString());
        }

        final ListView list = (ListView) findViewById(R.id.QuestionList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, printStrings);
        list.setAdapter(adapter);
    }
}
