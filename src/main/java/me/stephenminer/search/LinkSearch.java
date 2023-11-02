package me.stephenminer.search;

import me.stephenminer.search.Search;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LinkSearch implements Search<HashMap<String,String>> {
    private final HashMap<String, String> studentLinks;
    private final WebDriver driver;
    private String assignmentName;
    public LinkSearch(WebDriver driver, String assignmentName){
        studentLinks = new HashMap<>();
        this.driver = driver;
        this.assignmentName = assignmentName;
    }





    @Override
    public void search() {
        try {
            Thread.sleep(5000);
            WebElement root = driver.findElement(By.xpath("//div[@jscontroller='yidvwe']"));
            String assignmentClass = "qhnNic LBlAUc Aopndd TIunU ZoT1D idtp4e DkDwHe";
            List<WebElement> assignmentStream = root.findElements(By.xpath(".//div[@class='" + assignmentClass + "']")).stream().filter(e -> e.getAttribute("data-stream-item-id") != null).collect(Collectors.toList());
            List<String> ids = new ArrayList<>(assignmentStream.stream().map(assignment -> assignment.getAttribute("data-stream-item-id")).toList());
            System.out.println(ids.size());

            try {
                for (int i = 0; i < ids.size(); i++) {
                    final int index = i;
                    String id = ids.get(index);
                    //Program is grabbing correct ids
                    try {
                        WebElement assignment = root.findElement(By.xpath(".//div[@data-stream-item-id='" + id + "']"));
                        //Should be finding correct element, but seems as if it is grabbing not right element
                        // assignment.findElements(By.xpath(".//div[@class='PazDv']")).forEach(e-> System.out.println(e.getAttribute("aria-label")));
                        WebElement wantClick = assignment.findElements(By.xpath(".//div[@class='PazDv']")).stream().filter(e -> e.getAttribute("aria-label") != null).findFirst().orElse(null);
                        String title = wantClick.getAttribute("aria-label");
                        if (title.toLowerCase().contains(assignmentName.toLowerCase())) {
                            //  Thread.sleep(1000); // was 1000
                            assignment.click();
                            //    Thread.sleep(750);
                            gatherEntries();
                            //   Thread.sleep(500); // was 2000
                            updateIds(ids);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("Skipping element with data-stream-id of " + id);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        /*
        try {
            for (int i = assignmentStream.size()-1; i >=0;i--) {
                WebElement assignment = assignmentStream.get(i);
                WebElement wantClick = assignment.findElement(By.xpath(".//div[@class='PazDv']"));
                String title = wantClick.getAttribute("aria-label");
                if (title.contains("Week") && title.contains("Reflection")) {
                    wantClick.click();
                    gatherEntries();
                }
                Thread.sleep(3000);
            }
        }catch (Exception e){e.printStackTrace();}
      */
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateIds(List<String> ids){
        WebElement root = driver.findElement(By.xpath("//div[@jscontroller='yidvwe']"));
        String assignmentClass = "qhnNic LBlAUc Aopndd TIunU ZoT1D idtp4e DkDwHe";
        List<WebElement> assignmentStream = root.findElements(By.xpath(".//div[@class='" + assignmentClass + "']")).stream().filter(e -> e.getAttribute("data-stream-item-id") != null).toList();
        List<String> updated = new ArrayList<>(assignmentStream.stream().map(assignment -> assignment.getAttribute("data-stream-item-id")).filter(s->!ids.contains(s)).toList());
        ids.addAll(updated);
    }

    //get elements after some delay
    private void gatherEntries(){

        try {
            //Get elements on page with 'tr'
            Thread.sleep(500);
            WebElement root;
            List<WebElement> entries = driver.findElements(By.xpath("//div[@class='hYt5f EfvGO']"));
            if (entries.size() > 1) root = entries.get(entries.size()-1);
            else root = entries.get(0);
            List<WebElement> submissions = root.findElements(By.xpath(".//div[@class='WkZsyc']"));
            for (WebElement submission : submissions){
                String name = submission.findElement(By.xpath(".//div[@class='J33wTc']")).getAttribute("textContent");
                List<WebElement> items = submission.findElements(By.xpath(".//a[@class='vwNuXe JkIgWb  MymH0d maXJsd']"));
                for (WebElement e : items){
                    //pinpointing the element whose label has "attachment" so we know it is the submission data.
                    String label = e.getAttribute("aria-label");
                    if (label.contains("Attachment:")){
                        String link = e.getAttribute("href");
                        if (link == null || link.isEmpty()) continue;
                        else studentLinks.put(name, link);
                    }
                }

            }
            driver.findElement(By.xpath("//a[@class='onkcGd OGhwGf cGvavf juzHj']")).click();
        }catch (Exception e){
            e.printStackTrace();
        }
    }






    @Override
    public HashMap<String, String> searchData() {
        return studentLinks;
    }


}
