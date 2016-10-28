package com.uw_cse.str;

import java.util.Map;

/**
 * Created by James on 10/20/2016.
 */
public class Node {

    //Number of Nodes in the connections
    protected int connectNum;

    //The QID of the file
    protected String QID;

    //The body of the question in string format;
    protected String question;

    public Node(int connectNum, String QID, String question) {
        this.connectNum = connectNum;
        this.QID = QID;
        this.question = question;
    }

    public String getQID() {
        return QID;
    }

}
