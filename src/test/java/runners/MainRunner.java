package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import static io.cucumber.testng.CucumberOptions.SnippetType.CAMELCASE;


@CucumberOptions(
            features = {"src/test/features"},
            glue = {"stepdefinitions"},
            plugin = {"pretty", "com.epam.reportportal.cucumber.ScenarioReporter"
            },
            monochrome = true,
            dryRun = false,
            tags = "",
            snippets = CAMELCASE
    )
    public class MainRunner extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
            return super.scenarios();
        }

    }

