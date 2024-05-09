

import java.io.File;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static String[] removeDelimiters(String textLine) {
		String DELIMITERS = "[-+=" + " " + // space
				"’'\"" + "\r\n " + // carriage return line fit
				"1" + "2" + "3" + "4" + "5" + "6" + "7" + "8" + "9" + "0" + // numbers
				"(){}<>\\[\\]" + // brackets
				":" + // colon
				"," + // comma
				"‒–—―" + // dashes
				"…" + // ellipsis
				"!" + // exclamation mark
				"." + // full stop/period
				"«»" + // guillemets
				"-‐" + // hyphen
				"?" + // question mark
				"‘’“”" + // quotation marks
				";" + // semicolon
				"/" + // slash/stroke
				"⁄" + // solidus
				"␠" + // space?
				"·" + // interpunct
				"&" + // ampersand
				"@" + // at sign
				"*" + // asterisk
				"\\" + // backslash
				"•" + // bullet
				"^" + // caret
				"¤¢$€£¥₩₪" + // currency
				"†‡" + // dagger
				"°" + // degree
				"¡" + // inverted exclamation point
				"¿" + // inverted question mark
				"¬" + // negation
				"#" + // number sign (hashtag)
				"№" + // numero sign ()
				"%‰‱" + // percent and related signs
				"¶" + // pilcrow
				"′" + // prime
				"§" + // section sign
				"~" + // tilde/swung dash
				"¨" + // umlaut/diaeresis
				"_" + // underscore/understrike
				"|¦" + // vertical/pipe/broken bar
				"⁂" + // asterism
				"☞" + // index/fist
				"∴" + // therefore sign
				"‽" + // interrobang
				"※" + // reference mark
				"]";

		String[] splitted = textLine.split(DELIMITERS);

		return splitted;
	}

	public static String[] removeStopWords(String[] splittedLine, String[] stopWordsArray)
			throws FileNotFoundException {

		for (int j = 0; j < splittedLine.length; j++) {
			for (int k = 0; k < stopWordsArray.length; k++) {
				if (splittedLine[j].equalsIgnoreCase(stopWordsArray[k])) {
					splittedLine[j] = "";
				}
			}
		}
		return splittedLine;
	}

	public static void searchWord_in_HashTable(String word, HashedDictionary<String, Integer, String> hashTable) {

		Iterator<String> keyIterator = hashTable.getKeyIterator();

		while (keyIterator.hasNext()) {

			if (word.equals(keyIterator.next())) {

			}
		}

	}

	public static void searchFileOperation(HashedDictionary<String, Integer, String> hashTable)
			throws FileNotFoundException {

		try {

			File f1 = new File("search.txt");
			Scanner textFileReader = new Scanner(f1);
			long initial_time = System.nanoTime();

			while (textFileReader.hasNextLine()) {
				String searchWord = textFileReader.nextLine();

				searchWord_in_HashTable(searchWord, hashTable);

			}
			textFileReader.close();
			long final_time = System.nanoTime();
			System.out.println("min search time: " + (final_time - initial_time));

		} catch (FileNotFoundException exception) {
			System.out.println("Unexcpected error occurred!");
			exception.printStackTrace();
		}

	}

	public static void file_and_dict_Operations(String textFileName, Dictionary<String, Integer, String> dict) {

		try {
			File f1 = new File(textFileName);
			Scanner textFileReader = new Scanner(f1);
			while (textFileReader.hasNextLine()) {
				String textFileData = textFileReader.nextLine();
				String[] splittedData = removeDelimiters(textFileData);
				String[] finalformOfLine = removeStopWords(splittedData, stopWordsArray());

				for (int i = 0; i < finalformOfLine.length; i++) {

					if (!finalformOfLine[i].equalsIgnoreCase("")) {
						dict.add(finalformOfLine[i], 1, textFileName);
					}
				}

			}
			textFileReader.close();

		} catch (FileNotFoundException exception) {
			System.out.println("Unexcpected error occurred!");
			exception.printStackTrace();
		}

	}



	public static String[] stopWordsArray() throws FileNotFoundException {

		File f1 = new File("stop_words_en.txt");
		Scanner stopwordsReader = new Scanner(f1);
		String[] stopWords = new String[572];
		int i = 0;
		while (stopwordsReader.hasNextLine()) {
			String stop_word = stopwordsReader.nextLine().replace("'", "");
			if (stop_word != "") {
				stopWords[i] = stop_word;
				i++;
			}
		}
		stopwordsReader.close();
		return stopWords;
	}

	public static Dictionary<String, Integer, String> letterDictionary() {

		Dictionary<String, Integer, String> letterValue = new Dictionary<String, Integer, String>();

		letterValue.add("a", 1, "");
		letterValue.add("b", 2, "");
		letterValue.add("c", 3, "");
		letterValue.add("d", 4, "");
		letterValue.add("e", 5, "");
		letterValue.add("f", 6, "");
		letterValue.add("g", 7, "");
		letterValue.add("h", 8, "");
		letterValue.add("i", 9, "");
		letterValue.add("j", 10, "");
		letterValue.add("k", 11, "");
		letterValue.add("l", 12, "");
		letterValue.add("m", 13, "");
		letterValue.add("n", 14, "");
		letterValue.add("o", 15, "");
		letterValue.add("p", 16, "");
		letterValue.add("q", 17, "");
		letterValue.add("r", 18, "");
		letterValue.add("s", 19, "");
		letterValue.add("t", 20, "");
		letterValue.add("u", 21, "");
		letterValue.add("v", 22, "");
		letterValue.add("w", 23, "");
		letterValue.add("x", 24, "");
		letterValue.add("y", 25, "");
		letterValue.add("z", 26, "");

		return letterValue;
	}

	public static void main(String[] args) throws FileNotFoundException {

		String textFileName;
		Dictionary<String, Integer, String> letters = letterDictionary();
		Dictionary<String, Integer, String> unhashedDict = new Dictionary<String, Integer, String>();
		HashedDictionary<String, Integer, String> hashTable = new HashedDictionary<String, Integer, String>();

		for (int i = 1; i <= 100; i++) {
			if (i < 10) {
				textFileName = "00" + i + ".txt";
				file_and_dict_Operations(textFileName, unhashedDict);
			} else if (i >= 10 && i <= 99) {
				textFileName = "0" + i + ".txt";
				file_and_dict_Operations(textFileName, unhashedDict);
			} else {
				file_and_dict_Operations("100.txt", unhashedDict);
			}
		}

		// long initial_time= System.nanoTime();

		hashTable = hashing(unhashedDict, letters);
		// searchFileOperation(hashTable);

		// long final_time= System.nanoTime();

		// System.out.println(final_time - initial_time);
		// System.out.println("collision count: " + HashedDictionary.collision_count);

		try (Scanner searchWords = new Scanner(System.in)) {
			System.out.println("Please enter your search words: ");

			String words = searchWords.nextLine();
			String[] splitted_words = words.split(" ");
			
			System.out.println();
			
			for (int i = 0; i < splitted_words.length; i++) {

				searchEngine(splitted_words[i], hashTable);

			}
		}

	}

	private static void searchEngine(String str, HashedDictionary<String, Integer, String> hashTable) {

		Iterator<String> keyIterator = hashTable.getKeyIterator();
		Iterator<String> fileIterator = hashTable.getFileIterator();
		while (keyIterator.hasNext()) {

			String a = keyIterator.next();
			String c = fileIterator.next();

			if (str.equalsIgnoreCase(a)) {
				int temp = 0;
				String f = "";
				int b = c.length() / 10;

				for (int i = 0; i < b; i++) {

					String d = c.substring(i * 10, (i + 1) * 10);
					int e = Integer.valueOf(d.substring(9, 10));

					if (e >= temp) {
						temp = e;
						f = d;
					}
				}

				System.out.println(a + " || " + f);
				break;

			}
		}
	}

	public static int getLetterValue(Dictionary<String, Integer, String> letters, String key_letter) {

		Iterator<String> letterKeyIterator = letters.getKeyIterator();
		Iterator<Integer> letterValueIterator = letters.getValueIterator();
		int letter_value = 0;
		String letter = "";
		while (letterKeyIterator.hasNext()) {

			letter = letterKeyIterator.next().toString();
			letter_value = letterValueIterator.next();

			if (letter.equals(key_letter)) {
				break;
			}
		}
		return letter_value;
	}

	public static HashedDictionary<String, Integer, String> hashing(Dictionary<String, Integer, String> unsorted,
			Dictionary<String, Integer, String> letters) {

		HashedDictionary<String, Integer, String> hashTable = new HashedDictionary<String, Integer, String>();

		Iterator<String> keyIterator = unsorted.getKeyIterator();
		Iterator<String> fileIterator = unsorted.getFileIterator();

		while (keyIterator.hasNext()) {

			String key = keyIterator.next().toString().toLowerCase();

			// int value_SSF = simpleSummFunc(key, letters);
			// hashTable.add(key, value_SSF, fileIterator.next());

			int value_PAF = polynomialAccFunc(key, letters);
			hashTable.add(key, value_PAF, fileIterator.next());

		}

		return hashTable;

	}

	public static int simpleSummFunc(String key, Dictionary<String, Integer, String> letters) {

		String[] splittedKey = key.split("");
		int simpleSumm = 0;

		for (int i = splittedKey.length - 1; i >= 0; i--) {

			int letterValue = getLetterValue(letters, splittedKey[i]);

			simpleSumm += letterValue * 31;
		}

		return simpleSumm;
	}

	public static int polynomialAccFunc(String key, Dictionary<String, Integer, String> letters) {

		String[] splittedKey = key.split("");
		int polynomialAccumulation = 0;

		for (int i = splittedKey.length - 1; i >= 0; i--) {

			int letterValue = getLetterValue(letters, splittedKey[i]);

			polynomialAccumulation += letterValue * (int) Math.pow(31, i);
		}

		return polynomialAccumulation;

	}

}
