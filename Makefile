BUILD_DIR ?= build
SOURCES := $(shell find src -name '*.java' | sort)
TEST_SOURCES := $(shell find tests -name '*.java' | sort)

.PHONY: all test clean

all:
	mkdir -p $(BUILD_DIR)
	javac -Xlint:all -Werror -d $(BUILD_DIR) $(SOURCES)

test: all
	javac -Xlint:all -Werror -cp $(BUILD_DIR) -d $(BUILD_DIR) $(TEST_SOURCES)
	java -cp $(BUILD_DIR) RunHw4Checks
	java -cp $(BUILD_DIR) il.ac.tau.cs.sw1.ex4.WordPuzzleTester

clean:
	rm -rf $(BUILD_DIR)
