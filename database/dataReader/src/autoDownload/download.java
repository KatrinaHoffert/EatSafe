package autoDownload;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

public class download {

  //the path of folder that stores all inspection reports	
  private static final String FOLDER_PATH = "/Users/Doris/Downloads/Inspections/";
  
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  //an array list of names of RHA
  private List<String> RHAList;

  @Before
  public void setUp() throws Exception {

	//construct a new Firefox profile for the driver
	FirefoxProfile profile = new FirefoxProfile();

	//set the preference of the browser to handle the download pop-up window automatically
	profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
			"application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,application/octet-stream");
	profile.setPreference("browser.download.manager.showWhenStarting",false);
	profile.setPreference("browser.download.folderList", 2); 
	profile.setPreference("browser.download.dir",FOLDER_PATH); 
	  
	driver = new FirefoxDriver(profile);
	baseUrl = "http://orii.health.gov.sk.ca/"; //the URL for the "Saskatchewan Online Restaurant Inspection Information"
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
	RHAList = new ArrayList<String>(); //Ten RHA for Saskatchewan
	RHAList.add("Cypress");
	RHAList.add("Five Hills");
	RHAList.add("Heartland");
	RHAList.add("Kelsey Trail");
	RHAList.add("Prairie North");
	RHAList.add("Prince Albert Parkland");
	RHAList.add("Regina Qu'Appelle");
	RHAList.add("Saskatoon");
	RHAList.add("Sun Country");
	RHAList.add("Sunrise");
  }

  @Test
  public void testdownload() throws Exception {
	//The count of files
	int fileCount = 0;
	//check if there is already a file with the name "FoodInspectionReport.csv"; if yes, delete it
    File fileName = new File(FOLDER_PATH + "FoodInspectionReport.csv");
    if(fileName.exists()) {
    	fileName.delete();
    }

	//loop for 10 RHAs
	for(int i = 0; i < RHAList.size(); i ++){
		driver.get(baseUrl + "/");
	    
	    //select and click the RHA
	    driver.findElement(By.cssSelector("area[alt=\"" + RHAList.get(i) + "\"]")).click();
        String RHAName = RHAList.get(i);//get the RHA name for naming the files
	    
	    //get number of location in this RHA
	    Select selectLocation = new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")));
	    List<WebElement> locationList = selectLocation.getOptions();
	    System.out.println("- " + (i + 1) + " RHA: " + RHAName + ": " + (locationList.size() - 1) + " locations. ");

	    //loop for locations
	    for(int j = 1; j < locationList.size(); j ++) {//start count from 1 because the option 0 is "<Select a value>"
	    
	    	 //select a location
	        new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00"))).selectByValue(j + "");
            String locationName = driver.findElement(By.cssSelector("#ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00 > option[value=\"" + j + "\"]")).getText();
            
	        //click enter/return button to confirm selection
	        driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")).sendKeys(Keys.RETURN);;
	        
	        //get the number of premises (restaurants) in this location
	        Select selectRestaurant = new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")));
	        List<WebElement> restaurantList = selectRestaurant.getOptions();
            System.out.println("--- " + j + " Location: "  + locationName + ": " + (restaurantList.size() - 1) + " restaurants. ");
	        
	        //loop for premises (restaurant)
	        for(int k = 1; k < restaurantList.size(); k ++) { //start count from 1 because the option 0 is "<Select a value>"
	        	fileCount ++;
	        	
	        	String restaurantName = driver.findElement(By.cssSelector("#ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00 > option[value=\"" + k + "\"]")).getText();
	            System.out.print("----- " + k + ": " + restaurantName + ": ");

	            String newFileNameString = RHAName + "_" + locationName + "_" + restaurantName + ".csv";
	            //rename file
	            File newFileName = new File(FOLDER_PATH + newFileNameString);
	            if(newFileName.exists()) {
	            	System.out.println("ALREADY EXISTS: the " + fileCount + " th file: " + newFileNameString + ".");
	            }else {
	            	//select a premises name (name of restaurant)
		            new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00"))).selectByValue(k + "");
		            
		            //click enter/return button to confirm selection
		            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")).sendKeys(Keys.RETURN);;
		            
		            //some restaurant don't have any report, and the export format drop list won't show if no report
		            if(driver.findElements(By.cssSelector("#ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl09_ctl00 > option[value=\"0\"]")).size() != 0) {
		            	
		            	//click "View Report" button to get the report
			            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl00")).click();
			            
		            	 //select the "CSV(comma delimited)" in the format drop down list
			            new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00"))).selectByValue("CSV");
			           
			            //click enter/return button to confirm selection
			            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00")).sendKeys(Keys.RETURN);
			            
			            //click "Export" button to start download
			            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl01")).click();
			           
			            
			            //check if download is successful; if not, wait
			            while(!fileName.exists()) {
			                Thread.sleep(1000);
			            }
			            //then rename the file; pause when renaming failed, and manually rename, then hit enter to resume
			            if(!fileName.renameTo(newFileName)) {
			                System.err.println("\n---------rename failed for " + fileCount + " th file: " + newFileNameString + ".");
			                System.err.println("Please rename manually, then hit Enter to resume: ");
			                BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
			                br.readLine();//resume
			            }
		            	System.out.println("DOWNLOADED SUCCESSFUL: the " + fileCount + " th file: " + newFileNameString + ".");
		            }else {
		            	System.err.println("NO REPORT: the " + fileCount + " th file: " + newFileNameString + ".");
		            	fileCount --; // no report, count decrease by one
		            }
	            }  	
	        }
	    }
	}
	
	System.out.println("Done\n");
	System.out.println("Total number of files: " + fileCount + ".");

  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
