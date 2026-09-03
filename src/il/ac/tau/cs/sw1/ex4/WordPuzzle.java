package il.ac.tau.cs.sw1.ex4;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class WordPuzzle {
    public static final char HIDDEN_CHAR = '_';
    public static final int MAX_VOCABULARY_SIZE = 3000;

    public static String[] scanVocabulary(Scanner scanner) {
        String[] vocabulary = new String[MAX_VOCABULARY_SIZE];
        scanner.useDelimiter(" ");
        int count = 0;
        while (scanner.hasNext() && count < MAX_VOCABULARY_SIZE) {
            vocabulary[count] = scanner.next();
            count++;
        }

        Arrays.sort(vocabulary, 0, count);
        int uniqueCount = 0;
        for (int i = 0; i < count; i++) {
            if (i == 0 || !vocabulary[i].equals(vocabulary[i - 1])) {
                vocabulary[uniqueCount] = vocabulary[i];
                uniqueCount++;
            }
        }
        return Arrays.copyOf(vocabulary, uniqueCount);
    }

    public static boolean isInVocabulary(String[] vocabulary, String word) {
        int low = 0;
        int high = vocabulary.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int comparison = vocabulary[mid].compareTo(word);
            if (comparison < 0) {
                low = mid + 1;
            } else if (comparison > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isLegalPuzzleStructure(char[] puzzle) {
        int hiddenCount = 0;
        for (char value : puzzle) {
            if (value == HIDDEN_CHAR) {
                hiddenCount++;
            } else if (value < 'a' || value > 'z') {
                return false;
            }
        }
        return hiddenCount != 0;
    }

    public static int countHiddenInPuzzle(char[] puzzle) {
        int count = 0;
        for (char value : puzzle) {
            if (value == HIDDEN_CHAR) {
                count++;
            }
        }
        return count;
    }

    public static boolean checkSolution(char[] puzzle, String word, String[] vocabulary) {
        if (puzzle.length != word.length()) {
            return false;
        }

        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] != HIDDEN_CHAR) {
                if (puzzle[i] != word.charAt(i)) {
                    return false;
                }
                for (int j = 0; j < puzzle.length; j++) {
                    if (puzzle[j] == HIDDEN_CHAR && word.charAt(j) == puzzle[i]) {
                        return false;
                    }
                }
            }
        }
        return isInVocabulary(vocabulary, word);
    }

    public static String getSolution(char[] puzzle, String[] vocabulary) {
        int solutionCount = 0;
        String solution = null;
        for (String word : vocabulary) {
            if (checkSolution(puzzle, word, vocabulary)) {
                if (solutionCount == 1) {
                    return null;
                }
                solution = word;
                solutionCount++;
            }
        }
        return solution;
    }

    public static int applyGuess(char guess, String solution, char[] puzzle) {
        int changes = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == HIDDEN_CHAR && solution.charAt(i) == guess) {
                changes++;
                puzzle[i] = guess;
            }
        }
        return changes;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No specified file path for vocabulary, closing program.");
            System.exit(0);
        }

        File vocabularyFile = new File(args[0]);
        Scanner vocabularyScanner = new Scanner(vocabularyFile);
        String[] vocabulary = scanVocabulary(vocabularyScanner);
        vocabularyScanner.close();
        printReadVocabulary(args[0], vocabulary.length);

        printSettingsMessage();
        Scanner puzzleScanner = new Scanner(System.in);
        printEnterPuzzleMessage();
        char[] puzzle = puzzleScanner.next().toCharArray();
        while (!isLegalPuzzleStructure(puzzle) || getSolution(puzzle, vocabulary) == null) {
            if (!isLegalPuzzleStructure(puzzle)) {
                printIllegalPuzzleMessage();
            } else {
                printIllegalSolutionsNumberMessage();
            }
            printEnterPuzzleMessage();
            puzzle = puzzleScanner.next().toCharArray();
        }

        printGameStageMessage();
        int tries = countHiddenInPuzzle(puzzle) + 3;
        String solution = getSolution(puzzle, vocabulary);
        Scanner guessScanner = new Scanner(System.in);
        while (tries > 0) {
            printPuzzle(puzzle);
            printEnterYourGuessMessage();
            char guess = guessScanner.next().charAt(0);
            tries--;
            if (applyGuess(guess, solution, puzzle) == 0) {
                printWrongGuess(tries);
            } else if (new String(puzzle).equals(solution)) {
                printWinMessage();
                System.exit(0);
            } else {
                printCorrectGuess(tries);
            }
        }

        puzzleScanner.close();
        guessScanner.close();
        printGameOver();
    }

    /*************************************************************/
    /*********************  Don't change this ********************/
    /*************************************************************/

    public static void printReadVocabulary(String vocabularyFileName, int numOfWords) {
        System.out.println("Read " + numOfWords + " words from " + vocabularyFileName);
    }

    public static void printSettingsMessage() {
        System.out.println("--- Settings stage ---");
    }

    public static void printEnterPuzzleMessage() {
        System.out.println("Enter your puzzle:");
    }

    public static void printIllegalPuzzleMessage() {
        System.out.println("Illegal puzzle, try again!");
    }

    public static void printIllegalSolutionsNumberMessage() {
        System.out.println("Puzzle doesn't have a single solution, try again!");
    }

    public static void printGameStageMessage() {
        System.out.println("--- Game stage ---");
    }

    public static void printPuzzle(char[] puzzle) {
        System.out.println(puzzle);
    }

    public static void printEnterYourGuessMessage() {
        System.out.println("Enter your guess:");
    }

    public static void printCorrectGuess(int attemptsNum) {
        System.out.println("Correct Guess, " + attemptsNum + " guesses left");
    }

    public static void printWrongGuess(int attemptsNum) {
        System.out.println("Wrong Guess, " + attemptsNum + " guesses left");
    }

    public static void printWinMessage() {
        System.out.println("Congratulations! You solved the puzzle");
    }

    public static void printGameOver() {
        System.out.println("Game over!");
    }
}
