package com.selenium.test.junit.tests;

import com.selenium.test.junit.rules.ScreenShotOnFailRule;
import com.selenium.test.pages.YouTubePage;
import com.selenium.test.pages.YouTubeSearchResultsPage;
import com.selenium.test.webtestsbase.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * Uses JUnit test framework
 * Test demonstrates work with Page Object Pattern(https://code.google.com/p/selenium/wiki/PageObjects)
 */
public class PageObjectTest {

    @Rule
    public ScreenShotOnFailRule screenShotOnFailRule = new ScreenShotOnFailRule();

    @BeforeEach
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);
    }

    @Test
    public void testSearch() {
        String toSearch = "Selenium";
        YouTubePage youTubePage = new YouTubePage();
        youTubePage.insertSearchString(toSearch);
        YouTubeSearchResultsPage resultsPage = youTubePage.doSearch();
        Assertions.assertTrue("No results were found on results page", resultsPage.hasResults());
    }


    @AfterEach
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }
}
