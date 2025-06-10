package ru.learning.java;

public class ThreadPoolExample {
  public static void main(String[] args) throws InterruptedException {
    CustomThreadPool pool = new CustomThreadPool(3);

    System.out.println("=== Решение задачи с кастомным пулом потоков ===");

    System.out.println("Активных потоков: " + pool.getActiveThreadCount());
    // Здесь задачи начнут складываться в pool
    for (int i = 0; i < 10; i++) {
      final int taskId = i;
      pool.execute(() -> {
        System.out.println("Задача " + taskId + " выполняется в " + Thread.currentThread().getName());
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          System.out.println("Задача " + taskId + " прервана");
          Thread.currentThread().interrupt();
        }
        System.out.println("Задача " + taskId + " завершена");
      });
    }

    System.out.println("Задач в очереди: " + pool.getQueueSize());

    pool.shutdown();
    pool.awaitTermination();

    System.out.println("Все потоки завершены. Активных потоков: " + pool.getActiveThreadCount());
    System.out.println("=== Завершение задачи с кастомным пулом потоков ===");
  }
}