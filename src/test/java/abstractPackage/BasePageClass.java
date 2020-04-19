package abstractPackage;

import com.google.common.collect.Iterables;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class BasePageClass {

    private static final int WAITER_TIME_OUT = 20;
    protected static WebDriver driver;
    protected static final By SIGN_ON_FRAME = By.xpath("//iframe[@class='SignIn']");


    public WebDriver getDriver() {
        return driver;
    }


    public WebDriver initializeDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        System.setProperty("webdriver.chrome.driver", "D:\\SeleniumDrivers\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver(capabilities);
        maximizeWindow();
        return driver;
    }


    @BeforeClass(description = "Start browser")
    public void startBrowser() {
// init Webdriver
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setPlatform(Platform.WINDOWS);
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
            LOGGER.info("HUB");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public WebElement waitForExpectedElement(By webElementLocator) {
        return new WebDriverWait(driver, WAITER_TIME_OUT)
                .until(ExpectedConditions.visibilityOfElementLocated(webElementLocator));
    }


    public void switchToFrame(By frame) {
        getDriver().switchTo().frame(waitForExpectedElement(frame));
    }


    public void switchToLastOpenedWindow() {
        String baseWindowHandle = getDriver().getWindowHandle();
        String lastWindowHandle = Iterables.getLast(getDriver().getWindowHandles());
        if (!lastWindowHandle.equals(baseWindowHandle)) {
            getDriver().switchTo().window(lastWindowHandle);
            LOGGER.info("Execution was switch to latest opened window");

        }
    }

    public WebElement find(By locator) {
        return driver.findElement(locator);
    }

    protected String getParamFromProperty(String propertyName) {
        FileInputStream fis;
        Properties properties = new Properties();
        String emailFromPropertyFile = "";
        try {
            fis = new FileInputStream("./src/test/java/resources/application.properties");
            properties.load(fis);
            emailFromPropertyFile = properties.getProperty(propertyName);
        } catch (FileNotFoundException e) {
            LOGGER.warning("Check out file's path");
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
        }
        return emailFromPropertyFile;
    }

    @BeforeClass
    public void setUp() {
        initializeDriver();
    }

    @AfterClass
    public void closeDown() {
        driver.quit();
    }


    public void open(String url) {
        getDriver().get(url);
    }


    public void maximizeWindow() {
        driver.manage().window().maximize();
    }


}
