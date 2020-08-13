package main.java;

import com.github.javafaker.Faker;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.offset.PointOption.point;

@SuppressWarnings("Duplicates")
public class IOSTest extends BaseTest {

    IOSDriver driver;
    Faker faker;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        DesiredCapabilities capabilities = getIosCapabilities();
        driver = new IOSDriver<MobileElement>(getServiceUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        faker = new Faker();
        wait = new WebDriverWait(driver, 120);
    }

    @AfterMethod
    public void after() {
        driver.resetApp();
    }

    private DesiredCapabilities getIosCapabilities() {
        File app = new File("apps", "Project2.app");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setVersion("13.6");
        capabilities.setPlatform(Platform.IOS);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 11");
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        return capabilities;
    }

    private void clickNextButton(IOSDriver<MobileElement> driver) {
        MobileElement next_button = driver.findElementsByAccessibilityId("next-button-highlight").get(1);
        next_button.click();
    }

    private void fillNameStep() {
        MobileElement name = (MobileElement) driver.findElementsByAccessibilityId("name-input").get(1);
        MobileElement surname = (MobileElement) driver.findElementsByAccessibilityId("surname-input").get(1);

        name.sendKeys(faker.name().firstName());
        surname.sendKeys(faker.name().lastName());
    }

    private void fillCityStep() {
        MobileElement city_picker = (MobileElement) driver
                .findElementByIosNsPredicate("type == 'XCUIElementTypePickerWheel'");
        new TouchAction(driver).tap(point(city_picker.getCenter().x, city_picker.getCenter().y + 25)).perform();
    }

    private void fillGenderStep() {
        wait.until(ExpectedConditions.visibilityOf(driver.findElementByAccessibilityId("gender-Male")));
        MobileElement gender = (MobileElement) driver.findElementByAccessibilityId("gender-Male");
        gender.click();
    }

    private void fillLifestyleStep(int length) {
        WebElement textarea = driver.findElementByAccessibilityId("lifestyle-input");
        textarea.click();
        driver.getKeyboard().sendKeys(faker.lorem().characters(length));
    }

    private void selectDate(WebElement date_picker_button, String year) {
        date_picker_button.click();

        List<MobileElement> date_wheels = driver.findElementsByIosNsPredicate("type == 'XCUIElementTypePickerWheel'");
        MobileElement year_picker = date_wheels.get(2);
        for (int i = 0; i < 2020 - Integer.parseInt(year); i++) {
            new TouchAction(driver).tap(point(year_picker.getCenter().x, year_picker.getCenter().y - 25)).perform();
        }
    }

    @Test
    public void shouldShowFinishPageIfAllInputsAreValid() {
        fillNameStep();
        clickNextButton(driver);

        WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2000");
        clickNextButton(driver);

        fillCityStep();
        clickNextButton(driver);

        fillGenderStep();

        fillLifestyleStep(20);
        clickNextButton(driver);

        WebElement title = driver.findElementByAccessibilityId("question-title");
        Assert.assertEquals(title.getText(), "You have completed the survey");
    }

    @Test
    public void shouldShowAnErrorMessageIfTheSelectedAgeIsLessThan13() {
        WebElement step = wait
                .until(ExpectedConditions.visibilityOf(driver.findElementByAccessibilityId("survey-step-1")));
        step.click();

        WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2012");

        MobileElement error_text = (MobileElement) driver.findElementByAccessibilityId("error-text");
        Assert.assertEquals(error_text.getText(), "You must be at least 13 years old to participate in the survey.");
    }

    @Test
    public void shouldRememberPreviousValuesWhenTheUserReturns() {
        String name_value = faker.name().firstName();
        String surname_value = faker.name().lastName();
        WebElement name = (WebElement) driver.findElementsByAccessibilityId("name-input").get(1);
        WebElement surname = (WebElement) driver.findElementsByAccessibilityId("surname-input").get(1);

        name.sendKeys(name_value);
        surname.sendKeys(surname_value);

        clickNextButton(driver);

        // Return to first step
        WebElement step = wait
                .until(ExpectedConditions.visibilityOf(driver.findElementByAccessibilityId("survey-step-0")));
        step.click();

        name = (WebElement) driver.findElementsByAccessibilityId("name-input").get(1);
        surname = (WebElement) driver.findElementsByAccessibilityId("surname-input").get(1);
        Assert.assertEquals(name.getAttribute("value"), name_value);
        Assert.assertEquals(surname.getAttribute("value"), surname_value);
    }

    @Test
    public void shouldNotAcceptLifestyleInputsLessThan10Characters() {
        fillNameStep();
        clickNextButton(driver);

        WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2000");
        clickNextButton(driver);

        fillCityStep();
        clickNextButton(driver);

        fillGenderStep();

        fillLifestyleStep(5);
        clickNextButton(driver);

        WebElement title = driver.findElementByAccessibilityId("question-title");
        Assert.assertNotEquals(title.getText(), "You have completed the survey");
    }

    @Test
    public void shouldNavigateToTheStepWhenClickedOnTheButtonBar() {
        WebElement step = driver.findElementByAccessibilityId("survey-step-1");
        step.click();
        WebElement title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "What's your date of birth?"));
        Assert.assertEquals(title.getText(), "What's your date of birth?");

        step = driver.findElementByAccessibilityId("survey-step-2");
        step.click();
        title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "Which city are you from?"));
        Assert.assertEquals(title.getText(), "Which city are you from?");

        step = driver.findElementByAccessibilityId("survey-step-3");
        step.click();
        title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "What is your gender?"));
        Assert.assertEquals(title.getText(), "What is your gender?");

        step = driver.findElementByAccessibilityId("survey-step-4");
        step.click();
        title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title,
                "How has your lifestyle changed since the COVID-19 Outbreak started?"));
        Assert.assertEquals(title.getText(), "How has your lifestyle changed since the COVID-19 Outbreak started?");

        step = driver.findElementByAccessibilityId("survey-step-0");
        step.click();
        title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "What's your name?"));
        Assert.assertEquals(title.getText(), "What's your name?");
    }

    @Test
    public void shouldResetTheSurveyIfRetakeButtonIsPressed() {
        shouldShowFinishPageIfAllInputsAreValid();
        WebElement retake_button = driver.findElementByAccessibilityId("next-button-highlight");
        retake_button.click();

        WebElement title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "What's your name?"));

        MobileElement name = (MobileElement) driver.findElementsByAccessibilityId("name-input").get(1);
        MobileElement surname = (MobileElement) driver.findElementsByAccessibilityId("surname-input").get(1);

        Assert.assertEquals(name.getAttribute("value"), null);
        Assert.assertEquals(surname.getAttribute("value"), null);
        Assert.assertEquals(title.getText(), "What's your name?");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
