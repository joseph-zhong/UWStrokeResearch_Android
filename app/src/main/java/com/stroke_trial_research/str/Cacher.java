package com.stroke_trial_research.str;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kieran on 5/20/17.
 */

public class Cacher {
    private static final int BYTE_LIMIT = 40000;

    public static boolean isLatestFile(String name, File directory) {
        long givenTimeStamp = Long.parseLong(name.substring(0, name.indexOf('_')));
        name = name.substring(name.indexOf('_'), name.length() - 1);

        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            String savedName = files[i].getName();
            if(savedName.contains("_")) {
                long timestamp = Long.parseLong(savedName.substring(0, savedName.indexOf('_')));
                savedName = savedName.substring(savedName.indexOf('_'), savedName.length() - 1);

                if (files[i].isFile() && savedName.contains(name)) {
                    return timestamp <= givenTimeStamp;
                }
            }
        }
        return true;
    }

    public static void saveFile(File directory, String filename, String content) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            //delete old version
            File last = getLatestFile(filename, directory);
            if(last != null){
                last.delete();
            }

            //if over capacity clear files
            if(overCapacity(directory)){
                clearInternalStorage(directory);
            }

            File newFile = new File(directory, System.currentTimeMillis() + "_" + filename);
            newFile.createNewFile();

            fw = new FileWriter(newFile);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getLatestFile(String name, File directory) {
        List<String> results = new ArrayList<String>();
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if (files[i].isFile() && filename.contains(name)) {
                return files[i];
            }
        }
        return null;
    }

    public static void clearInternalStorage(File directory) {
        List<String> results = new ArrayList<String>();

        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile() && files[i].getName().contains(".json")) {
                files[i].delete();
            }
        }
    }

    public static List<String> filenames(File directory){
        List<String> results = new ArrayList<String>();
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().contains(".json")) {
                results.add(file.getName());
            }
        }
        return results;
    }

    private static boolean overCapacity(File directory){
        return directory.length() > BYTE_LIMIT;
    }
}

