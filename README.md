# Piano Virtual – Serenity Screenplay (QA Automatización)

Automatización del piano virtual de Musicca para validar notas y secuencias del Himno de la Alegría.

## Stack
Java 17 · Maven · Selenium (vía Serenity) · Serenity BDD (Screenplay) · JUnit 5 · AssertJ

## Requisitos
- JDK 17 (`java -version`)
- Maven (`mvn -version`)
- Google Chrome instalado

## Ejecutar
```bash
mvn -U clean verify
```
Reporte Serenity: `target/site/serenity/index.html`

### Test individuales
```bash
mvn -U -Dtest=SingleNotesTest -DforkCount=0 test
mvn -U -Dtest=OdeToJoyTests -DforkCount=0 test
```

## Headless (opcional)
Edita `src/test/resources/serenity.properties` y descomenta `chrome.switches`:
```
chrome.switches=--headless=new;--window-size=1920,1080;--start-maximized
```

## Notas/Limitaciones
- El piano puede renderizar en canvas o Shadow DOM; se simula click por coordenadas JS sobre el canvas.
- Si no hay label visible de nota, se valida por pasos ejecutados (evidencia Serenity) y ausencia de errores.
