package com.stroke_trial_research.str;

import android.app.Activity;
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

public class HistoryList extends Activity {

    private ArrayAdapter<String> adapter;
    public static final String STACK_KEY = "STACK_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Intent i = getIntent();
//        Stack<Node> nodeStack = (Stack<Node>) i.getExtras().getSerializable(STACK_KEY);
        List<Node> nodeList = (List<Node>) i.getExtras().getSerializable(STACK_KEY);

        List<String> printStrings = new LinkedList<>();

        for (Node n : nodeList) {
            printStrings.add(n.toString());
        }
//        while(!nodeStack.isEmpty()) {
//            printStrings.add(nodeStack.pop().toString());
//        }

        final ListView list = (ListView) findViewById(R.id.QuestionList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, printStrings);
        list.setAdapter(adapter);
    }
}
