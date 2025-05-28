package ru.learning.java.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class RunnerTest {

  public static void main(String[] args) {
    runTests(AnnotationTest.class);
  }

  public static void runTests(Class<?> testClass) {
    Method beforeSuite = null;
    Method afterSuite = null;
    List<Method> beforeTest = new ArrayList<>();
    List<Method> afterTest = new ArrayList<>();
    List<Method> tests = new ArrayList<>();

    // Сбор всех методов по аннотациям и базовые проверки
    for (Method m : testClass.getDeclaredMethods()) {
      if (m.isAnnotationPresent(BeforeSuite.class)) {
        if (beforeSuite != null) throw new RuntimeException("Только один метод может иметь @BeforeSuite");
        if (!Modifier.isStatic(m.getModifiers())) throw new RuntimeException("@BeforeSuite только для static");
        beforeSuite = m;
      }
      if (m.isAnnotationPresent(AfterSuite.class)) {
        if (afterSuite != null) throw new RuntimeException("Только один метод может иметь @AfterSuite");
        if (!Modifier.isStatic(m.getModifiers())) throw new RuntimeException("@AfterSuite только для static");
        afterSuite = m;
      }
      if (m.isAnnotationPresent(BeforeTest.class)) beforeTest.add(m);
      if (m.isAnnotationPresent(AfterTest.class)) afterTest.add(m);
      if (m.isAnnotationPresent(Test.class)) tests.add(m);
    }

    // Сортировка тестов по приоритету (10 → 1)
    tests.sort((m1, m2) -> {
      int p1 = m1.getAnnotation(Test.class).priority();
      int p2 = m2.getAnnotation(Test.class).priority();
      return Integer.compare(p1, p2);
    });

    Object testInstance;
    try {
      testInstance = testClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Не удалось создать тестовый класс: " + e.getMessage());
    }

    // Выполнение BeforeSuite
    if (beforeSuite != null) {
      try {
        beforeSuite.setAccessible(true);
        beforeSuite.invoke(null);
      } catch (Exception e) {
        throw new RuntimeException("@BeforeSuite завершился ошибкой: " + e.getMessage());
      }
    }

    // Запуск тестов
    for (Method testMethod : tests) {
      // Перед каждым тестом
      for (Method btm : beforeTest) {
        try {
          btm.setAccessible(true);
          btm.invoke(testInstance);
        } catch (Exception e) {
          System.err.println("@BeforeTest ошибка: " + e.getMessage());
        }
      }

      // Вызов теста, с поддержкой CsvSource
      try {
        testMethod.setAccessible(true);
        if (testMethod.isAnnotationPresent(CsvSource.class)) {
          String csv = testMethod.getAnnotation(CsvSource.class).value();
          Object[] args = parseCsvArguments(csv, testMethod.getParameterTypes());
          testMethod.invoke(testInstance, args);
        } else if (testMethod.getParameterCount() == 0) {
          testMethod.invoke(testInstance);
        } else {
          throw new RuntimeException(
            "У метода " + testMethod.getName() + " есть параметры, но нет @CsvSource"
          );
        }
      } catch (InvocationTargetException e) {
        System.err.println("Тест " + testMethod.getName() + " выбросил исключение: " + e.getTargetException());
      } catch (Exception e) {
        System.err.println("Ошибка при выполнении теста " + testMethod.getName() + ": " + e.getMessage());
      }

      // После каждого теста
      for (Method atm : afterTest) {
        try {
          atm.setAccessible(true);
          atm.invoke(testInstance);
        } catch (Exception e) {
          System.err.println("@AfterTest ошибка: " + e.getMessage());
        }
      }
      System.out.println("-----------------");
    }

    // Выполнение AfterSuite
    if (afterSuite != null) {
      try {
        afterSuite.setAccessible(true);
        afterSuite.invoke(null);
      } catch (Exception e) {
        throw new RuntimeException("@AfterSuite завершился ошибкой: " + e.getMessage());
      }
    }
  }

  /**
   * Преобразует csv-строку в массив аргументов нужных типов для передачи в метод.
   */
  private static Object[] parseCsvArguments(String csvRow, Class<?>[] paramTypes) {
    // Регулярное выражение для обработки заключённых в кавычки значений с запятыми
    String[] strParts = csvRow.split("\",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    if (strParts.length != paramTypes.length)
      throw new RuntimeException("CsvSource аргументов (" + strParts.length + ") не совпадает с количеством параметров метода (" + paramTypes.length + ")");
    Object[] args = new Object[paramTypes.length];
    for (int i = 0; i < paramTypes.length; ++i) {
      String trimmed = strParts[i].trim();
      Class<?> t = paramTypes[i];
      if (t == int.class || t == Integer.class) {
        args[i] = Integer.parseInt(trimmed);
      } else if (t == boolean.class || t == Boolean.class) {
        args[i] = Boolean.parseBoolean(trimmed);
      } else if (t == String.class) {
        args[i] = trimmed;
      } else if (t == double.class || t == Double.class) {
        args[i] = Double.parseDouble(trimmed);
      } else if (t == long.class || t == Long.class) {
        args[i] = Long.parseLong(trimmed);
      } else if (t == float.class || t == Float.class) {
        args[i] = Float.parseFloat(trimmed);
      } else if (t == char.class || t == Character.class) {
        if (trimmed.length() != 1)
          throw new RuntimeException("Невозможно привести строку к типу char: " + trimmed);
        args[i] = trimmed.charAt(0);
      } else {
        throw new RuntimeException("Не поддерживаемый тип аргумента: " + t);
      }
    }
    return args;
  }
}