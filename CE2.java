
/**
 * Author: Kevin
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CE2 {

	private static final String NO_INPUT_FILE_NAME = "NO INPUT FILE NAME";

	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use";

	private static final String COMMAND = "command: ";

	private static final String TEXT_INVALID = "please enter a valid command or index";

	private static final String TEXT_ADD = "added to %1$s: \"%2$s\"";

	private static final String TEXT_EMPTY = "%1$s is empty";

	private static final String TEXT_DELETE = "deleted from %1$s: \"%2$s\"";

	private static final String TEXT_CLEAR = "all content deleted from %1$s";

	private static final String TEXT_SORT = "All sorted alphabetically: ";

	private static final String TEXT_NOT_FOUND = "result not found";

	// store all texts from the user
	private static ArrayList<String> textFile; // An arraylist to store the
												// content
	private static File file;
	private static Scanner sc = new Scanner(System.in); // to store the command
														// from user
	private static String fileName;
	private static String command;

	enum Command_Type {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println(String.format(NO_INPUT_FILE_NAME));
			System.exit(0);
		} else {
			fileName = args[0];
			setup(fileName);
		}

		retrieveTexts(file);
		printWelcomeMessage();
		while (true) {
			command = readCommand();
			operate(command);
		}
	}

	// it is to initiate the File and arraylist
	private static void setup(String fileName) {
		file = new File(fileName);
		textFile = new ArrayList<String>();
	}

	// This method is to retrieve the last save text file from user
	private static void retrieveTexts(File file) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				textFile.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
		}
	}

	private static void printWelcomeMessage() {
		showMessage(WELCOME_MESSAGE, file.getName());
	}

	// This method is use to save the command that the user enter
	private static void operate(String command) throws IOException {
		Command_Type commandType = determineCommandType(getFirstWord(command));
		String content = removeFirstWord(command);

		switch (commandType) {
		case ADD:
			add(content);
			return;
		case DELETE:
			delete(content);
			return;
		case DISPLAY:
			display();
			return;
		case CLEAR:
			clear();
			return;
		case SORT:
			sort();
			return;
		case SEARCH:
			search(content);
			return;
		case EXIT:
			saveToFile();
			System.exit(0);
		case INVALID:
			// if the user any command that cannot be identified (e.g
			// "jump")
			showMessage(TEXT_INVALID);
			return;
		}
	}

	private static Command_Type determineCommandType(String firstWord) {
		if (firstWord == null)
			throw new Error("command type string cannot be null!");
		if (firstWord.equalsIgnoreCase("add")) {
			return Command_Type.ADD;
		} else if (firstWord.equalsIgnoreCase("display")) {
			return Command_Type.DISPLAY;
		} else if (firstWord.equalsIgnoreCase("delete")) {
			return Command_Type.DELETE;
		} else if (firstWord.equalsIgnoreCase("clear")) {
			return Command_Type.CLEAR;
		} else if (firstWord.equalsIgnoreCase("sort")) {
			return Command_Type.SORT;
		} else if (firstWord.equalsIgnoreCase("search")) {
			return Command_Type.SEARCH;
		} else if (firstWord.equalsIgnoreCase("exit")) {
			return Command_Type.EXIT;
		} else {
			return Command_Type.INVALID;
		}
	}

	private static String removeFirstWord(String command) {
		return command.replace(getFirstWord(command), "").trim();
	}

	private static String getFirstWord(String command) {
		String firstWord = command.trim().split("\\s+")[0];
		return firstWord;
	}

	/**
	 * * This operation is used to read command from the user. * * @return the
	 * command
	 */
	private static String readCommand() throws IOException {
		System.out.print(COMMAND);
		String userCommand = sc.nextLine();
		return userCommand;
	}

	// This operation is used to add the text.
	private static void add(String content) {
		textFile.add(content);
		showMessage(TEXT_ADD, file.getName(), content);
	}

	// Delete command performed
	private static void delete(String content) throws IOException {
		int index = Integer.parseInt(content);
		try {
			index = index - 1;
			if (index >= textFile.size()) {
				showMessage(TEXT_INVALID);
				sc.nextLine();
				return;
			}
			String deletedLine = textFile.get(index);
			if (textFile.size() == 0) {
				showMessage(TEXT_EMPTY, file.getName());
			} else {
				textFile.remove(index);
				showMessage(TEXT_DELETE, file.getName(), deletedLine);
			}
		} catch (java.util.InputMismatchException e) {
			showMessage(TEXT_INVALID);
			return;
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			showMessage(TEXT_INVALID);
			return;
		}
	}

	// Display command performed
	private static void display() {
		int size = textFile.size();
		if (size == 0) {
			showMessage(TEXT_EMPTY, file.getName());
		} else {
			// give index to each line
			int num = 1;
			String label;
			for (int i = 0; i < size; i++) {
				label = String.valueOf(num++) + ".";
				System.out.println(label + " " + textFile.get(i));
			}
		}
	}

	// Clear command performed
	private static void clear() {
		if (textFile.size() == 0) {
			showMessage(TEXT_EMPTY, file.getName());
		} else {
			textFile.clear();
			showMessage(TEXT_CLEAR, file.getName());
		}
	}

	private static void sort() {
		Collections.sort(textFile);
		showMessage(TEXT_SORT);
		display();
		return;
	}

	private static void search(String content) {
		String finding = "";
		int size = textFile.size();
		if (size == 0) {
			finding = TEXT_NOT_FOUND;
		} else {
			for (int i = 0; i < size; i++) {
				if (textFile.get(i).contains(content)) {
					finding += (i + 1) + ". " + textFile.get(i) + "\n";
				}
			}
		}
		return;
	}

	// This method is to store all the text into the arraylist into the text
	// file
	private static void saveToFile() throws FileNotFoundException {
		try {
			FileWriter fileW = new FileWriter(file);
			BufferedWriter buffW = new BufferedWriter(fileW);
			for (int i = 0; i < textFile.size(); i++) {
				buffW.write(textFile.get(i));
				buffW.newLine();
			}
			buffW.close();
		} catch (IOException e) {
		}
	}

	// This method is used to print out the error message
	// pre: errorType!=null && message!=null
	private static void showMessage(String message, Object... arguments) {
		System.out.println(String.format(message, arguments));
	}
}
