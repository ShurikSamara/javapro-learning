package ru.learning.java;

import java.util.concurrent.TimeUnit;

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
          TimeUnit.SECONDS.sleep(3);
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
    TimeUnit.SECONDS.sleep(1);

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
        while (!pool.isTerminated() && !Thread.currentThread().isInterrupted()) {
          TimeUnit.SECONDS.sleep(1);
          System.out.println("  [Мониторинг] isShutdown: " + pool.isShutdown() +
            ", isTerminated: " + pool.isTerminated() +
            ", активных потоков: " + pool.getActiveThreadCount() +
            ", задач в очереди: " + pool.getQueueSize());
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("  [Мониторинг] Поток мониторинга прерван");
      }
    });

    monitorThread.start();
    pool.awaitTermination();
    monitorThread.interrupt(); // Останавливаем мониторинг
    try {
      monitorThread.join(1000); // Ждём максимум 1 секунду
      if (monitorThread.isAlive()) {
        System.out.println("  [Предупреждение] Поток мониторинга не завершился за 1 секунду");
      }
    } catch (InterruptedException e) {
      System.out.println("❌ [Ошибка] Прервано ожидание завершения потока мониторинга");
      Thread.currentThread().interrupt();
    }

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