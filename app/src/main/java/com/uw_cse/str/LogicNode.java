package com.uw_cse.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 * Node for questions that must evaluate logical expressions and then
 * evaluate to binary decision. Maybe subclass from binary?
 */
public class LogicNode extends Node {
    //The logical expression to parse and arrange
    private String logicalExp;
    private Map<Boolean, Node> connections;

    public LogicNode(int connectNum, String QID, String question, String logicalExp) {
        super(connectNum, QID, question);
        this.logicalExp = logicalExp;
    }

    public Map<Boolean, Node> parseExpression() {
        return new HashMap<Boolean, Node>();
    }
}
