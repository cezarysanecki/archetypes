package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface PartyRepository {

    Optional<Party> findBy(PartyId partyId);

    Optional<Party> findBy(PartyId partyId, Class<? extends Party> partyType);

    void save(Party party);

    void delete(PartyId partyId);

    List<Party> findBy(RegisteredIdentifier registeredIdentifier);

    List<Party> findMatching(Predicate<Party> predicate);
}

class InMemoryPartyRepository implements PartyRepository {

    private final ConcurrentHashMap<PartyId, Party> DATABASE = new ConcurrentHashMap<>(10);

    @Override
    public Optional<Party> findBy(PartyId partyId) {
        return Optional.ofNullable(DATABASE.get(partyId));
    }

    @Override
    public Optional<Party> findBy(PartyId partyId, Class<? extends Party> partyType) {
        return Optional.ofNullable(DATABASE.get(partyId)).filter(it -> partyType.isAssignableFrom(it.getClass()));
    }

    @Override
    public void save(Party party) {
        DATABASE.put(party.partyId(), party);
    }

    @Override
    public void delete(PartyId partyId) {
        DATABASE.remove(partyId);
    }

    @Override
    public List<Party> findBy(RegisteredIdentifier registeredIdentifier) {
        return DATABASE.values().parallelStream()
                .filter(party -> party.registeredIdentifiers().contains(registeredIdentifier))
                .collect(Collectors.toList());
    }

    @Override
    public List<Party> findMatching(Predicate<Party> predicate) {
        return DATABASE.values().parallelStream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}