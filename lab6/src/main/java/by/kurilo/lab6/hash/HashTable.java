package by.kurilo.lab6.hash;

import java.util.*;

public class HashTable {
    private static final Map<Character, Integer> RUSSIAN_ALPHABET = new HashMap<>();
    static {
        String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        for (int i = 0; i < alphabet.length(); i++) {
            RUSSIAN_ALPHABET.put(alphabet.charAt(i), i);
        }
    }

    private final int size;
    private final int baseAddress;
    private final List<List<Entry>> table;
    private final List<Entry> entries;

    public HashTable(int size, int baseAddress) {
        this.size = size;
        this.baseAddress = baseAddress;
        this.table = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());
        }
        this.entries = new ArrayList<>();
    }

    private int calculateV(String key) {
        if (key.length() < 2) {
            key = key + "А";
        }
        int first = RUSSIAN_ALPHABET.getOrDefault(Character.toUpperCase(key.charAt(0)), 0);
        int second = RUSSIAN_ALPHABET.getOrDefault(Character.toUpperCase(key.charAt(1)), 0);
        return first * 33 + second;
    }

    private int calculateHash(int v) {
        return (v % size) + baseAddress;
    }

    public boolean insert(String key, String data) {
        if (entries.stream().anyMatch(e -> e.getId().equals(key) && e.getIsDeleted() == 0)) {
            System.out.println("Ошибка: Ключ '" + key + "' уже существует.");
            return false;
        }

        int v = calculateV(key);
        int h = calculateHash(v);
        List<Entry> chain = table.get(h);

        Entry newEntry = new Entry(key, data, v, h);
        if (!chain.isEmpty()) {
            newEntry.setHasCollision(1);
            chain.get(chain.size() - 1).setIsTail(0);
            chain.get(chain.size() - 1).setNextSlot(h);
        }
        chain.add(newEntry);
        entries.add(newEntry);
        return true;
    }

    public Entry search(String key) {
        int v = calculateV(key);
        int h = calculateHash(v);
        List<Entry> chain = table.get(h);

        for (Entry entry : chain) {
            if (entry.getId().equals(key) && entry.getIsDeleted() == 0) {
                return entry;
            }
        }
        return null;
    }

    public boolean update(String key, String newData) {
        Entry entry = search(key);
        if (entry == null) {
            System.out.println("Ошибка: Ключ '" + key + "' не найден.");
            return false;
        }
        entry.setData(newData);
        return true;
    }

    public boolean delete(String key) {
        int v = calculateV(key);
        int h = calculateHash(v);
        List<Entry> chain = table.get(h);

        for (int i = 0; i < chain.size(); i++) {
            Entry current = chain.get(i);
            if (current.getId().equals(key) && current.getIsDeleted() == 0) {
                current.setIsDeleted(1);

                if (current.getIsTail() == 1 && (current.getNextSlot() == null || current.getNextSlot().equals(h))) {
                    current.setIsUsed(0);
                    if (chain.size() == 1) {
                        chain.clear();
                    } else {
                        chain.remove(i);
                    }
                }
                else if (current.getIsTail() == 1) {
                    if (i > 0) {
                        Entry prev = chain.get(i - 1);
                        prev.setIsTail(1);
                        prev.setNextSlot(null);
                    }
                    chain.remove(i);
                }
                else {
                    if (i < chain.size() - 1) {
                        Entry next = chain.get(i + 1);
                        chain.set(i, next);
                        chain.remove(i + 1);
                        if (i == 0) {
                            chain.getFirst().setHasCollision(1);
                        }
                    } else {
                        chain.remove(i);
                    }
                }
                return true;
            }
        }
        System.out.println("Ошибка: Ключ '" + key + "' не найден.");
        return false;
    }

    public double loadFactor() {
        int occupied = (int) entries.stream().filter(e -> e.getIsDeleted() == 0).count();
        return (double) occupied / size;
    }

    public void display() {
        System.out.println("\nСодержимое хеш-таблицы:");
        System.out.printf("%-5s %-15s %-12s %-8s %-8s %-10s %-10s %-10s %-30s%n",
                "Слот", "ID", "Collision", "Used", "Tail", "Lock", "Deleted", "NextSlot", "Data");
        for (int i = 0; i < size; i++) {
            List<Entry> chain = table.get(i);
            if (chain.isEmpty()) {
                System.out.printf("%-5d %-15s %-12d %-8d %-8d %-10d %-10d %-10s %-30s%n",
                        i, "", 0, 0, 0, 0, 0, "", "");
            } else {
                for (Entry e : chain) {
                    String nextSlot = e.getNextSlot() != null ? e.getNextSlot().toString() : "";
                    System.out.printf("%-5d %-15s %-12d %-8d %-8d %-10d %-10d %-10s %-30s%n",
                            i, e.getId(), e.getHasCollision(), e.getIsUsed(), e.getIsTail(), e.getLockFlag(), e.getIsDeleted(), nextSlot, e.getData());
                }
            }
        }

        System.out.println("\nЗаписи (Ключ, KeyValue, Hash, Слот):");
        for (Entry e : entries) {
            if (e.getIsDeleted() == 0) {
                System.out.printf("Ключ: %-15s KeyValue: %-4d Hash: %-4d Слот: %d%n", e.getId(), e.getKeyValue(), e.getHash(), e.getHash());
            }
        }

        System.out.printf("\nКоэффициент заполнения: %.2f%%%n", loadFactor() * 100);
    }


}