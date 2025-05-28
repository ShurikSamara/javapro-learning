package ru.learning.java.annotations;

public class AnnotationTest {
  /*
   * Набор аннотаций Before, которые выполнятся перед запуском тестов
   */
  @BeforeSuite
  public static void setUpSuite() {
    System.out.println("== Перед стартом всех тестов ==");
  }

  @BeforeTest
  public void beforeEachTest() {
    System.out.println("Подготовка перед тестом");
  }

  /*
   * Набор тестов для проверки приоритетов
   */
  @Test(priority = 8)
  @CsvSource("42,hello,true")
  public void testWithCsvSource(int number, String word, boolean flag) {
    System.out.printf("Тест с параметрами: %d, %s, %b%n", number, word, flag);
  }

  @Test
  public void testDefaultPriority() {
    System.out.println("Выполнен тест по умолчанию");
  }

  @Test(priority = 2)
  public void testHighPriority() {
    System.out.println("Выполнен более приоритетный тест");
  }

  /*
   * Набор аннотаций After, которые выполнятся после запуска тестов
   */
  @AfterTest
  public void afterEachTest() {
    System.out.println("Очистка после теста");
  }

  @AfterSuite
  public static void tearDownSuite() {
    System.out.println("== После завершения всех тестов ==");
  }
}