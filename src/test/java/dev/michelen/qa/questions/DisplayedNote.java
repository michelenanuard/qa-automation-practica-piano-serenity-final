package dev.michelen.qa.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class DisplayedNote implements Question<String> {
    public static DisplayedNote text(){ return new DisplayedNote(); }
    @Override
    public String answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
            "function deepAll(){const out=[];const walk=r=>{if(!r)return;const els=r.querySelectorAll('*');" +
            "for(const el of els){out.push(el);if(el.shadowRoot){walk(el.shadowRoot);} }};walk(document);return out;}"+
            "const c=deepAll().filter(e=>{const t=(e.textContent||'').trim();return t && t.length<=10;});"+
            "for(const el of c){const t=(el.textContent||'').trim(); if(/^(do|re|mi|fa|sol|la|si|[A-G][#b]?(\\d)?)$/i.test(t)) return t;}"+
            "return '';";
        Object v = js.executeScript(script);
        return v == null ? "" : String.valueOf(v);
    }
}
