/**Copyright 2022 Zain Aziz (Github ID: zanyaziz)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
 (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, 
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    private static final String FILE_URL = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt";
    private static final String FILE_NAME_WORDLE_WORDS = "english_dictionary_frequent";
    private static final String FILE_NAME_ALL_WORDS = "english_dictionary";
    private static final int maxTurnsCount = 5;
    private static final int wordleWordLength = 5;
    private static final Set<Character> guessedChars = new HashSet<Character>();
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Wordle");
        var wordleWords = readFileFromDisk(FILE_NAME_WORDLE_WORDS);
        var allWords = readFileFromDisk(FILE_NAME_ALL_WORDS);
        var allWordsSet = allWords.stream().collect(Collectors.toSet());
        HashMap<Integer, List<String>> dict = new HashMap<Integer, List<String>>();
        wordleWords.stream().forEach(item -> {
            int size = item.length();
            // System.out.println(item);
            if(!dict.containsKey(size)) {
                dict.put(size, new LinkedList<String>());
            } 
            else {
                var subList = dict.get(size);
                subList.add(item);
            }
        });
        var subList = dict.get(wordleWordLength);
        String word = subList.get(getRandomIndex(0, subList.size()));
        // System.out.println(word);

        int currentTry = 0;
        Scanner readInputText = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Try to guess a five letter word guess");
        while(currentTry < maxTurnsCount) {
            String userInput = readInputText.nextLine();  // Read user input
            
            if (userInput.toLowerCase().equals("exit")) {
                System.out.println("Program Terminated");
                break;
            } 
            else if (userInput.toLowerCase().equals("showmeword")) {
                System.out.println(word);
            }
            else if (userInput.trim().length() != wordleWordLength){
                System.out.println(userInput + " does not match the expected word length of " + wordleWordLength + ".");
            }
            else if(!allWordsSet.contains(userInput.trim())) {
                System.out.println(userInput + " is not a valid word.");
            }
             else if (allWordsSet.contains(userInput.trim()) && userInput.trim().length() == wordleWordLength) {
                currentTry++;
                for(char character: userInput.toCharArray()){
                    if(word.contains(character + "")) {
                        guessedChars.add(character);
                    }
                }
                System.out.println("Correctly Guessed Characters: " + guessedChars);
                for(int i = 0; i< userInput.length(); i++) {
                    if(userInput.toCharArray()[i] == word.toCharArray()[i]) {
                        System.out.print(" " + userInput.toCharArray()[i] + " ");
                    } else {
                        System.out.print(" _ ");
                    }
                }
                System.out.println("");
            }
            else {
                System.out.println("Unanticipated input from user.");
            }

            if(word.equals(userInput.trim())) {
                System.out.println("Congratulations you have guessed the word.");
                break;
            }

            System.out.println("You have " + (maxTurnsCount - currentTry) + " more tries remaining..");
            System.out.println("_  _  _  _  _");
        }
        
        if(currentTry == maxTurnsCount) {
            System.out.println("You have run out of turns trying to guess the word " + word + ".");
            System.out.println("Thank you for playing. See you next time...");
        }

    }

    private static int getRandomIndex(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static List<String> readFileFromDisk(String fileName) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        List<String> list = new LinkedList<String>();
        try {
            inputStream = new FileInputStream("./" + fileName);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                list.add(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }  
        }
        return list;
    }

    private static void readAndWriteFileFromGitHubRepo() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME_ALL_WORDS)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // handle exception
            }
    }
}
