package com.stroke_trial_research.str;

/**
 * Created by James on 10/20/2016.
 *
 * Basic Node class which dictates the kind of functionality that
 * all the nodes should have. All nodes have a number keeping
 * track of how many other nodes they connect to.
 * They also have  a spot for their type, their id and their question
 * that should be displayed to the screen (except logic which is just for linking other nodes)
 */
public abstract class Node {

    //Number of Nodes in the connections
    protected int connectNum;

    //The QID of the file
    protected String QID;

    //The body of the question in string format;
    protected String question;

    //The type of node that is being used (Range, Discrete,... etc)
    protected String type;

    public Node(int connectNum, String QID, String question, String type) {
        this.connectNum = connectNum;
        this.QID = QID;
        this.question = question;
        this.type = type;
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
}
