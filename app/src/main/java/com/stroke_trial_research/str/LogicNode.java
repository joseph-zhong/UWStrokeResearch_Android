package com.stroke_trial_research.str;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 * Node for questions that must evaluate logical expressions and then
 * evaluate to binary decision.
 * Has a first and second option field corresponding to
 * the left and right hand of a logical expression respectively.
 * Also has a first and second Node connection
 *
 */
public class LogicNode extends Node {
    //The logical expression to parse and arrange
    //private String logicalExp;
    private String first;
    private String second;
    private Node firstN;
    private Node secondN;

    //Constructor for basic build
    public LogicNode(int connectNum, String QID, String question, String type) {
        super(connectNum, QID, question, type);
        this.first = null;
        this.second = null;
        this.firstN = null;
        this.secondN = null;
    }

    //Constructor for complete build
    public LogicNode(int connectNum, String QID, String question, String type, String first, String second) {
        super(connectNum, QID, question, type);
        this.first = first;
        this.second = second;
        this.firstN = null;
        this.secondN = null;
    }

    public LogicNode(int connectNum, String QID, String question, String type, Node first, Node second) {
        super(connectNum, QID, question, type);
        this.first = null;
        this.second = null;
        this.firstN = first;
        this.secondN = second;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setFirstN(Node firstN) {
        this.firstN = firstN;
    }

    public void setSecondN(Node secondN) {
        this.secondN = secondN;
    }

    public Node getSecondN() {
        return secondN;
    }

    public Node getFirstN() {
        return firstN;
    }
}
