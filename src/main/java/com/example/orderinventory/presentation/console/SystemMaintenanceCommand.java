package com.example.orderinventory.presentation.console;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class SystemMaintenanceCommand implements ConsoleCommand {

    @Override
    public String name() {
        return "system:ping";
    }

    @Override
    public String description() {
        return "Return a simple maintenance heartbeat.";
    }

    @Override
    public String execute(java.util.List<String> arguments) {
        return "System heartbeat at %s".formatted(Instant.now());
    }
}
