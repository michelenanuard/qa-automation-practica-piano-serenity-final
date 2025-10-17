package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
    import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class AcceptCookiesIfAny implements Task {
    private static final Target COOKIE_BUTTON = Target.the("Botón aceptar cookies")
        .located(By.xpath("(" +
            "//button[contains(translate(.,'ACEPTAROK','aceptarok'),'aceptar')]" +
            "|//button[contains(translate(.,'ACEPTAROK','aceptarok'),'ok')]" +
            "|//button[contains(@id,'cookie') or contains(@class,'cookie')]" +
            ")[1]"));
    public static AcceptCookiesIfAny now(){ return new AcceptCookiesIfAny(); }
    @Override
    public <T extends Actor> void performAs(T actor) {
        try {
            if (COOKIE_BUTTON.resolveFor(actor).isCurrentlyVisible()) {
                actor.attemptsTo(Click.on(COOKIE_BUTTON));
            }
        } catch (Throwable ignored) {}
    }
}
