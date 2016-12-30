package com.stroke_trial_research.str;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.Editable;

import java.util.List;


/**
 * Created by James on 12/27/2016.
 */

public class TreeTraverser extends Activity {
    QuestionHandler questionHandler;
    TextView questionBox;
    EditText editText;
    Button cont;
    Button yes;
    Button no;
    Button unknown;
    RelativeLayout rangeView;
    RelativeLayout buttonView;
    RelativeLayout terminalView;
    FrameLayout frameLayout;

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

        //frameLayout = new FrameLayout(this);


        /*
        for (Node node : nodeList) {
            Log.e(node.getQID(), node.getQuestion());
        }*/

        questionHandler = new QuestionHandler(nodeList);
        String type = questionHandler.getCurrentQuestionType();
        Log.e("TYPE", type);
        if (type.equals("NUMBER")) {
            //intial screen is a number view
            makeRangeVisable();
            rangeScreen();
        } else if (type.equals("BUTTON")) {
            //intial screen is a button view
            makeButtonVisible();
            buttonScreen(3); //just options for 3 buttons right now.
        } else {
            makeTerminalVisible();
            terminalScren();
        }
        /*
        questionBox = (TextView) findViewById(R.id.questionView);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));
        editText = (EditText) findViewById(R.id.rangeTextBox);
        cont = (Button) findViewById(R.id.rangeContinue);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab text from bar
                String response = editText.getText().toString();

                //go to the next node
                questionHandler.giveInput(response);
                //Log.e("DEBUG", response);
                //Log.e("DEBUG", questionHandler.getCurrentQuestion());
            }
        });*/
    }

    private void makeRangeVisable(){
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

        //frameLayout.addView(findViewById(R.id.rangeLayout));
        questionBox = (TextView) findViewById(R.id.questionView);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));
        editText = (EditText) findViewById(R.id.rangeTextBox);
        cont = (Button) findViewById(R.id.rangeContinue);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab text from bar
                String response = editText.getText().toString();

                //go to the next node
                questionHandler.giveInput(response);
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    makeButtonVisible();
                    buttonScreen(3);         //default is 3 buttons
                } else {                    //Also add logical options
                    makeTerminalVisible();
                    terminalScren();
                }

            }
        });
    }

    private void buttonScreen(int numButtons) {
        questionBox = (TextView) findViewById(R.id.questionViewB);
        questionBox.setText(questionHandler.getCurrentQuestion());
        questionBox.setBackgroundColor(getResources().getColor(R.color.silver));

        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        unknown = (Button) findViewById(R.id.unknown);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    makeButtonVisible();
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
                questionHandler.giveInput("no");
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    makeButtonVisible();
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
                questionHandler.giveInput("yes"); //Make special notice in history for review
                String type = questionHandler.getCurrentQuestionType();
                if (type.equals("NUMBER")) {
                    rangeScreen();
                } else if (type.equals("BUTTON")) {
                    makeButtonVisible();
                    buttonScreen(3);         //default is 3 buttons
                } else {                    //Also add logical options
                    makeTerminalVisible();
                    terminalScren();
                }
            }
        });
    }

    private void terminalScren(){

    }
}
