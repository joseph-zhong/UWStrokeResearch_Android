package com.stroke_trial_research.str;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by James on 11/9/2016.
 *
 * -Purpose
 * Class for taking JSON files and parsing them
 * into usable data structures.
 *
 * -Future upgrades
 * Create a method for grabbing files from internet source
 * Create a method for processing into Tree instead of set
 * Make a subclass of something more robust. Add loading screen
 */
//possible better subclassing in future. (service)
public class JSONParser {

    public JSONParser() {
        super();
    }

    //load a file from the Raw resource folder
    //Returns a string of the contents of the loaded file
    //Used for creating a JSON object from file
    public String loadJSONFromRaw(Context context, int name) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(name);
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
    // which it then loads into a HashSet of Nodes and returns
    public Set<Node> getNodeSet(Context context, int filename){
        Set<Node> nodes = new HashSet<Node>();
        
        try {
            JSONObject root = new JSONObject(this.loadJSONFromRaw(context, filename));
            Iterator<String> keys = root.keys();
            
            //Iterate through each node in the file
            while (keys.hasNext()) {
                String name = keys.next();
                Log.d("V", name);

                JSONObject jnode = root.getJSONObject(name);

                String type = jnode.getString("type");
                Log.d("V", type);

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

                switch (type) {
                    case "NUMBER": //REQUIRES USER INPUT OF NUMBER
                        RangeNode rn = new RangeNode(options.length(), name, question);
                        for (int i = 0; i < options.length(); i++) {
                            JSONObject jsonObject = options.getJSONObject(i);
                            String s = jsonObject.getString("type");
                            if (s.equals("RANGE")) {
                                rn.addRangeNode(jsonObject.getString("lower"),
                                        jsonObject.getString("upper"),
                                        s,
                                        jsonObject.getString("next"));
                            } else {
                                rn.addEqualsNode(jsonObject.getString("value"),
                                                jsonObject.getString("next"));
                            }
                        }
                        nodes.add(rn);
                        Log.d("V", "Successfully added a Number Node");
                        break;
                    case "BUTTON": //ADDS BUTTONS FOR OPTIONS
                        if (options.length() == 2) {
                            BinaryNode dn = new BinaryNode(options.length(), name, question);
                            for (int i = 0; i < options.length(); i++) {
                                JSONObject jsonObject = options.getJSONObject(i);
                                dn.addNode(jsonObject.getString("value"),
                                            jsonObject.getString("next"));
                            }
                            Log.d("V", "Successfully added a Binary Node");
                        } else {
                            DiscreteNode dn = new DiscreteNode(options.length(), name, question);
                            for (int i = 0; i < options.length(); i++) {
                                JSONObject jsonObject = options.getJSONObject(i);
                                dn.addNode(jsonObject.getString("value"),
                                        jsonObject.getString("next"));
                            }
                            Log.d("V", "Successfully added a Discrete Node");
                        }
                        break;
                    case "OR": //FOR LOGICAL OR NODE
                        JSONArray nexts = jnode.getJSONArray("question");
                        LogicNode ln = new LogicNode(nexts.length(), name, "", "OR",
                                                     nexts.getString(0), nexts.getString(1));
                        nodes.add(ln);
                        Log.d("V", "Successfully added a Logical OR Node");
                        break;
                    case "UNKNOWN": //FOR UNKNOWN OUTCOME NODES
                        ResultNode resnu = new ResultNode(name, question, jnode.getString("type"));
                        nodes.add(resnu);
                        Log.d("V", "Successfully added an UNKNOWN terminal node");
                        break;
                    case "RESULT": //FOR RESULT NODES
                        ResultNode resnr = new ResultNode(name, question, jnode.getString("type"),
                                                         jnode.getString("phone"),
                                                         jnode.getString("researcher"));
                        nodes.add(resnr);
                        Log.d("V", "Successfully added a RESULT terminal node");
                        break;
                    default:
                        Log.d("E", "Type not defined");
                        break;
                }
            }
        } catch (JSONException e) { //IF IT CANNOT LOAD THE JSON FILE
            e.printStackTrace();
            return null;
        }
        return nodes;
    }
}
