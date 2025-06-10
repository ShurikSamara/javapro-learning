package ru.learning.java;

public class ShutdownTest {
  public static void main(String[] args) throws InterruptedException {
    CustomThreadPool pool = new CustomThreadPool(2);

    System.out.println("=== Тестирование выполнения метода shutdown() ===");
    //после вызова метода новые задачи не должны приниматься в pool
    for (int i = 0; i < 5; i++) {
      final int taskId = i;
      pool.execute(() -> {
        try {
          Thread.sleep(3000);
          System.out.println("Длительная задача " + taskId + " выполнена");
        } catch (InterruptedException e) {
          System.out.println("Длительная задача " + taskId + " прервана");
        }
      });
    }

    pool.shutdown();

    try {
      pool.execute(() -> System.out.println("Это не должно выполняться"));
    } catch (IllegalStateException e) {
      System.out.println("Отклоненная задача после завершения работы: " + e.getMessage());
    }

    System.out.println("Ожидание завершения...Ваш звонок очень важен для нас");
    pool.awaitTermination();
    System.out.println("Завершёно");

    System.out.println("=== Завершение тестирования выполнения метода shutdown() ===");
  }
}