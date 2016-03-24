package cloudblog;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;
import com.opencsv.*;

public class App {
	public static void main( String[] args ) {
		try {
			test01();
		}
		catch (Exception ex) {}
	} // end of main

	private static void takeScreenOfId(
		WebDriver driver,
		String sfid,
		String name
	) throws Exception {
		driver.navigate().to("https://apX.salesforce.com/" + sfid);
		Thread.sleep(12000);
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(
			scrFile,
			new File("/home/user01/Downloads/" + name + ".png"))
		;
	} // end of takeScrenOfId

	public static void test01() throws Exception {
        WebDriver driver = new FirefoxDriver();
		driver.manage().window().setSize(new Dimension(1200, 1600));
        driver.get("http://login.salesforce.com/");
		Thread.sleep(4000);
		driver.findElement(By.id("username")).sendKeys("user@example.com");
		driver.findElement(By.id("password")).sendKeys("PASSWORD");
		driver.findElement(By.id("Login")).click();

		Thread.sleep(4000);

		List<Map<String, String>> lm = getIdNames();
		for (Map<String, String> m : lm) {
			takeScreenOfId(driver, m.get("Id"), m.get("Name"));
			//break;
		} // end of for (m)
        driver.quit();
	} // end of test01

	public static List<Map<String, String>> getIdNames() throws Exception {
		FileReader fr = null;
		CSVReader cr = null;
		List<Map<String, String>> lm = null;

		try {
			fr = new FileReader("/home/user01/out.csv");
			cr = new CSVReader(fr);

			String[] header = null;
			String[] line = null;
			int cnLine = 0;
			lm = new ArrayList<Map<String, String>>();
			while((line = cr.readNext()) != null) {
				if (cnLine == 0) {
					header = line;
				}
				else {
					HashMap<String, String> m =
						new HashMap<String, String>();
					Boolean bPut = true;
					for (int i = 0; i < header.length; i++) {
						if (i < line.length) {
							m.put(header[i], line[i]);
						}
						else {
							bPut = false;
						}
					} // end of for (i)

					if (bPut) {
						lm.add(m);
					}
				}
				cnLine++;
			} // end of while(line)
		} // end of try
		catch (Exception ex) {
			throw ex;
		}
		finally {
			if (cr != null) try { cr.close(); } catch (Exception ex){}
			if (fr != null) try { fr.close(); } catch (Exception ex){}
		}

		return lm;
	} // end of getIdName
} // end of class App
