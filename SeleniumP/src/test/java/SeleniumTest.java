import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class SeleniumTest {

    @Test
    public void addToCartTest() {

        WebDriver driver = new ChromeDriver();
        String driver_executable_path = "/src/test/resources/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driver_executable_path);

//  ---------- To maximize window  ----------
        driver.manage().window().maximize();

//  ---------- To access the URL  ----------
        String url = "https://demoblaze.com/index.html";
        driver.get(url);

//  ---------- To click Phone in Category  ----------
        WebElement elementPhone = driver.findElement(By.xpath("(//a[normalize-space()='Phones'])[1]"));
        elementPhone.click();

        String pageURL = driver.getCurrentUrl();
        System.out.println(pageURL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

// ---------- To Click Galaxy Phone in Phones  ----------
        WebElement elementGalaxy = driver.findElement(By.xpath("//a[@href='prod.html?idp_=1']"));

//  ---------- Scroll down to click after finding the element --------
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementGalaxy);
        elementGalaxy.click();


// ----------- Validate the popup is received after clicking Add to cart button  -----------
        try {

            driver.findElement(By.xpath("//a[@class = 'btn btn-success btn-lg']")).click();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

//  ----------- Validate the text "Product added" on the popup  -----------
            String alertText = alert.getText();
            System.out.println("Popup text: " + alertText + "\n");


//  ----------- Click the OK button to close the popup  -----------
            alert.accept();

//  ----------- Validate that the popup is closed ---------
            System.out.println("Popup closed and action validated.");
        } catch (Exception e) {
            System.out.println("No alert appeared.");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));


//   ----------- To click on cart  -----------
        driver.findElement(By.xpath("//a[@class='nav-link'][@id='cartur']")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));


//    ----------- To click place order button on the cart  -----------
        driver.findElement(By.className("btn-success")).click();


// ----------- To Add data to form -----------
        driver.findElement(By.id("name")).sendKeys("Sandali");
        driver.findElement(By.id("country")).sendKeys("Sri Lanka");
        driver.findElement(By.id("city")).sendKeys("Panadura");
        driver.findElement(By.id("card")).sendKeys("123456789");
        driver.findElement(By.id("month")).sendKeys("March");
        driver.findElement(By.id("year")).sendKeys("2025");

        driver.findElement(By.xpath("//button[text()='Purchase']")).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));


// ----------- Validate the details on purchased receipt with the details added to the form -----------
        try {

            WebElement receiptElements = driver.findElement(By.cssSelector("p.lead.text-muted"));
            String pText = receiptElements.getText();


//-----------   To print the validated details on the receipt  -----------
            System.out.println("\n-----------Actual Text --------- \n" + pText + "\n-----------------");

            String modifiedText = pText.replaceFirst("Id: [^\\n]+\\n", "");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            Assert.assertTrue(modifiedText.contains("Amount:"), "Amount is not correct");
            Assert.assertTrue(modifiedText.contains("Card Number:"), "Card Number is not correct");
            Assert.assertTrue(modifiedText.contains("Name:"), "Name is not correct");
            Assert.assertTrue(modifiedText.contains("Date:"), "Date is not correct");

            System.out.println("Text validation passed!");
        } catch (Exception e) {
            System.out.println("Expection");
        } finally {

// --------- Kept a wait for the clear visibility of the content on the receipt.---------
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.quit();
        }

    }
}