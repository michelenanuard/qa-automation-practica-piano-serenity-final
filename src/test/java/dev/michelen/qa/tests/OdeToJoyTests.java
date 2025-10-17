package dev.michelen.qa.tests;

import java.util.Arrays;
import java.util.List;

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
import dev.michelen.qa.tasks.PlaySequence;
import dev.michelen.qa.tasks.PreparePiano;
import dev.michelen.qa.tasks.SwitchToPianoFrame;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

@SerenityTest
@ExtendWith(SerenityJUnit5Extension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OdeToJoyTests {

    private WebDriver driver;
    private final Actor qa = Actor.named("QA-Player");

    @BeforeEach
    void openApp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(60));
        qa.can(BrowseTheWeb.with(driver));
        qa.attemptsTo(OpenTheApp.at("https://www.musicca.com/es/piano"));
        qa.attemptsTo(AcceptCookiesIfAny.now());
        qa.attemptsTo(SwitchToPianoFrame.tryFind());
        qa.attemptsTo(PreparePiano.now());
    }

    @AfterEach
    void close(){
        if(driver != null){ driver.quit(); driver = null; }
    }

    @Test @Order(1)
    @DisplayName("Himno de la Alegría - Escenario 1")
    void escenario1(){
        List<String> seq = Arrays.asList("si","si","do","re","re","do","si","la","sol","sol","la","si","si","la","la");
        qa.attemptsTo(PlaySequence.with(seq));
        String last = DisplayedNote.text().answeredBy(qa);
        Assertions.assertThat(last).isNotNull();
    }

    @Test @Order(2)
    @DisplayName("Himno de la Alegría - Escenario 2 (x2)")
    void escenario2(){
        List<String> seq = Arrays.asList("si","si","do","re","re","do","si","la","sol","sol","la","si","si","la","la");
        qa.attemptsTo(PlaySequence.with(seq));
        qa.attemptsTo(PlaySequence.with(seq));
        String last = DisplayedNote.text().answeredBy(qa);
        Assertions.assertThat(last).isNotNull();
    }

    @Test @Order(3)
    @DisplayName("Himno de la Alegría - Escenario 3")
    void escenario3(){
        List<String> seq = Arrays.asList(
            "si","si","do","re","re","do","si","la","sol","sol","la","si",
            "la","sol","sol","la","si","sol","la","si","do","si","sol","la","si",
            "do","si","sol","sol","fa","re"
        );
        qa.attemptsTo(PlaySequence.with(seq));
        List<String> esc1 = Arrays.asList("si","si","do","re","re","do","si","la","sol","sol","la","si","si","la","la");
        qa.attemptsTo(PlaySequence.with(esc1));
        String last = DisplayedNote.text().answeredBy(qa);
        Assertions.assertThat(last).isNotNull();
    }
}
