package com.zygne.stockalyze.presentation.printing;

public class ConsolePrinter implements Printer {

    private static final class TextColors {
        private static final String RESET = "\u001B[0m";
        private static final String BLACK = "\u001B[30m";
        private static final String RED = "\u001B[31m";
        private static final String GREEN = "\u001B[32m";
        private static final String YELLOW = "\u001B[33m";
        private static final String BLUE = "\u001B[34m";
        private static final String PURPLE = "\u001B[35m";
        private static final String CYAN = "\u001B[36m";
        private static final String WHITE = "\u001B[37m";

        private static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        private static final String RED_BRIGHT = "\033[0;91m";    // RED
        private static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        private static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        private static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        private static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        private static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        private static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
    }


    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private String backgroundColor = "";
    private String textColor = TextColors.BLACK;

    @Override
    public void setBackgroundColor(Color color) {
        switch (color) {
            case RED:
                backgroundColor = ANSI_RED_BACKGROUND;
                break;
            case BLACK:
                backgroundColor = ANSI_BLACK_BACKGROUND;
                break;
            case BLUE:
                backgroundColor = ANSI_BLUE_BACKGROUND;
                break;
            case YELLOW:
                backgroundColor = ANSI_YELLOW_BACKGROUND;
                break;
            case CYAN:
                backgroundColor = ANSI_CYAN_BACKGROUND;
                break;
            case PURPLE:
                backgroundColor = ANSI_PURPLE_BACKGROUND;
                break;
            case GREEN:
                backgroundColor = ANSI_GREEN_BACKGROUND;
                break;
        }
    }

    @Override
    public void setTextColor(Color color) {
        switch (color) {
            case RED:
                textColor = TextColors.RED;
                break;
            case BLACK:
                textColor = TextColors.BLACK;
                break;
            case BLUE:
                textColor = TextColors.BLUE;
                break;
            case YELLOW:
                textColor = TextColors.YELLOW;
                break;
            case CYAN:
                textColor = TextColors.CYAN;
                break;
            case PURPLE:
                textColor = TextColors.PURPLE;
                break;
            case GREEN:
                textColor = TextColors.GREEN;
                break;
        }
    }

    @Override
    public void print(String value) {
        System.out.print(backgroundColor + textColor + value);
        backgroundColor = "";
        textColor = TextColors.BLACK;
        System.out.print(TextColors.RESET);
    }
}
