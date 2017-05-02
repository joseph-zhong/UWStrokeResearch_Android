package com.stroke_trial_research.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 *  For use with the BUTTON tag in the json file.
 *  Allows user to link a response to a node connection.
 *
 *  Contains a Hashmap linking phrases for buttons
 *  to connection node QIDS.
 *
 *  Also Contains a map of phrases for buttons to
 *  Nodes.
 */
public class DiscreteNode extends Node {
    //Connections of phrase to QID
    private Map<String, String> connections;
    private Map<String, Node> nodeConnections;
    private int answerIndex;

    //Constructor for a HashSet of nodes
    public DiscreteNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "BUTTON");
        this.connections = new HashMap<String, String>();
        this.nodeConnections = new HashMap<String, Node>();
        this.answerIndex = -1;
    }

    //Adds a new node name to the connections map.
    //requires a phrase option and a QID for next
    public void addNodeS(String option, String next){
        connections.put(option, next);
    }

    //Adds a new Node to the node connections map
    public void addNode(String option, Node next) {
        nodeConnections.put(option, next);
    }

    public Map<String, String> getConnections() {
        return connections;
    }

    public Map<String, Node> getNodeConnections() {
        return nodeConnections;
    }

    public int getAnswerIndex() {
        return this.answerIndex;
    }

    /**
     * For setting the index of the position of the Node
     * when it is displayed. 0-4 are the values of yes, no, unknown,
     * right and left currently.
     * @param value the value corresponding to the position on the screen the node should be
     * displayed as.
     */
    public void setAnswerIndex(int value) {
        if (value > 4 || value < 0) {
            throw new IllegalArgumentException("Must be between 0 and 4 inclusive");
        }
        this.answerIndex = value;
    }
}
