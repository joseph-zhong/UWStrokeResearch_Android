package com.stroke_trial_research.str;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by James on 12/27/2016.
 *
 * An activity for traversing through the Nodes.
 *
 */

public class TreeTraverser extends Activity implements AdapterView.OnItemSelectedListener{
    private QuestionHandler questionHandler;
    private TextView questionBox;
    private EditText editText;
    private Button rangeCont, buttonCont, history;
    private RelativeLayout rangeView, buttonView, terminalView;
    private Spinner buttonSpinner;
    private String spinnerResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        JSONParser jsonParser = new JSONParser();
        List<Node> nodeList = jsonParser.getNodeList(this, bundle.getInt("ID"));

        setContentView(R.layout.combined_view);
        rangeView = (RelativeLayout) findViewById(R.id.rangeLayout);
        buttonView = (RelativeLayout) findViewById(R.id.buttonLayout);
        terminalView = (RelativeLayout) findViewById(R.id.terminalLayout);

        questionHandler = new QuestionHandler(nodeList);
        String type = questionHandler.getCurrentQuestionType();
        while (type.equals("OR")){
            ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
            String first = temp.get(0);
            questionHandler.giveInput(first);
            type = questionHandler.getCurrentQuestionType();
        }
        if (type.equals("NUMBER")) {
            //intial screen is a number view
            makeRangeVisible();
            rangeScreen();
        } else if (type.equals("BUTTON")) {
            //intial screen is a button view
            makeButtonVisible();
            buttonScreen(); //just options for 3 buttons right now.
        } else {
            makeTerminalVisible();
            terminalScreen();
        }
    }

    private void makeRangeVisible(){
        buttonView.setVisibility(View.GONE);
        terminalView.setVisibility(View.GONE);
        rangeView.setVisibility(View.VISIBLE);
    }

    private void makeButtonVisible(){
        terminalView.setVisibility(View.GONE);
        rangeView.setVisibility(View.GONE);
        buttonView.setVisibility(View.VISIBLE);
    }

    private void makeTerminalVisible(){
        buttonView.setVisibility(View.GONE);
        rangeView.setVisibility(View.GONE);
        terminalView.setVisibility(View.VISIBLE);
    }


    private void rangeScreen() {
        questionBox = (TextView) findViewById(R.id.questionView);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));
        editText = (EditText) findViewById(R.id.rangeTextBox);
        editText.setText("");
        rangeCont = (Button) findViewById(R.id.rangeContinue);

        rangeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab text from bar
                String response = editText.getText().toString();

                //go to the next node
                questionHandler.giveInput(response);
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")){
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                switch (type) {
                    case "NUMBER":
                        rangeScreen();
                        break;
                    case "BUTTON":
                        makeButtonVisible();
                        buttonScreen();         //default is 3 buttons
                        break;
                    default:                     //Also add logical options
                        makeTerminalVisible();
                        terminalScreen();
                        break;
                }

            }
        });
    }

    private void buttonScreen() {
        questionBox = (TextView) findViewById(R.id.questionViewB);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));

        /*
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        unknown = (Button) findViewById(R.id.unknown);
        */
        List<String> list = questionHandler.getConnectingAnswers();

        buttonSpinner = (Spinner) findViewById(R.id.buttonSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonSpinner.setAdapter(adapter);


        buttonSpinner.setOnItemSelectedListener(this);
        buttonCont = (Button) findViewById(R.id.buttonContinue);

        buttonCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput(spinnerResp);
                String type = questionHandler.getCurrentQuestionType();

                while (type.equals("OR")){
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                switch (type) {
                    case "NUMBER":
                        makeRangeVisible();
                        rangeScreen();
                        break;
                    case "BUTTON":
                        buttonScreen();
                        break;
                    default:
                        makeTerminalVisible();
                        terminalScreen();
                        break;
                }
            }
        });

        /*
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    makeRangeVisible();
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    buttonScreen(3);         //default is 3 buttons
                } else {                    //Also add logical options
                    makeTerminalVisible();
                    terminalScren();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    makeRangeVisible();
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    buttonScreen(3);         //default is 3 buttons
                } else {                    //Also add logical options
                    makeTerminalVisible();
                    terminalScren();
                }
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    makeRangeVisible();
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    buttonScreen(3);         //default is 3 buttons
                } else {                    //Also add logical options
                    makeTerminalVisible();
                    terminalScren();
                }
            }
        });*/
    }

    private void terminalScreen(){
        questionBox = (TextView) findViewById(R.id.questionViewT);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));

        history = (Button) findViewById(R.id.history);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerResp = (String) parent.getItemAtPosition(position);
        //Log.e("Spinner Response", spinnerResp);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Write a case for if the user doesn't select anything to prevent blank submissions
    }
}
