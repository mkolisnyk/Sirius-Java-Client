package com.github.mkolisnyk.sirius.client.bdd.samples.tests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "html:target/bdd/cucumber-html-report",
                "junit:target/bdd/cucumber-junit.xml",
                "json:target/bdd/cucumber.json",
                "pretty:target/bdd/cucumber-pretty.txt",
                "usage:target/bdd/cucumber-usage.json"
                },
        features = { "src/test/java/com/github/mkolisnyk/sirius/client/bdd/samples/features" },
        glue = { "com.github.mkolisnyk.sirius.client.bdd.samples.steps",
                "com.github.mkolisnyk.sirius.cucumber.steps"}
)
public class BankingTest {
}
