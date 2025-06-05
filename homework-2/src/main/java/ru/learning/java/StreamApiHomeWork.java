package ru.learning.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamApiHomeWork {

  public static void main(String[] args) {
    System.out.println("=== Решения задач со Stream API ===\n");

    // Задача 1: 3-е наибольшее число
    task1();

    // Задача 2: 3-е наибольшее уникальное число
    task2();

    // Задача 3: Имена 3 самых старших инженеров
    task3();

    // Задача 4: Средний возраст инженеров
    task4();

    // Задача 5: Самое длинное слово
    task5();

    // Задача 6: Подсчёт слов в строке
    task6();

    // Задача 7: Сортировка строк по длине и алфавиту
    task7();

    // Задача 8: Самое длинное слово в массиве строк
    task8();

    System.out.println("=== Завершение задач со Stream API ===\n");
  }

  // Задача 1: Найти 3-е наибольшее число
  private static void task1() {
    System.out.println("1. Третье наибольшее число:");
    List<Integer> numbers = Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13);
    System.out.println("Исходный список: " + numbers);

    Optional<Integer> thirdLargest = numbers.stream().sorted(Collections.reverseOrder()).skip(2).findFirst();

    System.out.println("3-е наибольшее число: " + thirdLargest.orElse(-1));
    System.out.println();
  }

  // Задача 2: Найти 3-е наибольшее уникальное число
  private static void task2() {
    System.out.println("2. Третье наибольшее уникальное число:");
    List<Integer> numbers = Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13);
    System.out.println("Исходный список: " + numbers);

    Optional<Integer> thirdLargestUnique = numbers.stream().distinct().sorted(Collections.reverseOrder()).skip(2).findFirst();

    System.out.println("3-е наибольшее уникальное число: " + thirdLargestUnique.orElse(-1));
    System.out.println();
  }

  // Задача 3: Имена 3 самых старших инженеров
  private static void task3() {
    System.out.println("3. Имена 3 самых старших инженеров:");
    List<Employee> employees = getListEmployees();

    List<String> oldestEngineers = employees.stream()
      .filter(emp -> "Инженер".equals(emp.position()))
      .sorted((e1, e2) -> Integer.compare(e2.age(), e1.age()))
      .limit(3)
      .map(Employee::name)
      .toList();

    System.out.println("3 самых старших инженера: " + oldestEngineers);
    System.out.println();
  }

  // Задача 4: Средний возраст инженеров
  private static void task4() {
    System.out.println("4. Средний возраст инженеров:");
    List<Employee> employees = getListEmployees();

    double averageAge = employees.stream()
      .filter(emp -> "Инженер".equals(emp.position()))
      .mapToInt(Employee::age)
      .average()
      .orElse(0.0);

    System.out.println("Средний возраст инженеров: " + String.format("%.1f", averageAge));
    System.out.println();
  }

  // Задача 5: Самое длинное слово
  private static void task5() {
    System.out.println("5. Самое длинное слово:");
    List<String> words = Arrays.asList("java", "stream", "api", "programming", "collection");
    System.out.println("Список слов: " + words);

    Optional<String> longestWord = words.stream().max(Comparator.comparing(String::length));

    System.out.println("Самое длинное слово: " + longestWord.orElse(""));
    System.out.println();
  }

  // Задача 6: Подсчёт слов в строке
  private static void task6() {
    System.out.println("6. Подсчет слов в строке:");
    String text = "java stream api java collection stream java";
    System.out.println("Исходная строка: \"" + text + "\"");

    Map<String, Long> wordCount = Arrays.stream(text.split(" "))
      .filter(word -> !word.isBlank())
      .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

    System.out.println("Подсчет слов: " + wordCount);
    System.out.println();
  }

  // Задача 7: Сортировка строк по длине и алфавиту
  private static void task7() {
    System.out.println("7. Сортировка строк по длине и алфавиту:");
    List<String> strings = Arrays.asList("banana", "apple", "cat", "dog", "elephant", "ant");
    System.out.println("Исходный список: " + strings);

    List<String> sortedStrings = strings.stream()
      .sorted(Comparator.comparing(String::length).thenComparing(String::compareTo))
      .toList();

    System.out.println("Отсортированные строки:");
    sortedStrings.forEach(System.out::println);
    System.out.println();
  }

  // Задача 8: Самое длинное слово в массиве строк
  private static void task8() {
    System.out.println("8. Самое длинное слово в массиве строк:");
    String[] sentences = {
      "java stream api programming language",
      "collection framework provides many utilities",
      "functional programming paradigm is powerful",
      "lambda expressions simplify code significantly"
    };

    System.out.println("Массив строк:");
    Arrays.stream(sentences).forEach(s -> System.out.println("\"" + s + "\""));

    Optional<String> longestWord = Arrays.stream(sentences)
      .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
      .max(Comparator.comparing(String::length));

    System.out.println("Самое длинное слово: " + longestWord.orElse(""));
    System.out.println();
  }

  private static List<Employee> getListEmployees() {
    return Arrays.asList(
      new Employee("Иван", 35, "Инженер"),
      new Employee("Петр", 28, "Менеджер"),
      new Employee("Анна", 42, "Инженер"),
      new Employee("Мария", 31, "Инженер"),
      new Employee("Сергей", 45, "Инженер"),
      new Employee("Елена", 29, "Дизайнер"),
      new Employee("Алексей", 38, "Инженер"),
      new Employee("Владимир", 73, "Инженер")
    );
  }

  // Класс для представления сотрудника
  record Employee(String name, int age, String position) {

    @Override
    public String toString() {
      return String.format("%s (%d лет, %s)", name, age, position);
    }
  }
}