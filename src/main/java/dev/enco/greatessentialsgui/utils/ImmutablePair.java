package dev.enco.greatessentialsgui.utils;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode @ToString
public class ImmutablePair<K, V> implements Serializable {
    private static final long serialVersionUID = 0L;
    protected final K left;
    protected final V right;

    public ImmutablePair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public static <K, V> ImmutablePair<K, V> of(K left, V right) {
        return new ImmutablePair(left, right);
    }

    public K left() {
        return this.left;
    }

    public V right() {
        return this.right;
    }
}
