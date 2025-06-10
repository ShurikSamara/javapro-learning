package ru.learning.java;

public class ShutdownTest {
  public static void main(String[] args) throws InterruptedException {
    CustomThreadPool pool = new CustomThreadPool(2);

    System.out.println("=== Тестирование выполнения метода shutdown() ===");

    // Проверяем начальное состояние
    System.out.println("Начальное состояние:");
    System.out.println("  isShutdown: " + pool.isShutdown());
    System.out.println("  isTerminated: " + pool.isTerminated());
    System.out.println("  Активных потоков: " + pool.getActiveThreadCount());
    System.out.println("  Задач в очереди: " + pool.getQueueSize());
    System.out.println();

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

    System.out.println("После добавления задач:");
    System.out.println("  isShutdown: " + pool.isShutdown());
    System.out.println("  isTerminated: " + pool.isTerminated());
    System.out.println("  Активных потоков: " + pool.getActiveThreadCount());
    System.out.println("  Задач в очереди: " + pool.getQueueSize());
    System.out.println();

    // Даём время задачам начать выполнение
    Thread.sleep(500);

    System.out.println("Вызываем shutdown()...");
    pool.shutdown();

    System.out.println("Сразу после shutdown():");
    System.out.println("  isShutdown: " + pool.isShutdown());
    System.out.println("  isTerminated: " + pool.isTerminated());
    System.out.println("  Активных потоков: " + pool.getActiveThreadCount());
    System.out.println("  Задач в очереди: " + pool.getQueueSize());
    System.out.println();

    try {
      pool.execute(() -> System.out.println("Это не должно выполняться"));
    } catch (IllegalStateException e) {
      System.out.println("Отклоненная задача после завершения работы: " + e.getMessage());
    }

    System.out.println();
    System.out.println("Ожидание завершения...Ваш звонок очень важен для нас");

    //Мониторинг состояния во время завершения
    Thread monitorThread = new Thread(() -> {
      try {
        while (!pool.isTerminated()) {
          Thread.sleep(1000);
          System.out.println("  [Мониторинг] isShutdown: " + pool.isShutdown() +
            ", isTerminated: " + pool.isTerminated() +
            ", активных потоков: " + pool.getActiveThreadCount() +
            ", задач в очереди: " + pool.getQueueSize());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    monitorThread.start();
    pool.awaitTermination();
    monitorThread.interrupt(); // Останавливаем мониторинг

    System.out.println();
    System.out.println("Финальное состояние:");
    System.out.println("  isShutdown: " + pool.isShutdown());
    System.out.println("  isTerminated: " + pool.isTerminated());
    System.out.println("  Активных потоков: " + pool.getActiveThreadCount());
    System.out.println("  Задач в очереди: " + pool.getQueueSize());

    System.out.println("Завершёно");
    System.out.println("=== Завершение тестирования выполнения метода shutdown() ===");
  }
}