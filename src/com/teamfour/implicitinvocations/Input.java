package com.teamfour.implicitinvocations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Input {
    File inputFile;

    public Input() {
        inputFile = new File("input.txt");
    }

    public Input(String pathName) {
        inputFile = new File(pathName);
    }

    public void readInto(LineStorage lineStorage) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineStorage.addLine(line);
            }

        } catch (FileNotFoundException e) {
            System.exit(1);
        }finally{
            scanner.close();
        }
    }
}