package pl.cezarysanecki.partyarchetypeapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import pl.cezarysanecki.partyarchetypeapp.model.Party;

import java.util.Map;
import java.util.Set;

public class PartyFactory {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static final String BASE_PACKAGE = "pl.cezarysanecki.partyarchetypeapp.model";
    static final Reflections REFLECTIONS = new Reflections(BASE_PACKAGE);

    public Party create(String type, Map<String, Object> values) {
        Class<? extends Party> clazz = findSubclassBy(type);
        if (clazz == null) {
            throw new IllegalArgumentException("No Party subclass found for type: " + type);
        }
        try {
            return OBJECT_MAPPER.convertValue(values, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create RegisteredIdentifier instance " + clazz.getSimpleName(), e);
        }
    }

    public Class<? extends Party> findSubclassBy(String type) {
        Set<Class<? extends Party>> subclasses = REFLECTIONS.getSubTypesOf(Party.class);

        return subclasses.stream()
                .filter(subclass -> subclass.getSimpleName().equals(type))
                .findFirst()
                .orElse(null);
    }

}
