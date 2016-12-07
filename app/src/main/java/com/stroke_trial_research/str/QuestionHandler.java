package com.stroke_trial_research.str;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private Stack<String> history;
    private Set<Node> nodes;

    public QuestionHandler(Set<Node> nodes){
        this.nodes = nodes;
        this.history = new Stack<String>();
        //Set the start node as q002
        setCurrentNode("q003");
    }

    //Get the question of the current node the handler is observing in the tree
    public String getCurrentQuestion(){
        if(this.currentQuestion.getType().equals("OR")){
            //If the type is OR, return QUIDs
            return getOrQuestions((LogicNode) this.currentQuestion);
        }else if(this.currentQuestion.getType().equals("RESULT")){
            //If the type is result, return researcher information
            ResultNode node = (ResultNode) this.currentQuestion;
            return "Please contact " + node.getResearcher() + " at " + node.getPhone();
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

    public Stack<String> getQuestionHistory(){
        return this.history;
    }

    //Moves the current node to a new node depending on the input
    public String giveInput(String input){
        switch(this.currentQuestion.getType()){
            case "NUMBER": //Requires number as an input
                Log.d("message", "first question asdfasdfasdfasdf" );
                Map<Range, String> nextNumber = ((RangeNode) this.currentQuestion).getConnections();
                int answer = 0;
                try {
                    answer = Integer.parseInt(input);
                } catch(NumberFormatException e) {
                    return "Please insert numeric value";
                }
                for(Range range : nextNumber.keySet()){
                    if(range.lower <= answer && answer < range.upper){
                        setCurrentNode(nextNumber.get(range));
                    }
                }
                return "Please insert numeric value inside the specified range";
            case "BUTTON": //Requires yes/no input
                Map<Boolean, String> nextNodes = ((BinaryNode) this.currentQuestion).getConnections();

                if(input.toLowerCase().equals("yes")){
                    setCurrentNode(nextNodes.get("yes"));
                    return "You have answered yes";
                }else if(input.toLowerCase().equals("no")) {
                    setCurrentNode(nextNodes.get("no"));
                    return "You have answered no";
                }else {
                    return "Please answer Yes or No";
                }
            case "OR": //Requires quid of the question the user wants to answer
                setCurrentNode(input);
                return "";
            case "UNKNOWN": //FOR UNKNOWN OUTCOME NODES
                return "This does not require an answer";
            case "RESULT": //FOR RESULT NODES
                return "This does not require an answer";
            default:
                break;
        }
        return "Error: No question available";
    }

    //Set the current node with the specified quid
    private void setCurrentNode(String QUID){
        for(Node node : this.nodes){
            if(node.getQID().equals(QUID)){
                this.currentQuestion = node;
                if(node.getType().equals("OR")) {
                    String s = getOrQuestions((LogicNode) node);
                    this.history.push(this.lookUpQuestion(s.substring(0, 4)) + " or " + this.lookUpQuestion(s.substring(5)));
                }else{
                    this.history.push(this.currentQuestion.getQuestion());
                }
            }
        }
    }

    //Get quid's of a logic node
    private String getOrQuestions(LogicNode n){
        return n.getFirst() + " " + n.getSecond();
    }
}