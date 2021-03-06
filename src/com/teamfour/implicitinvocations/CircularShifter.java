package com.teamfour.implicitinvocations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import com.teamfour.utilities.StringUtils;

public class CircularShifter implements Observer {
    private static final String WHITESPACE = " ";

    private LineStorage shiftedLines;
    private ArrayList<String> ignoredWords;

    public CircularShifter(LineStorage shiftedLines) {
        this.shiftedLines = shiftedLines;
        File ignoredWordsFile = new File("ignoredWords.txt");
        getIgnoredWords(ignoredWordsFile);
    }

    public CircularShifter(LineStorage shiftedLines, String pathName) {
        this.shiftedLines = shiftedLines;
        File ignoredWordsFile = new File(pathName);
        getIgnoredWords(ignoredWordsFile);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof LineStorage)) {
            return;
        }

        LineStorage originalLines = (LineStorage) o;
        ArrayList<List<String>> shiftedWordsList = getListOfShiftedWords(originalLines);
        filterListWithIgnoredWords(shiftedWordsList);
    }

    private void getIgnoredWords(File ignoredWordsFile) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(ignoredWordsFile);
            ignoredWords = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                ignoredWords.add(word);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private void filterListWithIgnoredWords(ArrayList<List<String>> shiftedWordsList) {
        for (List<String> sw : shiftedWordsList) {
            String firstWord = sw.get(0).toLowerCase();

            if (!ignoredWords.contains(firstWord)) {
                String line = buildStringFromWords(sw);
                shiftedLines.addLine(line);
            }
        }
    }

    private String buildStringFromWords(List<String> sw) {
        // iterate through words to build a string
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sw.size(); i++) {
            String word = sw.get(i);
            sb.append(word);

            // add whitespace for all words. Last word is excluded
            if (i != sw.size() - 1) {
                sb.append(WHITESPACE);
            }
        }
        return sb.toString();
    }

    private ArrayList<List<String>> getListOfShiftedWords(LineStorage lineStorage) {
        String line = lineStorage.getLine();
        String[] words = StringUtils.tokenize(line);
        ArrayList<List<String>> shiftedWordsList = new ArrayList<>();

        int numOfWords = words.length;
        int lastIndex = words.length - 1;

        // make a new copy of current string array
        String[] shiftedWords = new String[numOfWords];
        System.arraycopy(words, 0, shiftedWords, 0, numOfWords);

        // add original line to list
        shiftedWordsList.add(Arrays.asList(shiftedWords));

        // add first word to last pos and shift remaining words to the left
        for (int i = 0; i < numOfWords - 1; i++) {
            String firstWord = words[0];

            for (int j = 1; j < numOfWords; j++) {
                words[j - 1] = words[j];
            }

            words[lastIndex] = firstWord;

            // make a new copy of current string array and add to list
            shiftedWords = new String[numOfWords];
            System.arraycopy(words, 0, shiftedWords, 0, numOfWords);
            shiftedWordsList.add(Arrays.asList(shiftedWords));
        }
        return shiftedWordsList;
    }

}