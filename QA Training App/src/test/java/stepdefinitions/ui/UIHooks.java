package stepdefinitions.ui;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.annotations.Managed;
import org.openqa.selenium.WebDriver;

public class UIHooks {

    @Managed(driver = "chrome")
    private WebDriver driver;

    @Before("@UI_Plant_Create_001")
    public void setUpScreenplay() {
        OnStage.setTheStage(new OnlineCast());
    }

    @After("@UI_Plant_Create_001")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
