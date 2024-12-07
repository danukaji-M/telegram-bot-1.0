package com.ruufilms.enums;

/**
 * Enum representing the bot commands.
 */
public enum Commands {
    START("/start"),
    END("/end"),
    SETFILM("/setfilm"),
    SETSERIES("/setseries"),
    SEASON("/season"),
    NEW("/new"),
    OLD("/old"),
    OLDALL("/oldall"),
    SEARCH("/search"),
    SETNAME("/setname"),
    RESET("/reset"),
    CREATE("/create");

    private final String command;

    /**
     * Constructor to initialize the enum with the command string.
     *
     * @param command The string representation of the command.
     */
    Commands(String command) {
        this.command = command;
    }

    /**
     * Gets the string representation of the command.
     *
     * @return The command string.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Finds the corresponding enum value from a string command.
     *
     * @param command The string command to match.
     * @return The corresponding {@code Commands} enum value.
     * @throws IllegalArgumentException If the command does not match any enum value.
     */
    public static Commands fromString(String command) {
        for (Commands cmd : Commands.values()) {
            if (cmd.getCommand().equalsIgnoreCase(command)) {
                return cmd;
            }
        }
        throw new IllegalArgumentException("Invalid command: " + command);
    }
}
