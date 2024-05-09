
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<K, V, F> {
	private TableEntry<K, V, F>[] hashTable;
	private int numberOfEntries;
	private int locationsUsed;
	private static final int DEFAULT_SIZE = 2477;
	private static final double MAX_LOAD_FACTOR = 0.5;

	public static int collision_count = 0;

	public HashedDictionary() {
		this(DEFAULT_SIZE);
	}

	@SuppressWarnings("unchecked")
	public HashedDictionary(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {
		if (num <= 1)
			return 2;
		else if (isPrime(num))
			return num;
		boolean found = false;
		while (!found) {
			num++;
			if (isPrime(num))
				found = true;
		}
		return num;
	}

	public int doubleHashing(V value, int index, int hashTable_length) {

		int dh = 31 - ((int) value % 31);
		int hash2 = 0;

		for (int i = 0; i < hashTable_length; i++) {

			hash2 = (index + i * dh) % hashTable_length;

			if (hashTable[hash2] == null) {
				break;
			} else {
				collision_count++;
			}
		}

		return hash2;
	}

	public void add(K key, V value, F file) {

		if (isHashTableTooFull())
			rehash();

		int index = getHashIndex( value);
		//index = probe(index, key);

		if ((hashTable[index] == null)) {
			hashTable[index] = new TableEntry<K, V, F>(key, value, file);
			numberOfEntries++;
			locationsUsed++;
		}

		else if (hashTable[index] != null) {

			int dh_index=doubleHashing(value, index, hashTable.length);
			hashTable[dh_index]= new TableEntry<K, V,F>(key, value,file);
			numberOfEntries++;
			locationsUsed++;

		}
	}
	
	private int probe(int index, K key) {
		boolean found = false;
		int removedStateIndex = -1; 
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey()))
					found = true; 
				else 
					index = (index + 1) % hashTable.length; 
			} 
			else 
			{
				if (removedStateIndex == -1)
					removedStateIndex = index;
				index = (index + 1) % hashTable.length; 
			} 
		} 
		if (found || (removedStateIndex == -1))
			return index; 
		else
			return removedStateIndex; 
	}


	// first hash func.
	public int getHashIndex(V value) {

		int v = (int) value;

		if (v < 0) {
			v += Integer.MAX_VALUE;
		}

		int hashIndex = v % hashTable.length;

		return hashIndex;
	}

	public boolean isHashTableTooFull() {
		int load_factor = locationsUsed / hashTable.length;
		if (load_factor >= MAX_LOAD_FACTOR)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntry<K, V, F>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize];
		numberOfEntries = 0;
		locationsUsed = 0;

		for (int index = 0; index < oldSize; index++) {
			if ((oldTable[index] != null) && oldTable[index].isIn())
				add(oldTable[index].getKey(), oldTable[index].getValue(), oldTable[index].getFile());
		}
	}

	private int locate(K key, int index) {
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true;
			else
				index = (index + 1) % hashTable.length;
		}
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	public V getValue(K key, V value) {
		V result = null;
		int index = getHashIndex(value);
		index = locate(key, index);
		if (index != -1)
			result = hashTable[index].getValue();
		return result;
	}

	public boolean contains(K key, V value) {
		int index = getHashIndex(value);
		index = locate(key, index);
		if (index != -1)
			return true;
		return false;
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	public Iterator<F> getFileIterator() {
		return new FileIterator();
	}

	@SuppressWarnings("hiding")
	private class TableEntry<S, T, F> {
		private S key;
		private T value;
		private F file;
		private boolean inTable;

		private TableEntry(S key, T value, F file) {
			this.key = key;
			this.value = value;
			this.file = file;
			inTable = true;
		}

		private S getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private F getFile() {
			return file;
		}

		@SuppressWarnings("unused")
		private void setToIn() {
			inTable = true;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	private class KeyIterator implements Iterator<K> {
		private int currentIndex;
		private int numberLeft;

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null)) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private class ValueIterator implements Iterator<V> {
		private int currentIndex;
		private int numberLeft;

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null)) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private class FileIterator implements Iterator<F> {
		private int currentIndex;
		private int numberLeft;

		private FileIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}

		public boolean hasNext() {
			return numberLeft > 0;
		}

		public F next() {
			F result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null)) {
					currentIndex++;
				}
				result = hashTable[currentIndex].getFile();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
