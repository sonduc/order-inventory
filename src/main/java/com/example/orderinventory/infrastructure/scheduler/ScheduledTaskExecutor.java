package com.example.orderinventory.infrastructure.scheduler;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskExecutor {

    private final DistributedLockManager distributedLockManager;

    public ScheduledTaskExecutor(DistributedLockManager distributedLockManager) {
        this.distributedLockManager = distributedLockManager;
    }

    public void runLocked(String taskName, Runnable task) {
        distributedLockManager.executeWithLock(taskName, task);
    }
}

