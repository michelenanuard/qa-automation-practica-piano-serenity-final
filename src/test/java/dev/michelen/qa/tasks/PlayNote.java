package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlayNote implements Task {
    private final String note; // do, re, mi, fa, sol, la, si
    public static PlayNote withKey(String note){ return new PlayNote(note); }
    public PlayNote(String note){ this.note = note; }

    // posición horizontal
    private static final Map<String, Double> POS = new HashMap<String, Double>() {{
        put("do", 0.07); put("re", 0.21); put("mi", 0.35); put("fa", 0.49);
        put("sol", 0.63); put("la", 0.78); put("si", 0.92);
    }};

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String key = note.toLowerCase(Locale.ROOT).trim();
        Double ratio = POS.get(key);
        if (ratio == null) {
            throw new IllegalArgumentException("Usa solfeo: do,re,mi,fa,sol,la,si (recibido: " + note + ")");
        }

        String script =
            "function deepAll(){var out=[];function walk(r){if(!r)return;var els=r.querySelectorAll('*');" +
            "for(var i=0;i<els.length;i++){var el=els[i];out.push(el);if(el.shadowRoot){walk(el.shadowRoot);}}}" +
            "walk(document);return out;}" +

            "function isVisible(e){if(!e)return false;var r=e.getBoundingClientRect();" +
            "return r.width>0 && r.height>0 && window.getComputedStyle(e).visibility!=='hidden' && window.getComputedStyle(e).display!=='none';}" +

            "function scrollCenter(e){try{e.scrollIntoView({block:'center',inline:'center'});}catch(ex){try{e.scrollIntoView();}catch(ex2){}}}" +

            // 1) buscar por atributos
            "function tryAttrNote(txt){txt=String(txt||'').toLowerCase();var all=deepAll();" +
            "var attrs=['data-note','data-key','aria-label','title'];" +
            "for(var i=0;i<all.length;i++){var e=all[i];if(!isVisible(e))continue;" +
            "for(var j=0;j<attrs.length;j++){var a=attrs[j];var v=(e.getAttribute&&e.getAttribute(a))?String(e.getAttribute(a)).toLowerCase():'';" +
            "if(v && (v===txt || v.indexOf(txt)>-1)){scrollCenter(e); try{e.click();}catch(_){return 'OK_ATTR_NOCLICK';} return 'OK_ATTR';}}" +
            "}return null;}" +

            // 2) buscar por texto
            "function tryDomText(txt){txt=String(txt||'').toLowerCase();var all=deepAll();" +
            "for(var i=0;i<all.length;i++){var e=all[i];if(!isVisible(e))continue;" +
            "var t=(e.textContent||'').replace(/\\s+/g,' ').trim().toLowerCase();" +
            "if(t===txt || t.indexOf(' '+txt+' ')>-1 || t.endsWith(' '+txt) || t.startsWith(txt+' ')){" +
            "scrollCenter(e); try{e.click();}catch(_){return 'OK_TEXT_NOCLICK';} return 'OK_TEXT';}}" +
            "return null;}" +

            // 3) fallback: click por coordenadas en canvas/svg/contendedor
            "function clickCenterRatio(el,ratio){var r=el.getBoundingClientRect();" +
            "var x=r.left + r.width*ratio; var y=r.top + r.height*0.60;" +
            "var opts={bubbles:true,cancelable:true,clientX:x,clientY:y};" +
            "el.dispatchEvent(new MouseEvent('mousemove',opts));" +
            "el.dispatchEvent(new MouseEvent('mousedown',opts));" +
            "el.dispatchEvent(new MouseEvent('mouseup',opts));" +
            "el.dispatchEvent(new MouseEvent('click',opts));}" +

            "function tryCanvasOrSvg(ratio){var all=deepAll();var kbd=null;" +
            "for(var i=0;i<all.length;i++){var e=all[i];if(!isVisible(e))continue;" +
            "var tag=(e.tagName||'').toUpperCase();" +
            "if(tag==='CANVAS' || tag==='SVG'){kbd=e;break;}}" +
            "if(!kbd){for(var k=0;k<all.length;k++){var c=(all[k].className||'').toString().toLowerCase();" +
            "if(isVisible(all[k]) && (c.indexOf('piano')>-1||c.indexOf('keyboard')>-1||c.indexOf('keys')>-1)){kbd=all[k];break;}}}" +
            "if(!kbd){return 'NO_CANVAS';} scrollCenter(kbd); clickCenterRatio(kbd, ratio); return 'OK';}" +

            "try{document.body&&document.body.click();}catch(e){}" +
            "var res=tryAttrNote(arguments[0]); if(res) return res;" +
            "res=tryDomText(arguments[0]); if(res) return res;" +
            "return tryCanvasOrSvg(arguments[1]);";

        Object res = js.executeScript(script, key, ratio);
        String s = (res == null) ? "null" : String.valueOf(res);
        if (!("OK".equals(s) || "OK_ATTR".equals(s) || "OK_TEXT".equals(s))) {
            throw new AssertionError("No se pudo accionar la tecla '" + note + "' (resultado: " + s + ")");
        }
    }
}