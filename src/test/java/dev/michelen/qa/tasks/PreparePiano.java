package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class PreparePiano implements Task {
    public static PreparePiano now(){ return new PreparePiano(); }
    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try { js.executeScript("window.scrollTo(0, document.body.scrollHeight*0.33);"); } catch (Throwable ignored){}
        try { Thread.sleep(250); } catch (InterruptedException ignored) {}
    }
}
