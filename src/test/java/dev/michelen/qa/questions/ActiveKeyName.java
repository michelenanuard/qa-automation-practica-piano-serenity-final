package dev.michelen.qa.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class ActiveKeyName implements Question<String> {
    public static ActiveKeyName value(){ return new ActiveKeyName(); }
    @Override
    public String answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
            "function deepAll(){const out=[];const walk=r=>{if(!r)return;const els=r.querySelectorAll('*');" +
            "for(const el of els){out.push(el);if(el.shadowRoot){walk(el.shadowRoot);} }};walk(document);return out;}"+
            "function val(x){return (x||'').toString().toLowerCase();}"+
            "const keys=deepAll().filter(e=>{const c=val(e.className);return c.includes('key')||c.includes('piano');});"+
            "const a=keys.find(e=>{const c=val(e.className);return c.includes('active')||c.includes('pressed')||c.includes('down');});"+
            "if(!a) return '';"+
            "const attrs=['aria-label','data-note','data-key','title']; for(const k of attrs){const v=a.getAttribute(k); if(v) return v;}"+
            "return (a.textContent||'').trim();";
        Object v = js.executeScript(script);
        return v == null ? "" : String.valueOf(v);
    }
}
