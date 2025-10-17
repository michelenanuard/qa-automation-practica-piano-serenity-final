package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.*;

import java.util.List;

public class SwitchToPianoFrame implements Task {

    /** Factory method: devuelve una instancia utilizable en actor.attemptsTo(...) */
    public static SwitchToPianoFrame tryFind() {
        return new SwitchToPianoFrame();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        driver.switchTo().defaultContent();

        try { ((JavascriptExecutor) driver).executeScript("document.body && document.body.click();"); } catch (Throwable ignored) {}

        if (hasPianoLikeElements(driver)) return;

        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement frame : iframes) {
            try {
                driver.switchTo().defaultContent();
                if (!frame.isDisplayed()) continue;
                driver.switchTo().frame(frame);
                if (hasPianoLikeElements(driver)) return;
            } catch (Throwable ignored) { /* sigue con el siguiente iframe */ }
        }

        driver.switchTo().defaultContent();
    }

    private boolean hasPianoLikeElements(WebDriver driver) {
        try {
            Object ok = ((JavascriptExecutor) driver).executeScript(
                "function deepAll(){const out=[];const walk=r=>{if(!r)return;const els=r.querySelectorAll('*');" +
                "for(const el of els){out.push(el); if(el.shadowRoot){walk(el.shadowRoot);} }}; walk(document); return out; }" +
                "const all=deepAll();" +
                "return !!(all.find(e=>e.tagName==='CANVAS' && e.offsetWidth>0 && e.offsetHeight>0) || " +
                "          all.find(e=>e.tagName==='SVG'    && e.offsetWidth>0 && e.offsetHeight>0) || " +
                "          document.querySelector('[data-note],[data-key],[aria-label*=\"piano\" i]'));"
            );
            return Boolean.TRUE.equals(ok);
        } catch (Throwable t) {
            return false;
        }
    }
}