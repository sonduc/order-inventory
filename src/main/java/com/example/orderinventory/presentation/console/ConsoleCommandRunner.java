package com.example.orderinventory.presentation.console;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsoleCommandRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ConsoleCommandRunner.class);

    private final boolean enabled;
    private final String commandName;
    private final List<String> arguments;
    private final Map<String, ConsoleCommand> commands;

    public ConsoleCommandRunner(
            @Value("${app.console.enabled:false}") boolean enabled,
            @Value("${app.console.command:}") String commandName,
            @Value("${app.console.arguments:}") List<String> arguments,
            List<ConsoleCommand> commands
    ) {
        this.enabled = enabled;
        this.commandName = commandName;
        this.arguments = arguments;
        this.commands = commands.stream().collect(Collectors.toMap(ConsoleCommand::name, Function.identity()));
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled || commandName == null || commandName.isBlank()) {
            return;
        }

        ConsoleCommand command = commands.get(commandName);
        if (command == null) {
            log.warn("Unknown console command '{}'. Available commands: {}", commandName, commands.keySet());
            return;
        }

        log.info("Console command '{}' executed. Result: {}", commandName, command.execute(arguments));
    }
}

