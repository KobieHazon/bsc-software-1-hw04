# Software 1 - Homework 4

A 2018 CS BSc Java assignment submission implementing an interactive word-puzzle game and supporting utility methods under `il.ac.tau.cs.sw1.ex4`. The project includes a small tester and two text fixtures.

## Behavior

`WordPuzzle` loads a vocabulary, validates puzzle patterns, finds whether a pattern has a single legal solution, and runs a small interactive guessing game. Hidden characters are represented by `_`.

The public methods cover:

- vocabulary scanning and de-duplication
- binary search over sorted vocabulary
- puzzle-structure validation
- hidden-character counting
- candidate solution validation
- unique-solution lookup
- applying a guessed character to the puzzle state

## Build

```bash
make
```

This compiles the Java sources into `build/` using `javac -Xlint:all -Werror`.

## Usage

```bash
java -cp build il.ac.tau.cs.sw1.ex4.WordPuzzle resources/hw4/vocabulary.txt
```

The program then reads puzzle patterns and guesses from standard input.

## Testing

```bash
make test
```

The validation run compiles the project, executes the maintained regression harness, and runs the `WordPuzzleTester`.

## Repository Structure

- `resources/hw4/`: text fixtures packaged with the submission
- `src/il/ac/tau/cs/sw1/ex4/WordPuzzle.java`: my implementation, maintained for current toolchains
- `src/il/ac/tau/cs/sw1/ex4/WordPuzzleTester.java`: small tester from the submitted source tree
- `tests/RunHw4Checks.java`: maintained Java regression harness
