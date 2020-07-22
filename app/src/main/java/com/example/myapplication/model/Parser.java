package com.example.myapplication.model;

import java.util.regex.Pattern;

public class Parser {

    private final String input;
    private final String data;
    private final int timeOut;

    public int getTimeOut() {
        return timeOut;
    }

    public Parser(String input) throws Exception {
        if (input == null)
            throw new Exception("null input!");

        this.input = input;

        String s1 = input.trim().substring(1, input.trim().length() - 1).trim();
        String val = s1.substring(s1.lastIndexOf('{') + 1).trim();
        String s2 = s1.substring(0, s1.lastIndexOf(',')).trim();
        this.data = s2.substring(0, s2.lastIndexOf('}')).trim();

        String tm = "";
        for (int i = 0; i < val.length(); i++) {
            if (val.charAt(i) != ' ')
                tm += val.charAt(i);
        }
        this.timeOut = Integer.parseInt(tm);
    }

    public String[] getParsedInput() {
        int sentencesNum = 0;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '[')
                sentencesNum++;
        }
        String[] sentences = new String[sentencesNum];
        String subSentence = data;
        for (int i = 0; i < sentencesNum; i++) {
            sentences[i] = subSentence.substring(1, subSentence.indexOf(']')).trim();
            if (subSentence.indexOf('[', subSentence.indexOf(']')) != -1)
                subSentence = subSentence.substring(subSentence.indexOf('[', subSentence.indexOf(']'))).trim();
        }

        String[] parsedInput = new String[sentencesNum];
        String subSen;
        for (int i = 0; i < sentencesNum; i++) {
            subSen = sentences[i];
            parsedInput[i] = "";
            for (int j = 0; j < 4; j++) {
                parsedInput[i] += subSen.substring(1, subSen.indexOf('}')).trim() + "-";
                if (subSen.indexOf('{', subSen.indexOf('}')) != -1)
                    subSen = subSen.substring(subSen.indexOf('{', subSen.indexOf('}'))).trim();
            }
        }

        return parsedInput;
    }

    public Question[] getQuestions() {
        String[] parsedInput = getParsedInput();

        Question[] questions = new Question[parsedInput.length];
        for (int i = 0; i < parsedInput.length; i++) {
            String[] s = parsedInput[i].split("-");
            questions[i] = new Question(
                    s[0].substring(1, s[0].length() - 1),
                    Boolean.parseBoolean(s[1].toLowerCase()),
                    Boolean.parseBoolean(s[2].toLowerCase()),
                    Color.valueOf(s[3].toUpperCase())
            );
        }

        return questions;
    }

    public boolean checkFormatAndValidation() {
        String pattern = "\\{" +
                "(\\[\\{(\\D|\\d){15,}\\},(\\{(true|false)\\},){2}\\{(BLACK|RED|BLUE|GREEN)\\}\\],?)*\\}" +
                ",\\{\\d{2,3}\\}";
        return Pattern.matches(pattern, input);
    }
}
