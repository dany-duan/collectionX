package com.github.collectionx;


import com.github.collectionx.internal.PairX;

import java.util.Map;

public class MultimapX<K, V> {

    private final MapX<K, SetX<V>> data = MapX.newMap();

    public static<K, V> MultimapX<K, V> newMultimap(){
        return new MultimapX<>();
    }

    public static<K, V> MultimapX<K, V> newMultimap(Map<K, V> data){
        MultimapX<K, V> result = new MultimapX<>();
        for (Map.Entry<K, V> entry : data.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public int size() {
        return data.values().folder(0, (seed, vs) -> seed + vs.size());
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean isNotEmpty() {
        return data.isNotEmpty();
    }

    public boolean containsKey(K k) {
        return data.containsKey(k);
    }

    public SetX<K> keys(){
        return data.keySet();
    }

    public ListX<SetX<V>> values(){
        return data.values();
    }

    public CollectionX<PairX<K, V>> entries() {
        return data.entrySet().flatmap(entry -> entry.getValue()
                                        .map(v -> PairX.pairOf(entry.getKey(), v)));
    }

    public boolean put(K k, V v) {
        SetX<V> values = getOrCreate(k);
        return values.add(v);
    }

    public boolean putAll(K k, Iterable<? extends V> values) {
        boolean updated = false;
        for (V v : values) {
            updated = updated || put(k, v);
        }
        return updated;
    }

    public boolean remove(K k, V v) {
        SetX<V> values = getOrCreate(k);
        boolean result = values.remove(v);
        if (values.isEmpty()) {
            data.remove(k);
        }
        return result;
    }

    public boolean removeAll(K k) {
        return data.remove(k) != null;
    }

    private SetX<V> getOrCreate(K k) {
        SetX<V> values = data.get(k);
        if (values == null) {
            values = SetX.newSet();
            data.put(k, values);
        }
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultimapX<?, ?> that = (MultimapX<?, ?>) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
