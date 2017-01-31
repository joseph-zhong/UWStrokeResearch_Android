package com.stroke_trial_research.str;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by James on 11/9/2016.
 *
 * -Purpose
 * Class for taking JSON files and parsing them
 * into usable data structures.
 * Currently can parse into a Tree or a ArrayList
 *
 * -Future upgrades
 * Create a method for grabbing files from internet source
 * Make a subclass of something more robust. Add loading screen
 */
//possible better subclassing in future. (service)
public class JSONParser {

    public JSONParser() {
        super();
    }

    /**
     * Load a file from the Raw resource folder
     * Used for creating a JSON object from file
     * @param context
     * @param id ID of Resource
     * @return a string of the contents of the loaded file */
    public String loadJSONFromRaw(Context context, int id) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(id);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    //Loads a JSON file from raw and finds all possible nodes
    // which it then loads into a ArrayList of Node names and returns
    public ArrayList<Node> getNodeList(Context context, int filename){
        ArrayList<Node> nodes = new ArrayList<Node>();
        
        try {
            JSONObject root = new JSONObject(this.loadJSONFromRaw(context, filename));
            Iterator<String> keys = root.keys();
            
            //Iterate through each node in the file
            while (keys.hasNext()) {
                String name = keys.next();
              
                Log.d("DEBUG", name);                                   //name

                JSONObject jnode = root.getJSONObject(name);

                String type = jnode.getString("type");
                Log.d("DEBUG", type);                                  //type

                JSONArray options = null;
                String question = "";

                if (!(name.charAt(0) == 'r')) {
                    question = jnode.getString("question");

                    if (!type.equals("OR")) {
                        options = jnode.getJSONArray("options");
                    }
                } else {
                    question = jnode.getString("message");
                }
                Log.d("thing", name + " " + type);
                switch (type) {
                    case "NUMBER": //REQUIRES USER INPUT OF NUMBER
                        RangeNode rn = new RangeNode(options.length(), name, question);
                        for (int i = 0; i < options.length(); i++) {
                            JSONObject jsonObject = options.getJSONObject(i);
                            String s = jsonObject.getString("type");
                            if (s.equals("RANGE")) {
                                rn.addRangeNodeS(jsonObject.getString("lower"),
                                        jsonObject.getString("upper"),
                                        s,
                                        jsonObject.getString("next"));
                            } else {
                                rn.addEqualsNodeS(jsonObject.getString("value"),
                                                jsonObject.getString("next"));
                            }
                        }
                        nodes.add(rn);
                        Log.d("DEBUG", "Successfully added a Number Node");
                        break;
                    case "BUTTON": //ADDS BUTTONS FOR OPTIONS
                        DiscreteNode dn = new DiscreteNode(options.length(), name, question);
                        for (int i = 0; i < options.length(); i++) {
                            JSONObject jsonObject = options.getJSONObject(i);
                            dn.addNodeS(jsonObject.getString("value"),
                                    jsonObject.getString("next"));
                        }
                        nodes.add(dn);
                        Log.d("DEBUG", "Successfully added a Discrete Node");
                        break;
                    case "OR": //FOR LOGICAL OR NODE
                        JSONArray nexts = jnode.getJSONArray("question");
                        LogicNode ln = new LogicNode(nexts.length(), name, "", "OR",
                                                     nexts.getString(0), nexts.getString(1));
                        nodes.add(ln);
                        Log.d("DEBUG", "Successfully added a Logical OR Node");
                        break;
                    case "UNKNOWN": //FOR UNKNOWN OUTCOME NODES
                        ResultNode resnu = new ResultNode(name, question, jnode.getString("type"));
                        nodes.add(resnu);
                        Log.d("DEBUG", "Successfully added an UNKNOWN terminal node");
                        break;
                    case "RESULT": //FOR RESULT NODES
                        ResultNode resnr = new ResultNode(name, question, jnode.getString("type"), jnode.getString("phone"));
                        nodes.add(resnr);
                        Log.d("DEBUG", "Successfully added a RESULT terminal node");
                        break;
                    default:
                        Log.e("ERROR", "Type not defined");
                        break;
                }
            }
        } catch (JSONException e) { //IF IT CANNOT LOAD THE JSON FILE
            e.printStackTrace();
            return null;
        }
        return nodes;
    }

    //Loads a JSON file from raw and finds a root node which it then
    //creates a Node tree from and returns the root node.
    public Node getNodeTree(Context context, int filename) {
        Node node = null;

        try {
            JSONObject root = new JSONObject(this.loadJSONFromRaw(context, filename));
            Iterator<String> keys = root.keys();

            if (keys.hasNext()) {
                String first = keys.next();
                Log.d("DEBUG", first);
                node = processNode(first, root);
            } else {
                Log.e("ERROR", "No nodes found");
            }


        } catch (JSONException e) { //IF IT CANNOT LOAD THE JSON FILE
            e.printStackTrace();
            return null;
        }

        return node;
    }

    //Recursive function for use with getNodeTree.
    private Node processNode(String QID, JSONObject root) {
        try {
            JSONObject jnode = root.getJSONObject(QID); //get the contents of the node
            Log.d("DEBUG", QID);

            String type = jnode.getString("type");
            Log.d("DEBUG", type);

            if (QID.startsWith("r")) { //Base case, return result node
                String question = jnode.getString("message");
                ResultNode rn = new ResultNode(QID, question, type);

                return rn;
            } else {
                switch (type) {
                    case "NUMBER":
                        JSONArray rop = jnode.getJSONArray("options"); //all connecting nodes
                        String rquestion = jnode.getString("question"); //the question

                        RangeNode rn = new RangeNode(rop.length(), QID, rquestion);

                        for (int i = 0; i < rop.length(); i++) {
                            JSONObject jsonObject = rop.getJSONObject(i);
                            if (jsonObject.getString("type").equals("RANGE")) {
                                rn.addRangeNode(jsonObject.getString("lower"),
                                                jsonObject.getString("upper"),
                                                jsonObject.getString("type"),
                                                processNode(jsonObject.getString("next"), root));
                            } else {
                                rn.addEqualsNode(jsonObject.getString("value"),
                                        processNode(jsonObject.getString("next"), root));
                            }
                        }

                        return rn;
                        //break;

                    case "BUTTON":
                        JSONArray bop = jnode.getJSONArray("options");
                        String bquestion = jnode.getString("question"); //the question

                        DiscreteNode bn = new DiscreteNode(bop.length(), QID, bquestion);

                        for (int i = 0; i < bop.length(); i++) {
                            JSONObject jsonObject = bop.getJSONObject(i);
                            bn.addNode(jsonObject.getString("value"),
                                        processNode(jsonObject.getString("next"), root));
                        }

                        return bn;

                        //break;
                    case "OR":
                        JSONArray nexts = jnode.getJSONArray("question");
                        LogicNode ln = new LogicNode(nexts.length(), QID, "", "OR",
                                                    processNode(nexts.getString(0), root),
                                                    processNode(nexts.getString(1), root));
                        return ln;

                        //break;
                    case "DEFAULT":
                        Log.d("E", "Illegal type");
                        return null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
