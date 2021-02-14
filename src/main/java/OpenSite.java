import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class OpenSite {
    private WebDriver webDriver;

    @BeforeTest
    public void initDriver() {
        webDriver = DriverFactory.initDriver(EDriverType.IE);
    }

    @AfterTest
    public void quitDriver() {
        if (null != webDriver) {
            webDriver.quit();
            webDriver = null;
        }
    }

    @Test
    public void fillFormAndSubmit(){
        webDriver.get("https://www.kmslh.com/automation-test/");
        waitAndFindElement(By.xpath("//input[@name='firstname']")).sendKeys("Igor");
        webDriver.findElement(By.xpath("//input[@name='lastname']")).sendKeys("Brodov");
        webDriver.findElement(By.xpath("//input[@name='email']")).sendKeys("ibrodov@kmslh.com");
        webDriver.findElement(By.xpath("//input[@name='phone']")).sendKeys("111-22-33");
        webDriver.findElement(By.xpath("//input[@name='company']")).sendKeys("kmslh");
        webDriver.findElement(By.xpath("//input[@value='Submit']")).click();
        WebElement we = waitAndFindElement(By.xpath("//h1[contains(text(),'Thank you for singing up!')]"));
        String confirmationText = we.getText();
        String actualText = confirmationText.substring(0,confirmationText.indexOf("!"));
        Assert.assertEquals(actualText,"Thank you for singing up");
    }

    @Test
    public void openSelfServiceVideoInSeparateTab(){
        webDriver.get("https://www.kmslh.com");
        waitAndFindElement(By.xpath(" //a[contains(@href,'self-service')]/parent::div")).click();
        // For my IE browser popup doesn't display sometimes even with 'turn on pop-up blocker' option unckecked and this test often fails
        // in chrome test passes
        waitAndFindElement(By.xpath("//div[@data-elementor-type='popup']"));
        webDriver.switchTo().frame("widget2");
        webDriver.findElement(By.xpath("//button[@title='Copy link']")).click();
        ((JavascriptExecutor)webDriver).executeScript("window.open()");
        List<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriver.get(getClipboardContents());
        String actualTitle = waitAndFindElement(By.xpath("//div[@id='container']/h1")).getText();
        String expectedTitle = "KMS Lighthouse Explained";
        Assert.assertEquals(actualTitle,expectedTitle);
    }

    private String getClipboardContents(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result=null;
        try{
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private FluentWait<WebDriver> newWait() {
        return new WebDriverWait(webDriver, 20)
                .pollingEvery(ofSeconds(1))
                .ignoring(WebDriverException.class);
    }

    private WebElement waitAndFindElement(By by){
        newWait().until(visibilityOfElementLocated(by));
        return webDriver.findElement(by);
    }

}
