package base;


import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


import pageObjects.HomePageObject;
import pageObjects.PointsTablePageObject;

public class BaseTest {
	
	protected ThreadLocal<WebDriver>driver=new ThreadLocal<WebDriver>();
	protected Properties p;
	protected HomePageObject hpo;
	protected PointsTablePageObject pto;
	protected Logger logger;

	@BeforeClass(alwaysRun = true)
	@Parameters({ "browser" })
	public void setUp(String browser) {
		
		try {
			
		logger=LogManager.getLogger(this.getClass());	
		
		logger.info("===== Test Execution Started =====");
		
		FileReader file=new FileReader("./src//test//resources//config.properties");
		
		p=new Properties();
		p.load(file);
		
		switch (browser.toLowerCase()) {
		case "chrome":
			driver.set(new ChromeDriver());
			break;

		case "edge":
			driver.set(new EdgeDriver());
			break;
			
		case "firefox":
			driver.set(new FirefoxDriver());
			break;

		default:
			logger.error("Invalid browser name provided: " + browser);
			throw new RuntimeException("Browser not supported");

		}
		
		logger.info("Launching "+ browser.toUpperCase() +" Browser");
		driver.get().manage().window().maximize();

		driver.get().get(p.getProperty("url"));
		logger.info("Navigated to URL: " + p.getProperty("url"));
		
		hpo=new HomePageObject(driver.get());
		
		
		}
		catch (IOException e) {
			logger.error("Error while loading config file", e);
		}
		

	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {

		if (driver!= null) {
			file.close();
            driver.get().quit();
            driver.remove();
            logger.info("Browser closed");
        }

        logger.info("===== Test Execution Finished =====");

	}
	
	public String captureScreen(String tname) throws IOException {

	    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	    WebDriver driverInstance = driver.get();

	    TakesScreenshot ts = (TakesScreenshot) driverInstance;
	    File sourceFile = ts.getScreenshotAs(OutputType.FILE);

	    String targetDir = System.getProperty("user.dir") 
	            + File.separator + "screenshots" 
	            + File.separator;

	    File dir = new File(targetDir);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    String targetFilePath = targetDir + tname + "_" + timeStamp + ".png";

	    FileUtils.copyFile(sourceFile, new File(targetFilePath));

	    return targetFilePath;
	}

}
