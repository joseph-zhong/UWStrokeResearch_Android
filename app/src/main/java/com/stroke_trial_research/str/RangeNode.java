package com.stroke_trial_research.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 * The class for a node where the user inputs a number
 * on a scale and it is checked to see if it fits within
 * a certain range.
 * Contains a map where certain Range instances are connected
 * to corresponding QIDS.
 * Also contains a map where ranges map to Nodes.
 */
public class RangeNode extends Node {
    private Map<Range, Node> nodeConnections;

    public RangeNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "NUMBER");
        this.nodeConnections = new HashMap<Range, Node>();
    }

    //Adds a node for an explicit range. Requires an upper or lower bound
    public void addRangeNode(String lower, String upper, String type, Node next) {
        Range range = new Range(lower, upper, type);
        this.nodeConnections.put(range, next);
    }

    //For adding a new node connection
    public void addEqualsNode(String value, Node next) {
        Range equals = new Range(value);
        this.nodeConnections.put(equals, next);
    }

    public Map<Range, Node> getNodeConnections() {
        return nodeConnections;
    }
}
