package ru.learning.java;

import java.util.LinkedList;

public class CustomThreadPool {
  private final LinkedList<Runnable> taskQueue;
  private boolean isShutdown = false;
  private volatile int activeThreads;
  private final Object shutdownLock = new Object();

  public CustomThreadPool(int poolSize) {
    if (poolSize <= 0) {
      throw new IllegalArgumentException("Значение для размера pool не должно быть отрицательным или равным нулю");
    }

    Worker[] workers = new Worker[poolSize];
    this.taskQueue = new LinkedList<>();
    this.activeThreads = poolSize;

    // Создание и запуск рабочих потоков
    for (int i = 0; i < poolSize; i++) {
      workers[i] = new Worker("Worker-" + i);
      workers[i].start();
    }
  }

  public void execute(Runnable task) {
    if (task == null) {
      throw new NullPointerException("Задача не должна быть null");
    }

    synchronized (taskQueue) {
      if (isShutdown) {
        throw new IllegalStateException("Завершение ThreadPool");
      }

      taskQueue.addLast(task);
      taskQueue.notify(); // Пробуждение одного ожидающего потока
    }
  }

  public void shutdown() {
    synchronized (taskQueue) {
      if (isShutdown) {
        return; // Процесс завершения
      }
      isShutdown = true;
      taskQueue.notifyAll(); // Пробуждение всех ожидающих потоков
    }
  }

  private void decrementActiveThreadCount() {
    synchronized (shutdownLock) {
      activeThreads--;
      if (activeThreads == 0) {
        shutdownLock.notifyAll();
      }
    }
  }

  public void awaitTermination() throws InterruptedException {
    synchronized (shutdownLock) {
      while (activeThreads > 0) {
        shutdownLock.wait();
      }
    }
  }

  private class Worker extends Thread {
    private final String name;

    public Worker(String name) {
      this.name = name;
      setName(name);
    }

    @Override
    public void run() {
      try {
        while (true) {
          Runnable task = getNextTask();
          if (task == null) {
            break; // Завершаем работу
          }

          try {
            task.run();
          } catch (Exception e) {
            // Логирование ошибки, но нужно продолжить работу потока
            System.err.println("Выполнение задачи не удалось " + name + ": " + e.getMessage());
          }
        }
      } finally {
        decrementActiveThreadCount();
      }
    }

    private Runnable getNextTask() {
      synchronized (taskQueue) {
        while (taskQueue.isEmpty() && !isShutdown) {
          try {
            taskQueue.wait();
          } catch (InterruptedException e) {
            return null;
          }
        }

        if (taskQueue.isEmpty()) {
          return null; // Сигнал к завершению
        }
        return taskQueue.removeFirst();
      }
    }
  }
}