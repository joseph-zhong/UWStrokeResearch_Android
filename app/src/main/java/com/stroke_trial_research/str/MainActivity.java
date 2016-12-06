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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends Activity {
    ListView mListView = null;
    JSONParser mJsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpHome();


        //TEST OF PARSER CLASS
        JSONParser jsonParser = new JSONParser();

    }

    private void setUpHome() {
        final Button clientButton = (Button) findViewById(R.id.client_btn);
        final Button adminButton = (Button) findViewById(R.id.admin_btn);

        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClientListActivity.class));
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testJsonParser() {
        long startTime = System.nanoTime();
        mJsonParser.getNodeSet(getApplicationContext(), R.raw.stroke_demo);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        Log.d("DEBUG", ("Set: " + duration));
        //Takes about 70 milliseconds. Absolutely baffling.

        startTime = System.nanoTime();
        Node n = mJsonParser.getNodeTree(getApplicationContext(), R.raw.stroke_demo_new);
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        String dur = "Tree: " + duration;
        Log.d("DEBUG", dur);
        //Takes about 37 milliseconds. Hella slow right now.

        //Eventual test of tree connections.
        /*
        String tip = "";

        while (!n.type.equals("RESULT") || !n.type.equals("UNKNOWN")) {
            tip = n.type;

            switch (tip) {
                case "NUMBER":
                    break;
                case "BUTTON":
                    break;
                case ""
            }
        }
        */
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
