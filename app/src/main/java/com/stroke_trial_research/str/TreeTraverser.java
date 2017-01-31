package com.stroke_trial_research.str;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


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
    private Button yes, no, unknown, left, right;
    private  RelativeLayout rangeView, spinnerView,
            terminalView, buttonView, twoButtonView, currentView;
    private Spinner buttonSpinner;
    private String spinnerResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        JSONParser jsonParser = new JSONParser();
        //Grab the Node List
        List<Node> nodeList = jsonParser.getNodeList(this, bundle.getInt("ID"));

        setContentView(R.layout.combined_view);

//        historyView = (RelativeLayout) findViewById(R.id.)

        rangeView = (RelativeLayout) findViewById(R.id.rangeLayout);
        spinnerView = (RelativeLayout) findViewById(R.id.spinnerLayout);
        terminalView = (RelativeLayout) findViewById(R.id.terminalLayout);
        buttonView = (RelativeLayout) findViewById(R.id.buttonLayout);
        twoButtonView = (RelativeLayout) findViewById(R.id.twoButtonLayout);

        rangeView.setVisibility(View.GONE);
        spinnerView.setVisibility(View.GONE);
        terminalView.setVisibility(View.GONE);
        buttonView.setVisibility(View.GONE);
        twoButtonView.setVisibility(View.GONE);


        //DEBUG- change
        currentView = spinnerView;

        //Initialize all components and listeners
        buttonScreen();
        spinnerScreen();
        rangeScreen();
        twoButtonScreen();

        //Box For displaying the question
        questionBox = (TextView) findViewById(R.id.questionView);
        questionHandler = new QuestionHandler(nodeList);
        String type = questionHandler.getCurrentQuestionType();
        questionBox.setText(questionHandler.getCurrentQuestion());

        while (type.equals("OR")){
            ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
            String first = temp.get(0);
            questionHandler.giveInput(first);
            type = questionHandler.getCurrentQuestionType();
        }
        if (type.equals("NUMBER")) {
            //intial screen is a number view
            switchView(rangeView);
            rangeScreen();
            openKeyboard();
        } else if (type.equals("BUTTON")) {
            //intial screen is a button view
            int size = questionHandler.getConnectingAnswers().size();

            if (size == 3) {
                switchView(buttonView);
                questionBox.setText(questionHandler.getCurrentQuestion());
            } else if (size == 2) {
                switchView(twoButtonView);
                twoButtonViewInit();
            } else {
                switchView(spinnerView);
                initSpinnerResults();
            }
        } else {
            switchView(terminalView);
            terminalScreen();
        }
    }

    private void switchView (View newV) {
        this.currentView.setVisibility(View.GONE);
        newV.setVisibility(View.VISIBLE);

        //TODO Assert type
        this.currentView = (RelativeLayout) newV;
    }

    //Initialize
    private void rangeScreen() {
        final Button rangeCont = (Button) findViewById(R.id.rangeContinue);

        editText = (EditText) findViewById(R.id.rangeTextBox);
        editText.setText("");

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
                        questionBox.setText(questionHandler.getCurrentQuestion());
                        break;
                    case "BUTTON":
                        int size = questionHandler.getConnectingAnswers().size();

                        if (size == 3) {
                            switchView(buttonView);
                            questionBox.setText(questionHandler.getCurrentQuestion());
                        } else if (size == 2) {
                            switchView(twoButtonView);
                            twoButtonViewInit();
                        } else {
                            switchView(spinnerView);
                            initSpinnerResults();
                        }
                        break;
                    default:
                        switchView(terminalView);
                        terminalScreen();
                        break;
                }

            }
        });
    }

    //Initialize spinner listeners
    private void spinnerScreen() {
        buttonSpinner = (Spinner) findViewById(R.id.buttonSpinner);
        final Button spinnerCont = (Button) findViewById(R.id.spinnerContinue);

        spinnerCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput(spinnerResp);
                String type = questionHandler.getCurrentQuestionType();

                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                switch (type) {
                    case "NUMBER":
                        switchView(rangeView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                        openKeyboard();
                        break;
                    case "BUTTON":
                        int size = questionHandler.getConnectingAnswers().size();

                        if (size == 3) {
                            switchView(buttonView);
                            questionBox.setText(questionHandler.getCurrentQuestion());
                        } else if (size == 2) {
                            switchView(twoButtonView);
                            twoButtonViewInit();
                        } else {
                            switchView(spinnerView);
                            initSpinnerResults();
                        }
                        break;
                    default:
                        switchView(terminalView);
                        terminalScreen();
                        break;
                }
            }
        });
    }

    //initialize Button Listeners
    private void buttonScreen() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        unknown = (Button) findViewById(R.id.unknown);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                if (type.equals("NUMBER")) {
                    switchView(rangeView);
                    questionBox.setText(questionHandler.getCurrentQuestion());
                    openKeyboard();
                } else if (type.equals("BUTTON")) {
                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        switchView(buttonView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                    } else if (size == 2) {
                        switchView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        switchView(spinnerView);
                        initSpinnerResults();
                    }
                } else {                    //Also add logical options
                    switchView(terminalView);
                    terminalScreen();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("no");
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                if (type.equals("NUMBER")) {
                    switchView(rangeView);
                    questionBox.setText(questionHandler.getCurrentQuestion());
                    openKeyboard();
                } else if (type.equals("BUTTON")) {
                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        switchView(buttonView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                    } else if (size == 2) {
                        switchView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        switchView(spinnerView);
                        initSpinnerResults();
                    }
                } else {                    //Also add logical options
                    switchView(terminalView);
                    terminalScreen();
                }
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput("yes");
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                if (type.equals("NUMBER")) {
                    switchView(rangeView);
                    questionBox.setText(questionHandler.getCurrentQuestion());
                    openKeyboard();
                } else if (type.equals("BUTTON")) {
                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        switchView(buttonView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                    } else if (size == 2) {
                        switchView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        switchView(spinnerView);
                        initSpinnerResults();
                    }
                } else {                    //Also add logical options
                    switchView(terminalView);
                    terminalScreen();
                }
            }
        });
    }

    private void twoButtonScreen() {
        right = (Button) findViewById(R.id.right);
        left = (Button) findViewById(R.id.left);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput(right.getText().toString());
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                if (type.equals("NUMBER")) {
                    switchView(rangeView);
                    questionBox.setText(questionHandler.getCurrentQuestion());
                    openKeyboard();
                } else if (type.equals("BUTTON")) {
                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        switchView(buttonView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                    } else if (size == 2) {
                        switchView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        switchView(spinnerView);
                        initSpinnerResults();
                    }
                } else {                    //Also add logical options
                    switchView(terminalView);
                    terminalScreen();
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionHandler.giveInput(left.getText().toString());
                String type = questionHandler.getCurrentQuestionType();
                while (type.equals("OR")) {
                    ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
                    String first = temp.get(0);
                    questionHandler.giveInput(first);
                    type = questionHandler.getCurrentQuestionType();
                }
                if (type.equals("NUMBER")) {
                    switchView(rangeView);
                    questionBox.setText(questionHandler.getCurrentQuestion());
                    openKeyboard();
                } else if (type.equals("BUTTON")) {
                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        switchView(buttonView);
                        questionBox.setText(questionHandler.getCurrentQuestion());
                    } else if (size == 2) {
                        switchView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        switchView(spinnerView);
                        initSpinnerResults();
                    }
                } else { //Also add logical options
                    switchView(terminalView);
                    terminalScreen();
                }
            }
        });
    }

    private void initSpinnerResults() {
        questionBox.setText(questionHandler.getCurrentQuestion());
        List<String> list = questionHandler.getConnectingAnswers();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonSpinner.setAdapter(adapter);

        buttonSpinner.setOnItemSelectedListener(this);
    }

    private void twoButtonViewInit() {
        List<String> list = questionHandler.getConnectingAnswers();
        right.setText(list.get(0));
        left.setText(list.get(1));
    }

    private void terminalScreen(){
        terminalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(TreeTraverser.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        editText.getWindowToken(), 0);
            }
        });

        questionBox.setText(questionHandler.getCurrentQuestion());


        final Button history = (Button) findViewById(R.id.history);

        final Intent intent = new Intent(this, HistoryList.class);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                List<String> questions = new Stack<String>();
                Stack<String> s = questionHandler.getQuestionHistory();

                intent.putExtra("Quest", (Serializable) s);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerResp = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Write a case for if the user doesn't select anything to prevent blank submissions
    }

    private void openKeyboard() {
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
                        editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }
}
