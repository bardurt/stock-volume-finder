package com.zygne.volfinder.domain.printing;

import com.zygne.volfinder.domain.printing.command.AlignmentCommand;
import com.zygne.volfinder.domain.printing.command.BackgroundColorCommand;
import com.zygne.volfinder.domain.printing.command.ColumnCommand;
import com.zygne.volfinder.domain.printing.command.NewLineCommand;
import com.zygne.volfinder.domain.printing.command.PrinterCommand;
import com.zygne.volfinder.domain.printing.command.TextColorCommand;
import com.zygne.volfinder.domain.printing.command.TextCommand;
import com.zygne.volfinder.domain.printing.command.TextStyleCommand;

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

        private static final String BLACK_BRIGHT = "\033[0;90m";
        private static final String RED_BRIGHT = "\033[0;91m";
        private static final String GREEN_BRIGHT = "\033[0;92m";
        private static final String YELLOW_BRIGHT = "\033[0;93m";
        private static final String BLUE_BRIGHT = "\033[0;94m";
        private static final String PURPLE_BRIGHT = "\033[0;95m";
        private static final String CYAN_BRIGHT = "\033[0;96m";
        private static final String WHITE_BRIGHT = "\033[0;97m";
    }

    private static final class BackgroundColors{

        private static final String BLACK = "\u001B[40m";
        private static final String RED = "\u001B[41m";
        private static final String GREEN = "\u001B[42m";
        private static final String YELLOW = "\u001B[43m";
        private static final String BLUE = "\u001B[44m";
        private static final String PURPLE = "\u001B[45m";
        private static final String CYAN = "\u001B[46m";
        private static final String WHITE = "\u001B[47m";
    }

    private static final class FontStyle{

        private static final String BLACK_UNDERLINED = "\033[4;30m";
        private static final String RED_UNDERLINED = "\033[4;31m";
        private static final String GREEN_UNDERLINED = "\033[4;32m";
        private static final String YELLOW_UNDERLINED = "\033[4;33m";
        private static final String BLUE_UNDERLINED = "\033[4;34m";
        private static final String PURPLE_UNDERLINED = "\033[4;35m";
        private static final String CYAN_UNDERLINED = "\033[4;36m";
        private static final String WHITE_UNDERLINED = "\033[4;37m";

        private static final String BLACK_BOLD = "\033[1;30m";
        private static final String RED_BOLD = "\033[1;31m";
        private static final String GREEN_BOLD = "\033[1;32m";
        private static final String YELLOW_BOLD = "\033[1;33m";
        private static final String BLUE_BOLD = "\033[1;34m";
        private static final String PURPLE_BOLD = "\033[1;35m";
        private static final String CYAN_BOLD = "\033[1;36m";
        private static final String WHITE_BOLD = "\033[1;37m";
    }

    private String backgroundColor = "";
    private String textColor = TextColors.BLACK;
    private String content = "";
    private int columnWidth = -1;

    private Color fontColor = Color.BLACK;
    private Alignment alignment = Alignment.LEFT;
    private TextStyle textStyle = TextStyle.DEFAULT;

    private void setBackgroundColor(Color color) {
        switch (color) {
            case RED -> backgroundColor = ConsolePrinter.BackgroundColors.RED;
            case BLACK -> backgroundColor = ConsolePrinter.BackgroundColors.BLACK;
            case BLUE -> backgroundColor = ConsolePrinter.BackgroundColors.BLUE;
            case YELLOW -> backgroundColor = ConsolePrinter.BackgroundColors.YELLOW;
            case CYAN -> backgroundColor = ConsolePrinter.BackgroundColors.CYAN;
            case PURPLE -> backgroundColor = ConsolePrinter.BackgroundColors.PURPLE;
            case GREEN -> backgroundColor = ConsolePrinter.BackgroundColors.GREEN;
            case WHITE -> backgroundColor = ConsolePrinter.BackgroundColors.WHITE;
            case DEFAULT -> backgroundColor = "";
        }
    }

    private void setTextColor() {

        if(textStyle == TextStyle.DEFAULT) {
            switch (fontColor) {
                case RED -> textColor = ConsolePrinter.TextColors.RED;
                case BLACK -> textColor = ConsolePrinter.TextColors.BLACK;
                case BLUE -> textColor = ConsolePrinter.TextColors.BLUE;
                case YELLOW -> textColor = ConsolePrinter.TextColors.YELLOW;
                case CYAN -> textColor = ConsolePrinter.TextColors.CYAN;
                case PURPLE -> textColor = ConsolePrinter.TextColors.PURPLE;
                case GREEN -> textColor = ConsolePrinter.TextColors.GREEN;
                case WHITE -> textColor = ConsolePrinter.TextColors.WHITE;
                case DEFAULT -> textColor = "";
            }
        } else if(textStyle == TextStyle.UNDERLINE){
            switch (fontColor) {
                case RED -> textColor = ConsolePrinter.FontStyle.RED_UNDERLINED;
                case BLACK -> textColor = ConsolePrinter.FontStyle.BLACK_UNDERLINED;
                case BLUE -> textColor = ConsolePrinter.FontStyle.BLUE_UNDERLINED;
                case YELLOW -> textColor = ConsolePrinter.FontStyle.YELLOW_UNDERLINED;
                case CYAN -> textColor = ConsolePrinter.FontStyle.CYAN_UNDERLINED;
                case PURPLE -> textColor = ConsolePrinter.FontStyle.PURPLE_UNDERLINED;
                case GREEN -> textColor = ConsolePrinter.FontStyle.GREEN_UNDERLINED;
                case WHITE -> textColor = ConsolePrinter.FontStyle.WHITE_UNDERLINED;
                case DEFAULT -> textColor = "";
            }
        } else if(textStyle == TextStyle.BOLD){
            switch (fontColor) {
                case RED -> textColor = ConsolePrinter.FontStyle.RED_BOLD;
                case BLACK -> textColor = ConsolePrinter.FontStyle.BLACK_BOLD;
                case BLUE -> textColor = ConsolePrinter.FontStyle.BLUE_BOLD;
                case YELLOW -> textColor = ConsolePrinter.FontStyle.YELLOW_BOLD;
                case CYAN -> textColor = ConsolePrinter.FontStyle.CYAN_BOLD;
                case PURPLE -> textColor = ConsolePrinter.FontStyle.PURPLE_BOLD;
                case GREEN -> textColor = ConsolePrinter.FontStyle.GREEN_BOLD;
                case WHITE -> textColor = ConsolePrinter.FontStyle.WHITE_BOLD;
                case DEFAULT -> textColor = "";
            }
        }
    }

    private void reset(){
        backgroundColor = "";
        textColor = TextColors.BLACK;
        content = "";
        columnWidth = -1;
        alignment = Alignment.LEFT;
        textStyle = TextStyle.DEFAULT;
        fontColor = Color.DEFAULT;
        System.out.print(TextColors.RESET);
    }

    private String fitText(String source, int width){
        if(source.length() > width){
            return source.substring(0, width-3)+"...";
        }

        return source;
    }

    private String center(String source, int width){

        int padding = (width - source.length()) / 2;

        return " ".repeat(Math.max(0, padding)) +
                fitText(source, width) +
                " ".repeat(Math.max(0, padding));
    }

    private String alignLeft(String source, int width){

        int padding = (width - source.length());


        return fitText(source, width) +
                " ".repeat(Math.max(0, padding));
    }

    private String alignRight(String source, int width){

        int padding = (width - source.length());


        return " ".repeat(Math.max(0, padding)) +
                fitText(source, width);
    }

    private String format(){

        setTextColor();

        String format = backgroundColor + textColor;

        if(columnWidth > 0){
            switch (alignment) {
                case LEFT -> format += alignLeft(content, columnWidth);
                case RIGHT -> format += alignRight(content, columnWidth);
                case CENTER -> format += center(content, columnWidth);
            }

        } else {
            format += content;
        }


        return format;
    }

    @Override
    public void addCommand(PrinterCommand printerCommand) {
        if(printerCommand instanceof TextColorCommand){
            fontColor = ((TextColorCommand) printerCommand).getColor();
        }

        if(printerCommand instanceof BackgroundColorCommand){
            setBackgroundColor(((BackgroundColorCommand) printerCommand).getColor());
        }

        if(printerCommand instanceof TextCommand){
            content = ((TextCommand)printerCommand).getContent();
        }

        if(printerCommand instanceof com.zygne.volfinder.domain.printing.command.RepeatCommand){
            content = ((com.zygne.volfinder.domain.printing.command.RepeatCommand)printerCommand).getContent();
        }

        if(printerCommand instanceof NewLineCommand){
            content += "\n";
        }

        if(printerCommand instanceof ColumnCommand){
            columnWidth = ((ColumnCommand)printerCommand).getWidth();
        }

        if(printerCommand instanceof AlignmentCommand){
            alignment = ((AlignmentCommand)printerCommand).getAlignment();
        }

        if (printerCommand instanceof TextStyleCommand){
            textStyle = ((TextStyleCommand)printerCommand).getTextStyle();
        }
    }

    @Override
    public void print() {
        System.out.print(format());
        reset();
    }

    public static void main(String[] args) {
        Printer printer = new ConsolePrinter();

        printer.addCommand(new BackgroundColorCommand(Color.BLACK));
        printer.addCommand(new TextColorCommand(Color.WHITE));
        printer.addCommand(new TextCommand("123456789"));
        printer.addCommand(new ColumnCommand(8));
        printer.addCommand(new TextStyleCommand(TextStyle.DEFAULT));
        printer.addCommand(new AlignmentCommand(Alignment.LEFT));
        printer.print();
    }
}
