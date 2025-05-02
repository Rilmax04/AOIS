package by.kurilo.lab6.hash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {
    private HashTable hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new HashTable(10, 0);
    }

    @Test
    public void testInsertSuccess() {
        boolean result = hashTable.insert("Вектор", "Величина с направлением");
        assertTrue(result, "Вставка должна быть успешной");
        hashTable.display();
        Entry entry = hashTable.search("Вектор");
        assertNotNull(entry, "Элемент должен быть найден");
        assertEquals("Вектор", entry.getId(), "Ключ должен совпадать");
        assertEquals("Величина с направлением", entry.getData(), "Данные должны совпадать");
        assertEquals(0, entry.getIsDeleted(), "Элемент не должен быть помечен как удалённый");
        assertEquals(1, entry.getIsUsed(), "Элемент должен быть помечен как используемый");
        assertEquals(1, entry.getIsTail(), "Элемент должен быть последним в цепочке");
    }

    @Test
    public void testInsertDuplicateKey() {
        hashTable.insert("Матрица", "Массив чисел");
        boolean result = hashTable.insert("Матрица", "Другое описание");
        assertFalse(result, "Вставка дублирующего ключа должна быть отклонена");

        Entry entry = hashTable.search("Матрица");
        assertNotNull(entry, "Элемент должен быть найден");
        assertEquals("Массив чисел", entry.getData(), "Данные не должны измениться");
    }

    @Test
    public void testSearchNonExistentKey() {
        Entry entry = hashTable.search("Несуществующий");
        assertNull(entry, "Поиск несуществующего ключа должен вернуть null");
    }

    @Test
    public void testUpdateSuccess() {
        hashTable.insert("Интеграл", "Площадь под кривой");
        boolean result = hashTable.update("Интеграл", "Новая площадь");
        assertTrue(result, "Обновление должно быть успешным");

        Entry entry = hashTable.search("Интеграл");
        assertNotNull(entry, "Элемент должен быть найден");
        assertEquals("Новая площадь", entry.getData(), "Данные должны быть обновлены");
    }

    @Test
    public void testUpdateNonExistentKey() {

        boolean result = hashTable.update("Несуществующий", "Новое значение");
        assertFalse(result, "Обновление несуществующего ключа должно быть отклонено");
    }

    @Test
    public void testDeleteSuccess() {

        hashTable.insert("Тензор", "Обобщение векторов");
        boolean result = hashTable.delete("Тензор");
        assertTrue(result, "Удаление должно быть успешным");

        Entry entry = hashTable.search("Тензор");
        assertNull(entry, "Удалённый элемент не должен быть найден");
    }

    @Test
    public void testDeleteNonExistentKey() {
        boolean result = hashTable.delete("Несуществующий");
        assertFalse(result, "Удаление несуществующего ключа должно быть отклонено");
    }

    @Test
    public void testLoadFactor() {
        assertEquals(0.0, hashTable.loadFactor(), 0.001, "Коэффициент заполнения пустой таблицы должен быть 0");

        hashTable.insert("Вектор", "Величина с направлением");
        hashTable.insert("Матрица", "Массив чисел");
        double expectedLoadFactor = 2.0 / 10.0; // 2 элемента в таблице размером 10
        assertEquals(expectedLoadFactor, hashTable.loadFactor(), 0.001, "Коэффициент заполнения должен быть 0.2");

        hashTable.delete("Вектор");
        expectedLoadFactor = 1.0 / 10.0; // 1 элемент после удаления
        assertEquals(expectedLoadFactor, hashTable.loadFactor(), 0.001, "Коэффициент заполнения после удаления должен быть 0.1");
    }

    @Test
    public void testCollisionHandling() {
        hashTable.insert("Вектор", "Первый элемент");
        hashTable.insert("Ввод", "Второй элемент");

        Entry entry1 = hashTable.search("Вектор");
        Entry entry2 = hashTable.search("Ввод");

        assertNotNull(entry1, "Первый элемент с коллизией должен быть найден");
        assertNotNull(entry2, "Второй элемент с коллизией должен быть найден");
        assertEquals("Первый элемент", entry1.getData(), "Данные первого элемента должны совпадать");
        assertEquals("Второй элемент", entry2.getData(), "Данные второго элемента должны совпадать");
        assertEquals(0, entry2.getHasCollision(), "Второй элемент должен иметь флаг коллизии");
        assertEquals(1, entry1.getIsTail(), "Первый элемент не должен быть последним в цепочке");
        assertEquals(1, entry2.getIsTail(), "Второй элемент должен быть последним в цепочке");
    }

    @Test
    public void testDeleteInCollisionChainFirstElement() {
        hashTable.insert("Вектор", "Первый элемент");
        hashTable.insert("Ввод", "Второй элемент");

        boolean result = hashTable.delete("Вектор");
        assertTrue(result, "Удаление элемента из цепочки должно быть успешным");

        Entry entry1 = hashTable.search("Вектор");
        Entry entry2 = hashTable.search("Ввод");

        assertNull(entry1, "Удалённый элемент не должен быть найден");
        assertNotNull(entry2, "Оставшийся элемент в цепочке должен быть найден");
        assertEquals("Второй элемент", entry2.getData(), "Данные оставшегося элемента должны совпадать");
        assertEquals(0, entry2.getHasCollision(), "Оставшийся элемент не должен иметь флаг коллизии");
        assertEquals(1, entry2.getIsTail(), "Оставшийся элемент должен быть последним в цепочке");
    }

    @Test
    public void testDeleteLastElementInMultiElementChain() {
        hashTable.insert("Вектор", "Первый элемент");
        hashTable.insert("Ввод", "Второй элемент");

        boolean result = hashTable.delete("Ввод");
        assertTrue(result, "Удаление последнего элемента из цепочки должно быть успешным");

        Entry entry1 = hashTable.search("Вектор");
        Entry entry2 = hashTable.search("Ввод");

        assertNotNull(entry1, "Оставшийся элемент должен быть найден");
        assertNull(entry2, "Удалённый элемент не должен быть найден");
        assertEquals("Первый элемент", entry1.getData(), "Данные оставшегося элемента должны совпадать");
        assertEquals(0, entry1.getHasCollision(), "Оставшийся элемент не должен иметь флаг коллизии");
        assertEquals(1, entry1.getIsTail(), "Оставшийся элемент должен стать последним в цепочке");
        assertNull(entry1.getNextSlot(), "Указатель на следующий слот должен быть null");
    }

    @Test
    public void testDeleteMiddleElementInChain() {

        hashTable.insert("Вектор", "Первый элемент");
        hashTable.insert("Ввод", "Второй элемент");
        hashTable.insert("Вес", "Третий элемент");

        boolean result = hashTable.delete("Ввод");
        assertTrue(result, "Удаление элемента из середины цепочки должно быть успешным");

        Entry entry1 = hashTable.search("Вектор");
        Entry entry2 = hashTable.search("Ввод");
        Entry entry3 = hashTable.search("Вес");

        assertNotNull(entry1, "Первый элемент должен быть найден");
        assertNull(entry2, "Удалённый элемент не должен быть найден");
        assertNotNull(entry3, "Третий элемент должен быть найден");
        assertEquals("Первый элемент", entry1.getData(), "Данные первого элемента должны совпадать");
        assertEquals("Третий элемент", entry3.getData(), "Данные третьего элемента должны совпадать");
        assertEquals(1, entry3.getHasCollision(), "Третий элемент должен иметь флаг коллизии");
        assertEquals(1, entry3.getIsTail(), "Третий элемент должен быть последним в цепочке");
    }

    @Test
    public void testDeleteNonTailLastElementInChain() {
        hashTable.insert("Вектор", "Первый элемент");
        hashTable.insert("Ввод", "Второй элемент");
        Entry lastEntry = hashTable.search("Ввод");
        lastEntry.setIsTail(0); // Искусственно убираем флаг isTail

        boolean result = hashTable.delete("Ввод");
        assertTrue(result, "Удаление последнего элемента без флага isTail должно быть успешным");

        Entry entry1 = hashTable.search("Вектор");
        Entry entry2 = hashTable.search("Ввод");

        assertNotNull(entry1, "Оставшийся элемент должен быть найден");
        assertNull(entry2, "Удалённый элемент не должен быть найден");
        assertEquals("Первый элемент", entry1.getData(), "Данные оставшегося элемента должны совпадать");
        assertEquals(0, entry1.getHasCollision(), "Оставшийся элемент не должен иметь флаг коллизии");
        assertEquals(1, entry1.getIsTail(), "Оставшийся элемент должен быть последним в цепочке");
    }

    @Test
    public void testEntryFields() {
        hashTable.insert("Граф", "Множество вершин");
        Entry entry = hashTable.search("Граф");

        assertNotNull(entry, "Элемент должен быть найден");
        assertEquals("Граф", entry.getId(), "Ключ должен совпадать");
        assertEquals("Множество вершин", entry.getData(), "Данные должны совпадать");
        assertEquals(0, entry.getHasCollision(), "Флаг коллизии должен быть 0 для единственного элемента");
        assertEquals(1, entry.getIsUsed(), "Флаг использования должен быть 1");
        assertEquals(1, entry.getIsTail(), "Флаг конца цепочки должен быть 1");
        assertEquals(0, entry.getLockFlag(), "Флаг блокировки должен быть 0");
        assertEquals(0, entry.getIsDeleted(), "Флаг удаления должен быть 0");
        assertNull(entry.getNextSlot(), "Указатель на следующий слот должен быть null");
        assertTrue(entry.getKeyValue() >= 0, "Значение keyValue должно быть неотрицательным");
        assertTrue(entry.getHash() >= 0 && entry.getHash() < 10, "Хеш должен быть в пределах размера таблицы");
    }
}