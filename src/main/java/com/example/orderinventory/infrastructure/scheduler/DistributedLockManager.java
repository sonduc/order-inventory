package com.example.orderinventory.infrastructure.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

@Component
public class DistributedLockManager {

    private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void executeWithLock(String lockName, Runnable task) {
        ReentrantLock lock = locks.computeIfAbsent(lockName, ignored -> new ReentrantLock());
        lock.lock();
        try {
            task.run();
        } finally {
            lock.unlock();
        }
    }
}

