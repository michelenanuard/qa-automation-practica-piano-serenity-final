package dev.michelen.qa.tests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dev.michelen.qa.questions.DisplayedNote;
import dev.michelen.qa.tasks.AcceptCookiesIfAny;
import dev.michelen.qa.tasks.OpenTheApp;
import dev.michelen.qa.tasks.PlayNote;
import dev.michelen.qa.tasks.PreparePiano;
import dev.michelen.qa.tasks.SwitchToPianoFrame;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

@SerenityTest
@ExtendWith(SerenityJUnit5Extension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleNotesTest {

    private WebDriver driver;
    private final Actor actor = Actor.named("QA");

    @BeforeEach
    void setup(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(60));
        actor.can(BrowseTheWeb.with(driver));
        actor.attemptsTo(OpenTheApp.at("https://www.musicca.com/es/piano"));
        actor.attemptsTo(AcceptCookiesIfAny.now());
        actor.attemptsTo(SwitchToPianoFrame.tryFind());
        actor.attemptsTo(PreparePiano.now());
    }

    @AfterEach
    void tearDown(){
        if(driver != null){ driver.quit(); driver = null; }
    }

    @Test @Order(1)
    @DisplayName("Tocar una nota (do) y validar que no hay errores")
    void play_single_note(){
        actor.attemptsTo(PlayNote.withKey("do"));
        String shown = DisplayedNote.text().answeredBy(actor);
        Assertions.assertThat(shown).isNotNull();
    }
}
