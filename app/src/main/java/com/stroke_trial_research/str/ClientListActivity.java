package com.stroke_trial_research.str;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Set;

public class ClientListActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        setUpNavigation();
        setUpList();
    }

    private void setUpList() {
        final ListView rootListView = (ListView) findViewById(R.id.rootList);

        rootListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                int id = R.raw.stroke_demo_new;
                bundle.putInt("ID",id);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TreeTraverser.class); //perhaps wrong context
                // Initialize Tree and Tree handler then begin traversal.
                //startActivity(intent, bundle);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.i("INFO", "List Item Clicked");
            }
        });

        JSONParser jsonParser = new JSONParser();
        ArrayList<Node> nodeList = jsonParser.getNodeList(this, R.raw.stroke_demo_new);
        //List of the qid names
        ArrayList<String> qidList = new ArrayList<>();
        for (Node n:nodeList) {
            qidList.add(n.getQID());
        }
        String[] values = new String[qidList.size()];
        for (int i = 0; i < qidList.size(); i++) {
            values[i] = qidList.get(i);
        }

        //Test Array for simulated one Tree
        String[] testTrees = {"Default Tree"};

        /* Links to Nodes. In future, link to list of saved trees on System
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        rootListView.setAdapter(adapter);
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, testTrees);
        rootListView.setAdapter(adapter);

//        // Create a progress bar to display while the list loads
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//        progressBar.setIndeterminate(true);
//        rootListView.setEmptyView(progressBar);
//
//        // Must add the progress bar to the root of the layout
//        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
//        root.addView(progressBar);
//
//        // For the cursor adapter, specify which columns go into which views
//        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
//        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1
//
//        // Create an empty adapter we will use to display the loaded data.
//        // We pass null for the cursor, then update it in onLoadFinished()
//        mAdapter = new SimpleCursorAdapter(this,
//                android.R.layout.simple_list_item_1, null,
//                fromColumns, toViews, 0);
//        rootListView.setAdapter(mAdapter);
//
//        // Prepare the loader.  Either re-connect with an existing one,
//        // or start a new one.
//        getLoaderManager().initLoader(0, null, this);

    }

//    private void testJsonParser() {
//        long startTime = System.nanoTime();
//        mJsonParser.getNodeSet(getApplicationContext(), R.raw.stroke_demo);
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime);
//        Log.d("DEBUG", ("Set: " + duration));
//        //Takes about 70 milliseconds. Absolutely baffling.
//
//        startTime = System.nanoTime();
//        Node n = mJsonParser.getNodeTree(getApplicationContext(), R.raw.stroke_demo_new);
//        endTime = System.nanoTime();
//
//        duration = (endTime - startTime);
//        String dur = "Tree: " + duration;
//        Log.d("DEBUG", dur);
//        //Takes about 37 milliseconds. Hella slow right now.
//
//        //Eventual test of tree connections.
//        /*
//        String tip = "";
//
//        while (!n.type.equals("RESULT") || !n.type.equals("UNKNOWN")) {
//            tip = n.type;
//
//            switch (tip) {
//                case "NUMBER":
//                    break;
//                case "BUTTON":
//                    break;
//                case ""
//            }
//        }
//        */
//    }

    private void setUpNavigation() {
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.client_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("INFO", "called OnCreateLoader");
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        Log.i("INFO", "called onLoadFinished");
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        Log.i("INFO", "called onLoadReset");
    }
}
