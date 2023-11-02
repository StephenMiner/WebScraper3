package me.stephenminer.writers;

import me.stephenminer.writers.OutputWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class LinkWriter extends OutputWriter<HashMap<String,String>> {
    public LinkWriter(String assignment, String className, HashMap<String, String> data) {
        super("Student-Links", assignment + "-" + className, data);
    }

    @Override
    protected void writeData(File file) {
        try {
            FileWriter writer = new FileWriter(file, false);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("[Student-Name](Link) \\");
            bufferWriter.newLine();
            for (String name : data.keySet()){
                String link = data.get(name);
                //remove last name
                name = name.substring(0,name.indexOf(" "));
                bufferWriter.write("[" + name + "](" + link + ")" + " \\");
                bufferWriter.newLine();
            }
            bufferWriter.close();
            writer.close();
        }catch (Exception e){e.printStackTrace();}
    }
}
