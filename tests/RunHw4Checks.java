import il.ac.tau.cs.sw1.ex4.WordPuzzle;

import java.util.Arrays;
import java.util.Scanner;

public class RunHw4Checks {
    public static void main(String[] args) {
        testScanVocabulary();
        testVocabularySearch();
        testPuzzleStructure();
        testSolutionChecks();
        testApplyGuess();
        System.out.println("16 Java checks passed");
    }

    private static void testScanVocabulary() {
        Scanner scanner = new Scanner("pear apple pear banana apple");
        String[] vocabulary = WordPuzzle.scanVocabulary(scanner);
        assertArrayEquals(new String[] {"apple", "banana", "pear"}, vocabulary, "sorted unique vocabulary");

        StringBuilder manyWords = new StringBuilder();
        for (int i = 0; i < WordPuzzle.MAX_VOCABULARY_SIZE + 5; i++) {
            if (i > 0) {
                manyWords.append(' ');
            }
            manyWords.append(String.format("w%04d", i));
        }
        String[] capped = WordPuzzle.scanVocabulary(new Scanner(manyWords.toString()));
        assertEquals(WordPuzzle.MAX_VOCABULARY_SIZE, capped.length, "vocabulary cap");
    }

    private static void testVocabularySearch() {
        String[] vocabulary = {"apple", "banana", "pear"};
        assertEquals(true, WordPuzzle.isInVocabulary(vocabulary, "banana"), "known word");
        assertEquals(false, WordPuzzle.isInVocabulary(vocabulary, "orange"), "unknown word");
    }

    private static void testPuzzleStructure() {
        assertEquals(true, WordPuzzle.isLegalPuzzleStructure(new char[] {'a', WordPuzzle.HIDDEN_CHAR, 'b'}), "legal puzzle");
        assertEquals(false, WordPuzzle.isLegalPuzzleStructure(new char[] {'a', 'b'}), "no hidden letters");
        assertEquals(false, WordPuzzle.isLegalPuzzleStructure(new char[] {'a', '-', WordPuzzle.HIDDEN_CHAR}), "illegal character");
        assertEquals(2, WordPuzzle.countHiddenInPuzzle(new char[] {'a', WordPuzzle.HIDDEN_CHAR, WordPuzzle.HIDDEN_CHAR}), "hidden count");
    }

    private static void testSolutionChecks() {
        String[] vocabulary = {"other", "while", "world"};
        assertEquals(true, WordPuzzle.checkSolution(new char[] {'w', WordPuzzle.HIDDEN_CHAR, 'i', WordPuzzle.HIDDEN_CHAR, 'e'}, "while", vocabulary), "valid solution");
        assertEquals(false, WordPuzzle.checkSolution(new char[] {'w', WordPuzzle.HIDDEN_CHAR, 'i'}, "while", vocabulary), "length mismatch");
        assertEquals(false, WordPuzzle.checkSolution(new char[] {'w', WordPuzzle.HIDDEN_CHAR, 'i', WordPuzzle.HIDDEN_CHAR, 'e'}, "white", vocabulary), "not in vocabulary");
        assertEquals(false, WordPuzzle.checkSolution(new char[] {'w', WordPuzzle.HIDDEN_CHAR, 'i', WordPuzzle.HIDDEN_CHAR, 'e'}, "wiiie", vocabulary), "hidden reuses visible letter");
        assertEquals("while", WordPuzzle.getSolution(new char[] {'w', WordPuzzle.HIDDEN_CHAR, 'i', WordPuzzle.HIDDEN_CHAR, 'e'}, vocabulary), "unique solution");
        assertEquals(null, WordPuzzle.getSolution(new char[] {WordPuzzle.HIDDEN_CHAR, WordPuzzle.HIDDEN_CHAR, 'r', 'l', 'd'}, new String[] {"world", "xorld"}), "ambiguous solution");
    }

    private static void testApplyGuess() {
        char[] puzzle = {'w', WordPuzzle.HIDDEN_CHAR, 'i', WordPuzzle.HIDDEN_CHAR, 'e'};
        assertEquals(1, WordPuzzle.applyGuess('h', "while", puzzle), "changed one position");
        assertArrayEquals(new String[] {"w", "h", "i", "_", "e"}, toStrings(puzzle), "puzzle after h");
        assertEquals(0, WordPuzzle.applyGuess('i', "while", puzzle), "visible letter unchanged");
        assertEquals(1, WordPuzzle.applyGuess('l', "while", puzzle), "complete puzzle");
    }

    private static String[] toStrings(char[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = String.valueOf(values[i]);
        }
        return result;
    }

    private static void assertEquals(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + " expected " + expected + " but got " + actual);
        }
    }

    private static void assertArrayEquals(String[] expected, String[] actual, String label) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(label + " expected " + Arrays.toString(expected) + " but got " + Arrays.toString(actual));
        }
    }
}
