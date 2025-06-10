package ru.learning.java;

public class ThreadPoolExample {
  public static void main(String[] args) throws InterruptedException {
    CustomThreadPool pool = new CustomThreadPool(3);

    System.out.println("=== Решение задачи с кастомным пулом потоков ===");
    // Здесь задачи начнут складываться в pool
    for (int i = 0; i < 10; i++) {
      final int taskId = i;
      pool.execute(() -> {
        System.out.println("Выполнение задачи " + taskId + " в потоке " + Thread.currentThread().getName());
        try {
          Thread.sleep(1000); // ы
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        System.out.println("Завершение задачи " + taskId);
      });
    }

    // Завершение работы пула
    pool.shutdown();
    pool.awaitTermination();
    System.out.println("Все задачи завершены");
    System.out.println("=== Завершение задачи с кастомным пулом потоков ===");
  }
}