package pl.cezarysanecki.partyarchetypeapp.common;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class Pair<S, T> {

    private final S first;
    private final T second;

    private Pair(S first, T second) {
        Preconditions.checkNotNull(first, "First must not be null");
        Preconditions.checkNotNull(second, "Second must not be null");

        this.first = first;
        this.second = second;
    }

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<S, T>(first, second);
    }

    public S getFirst() {
        return this.first;
    }

    public T getSecond() {
        return this.second;
    }

    public static <S, T> Collector<Pair<S, T>, ?, Map<S, T>> toMap() {
        return Collectors.toMap(Pair::getFirst, Pair::getSecond);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}