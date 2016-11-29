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
public abstract class Node implements Comparable<Node> {

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
}
