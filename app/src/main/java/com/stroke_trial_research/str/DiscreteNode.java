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

    //Constructor for a HashSet of nodes
    public DiscreteNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "BUTTON");
        this.connections = new HashMap<String, String>();
        this.nodeConnections = new HashMap<String, Node>();
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
}
