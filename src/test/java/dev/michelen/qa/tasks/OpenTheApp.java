package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class OpenTheApp implements Task {
    private final String url;
    public OpenTheApp(String url){ this.url = url; }
    public static OpenTheApp at(String url){ return new OpenTheApp(url); }
    @Override
    public <T extends Actor> void performAs(T actor) {
        BrowseTheWeb.as(actor).getDriver().get(url);
    }
}
