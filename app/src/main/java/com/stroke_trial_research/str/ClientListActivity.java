/**
 * Copyright 2017 COLLABORATORS
 *
 *  Activity for displaying a list of available trees.
 *  When one such item is clicked, the ID of the item the user wants
 *  to read is passed to a TreeTraverser Activity.
 */
package com.stroke_trial_research.str;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClientListActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private int[] id_table = {R.raw.decision_tree};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

        setUpNavigation();
        setUpList();
    }

    /**
     * Initializes the ListView for this activity and links it to
     * the available trees from memory.
     */
    private void setUpList() {
        // Initialize a ListView and set listener
        final ListView rootListView = (ListView) findViewById(R.id.rootList);
        rootListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Make a bundle with the id of the item clicked
                Bundle bundle = new Bundle();
                bundle.putInt(CONSTANTS.BUNDLE_TREE_ID, id_table[i]);
                // Make an intent to pass along with the bundle
                Intent intent = new Intent(getApplicationContext(), TreeTraverser.class);
                // Initialize Tree and Tree handler then begin traversal.
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // This array should have a loaded list of all the available
        // trees. Since this is a demo, only the one default tree is needed
        String[] testTrees = {"Default Tree"};

        // Put the array into an adapter and then set the ListView's adapter to this new adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, testTrees);
        rootListView.setAdapter(adapter);
    }

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
