package autoDownload;

import static org.junit.Assert.fail;

import java.io.File;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.Select;

public class download {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {

	FirefoxProfile profile = new FirefoxProfile();

	profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
			"application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,application/octet-stream");
	profile.setPreference("browser.download.manager.showWhenStarting",false);
	profile.setPreference("browser.download.folderList", 2); 
	profile.setPreference("browser.download.dir","/Users/Doris/Downloads/Inspections/"); 
	  
	driver = new FirefoxDriver(profile);
	baseUrl = "http://orii.health.gov.sk.ca/";
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testIDE() throws Exception {
	  
    driver.get(baseUrl + "/");
    driver.findElement(By.cssSelector("area[alt=\"Saskatoon\"]")).click();
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")).click();
    new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00"))).selectByValue("1");
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl05_ctl00")).sendKeys(Keys.RETURN);;
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")).click();
    new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00"))).selectByValue("1");
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl07_ctl00")).sendKeys(Keys.RETURN);;

    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl00_ctl00")).click();
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00")).click();
    new Select(driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00"))).selectByValue("CSV");
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl00")).sendKeys(Keys.RETURN);
    driver.findElement(By.id("ctl00_ContentPlaceHolder1_rvReport_ctl01_ctl05_ctl01")).click();
   
    File file = new File("/Users/Doris/Downloads/Inspections/FoodInspectionReport.csv");
    while (!file.exists()) {
        Thread.sleep(1000);
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
