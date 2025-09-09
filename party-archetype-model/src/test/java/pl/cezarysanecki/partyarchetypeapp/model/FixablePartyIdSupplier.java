package pl.cezarysanecki.partyarchetypeapp.model;

import java.util.function.Supplier;

class FixablePartyIdSupplier implements Supplier<PartyId> {

    private PartyId fixedValue;

    void clear() {
        fixedValue = null;
    }

    void fixPartyIdTo(PartyId value) {
        fixedValue = value;
    }

    @Override
    public PartyId get() {
        return fixedValue != null ? fixedValue : PartyId.random();
    }
}
