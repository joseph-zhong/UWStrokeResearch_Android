package com.stroke_trial_research.str;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

    private static final int YES_INDEX = 0;
    private static final int UNKNOWN_INDEX = 1;
    private static final int NO_INDEX = 2;
    private static final int LEFT_INDEX = 3;
    private static final int RIGHT_INDEX = 4;

    private int lastClicked = -1;

    private QuestionHandler questionHandler;
    private TextView questionTextView, historyQuestionTextView, historyAnswerTextView;
    private EditText answerEditText;
    private Button yes, no, unknown, left, right,
            historyUnknown, historyYes, historyNo, historyLeft, historyRight;
    private RelativeLayout rangeView, spinnerView,
            terminalView, buttonView, twoButtonView, currentView,
            historyLayout;
    private Spinner buttonSpinner;
    private String spinnerResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONParser jsonParser = new JSONParser();

        //Grab the Node List
        List<Node> nodeList = jsonParser.getNodeList(this, R.raw.decision_tree);

        setContentView(R.layout.combined_view);

        // init relativeLayouts
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
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionHandler = new QuestionHandler(nodeList);
        String type = questionHandler.getCurrentQuestionType();
        questionTextView.setText(questionHandler.getCurrentQuestion());

        View.OnClickListener traverseHistory = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TESTING", "Traverse History OnClicked!");
                hideKeyboard();
                Node prevNode = questionHandler.revertHistory();

                Stack<Node> asdf = (Stack<Node>) questionHandler.getQuestionHistory().clone();
                while (!asdf.isEmpty()) {
                    Log.i("TESTING", "History: " + asdf.pop());
                }
                Log.i("TESTING", "History: " + prevNode.getType());

                String type = questionHandler.getCurrentQuestionType();
                questionTextView.setText(questionHandler.getCurrentQuestion());
                if (type.equals(Node.NUMBER_TYPE)) {
                    updateView(rangeView);

                    openKeyboard();
                } else if (type.equals(Node.BUTTON_TYPE)) {

                    int size = questionHandler.getConnectingAnswers().size();

                    if (size == 3) {
                        updateView(buttonView);
                    } else if (size == 2) {
                        updateView(twoButtonView);
                        twoButtonViewInit();
                    } else {
                        updateView(spinnerView);
                        initSpinnerResults();
                    }
                } else {                    //Also add logical options
                    updateView(terminalView);
                    terminalScreen();
                }
            }
        };
        // history
        historyLayout = (RelativeLayout) findViewById(R.id.historyLayout);
        historyUnknown = (Button) findViewById(R.id.historyUnknown);
        historyNo = (Button) findViewById(R.id.historyNo);
        historyYes = (Button) findViewById(R.id.historyYes);
        historyLeft = (Button) findViewById(R.id.historyLeft);
        historyRight = (Button) findViewById(R.id.historyRight);
        historyAnswerTextView = (TextView) findViewById(R.id.historyAnswer);
        historyQuestionTextView = (TextView) findViewById(R.id.historyQuestionTextView);

        historyLayout.setOnClickListener(traverseHistory);
        historyUnknown.setOnClickListener(traverseHistory);
        historyNo.setOnClickListener(traverseHistory);
        historyYes.setOnClickListener(traverseHistory);
        historyLeft.setOnClickListener(traverseHistory);
        historyRight.setOnClickListener(traverseHistory);
        historyQuestionTextView.setOnClickListener(traverseHistory);
        historyAnswerTextView.setOnClickListener(traverseHistory);

        updateQuestion();
    }

    private void hideKeyboard() {
//        ((InputMethodManager) getSystemService(TreeTraverser.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                answerEditText.getWindowToken(), 0);
    }

    private void updateView(View newV) {
        resetHistoryView();
        this.currentView.setVisibility(View.GONE);
        newV.setVisibility(View.VISIBLE);

        //TODO Assert type
        this.currentView = (RelativeLayout) newV;

        // update history
        Node prevNode = questionHandler.getPrevNode();
        if (prevNode != null) {
            historyQuestionTextView.setText(prevNode.question);
            historyQuestionTextView.setVisibility(View.VISIBLE);
            if (prevNode.type.equals(Node.BUTTON_TYPE)) {
                switch (lastClicked) {
                    case RIGHT_INDEX:
                        historyRight.setText(prevNode.answer);
                        historyRight.setVisibility(View.VISIBLE);
                        break;
                    case LEFT_INDEX:
                        historyLeft.setText(prevNode.answer);
                        historyLeft.setVisibility(View.VISIBLE);
                        break;
                    case NO_INDEX:
                        historyNo.setText(prevNode.answer);
                        historyNo.setVisibility(View.VISIBLE);
                        break;
                    case YES_INDEX:
                        historyYes.setText(prevNode.answer);
                        historyYes.setVisibility(View.VISIBLE);
                        break;
                    case UNKNOWN_INDEX:
                        historyUnknown.setText(prevNode.answer);
                        historyUnknown.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                // text response
                historyAnswerTextView.setText(prevNode.answer);
                historyAnswerTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    private void resetHistoryView() {
        historyAnswerTextView.setText("");
        historyQuestionTextView.setText("");

        historyRight.setVisibility(View.INVISIBLE);
        historyLeft.setVisibility(View.INVISIBLE);
        historyNo.setVisibility(View.INVISIBLE);
        historyYes.setVisibility(View.INVISIBLE);
        historyUnknown.setVisibility(View.INVISIBLE);
        historyQuestionTextView.setVisibility(View.INVISIBLE);
        historyAnswerTextView.setVisibility(View.INVISIBLE);
    }

    //Initialize
    private void rangeScreen() {
        final Button rangeCont = (Button) findViewById(R.id.rangeContinue);

        answerEditText = (EditText) findViewById(R.id.rangeTextBox);

        rangeCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TESTING", "Continue button clicked");
                //grab text from bar
                String response = answerEditText.getText().toString();

                //go to the next node
                questionHandler.giveInput(response);
                updateQuestion();
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
                hideKeyboard();
                questionHandler.giveInput(spinnerResp);
                updateQuestion();
            }
        });
    }

    /** only updates for next question movements */
    private void updateQuestion() {
        answerEditText.setText("");

        String type = questionHandler.getCurrentQuestionType();
        while (type.equals(Node.OR_TYPE)) {
            ArrayList<String> temp = (ArrayList<String>) questionHandler.getConnectingNodes();
            String first = temp.get(0);
            questionHandler.giveInput(first);
            type = questionHandler.getCurrentQuestionType();
        }
        if (type.equals(Node.NUMBER_TYPE)) {
            updateView(rangeView);
            questionTextView.setText(questionHandler.getCurrentQuestion());
            openKeyboard();
        } else if (type.equals(Node.BUTTON_TYPE)) {
            int size = questionHandler.getConnectingAnswers().size();

            if (size == 3) {
                updateView(buttonView);
                questionTextView.setText(questionHandler.getCurrentQuestion());
            } else if (size == 2) {
                updateView(twoButtonView);
                twoButtonViewInit();
            } else {
                updateView(spinnerView);
                initSpinnerResults();
            }
        } else {                    //Also add logical options
            updateView(terminalView);
            terminalScreen();
        }
    }

    //initialize Button Listeners
    private void buttonScreen() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        unknown = (Button) findViewById(R.id.unknown);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                lastClicked = YES_INDEX;

                questionHandler.giveInput("yes");
                updateQuestion();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                lastClicked = NO_INDEX;
                questionHandler.giveInput("no");
                updateQuestion();
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                lastClicked = UNKNOWN_INDEX;
                questionHandler.giveInput("unknown");
                updateQuestion();
            }
        });
    }

    private void twoButtonScreen() {
        right = (Button) findViewById(R.id.right);
        left = (Button) findViewById(R.id.left);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                lastClicked = RIGHT_INDEX;
                questionHandler.giveInput(right.getText().toString());
                updateQuestion();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                lastClicked = LEFT_INDEX;
                questionHandler.giveInput(left.getText().toString());
                updateQuestion();
            }
        });
    }

    //Sets the Text in the question
    private void initSpinnerResults() {
        questionTextView.setText(questionHandler.getCurrentQuestion());
        List<String> list = questionHandler.getConnectingAnswers();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buttonSpinner.setAdapter(adapter);

        buttonSpinner.setOnItemSelectedListener(this);
    }

    //Sets the Text of the Buttons when there are 2 to view
    private void twoButtonViewInit() {
        List<String> list = questionHandler.getConnectingAnswers();
        right.setText(list.get(0));
        left.setText(list.get(1));
    }

    private void terminalScreen(){
        questionTextView.setText(questionHandler.getCurrentQuestion());
        Log.e("Term get", questionHandler.getCurrentQuestion());

        final Button history = (Button) findViewById(R.id.history);

        final Intent intent = new Intent(this, HistoryList.class);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                intent.putExtra(HistoryList.STACK_KEY, questionHandler.getQuestionHistory());
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
        answerEditText.requestFocus();
        answerEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
                        answerEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }
}
