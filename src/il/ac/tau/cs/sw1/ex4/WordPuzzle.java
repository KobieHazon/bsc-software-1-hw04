package il.ac.tau.cs.sw1.ex4;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class WordPuzzle {
	public static final char HIDDEN_CHAR = '_';
	public static final int MAX_VOCABULARY_SIZE = 3000;
	
	
	public static String[] scanVocabulary(Scanner scanner){          // Q - 1
		String[] vocab = new String[MAX_VOCABULARY_SIZE];
		scanner.useDelimiter(" ");
		int i = 0;
		while (scanner.hasNext() && i < MAX_VOCABULARY_SIZE) { 
			vocab[i] = scanner.next();
			i++;
		}
		Arrays.sort(vocab, 0, i);
		int cnt = 0;
		for (int j = 0; j < vocab.length-1; j++) {
			if (vocab[j] != null && !vocab[j].equals(vocab[j+1])) {
				cnt++;
			}
			else {
				vocab[j] = null;
			}
		}
		String[] retVocab = new String[cnt];
		i = 0;
		for (int j = 0; j < vocab.length; j++) {
			if (vocab[j] != null) {
				retVocab[i] = vocab[j];
				i++;
			}
		}
		return retVocab;
	}
	
	
	
	public static boolean isInVocabulary(String[] vocabulary, String word){ // Q - 2
		int low = 0;
        int high = vocabulary.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (vocabulary[mid].compareTo(word) < 0) {
                low = mid + 1;
            } else if (vocabulary[mid].compareTo(word) > 0) {
                high = mid - 1;
            } else {
                return true;
            }
        }
        return false;
	}

	
	public static boolean isLegalPuzzleStructure(char[] puzzle){  // Q - 3
		int cnt = 0;
		for (int i = 0; i < puzzle.length; i++) {
			if (puzzle[i] != HIDDEN_CHAR) {
				if (puzzle[i] < 'a' || puzzle[i] > 'z') {
					return false;
				}
			}
			else {
				cnt++;
			}
		}
		return (cnt != 0);
	}
	
	
	public static int countHiddenInPuzzle(char[] puzzle){ // Q - 4
		int cnt = 0;
		for (int i = 0; i < puzzle.length; i++) {
			if (puzzle[i] == HIDDEN_CHAR) {
				cnt++;
			}
		}
		return cnt;
	}
	
	
	public static boolean checkSolution(char[] puzzle, String word, String[] vocabulary){ // Q - 5
		if (puzzle.length != word.length()) {
			return false;
		}
		for (int i = 0; i < puzzle.length; i++) {
			if (puzzle[i] != HIDDEN_CHAR) {
				if (puzzle[i] != word.charAt(i)) {
					return false;
				}
				else {
					for (int j = 0; j < puzzle.length; j++) {
						if (puzzle[j] == HIDDEN_CHAR && word.charAt(j) == puzzle[i]) {
							return false;
						}
					}
				}
			}
		}
		if (isInVocabulary(vocabulary, word)) {
			return true;
		}
		return false;
	}
	
	
	public static String getSolution(char[] puzzle, String[] vocabulary){ // Q - 6
		int cntSol = 0;
		String sol = null;
		for (int i = 0; i < vocabulary.length; i++) {
			if (checkSolution(puzzle, vocabulary[i], vocabulary)) {
				if (cntSol == 1) {
					return null;
				}
				sol = vocabulary[i];
				cntSol++;
			}
		}
		return sol;
	}
	
	
	public static int applyGuess(char guess, String solution, char[] puzzle){ // Q - 7
		int changes = 0;
		for (int i = 0; i < puzzle.length; i++) {
			if (puzzle[i] == HIDDEN_CHAR && solution.charAt(i) == guess) {
				changes++;
				puzzle[i] = guess;
			}
		}
		return changes;
	}


	public static void main(String[] args) throws Exception{ //Q - 8
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
		Scanner getPuzzle = new Scanner(System.in);
		printEnterPuzzleMessage();
		char[] puzzle = getPuzzle.next().toCharArray();
		while (!isLegalPuzzleStructure(puzzle) || getSolution(puzzle, vocabulary) == null) {
			if (!isLegalPuzzleStructure(puzzle)) {
				printIllegalPuzzleMessage();
			}
			else {
				printIllegalSolutionsNumberMessage();
			}
			printEnterPuzzleMessage();
			puzzle = getPuzzle.next().toCharArray();
		}
		

		printGameStageMessage();
		int tries = countHiddenInPuzzle(puzzle) + 3;
		String solution = getSolution(puzzle, vocabulary);
		Scanner getGuess = new Scanner(System.in);
		while (tries > 0) {
			printPuzzle(puzzle);
			printEnterYourGuessMessage();
			char guess = getGuess.next().charAt(0);
			tries--;
			if (applyGuess(guess, solution, puzzle) == 0) {
				printWrongGuess(tries);
			}
			else {
				if (new String(puzzle).equals(solution)) {
					printWinMessage();
					System.exit(0);
				}
				else {
					printCorrectGuess(tries);
				}
			}
			
		}
		getPuzzle.close();
		getGuess.close();
		printGameOver();
	}


	/*************************************************************/
	/*********************  Don't change this ********************/
	/*************************************************************/
	
	public static void printReadVocabulary(String vocabularyFileName, int numOfWords){
		System.out.println("Read " + numOfWords + " words from " + vocabularyFileName);
	}

	public static void printSettingsMessage(){
		System.out.println("--- Settings stage ---");
	}
	
	public static void printEnterPuzzleMessage(){
		System.out.println("Enter your puzzle:");
	}
	
	public static void printIllegalPuzzleMessage(){
		System.out.println("Illegal puzzle, try again!");
	}
	
	public static void printIllegalSolutionsNumberMessage(){
		System.out.println("Puzzle doesn't have a single solution, try again!");
	}
	
	
	public static void printGameStageMessage(){
		System.out.println("--- Game stage ---");
	}
	
	public static void printPuzzle(char[] puzzle){
		System.out.println(puzzle);
	}
	
	public static void printEnterYourGuessMessage(){
		System.out.println("Enter your guess:");
	}
	
	public static void printCorrectGuess(int attemptsNum){
		System.out.println("Correct Guess, " + attemptsNum + " guesses left");
	}
	
	public static void printWrongGuess(int attemptsNum){
		System.out.println("Wrong Guess, " + attemptsNum + " guesses left");
	}

	public static void printWinMessage(){
		System.out.println("Congratulations! You solved the puzzle");
	}
	
	public static void printGameOver(){
		System.out.println("Game over!");
	}

}
