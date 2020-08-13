package main.java;

import com.github.javafaker.Faker;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
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

public class AndroidTest extends BaseTest {

    AndroidDriver driver;
    Faker faker;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        final DesiredCapabilities capabilities = getAndroidCapabilities();
        driver = new AndroidDriver<MobileElement>(getServiceUrl(), capabilities);
        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
        faker = new Faker();
        wait = new WebDriverWait(driver, 120);
    }

    @AfterMethod
    public void after() {
        driver.resetApp();
    }

    private DesiredCapabilities getAndroidCapabilities() {
        final File app = new File("apps", "Project2.apk");
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setVersion("9");
        capabilities.setPlatform(Platform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        return capabilities;
    }

    private void fillNameStep() {
        final WebElement step = driver.findElementByAccessibilityId("survey-step-0");
        step.click();

        final WebElement name = driver.findElementByAccessibilityId("name-input");
        final WebElement surname = driver.findElementByAccessibilityId("surname-input");

        name.sendKeys(faker.name().firstName());
        surname.sendKeys(faker.name().lastName());
    }

    private void fillCityStep() {
        final WebElement step = driver.findElementByAccessibilityId("survey-step-2");
        step.click();

        final WebElement picker = driver.findElementByAccessibilityId("city-picker");
        picker.click();

        driver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"Adana\")").click();
    }

    private void fillGenderStep() {
        final WebElement step = driver.findElementByAccessibilityId("survey-step-3");
        step.click();

        final WebElement gender = driver.findElementByAccessibilityId("gender-Male");
        gender.click();
    }

    private void fillLifestyleStep() {
        final WebElement step = driver.findElementByAccessibilityId("survey-step-4");
        step.click();

        final WebElement textarea = driver.findElementByAccessibilityId("lifestyle-input");
        textarea.sendKeys(faker.lorem().characters(20));
    }

    private void selectDate(final WebElement date_picker_button, final String year) {
        date_picker_button.click();
        final WebElement header_year_picker = driver.findElementById("android:id/date_picker_header_year");
        header_year_picker.click();

        final MobileElement visible_years = (MobileElement) driver
                .findElementById("android:id/date_picker_year_picker");
        swipeInListTillExpectedTextAndTap(visible_years, year);

        final MobileElement date_picker_ok_button = (MobileElement) driver.findElementById("android:id/button1");
        date_picker_ok_button.click();
    }

    public void swipeInListTillExpectedTextAndTap(final MobileElement yearPicker, final String expectedText) {
        List<MobileElement> list = yearPicker.findElements(By.className("android.widget.TextView"));
        while (!driver.getPageSource().contains(expectedText)) {
            swipeList(list);
            list = yearPicker.findElements(By.className("android.widget.TextView"));
        }
        driver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + expectedText + "\")").click();
    }

    public void swipeList(final List<MobileElement> listID) {
        final int items = listID.size();
        final org.openqa.selenium.Point centerOfFirstElement = listID.get(0).getCenter();
        final org.openqa.selenium.Point centerOfLastElement = listID.get(items - 1).getCenter();
        new TouchAction(driver).longPress(point(centerOfFirstElement.x, centerOfFirstElement.y))
                .moveTo(point(centerOfLastElement.x, centerOfLastElement.y)).release().perform();
    }

    @Test
    public void shouldShowFinishPageIfAllInputsAreValid() {
        fillNameStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        final WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2000");
        driver.findElementByAccessibilityId("next-button-highlight").click();

        fillCityStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        fillGenderStep();

        fillLifestyleStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        final WebElement title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "You have completed the survey"));

        Assert.assertEquals(title.getText(), "You have completed the survey");
    }

    @Test
    public void shouldShowAnErrorMessageIfTheSelectedAgeIsLessThan13() {
        final WebElement step = wait
                .until(ExpectedConditions.visibilityOf(driver.findElementByAccessibilityId("survey-step-1")));
        step.click();

        final WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2012");

        final MobileElement error_text = (MobileElement) driver.findElementByAccessibilityId("error-text");
        Assert.assertEquals(error_text.getText(), "You must be at least 13 years old to participate in the survey.");
    }

    @Test
    public void shouldRememberPreviousValuesWhenTheUserReturns() throws InterruptedException {
        final String name_value = faker.name().firstName();
        final String surname_value = faker.name().lastName();
        WebElement name = driver.findElementByAccessibilityId("name-input");
        WebElement surname = driver.findElementByAccessibilityId("surname-input");

        name.sendKeys(name_value);
        surname.sendKeys(surname_value);
        driver.findElementByAccessibilityId("next-button-highlight").click();

        // Return to first step
        final WebElement step = wait
                .until(ExpectedConditions.visibilityOf(driver.findElementByAccessibilityId("survey-step-0")));
        step.click();
        Thread.sleep(2000);

        name = driver.findElementByAccessibilityId("name-input");
        surname = driver.findElementByAccessibilityId("surname-input");
        Assert.assertEquals(name.getText(), name_value);
        Assert.assertEquals(surname.getText(), surname_value);
    }

    @Test
    public void shouldNotAcceptLifestyleInputsLessThan10Characters() {
        fillNameStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        final WebElement birthday_button = driver.findElementByAccessibilityId("birthday-button");
        selectDate(birthday_button, "2000");

        fillCityStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        fillGenderStep();
        driver.findElementByAccessibilityId("next-button-highlight").click();

        final WebElement textarea = driver.findElementByAccessibilityId("lifestyle-input");
        textarea.sendKeys(faker.lorem().characters(5));

        final WebElement title = driver.findElementByAccessibilityId("question-title");
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
        final WebElement retake_button = driver.findElementByAccessibilityId("next-button-highlight");
        retake_button.click();

        final WebElement title = driver.findElementByAccessibilityId("question-title");
        wait.until(ExpectedConditions.textToBePresentInElement(title, "What's your name?"));

        final WebElement name = driver.findElementByAccessibilityId("name-input");
        final WebElement surname = driver.findElementByAccessibilityId("surname-input");

        Assert.assertEquals(name.getText(), "");
        Assert.assertEquals(surname.getText(), "");
        Assert.assertEquals(title.getText(), "What's your name?");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
