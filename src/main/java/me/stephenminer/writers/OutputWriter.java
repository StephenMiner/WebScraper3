package me.stephenminer.writers;

import me.stephenminer.WebScraper3;

import java.io.File;

public abstract class OutputWriter<T> {
    protected final String baseName,className;
    protected final T data;

    public OutputWriter(String baseName, String className, T data){
        this.baseName = baseName;
        this.className = className;
        this.data = data;
    }

    public void output(){
        try {
            String path = WebScraper3.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace("WebScraper3-1.0-SNAPSHOT-shaded.jar", baseName + "-" + className + ".csv");
            System.out.println(path);
            File file = new File(path);
            boolean created = file.createNewFile();
            if (created) {
                System.out.println("Created a new file" + file.getName() + " at " + file.getCanonicalPath());
            }else{
                file.delete();
                if (file.createNewFile()) System.out.println("Created a new file" + file.getName() + " at " + file.getCanonicalPath());
            }
            writeData(file);
        }catch (Exception e){e.printStackTrace();}
    }


    protected abstract void writeData(File file);
}
