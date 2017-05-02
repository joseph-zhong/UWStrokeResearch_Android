package com.stroke_trial_research.str;
import java.io.Serializable;

/**
 * Created by James on 10/20/2016.
 *
 * Basic Node class which dictates the kind of functionality that
 * all the nodes should have. All nodes have a number keeping
 * track of how many other nodes they connect to.
 * They also have  a spot for their type, their id and their question
 * that should be displayed to the screen (except logic which is just for linking other nodes)
 * Nodes can also be compared to one another to see which one comes first or to sort them.
 */
public abstract class Node implements Comparable<Node>, Serializable {

    public static final String NUMBER_TYPE = "NUMBER";
    public static final String BUTTON_TYPE = "BUTTON";
    public static final String OR_TYPE = "OR";
    public static final String RESULT_TYPE = "RESULT";
    public static final String UNKNOWN_RESPONSE = "UNKNOWN";

    //Number of Nodes in the connections
    protected int connectNum;

    //The QID of the file
    protected String QID;

    //The body of the question in string format;
    protected String question;

    //The type of node that is being used (Range, Discrete,... etc)
    protected String type;

    //The answer the user selects for the node
    protected String answer;

    public Node(int connectNum, String QID, String question, String type) {
        this.connectNum = connectNum;
        this.QID = QID;
        this.question = question;
        this.type = type;
        this.answer = "";
    }

    public String getQID() {
        return QID;
    }

    public int getConnectNum() {
        return connectNum;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // Says that higher up nodes (ie with smaller QIDS) are greater and
    //that  q nodes are always greater than r nodes.
    public int compareTo(Node node) {
        if (node == null) {
            throw new NullPointerException();
        }
        if (this.QID.startsWith("q") && node.QID.startsWith("r")) {
            return 1;
        } else if (this.QID.startsWith("r") && node.QID.startsWith("q")) {
            return -1;
        } else {
            return Integer.parseInt(node.QID.substring(1)) - Integer.parseInt(this.QID.substring(1));
        }
    }

    @Override
    public String toString() {
        return ("Q:" + this.question +"\nA: " + this.answer);
    }

}