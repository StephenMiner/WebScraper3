package me.stephenminer;

import me.stephenminer.writers.LinkWriter;
import me.stephenminer.writers.ScoreWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class WebScraper3 {
    public static void main(String[] args) {
        web();
      //  q4();
    }

    public static void web(){
        ConfigFile file = new ConfigFile();

        boolean cont = false;
        if (!file.hasValue("fire-fox-profile")){
            System.out.println("Please get the directory to a firefox profile connected to your google account and put it in the config.txt file and try again");
            return;

        }
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(new FirefoxProfile(new File(file.readSetting("fire-fox-profile"))));
        options.addArguments("--disable-blink-features=AutomationControlled");


        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://classroom.google.com/u/1/h");
       // driver.get("https://myaccount.google.com/?pli=0");
        List<String> classNames = new ArrayList<>(classes(driver));
        System.out.println("Type Close to end the program or a number corresponding to a class...");
        Scanner scanner = new Scanner(System.in);
        start(classNames,driver);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Close")){
                scanner.close();
                return;
            }
            else {

                try{
                    int index = Integer.parseInt(input);
                    String name = classNames.get(index);
                    ClassroomClass clazz = new ClassroomClass(name, driver);
                    System.out.println("Would you like to 1. get reflections or 2. gather links from an assignment?");
                    String searchType = scanner.nextLine();
                    if (Integer.parseInt(searchType) == 1) {
                        clazz.getReflections();
                        ScoreWriter writer = new ScoreWriter(name, (HashMap<String, Double>) clazz.getTask().searchData());
                        writer.output();
                    }
                    else {
                        System.out.println("Please type the name of the assignment you are looking to get links from.");
                        String assignment = scanner.nextLine();
                        clazz.getLinks(assignment);
                        LinkWriter linkWriter = new LinkWriter(assignment,name,(HashMap<String, String>) clazz.getTask().searchData());
                        linkWriter.output();
                    }


                    start(classNames,driver);
                    driver.get("https://classroom.google.com/u/1/h");
                    System.out.println("Type Close to end the program or a Class Name");
                }catch (Exception e){
                    System.out.println("You must input a number!");
                    start(classNames,driver);
                }

            }
        }





        //Not Used
        /*
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='identifierId']")))
                    .sendKeys("stephen.archer.j@gmail.com");
            new WebDriverWait(driver, Duration.ofSeconds(2));
            driver.findElement(By.id("identifierNext")).click();
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='password']")))
                    .sendKeys("misscat13");
            driver.findElement(By.id("passwordNext")).click();
            System.out.println(driver.getTitle());
        }catch (Exception ignoreed){}
         */
    }

    public static List<String> start(List<String> classes, WebDriver driver){
        for (int i = 0; i < classes.size();i++){
            System.out.println(i + ". " + classes.get(i));
        }
        return classes;
    }
    private static Set<String> classes(WebDriver driver){
        return driver.findElements(By.xpath("//a[@class='Xi8cpb vG1fDb TMOcX']")).stream()
                .filter(e->e.getAttribute("aria-label")!=null)
                .map(e->e.getAttribute("aria-label"))
                .collect(Collectors.toSet());

    }




}
