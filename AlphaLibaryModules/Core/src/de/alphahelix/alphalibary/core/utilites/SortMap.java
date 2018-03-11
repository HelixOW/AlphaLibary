package de.alphahelix.alphalibary.core.utilites;

import java.util.*;


public class SortMap<K, V> implements Map<K, V> {

    private final Map<K, V> tempMap;
    private final transient Map<K, V> lookUp = new HashMap<>();

    public SortMap(Object... content) {
        this(SortType.UNSORTED, null, content);
    }

    public SortMap(SortType type, SortOrder order, Object... content) {
        if (type != null && type == SortType.KEY) {
            tempMap = new TreeMap<>((o1, o2) -> {
                if (o1 == null)
                    return -order.val;
                if (o2 == null)
                    return order.val;
                if (!(o1 instanceof Comparable<?> && o2 instanceof Comparable<?>))
                    return 0;

                int cmp = ((Comparable<K>) o1).compareTo(o2);

                if (cmp == 0)
                    return order.val * ((Comparable<V>) lookUp.get(o1)).compareTo(lookUp.get(o2));

                return order.val * cmp;
            });
        } else if (type != null && type == SortType.VALUE) {
            tempMap = new TreeMap<>((k1, k2) -> {
                V v1 = lookUp.get(k1);

                if (v1 == null)
                    return -order.val;

                V v2 = lookUp.get(k2);

                if (v2 == null)
                    return order.val;

                int cmp = ((Comparable<V>) v1).compareTo(v2);

                if (cmp == 0)
                    return order.val * ((Comparable<K>) k1).compareTo(k2);


                return order.val * cmp;
            });
        } else {
            tempMap = new HashMap<>();
        }

        if (content.length != 0 && content.length % 2 != 0)
            throw new IllegalArgumentException("Size must be a multiple of 2");

        for (int i = 0; i < content.length; i += 2)
            tempMap.put((K) content[i], (V) content[i + 1]);
    }

    public static <TK, TV> SortMap<TK, TV> createMap(Object... objects) {
        if (objects.length != 0 && objects.length % 2 != 0)
            throw new IllegalArgumentException("Size must be a multiple of 2");

        SortMap<TK, TV> map = new SortMap<>();

        for (int i = 0; i < objects.length; i += 2)
            map.put((TK) objects[i], (TV) objects[i + 1]);

        return map;
    }

    @Override
    public int size() {
        return tempMap.size();
    }

    @Override
    public boolean isEmpty() {
        return tempMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return lookUp.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return lookUp.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return lookUp.get(key);
    }

    @Override
    public V put(K key, V value) {
        lookUp.put(key, value);
        return tempMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        lookUp.remove(key);
        return tempMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lookUp.putAll(m);
        tempMap.putAll(m);
    }

    @Override
    public void clear() {
        lookUp.clear();
        tempMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return tempMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return tempMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return tempMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortMap<?, ?> sortMap = (SortMap<?, ?>) o;

        return tempMap != null ? tempMap.equals(sortMap.tempMap) : sortMap.tempMap == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder str = new StringBuilder("{ ");
        for (K k : tempMap.keySet())
            str.append("[").append(k.toString()).append(" -- ").append(tempMap.get(k).toString()).append("] || ");

        str = str.delete(0, str.length() - " || ".length()).append(" }");

        return str.toString();
    }

    public K getKeyByValue(V v) {
        for (K k : keySet())
            if (get(k).equals(v))
                return k;
        return null;
    }

    public enum SortType {
        KEY, VALUE, UNSORTED
    }

    public enum SortOrder {
        FORWARDS((byte) 1), BACKWARDS((byte) -1);

        private final byte val;

        SortOrder(byte val) {
            this.val = val;
        }
    }
}
