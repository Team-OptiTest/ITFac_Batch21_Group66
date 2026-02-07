package runners.ui;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features/ui/sales",
    glue = "stepdefinitions.ui",
    plugin = {"pretty"}
)
public class SalesUiTestRunner {
}
