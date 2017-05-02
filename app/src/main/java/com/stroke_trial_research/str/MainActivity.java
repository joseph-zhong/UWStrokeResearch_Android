package com.stroke_trial_research.str;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.R.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MAIN
        // setContentView(R.layout.activity_main);
        // setUpHome();

        // TEST
        setContentView(R.layout.test_view);
        setUpTest();
    }

    /**
     * Sets up the HomeScreen for the app with buttons to take the user to
     * to the administration part of the app or the client interface.
     */
    private void setUpHome() {
        final Button clientButton = (Button) findViewById(R.id.client_btn);
        final Button adminButton = (Button) findViewById(R.id.admin_btn);

        // Takes the user to the tree selection pane
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClientListActivity.class));
                //startActivity(new Intent(getApplicationContext(), TreeTraverser.class));
            }
        });

        // Need to implement the Admin page
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initializes a single button which when clicked will start
     * the client interface with the default tree.
     */
    private void setUpTest() {
        final Button clientButton = (Button) findViewById(R.id.beginButton);

        // Directly starts the TreeTraverser application.
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(CONSTANTS.BUNDLE_TREE_ID, R.raw.decision_tree);
                Intent intent = new Intent(getApplicationContext(), TreeTraverser.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
