package pl.cezarysanecki.partyarchetypeapp.utils;

import org.reflections.Reflections;
import pl.cezarysanecki.partyarchetypeapp.model.Party;

import java.util.Set;

public class PartyFactory {

    static final String BASE_PACKAGE = "pl.cezarysanecki.partyarchetypeapp";
    static final Reflections REFLECTIONS = new Reflections(BASE_PACKAGE);

    public Class<? extends Party> findSubclassBy(String type) {
        Set<Class<? extends Party>> subclasses = REFLECTIONS.getSubTypesOf(Party.class);

        return subclasses.stream()
                .filter(subclass -> subclass.getSimpleName().equals(type))
                .findFirst()
                .orElse(null);
    }

}
