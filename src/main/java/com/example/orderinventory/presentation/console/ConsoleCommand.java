package com.example.orderinventory.presentation.console;

import java.util.List;

public interface ConsoleCommand {

    String name();

    String description();

    String execute(List<String> arguments);
}

