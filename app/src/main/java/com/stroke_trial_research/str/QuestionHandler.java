package com.stroke_trial_research.str;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Kieran on 11/30/16.
 */

/**
 * Used to traverse the question tree and get necessary data from the question tree
 */
public class QuestionHandler {
    private Node currentQuestion;
    private Stack<Node> history;
    private List<Node> nodes;


    public QuestionHandler(List<Node> nodes){
        this.nodes = nodes;
        this.history = new Stack<Node>();

        //Set the start node as q001
        setCurrentNode("q001", null);

    }

    //Get the question of the current node the handler is observing in the tree
    public String getCurrentQuestion(){
        if(this.currentQuestion.getType().equals(Node.OR_TYPE)){
            //If the type is OR, return QUIDs
            return getOrQuestions((LogicNode) this.currentQuestion);
        }else if(this.currentQuestion.getType().equals("RESULT")){
            //If the type is result, return researcher information
            ResultNode node = (ResultNode) this.currentQuestion;
            return node.getQuestion() + "\n" + node.getPhone();
        }
        //Return the question of the current node
        return this.currentQuestion.question;
    }

    //Look up the question by quid, returns null if node with quid not existing
    public String lookUpQuestion(String quid){
        for(Node node : nodes){
            if(node.getQID().equals(quid)){
                return node.getQuestion();
            }
        }
        return null;
    }

    //Return the type of the current question
    public String getCurrentQuestionType(){
        return this.currentQuestion.type;
    }

    public String getCurrentAnswer() {
        return this.currentQuestion.answer;
    }

    public Stack<Node> getQuestionHistory(){
        return this.history;
    }

    public Node getPrevNode() {
        if (this.history == null || this.history.isEmpty()) {
            return null;
        }
        return this.history.peek();
    }

    //Moves the current node to a new node depending on the input
    public String giveInput(String input){
        switch(this.currentQuestion.getType()){
            case Node.NUMBER_TYPE: //Requires number as an input
                Map<Range, String> nextNumber = ((RangeNode) this.currentQuestion).getConnections();
                int answer = 0;
                try {
                    answer = Integer.parseInt(input);
                } catch(NumberFormatException e) {
                    return "Please insert numeric value";
                }
                for(Range range : nextNumber.keySet()){
                    //TODO implement Range.isBetween method
                    if(range.isBetween(answer)){
                        Log.e("connections", nextNumber.get(range));
                        setCurrentNode(nextNumber.get(range), input);
                    }
                }
                return "Please insert numeric value inside the specified range";
            case Node.BUTTON_TYPE: //Requires yes/no input
                Map<String, String> nextNodes = ((DiscreteNode) this.currentQuestion).getConnections();

                try {
                    String next = nextNodes.get(input);
                    setCurrentNode(next, input);
                    return "You have answered " + input;
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                    return "Illegal Key";
                }

            case Node.OR_TYPE: //Requires quid of the question the user wants to answer
                setCurrentNode(input);
                return "";
            case Node.UNKNOWN_RESPONSE: //FOR UNKNOWN OUTCOME NODES
                return "This does not require an answer";
            case Node.RESULT_TYPE: //FOR RESULT NODES
                return "This does not require an answer";
            default:
                break;
        }
        return "Error: No question available";
    }

    //Set the current node with the specified quid
    private void setCurrentNode(String QUID){
        Log.v("VERBOSE", "setCurrentNode called");
        for(Node node : this.nodes){
            Log.d("thing", node.getQuestion() + " " + node.getQID());
            if(node.getQID().equals(QUID)){
                this.currentQuestion = node;
                /* no history right now
                if(node.getType().equals(Node.OR_TYPE)) {
                    String s = getOrQuestions((LogicNode) node);
                    this.history.push(this.lookUpQuestion(s.substring(0, 4))
                            + " or " + this.lookUpQuestion(s.substring(5)));
                }else{
                    this.history.push(this.currentQuestion.getQuestion());
                }*/
            }
        }
    }

    //Setting the current node with input
    private void setCurrentNode(String QUID, String input){
        //push the current question
        Log.v("VERBOSE", "setCurrentNode called");
        if (this.currentQuestion != null) {
            if(this.currentQuestion.getType().equals(Node.OR_TYPE)) {
                //do nothing?
                //String s = getOrQuestions((LogicNode) this.currentQuestion);
                //this.history.push(this.lookUpQuestion(s.substring(0, 4)) + " or " + this.lookUpQuestion(s.substring(5)) + " A: " + input);
            }else{
                this.currentQuestion.setAnswer(input);
                this.history.push(this.currentQuestion);
            }
        }

        for(Node node : this.nodes){
            if(node.getQID().equals(QUID)){
                this.currentQuestion = node;
            }
        }
    }

    //Get quid's of a logic node
    private String getOrQuestions(LogicNode n){
        return n.getFirst() + " " + n.getSecond();
    }

    public List<String> getConnectingNodes(){
        String type = getCurrentQuestionType();
        List<String> list = new ArrayList<String>();
        switch (type) {
            case Node.NUMBER_TYPE:
                Map<Range, String> mapR = ((RangeNode) this.currentQuestion).getConnections();
                for( String s : mapR.values()){
                    list.add(s);
                }
                return list;
            case Node.BUTTON_TYPE:
                Map<String, String> mapB = ((DiscreteNode) this.currentQuestion).getConnections();
                for (String s: mapB.values()){
                    list.add(s);
                }
                return list;
            case Node.OR_TYPE:
                list.add(((LogicNode) this.currentQuestion).getFirst());
                list.add(((LogicNode) this.currentQuestion).getSecond());
                return list;
            default:
                break;
        }
        return null;
    }

    public List<String> getConnectingAnswers() {
        String type = getCurrentQuestionType();
        List<String> list = new ArrayList<String>();
        switch (type) {
            case Node.NUMBER_TYPE:
                Map<Range, String> mapR = ((RangeNode) this.currentQuestion).getConnections();
                for(Range r : mapR.keySet()){
                    list.add(r.toString());
                }
                return list;
            case Node.BUTTON_TYPE:
                Map<String, String> mapB = ((DiscreteNode) this.currentQuestion).getConnections();
                for (String s: mapB.keySet()){
                    list.add(s);
                }
                return list;
            case Node.OR_TYPE:
                list.add(((LogicNode) this.currentQuestion).getFirst());
                list.add(((LogicNode) this.currentQuestion).getSecond());
                return list;
            default:
                break;
        }
        return null;
    }

    public Node revertHistory() {
        if (this.history.isEmpty()) {
            return null;
        }
        Node n = this.history.pop();
        this.currentQuestion = n;
        return n;
    }
}
