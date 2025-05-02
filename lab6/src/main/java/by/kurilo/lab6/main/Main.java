package by.kurilo.lab6.main;

import by.kurilo.lab6.hash.Entry;
import by.kurilo.lab6.hash.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable ht = new HashTable(20, 0);

        String[][] mathTerms = {
                {"Вектор", "Величина с величиной и направлением, Алгебра"},
                {"Матрица", "Прямоугольный массив чисел, Алгебра"},
                {"Интеграл", "Площадь под кривой, Анализ"},
                {"Дифференциал", "Мера изменения функции, Анализ"},
                {"Тензор", "Обобщение векторов и матриц, Алгебра"},
                {"Множество", "Коллекция объектов, Теория множеств"},
                {"Функция", "Отношение между входом и выходом, Анализ"},
                {"Предел", "Значение, к которому стремится функция, Анализ"},
                {"Кольцо", "Структура с двумя операциями, Алгебра"},
                {"Группа", "Множество с одной операцией, Алгебра"},
                {"Поле", "Структура с двумя операциями, Алгебра"},
                {"Граф", "Множество вершин и рёбер, Теория графов"}
        };

        for (String[] term : mathTerms) {
            ht.insert(term[0], term[1]);
        }

        ht.display();

        System.out.println("\nПоиск 'Вектор':");
        Entry result = ht.search("Вектор");
        System.out.println(result != null ? "Найдено: " + result.getId() + ", " + result.getData() : "Не найдено");

        System.out.println("\nОбновление 'Матрица':");
        if (ht.update("Матрица", "Массив чисел для линейных преобразований, Алгебра")) {
            System.out.println("Обновление успешно");
            ht.display();
        }

        System.out.println("\nУдаление 'Интеграл':");
        if (ht.delete("Интеграл")) {
            System.out.println("Удаление успешно");
            ht.display();
        }

        System.out.println("\nВставка 'Скаляр':");
        if (ht.insert("Скаляр", "Величина без направления, Алгебра")) {
            System.out.println("Вставка успешна");
            ht.display();
        }
    }
}
