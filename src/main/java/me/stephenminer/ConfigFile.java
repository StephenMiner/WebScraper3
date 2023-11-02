package me.stephenminer;

import me.stephenminer.writers.OutputWriter;

import java.io.*;

public class ConfigFile {
    public File file;

    public ConfigFile(){
        init();
    }



    private void init()  {
        try{
            String path = WebScraper3.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace("WebScraper3-1.0-SNAPSHOT-shaded.jar", "config.txt");
            file = new File(path);
            createFile();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed to initialize file");
        }


    }

    private void createFile(){
        try {
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("Generated a new config file " + file.getName() + ". Please put your firefox profile path here.");
                FileWriter writer = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write("Make sure to log in to google on the firefox profile provided below before running this program!");
                bufferedWriter.newLine();
                bufferedWriter.write("fire-fox-profile:");
                bufferedWriter.close();
                writer.close();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not generate files due to a security thing");
        }
    }
    public String readSetting(String setting){
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String profileLine = bufferedReader.lines().filter(s->s.contains(setting + ":")).findFirst().orElse("");
            return profileLine.replace(setting + ":","").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean hasValue(String setting){
        String val = readSetting(setting);
        return !val.isEmpty();
    }
    public File getFile(){ return file; }
}
