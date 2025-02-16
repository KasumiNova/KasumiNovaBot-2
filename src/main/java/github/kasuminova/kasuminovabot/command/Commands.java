package github.kasuminova.kasuminovabot.command;

import github.kasuminova.kasuminovabot.KasumiNovaBot2;

import java.util.ArrayList;

public class Commands {
    public static boolean isCommand(String message) {
        if (message.length() <= 2) return false;

        return message.toCharArray()[0] == KasumiNovaBot2.COMMAND_PREFIX;
    }

    public static String getCommand(String message) {
        StringBuilder command = new StringBuilder(8);

        char[] messageChars = message.toCharArray();
        for (int i = 1; i < messageChars.length; i++) {
            if (messageChars[i] == ' ') {
                break;
            } else {
                command.append(messageChars[i]);
            }
        }

        return command.toString();
    }

    public static ArrayList<String> getArgs(String command, int argLength) {
        String[] split = command.split(" ");

        ArrayList<String> args = new ArrayList<>(argLength);

        for (int i = 1; i < split.length; i++) {
            if (i >= argLength) {
                StringBuilder lastArg = new StringBuilder();

                for (int i1 = argLength; i1 < split.length; i1++) {
                    lastArg.append(split[i1]);

                    if (i1 + 1 != split.length) {
                        lastArg.append(' ');
                    }
                }

                args.add(lastArg.toString());
                break;
            } else {
                if (!split[i].isEmpty()) {
                    args.add(split[i]);
                }
            }
        }

        return args;
    }
}
