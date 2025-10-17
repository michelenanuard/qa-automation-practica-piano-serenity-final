package dev.michelen.qa.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import java.util.List;

public class PlaySequence implements Task {
    private final List<String> notes;
    public PlaySequence(List<String> notes){ this.notes = notes; }
    public static Performable with(List<String> notes){ return new PlaySequence(notes); }
    @Override
    public <T extends Actor> void performAs(T actor) {
        for(String n : notes){
            actor.attemptsTo(PlayNote.withKey(n));
        }
    }
}
