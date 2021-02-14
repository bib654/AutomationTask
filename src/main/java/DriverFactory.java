import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverFactory {

    public static WebDriver initDriver(EDriverType type) {
        WebDriver driver;
        switch (type) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case IE:
                System.setProperty("webdriver.ie.driver", "./src/main/resources/IEDriverServer_32.exe");
                DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
                InternetExplorerOptions options = new InternetExplorerOptions(capabilities);
                capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                driver = new InternetExplorerDriver(options);
                break;
            default:
                throw new RuntimeException(String.format("Browser '%s' is not supported", type));
        }
        return driver;
    }

}
