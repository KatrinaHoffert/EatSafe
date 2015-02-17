package autoDownload;

import static org.junit.Assert.fail;
import java.io.File;
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
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private List<String> RHAList;

  @Before
  public void setUp() throws Exception {

	FirefoxProfile profile = new FirefoxProfile();

	//set the preference of the browser to handle the download pop-up window automatically
	profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
			"application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,application/octet-stream");
	profile.setPreference("browser.download.manager.showWhenStarting",false);
	profile.setPreference("browser.download.folderList", 2); 
	profile.setPreference("browser.download.dir","/Users/Doris/Downloads/Inspections/"); 
	  
	driver = new FirefoxDriver(profile);
	baseUrl = "http://orii.health.gov.sk.ca/";
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	
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
	  
    driver.get(baseUrl + "/");
    int fileCount = 0;
    //select and click the RHA
    driver.findElement(By.cssSelector("area[alt=\"" + RHAList.get(0) + "\"]")).click();
    
    Select selectLocation = new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")));

    List<WebElement> locationList = selectLocation.getOptions();
    System.out.print(locationList.size());
    for(int j = 1; j < locationList.size(); j ++) {
    
    	 //select a location
        new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00"))).selectByValue(j + "");
        
        //click enter/return button to confirm selection
        driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")).sendKeys(Keys.RETURN);;
        
        //get the number of premises (restaurants) in this location
        Select selectRestaurant = new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")));
        List<WebElement> restaurantList = selectRestaurant.getOptions();
        
        for(int k = 1; k < restaurantList.size(); k ++) { //start count from 1 because the option 0 is "<Select a value>"
        	fileCount ++;
        	//select a premises name (name of restaurant)
            new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00"))).selectByValue(k + "");
            
            //click enter/return button to confirm selection
            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")).sendKeys(Keys.RETURN);;

            //click "View Report" button to get the report
            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl00")).click();
            
            //select the "CSV(comma delimited)" in the format drop down list
            new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00"))).selectByValue("CSV");
           
            //click enter/return button to confirm selection
            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00")).sendKeys(Keys.RETURN);
            
            //click "Export" button to start download
            driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl01")).click();
           
            File fileNaming;
            if(fileCount == 0) {
                fileNaming = new File("/Users/Doris/Downloads/Inspections/FoodInspectionReport.csv");
            }else {
            	fileNaming = new File("/Users/Doris/Downloads/Inspections/FoodInspectionReport(" + fileCount + ").csv");
            }
            while (!fileNaming.exists()) {
                Thread.sleep(1000);
            	System.out.println("wait for the " + fileCount + "th file to download.\n");
            }
        }
    }
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
