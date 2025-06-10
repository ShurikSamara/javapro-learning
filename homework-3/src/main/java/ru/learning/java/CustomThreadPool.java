package ru.learning.java;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPool {
  private final LinkedList<Runnable> taskQueue;
  private final Thread[] workers;
  private volatile boolean isShutdown = false;
  private final AtomicInteger activeThreads;
  private final Object shutdownLock = new Object();
  private final Object terminationLock = new Object();

  public CustomThreadPool(int poolSize) {
    if (poolSize <= 0) {
      throw new IllegalArgumentException("Размер пула должен быть положительным числом");
    }

    this.taskQueue = new LinkedList<>();
    this.workers = new Thread[poolSize];
    this.activeThreads = new AtomicInteger(poolSize);

    // Создание и запуск рабочих потоков
    for (int i = 0; i < poolSize; i++) {
      workers[i] = new Worker(i);
      workers[i].start();
    }
  }

  public void execute(Runnable task) {
    if (task == null) {
      throw new NullPointerException("Задача не может быть null");
    }

    synchronized (shutdownLock) {
      if (isShutdown) {
        throw new IllegalStateException("ThreadPool завершает работу, новые задачи не принимаются");
      }
      synchronized (taskQueue) {
        taskQueue.addLast(task);
        taskQueue.notify(); // Пробуждаем один ожидающий поток
      }
    }
  }

  public void shutdown() {
    synchronized (taskQueue) {
      if (isShutdown) {
        return; // Уже завершается
      }
      isShutdown = true;
      taskQueue.notifyAll(); // Пробуждаем все ожидающие потоки
    }
  }


  public void awaitTermination() throws InterruptedException {
    synchronized (terminationLock) {
      while (activeThreads.get() > 0) {
        terminationLock.wait();
      }
    }
  }

  /**
   * Проверяет, завершён ли пул потоков
   */
  public boolean isTerminated() {
    synchronized (taskQueue) {
      return isShutdown && activeThreads.get() == 0;
    }
  }

  /**
   * Проверяет, начат ли процесс завершения
   */
  public boolean isShutdown() {
    return isShutdown;
  }

  /**
   * Возвращает количество активных потоков
   */
  public int getActiveThreadCount() {
    return activeThreads.get();
  }

  /**
   * Возвращает количество задач в очереди
   */
  public int getQueueSize() {
    synchronized (taskQueue) {
      return taskQueue.size();
    }
  }

  private void decrementActiveThreadCount() {
    if (activeThreads.decrementAndGet() == 0) {
      synchronized (terminationLock) {
        terminationLock.notifyAll();
      }
    }
  }

  private class Worker extends Thread {

    public Worker(int workerId) {
      super("CustomThreadPool-Worker-" + workerId);
      setDaemon(false); // Явно определяем, что это не демон-поток
    }

    @Override
    public void run() {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          Runnable task = getNextTask();
          if (task == null) {
            break; // Завершаем работу потока
          }

          try {
            task.run();
          } catch (Exception e) {
            // Логируем ошибку, но продолжаем работу потока
            System.err.println("Ошибка при выполнении задачи в потоке " +
              Thread.currentThread().getName() + ": " + e.getMessage());
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
            Thread.currentThread().interrupt();
            return null; // Завершаем при прерывании
          }
        }

        // Если очередь пуста и пул завершается, возвращаем null
        if (taskQueue.isEmpty()) {
          return null;
        }

        return taskQueue.removeFirst();
      }
    }
  }
}