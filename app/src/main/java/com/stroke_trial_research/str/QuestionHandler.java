package com.stroke_trial_research.str;

import android.util.Log;

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
    private Node head;

    public QuestionHandler(Node head) {
        this.head = head;
        this.history = new Stack<>();
        this.currentQuestion = head;
    }

    //Get the question of the current node the handler is observing in the tree
    public String getCurrentQuestion() {
        if(this.currentQuestion.getType().equals(Node.OR_TYPE)){
            //If the type is OR, return QIDS
            return getOrQuestions((LogicNode) this.currentQuestion);
        }else if(this.currentQuestion.getType().equals("RESULT")){
            //If the type is result, return researcher information
            ResultNode node = (ResultNode) this.currentQuestion;
            return node.getQuestion() + "\n" + node.getPhone();
        }
        //Return the question of the current node
        return this.currentQuestion.question;
    }

    /**
     * Searches the tree for the question of the Node with the given QID
     * @param quid The QID to search for.
     * @return The Question of the node which was searched for, or NULL.
     */
    public String lookUpQuestion(String quid) {
        Node n = searchTree(head, quid);
        return n == null ? null : n.getQuestion();
    }

    /**
     * Recursively searches through the tree from the given Node for the Node
     * with the given quid.
     * @param current The Node to check, after which its children will be checked
     * if children exist.
     * @param quid The quid to look for
     * @return The Node to be found, or null if no Node with the quid exists in the tree.
     */
    private Node searchTree(Node current, String quid) {
        if (current.QID.equals(quid)) {
            return current;
        } else { // recurse
            Node res;
            switch(current.getType()) {
                case Node.BUTTON_TYPE:
                    Map<String, Node> maps = ((DiscreteNode)current).getNodeConnections();
                    for (String s : maps.keySet()) {
                        res = searchTree(maps.get(s), quid);
                        if (res != null) {
                            return res;
                        }
                    }
                    return null;
                case Node.NUMBER_TYPE:
                    Map<Range, Node> mapr = ((RangeNode)current).getNodeConnections();
                    for (Range r : mapr.keySet()) {
                        res = searchTree(mapr.get(r), quid);
                        if (res != null) {
                            return res;
                        }
                    }
                    return null;
                case Node.OR_TYPE:
                    res = searchTree(((LogicNode)current).getFirstN(), quid);
                    if (res != null) {
                        return res;
                    }
                    return searchTree(((LogicNode)current).getFirstN(), quid);
                case Node.RESULT_TYPE:
                    return null;
                case Node.UNKNOWN_RESPONSE:
                    return null;
                default:
                    Log.e("Search Error", "Unidentified type: "+ current.getType());
                    return null;
            }
        }
    }

    /**
     * Gets the type of the current question
     * @return The type of the current question.
     */
    public String getCurrentQuestionType(){
        return this.currentQuestion.type;
    }

    /**
     * Gets the answer of the current Node. This will only be defined given that one
     * has previously been defined for the Node.
     * @return A String representing the answer of the current Node.
     */
    public String getCurrentAnswer() {
        return this.currentQuestion.answer;
    }

    /**
     * Gets a reference to the Stack containing the question history.
     * @return A reference to a Stack of all previously selected Nodes
     * in order.
     */
    public Stack<Node> getQuestionHistory(){
        return this.history;
    }

    /**
     * Given that at least one Node is on the history, returns a reference to
     * the Node of the previously answered question.
     * @return A reference to the previously answered question or null if none exists.
     */
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
                Map<Range, Node> nextNumber = ((RangeNode) this.currentQuestion).getNodeConnections();
                int answer;
                try {
                    answer = Integer.parseInt(input);
                } catch(NumberFormatException e) {
                    return "Please insert numeric value";
                }
                for(Range range : nextNumber.keySet()){
                    if(range.isBetween(answer)){
                        // @TODO Change setCurrentNode to just have the node right lads?
                        setCurrentNode(nextNumber.get(range), input);
                    }
                }
                return "Please insert numeric value inside the specified range";
            case Node.BUTTON_TYPE: //Requires yes/no input
                Map<String, Node> nextNodes = ((DiscreteNode) this.currentQuestion).getNodeConnections();
                try {
                    Node next = nextNodes.get(input.toUpperCase());
                    if (next == null) {
                        return "Illegal Key";
                    }
                    setCurrentNode(next, input);
                    return "You have answered " + input;
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                    return "Illegal Key";
                }

            case Node.OR_TYPE: //Requires quid of the question the user wants to answer
                // TODO Is this a reachable statement?
                setCurrentNode(input);
                return "OR?";
            case Node.UNKNOWN_RESPONSE: //FOR UNKNOWN OUTCOME NODES
                return "This does not require an answer";
            case Node.RESULT_TYPE: //FOR RESULT NODES
                return "This does not require an answer";
            default:
                break;
        }
        return "Error: No question available";
    }

    /**
     * Sets the answer of the current question Node to  be the input String,
     * updates the history, and sets the given Node to be the current question.
     * @param n A reference to the new current question.
     * @param input A string representing the answer the user gave to the current question.
     */
    private void setCurrentNode(Node n, String input) {
        if (n != null) {
            if (currentQuestion != null) {
                currentQuestion.setAnswer(input);
                history.push(currentQuestion);
            }
            currentQuestion = n;
        }
    }

    /**
     * Sets the current Node based on the given QID.
     * @TODO check all instances of usage to make sure that its valid.
     */
    private void setCurrentNode(String QID){
        Node n = searchTree(head, QID);
        if (n != null) {
            if (currentQuestion != null) {
                history.push(currentQuestion);
            }
            currentQuestion = n;
        } else {
            Log.e("setNode", "QID " + QID + " not found and cannot be set.");
        }
    }

    //Get quid's of a logic node formatted as a String
    private String getOrQuestions(LogicNode n){
        return n.getFirstN().getQuestion() + " " + n.getSecondN().getQuestion();
    }

    /**
     * Gets a List of all the QIDS of all the outgoing nodes
     * connected to the current Node.
     * @return A List of the adjacent outgoing QID Strings
     */
    public List<String> getConnectingNodes(){
        String type = getCurrentQuestionType();
        List<String> list = new ArrayList<String>();
        switch (type) {
            case Node.NUMBER_TYPE:
                Map<Range, Node> mapR = ((RangeNode) this.currentQuestion).getNodeConnections();
                for(Node ns: mapR.values()) {
                    list.add(ns.getQID());
                }
                return list;
            case Node.BUTTON_TYPE:
                Map<String, Node> mapB = ((DiscreteNode) this.currentQuestion).getNodeConnections();
                for (Node nb : mapB.values()){
                    list.add(nb.getQID());
                }
                return list;
            case Node.OR_TYPE:
                list.add(((LogicNode) this.currentQuestion).getFirstN().getQID());
                list.add(((LogicNode) this.currentQuestion).getSecondN().getQID());
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
                Map<Range, Node> mapR = ((RangeNode) this.currentQuestion).getNodeConnections();
                for(Range r : mapR.keySet()){
                    list.add(r.toString());
                }
                return list;
            case Node.BUTTON_TYPE:
                Map<String, Node> mapB = ((DiscreteNode) this.currentQuestion).getNodeConnections();
                for (String s: mapB.keySet()){
                    list.add(s);
                }
                return list;
            case Node.OR_TYPE:
                list.add(((LogicNode) this.currentQuestion).getFirstN().getQID());
                list.add(((LogicNode) this.currentQuestion).getSecondN().getQID());
                return list;
            default:
                break;
        }
        return null;
    }

    public static List<String> getConnectingAnswers(Node node) {
        String type = node.type;
        List<String> list = new ArrayList<String>();
        switch (type) {
            case Node.NUMBER_TYPE:
                Map<Range, Node> mapR = ((RangeNode) node).getNodeConnections();
                for(Range r : mapR.keySet()){
                    list.add(r.toString());
                }
                return list;
            case Node.BUTTON_TYPE:
                Map<String, Node> mapB = ((DiscreteNode) node).getNodeConnections();
                for (String s: mapB.keySet()){
                    list.add(s);
                }
                return list;
            case Node.OR_TYPE:
                list.add(((LogicNode) node).getFirstN().getQID());
                list.add(((LogicNode) node).getSecondN().getQID());
                return list;
            default:
                break;
        }
        return null;
    }

    public boolean setAnswerIndex(int value) {
        if (this.currentQuestion instanceof DiscreteNode) {
            ((DiscreteNode)this.currentQuestion).setAnswerIndex(value);
            return true;
        }
        return false;
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
