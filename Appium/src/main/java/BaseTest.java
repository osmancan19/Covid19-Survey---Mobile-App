package main.java;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.URL;

public abstract class BaseTest {

    private static AppiumDriverLocalService service;

    @BeforeSuite
    public void globalSetup () throws IOException {
        DesiredCapabilities cap = new DesiredCapabilities();

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(4723);
        builder.withCapabilities(cap);

        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");

        //Start the server with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        System.out.println("Server started");
    }

    @AfterSuite
    public void globalTearDown () {
        service.stop();
        System.out.println("Server stopped");
    }

    public URL getServiceUrl () {
        return service.getUrl();
    }

}