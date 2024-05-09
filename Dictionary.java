
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("hiding")
public class Dictionary<K extends Comparable<? super K>, V,F>  {

	private Entry<K,V,F>[] dictionary;
	private int numOfEntries;
	private final static int DEFAULT_CAPACITY= 30;
	

	public 	Dictionary() {
		this(DEFAULT_CAPACITY);
	}

	public Dictionary(int initialCapacity) {
		@SuppressWarnings( "unchecked")
		Entry<K,V,F>[] tempDictionary= (Entry<K,V,F>[]) new Entry[initialCapacity];
		dictionary= tempDictionary;
		numOfEntries=0;
	}
	
	 private int locateIndex(K key) {
		int index=0;
		while((index < numOfEntries) && key.compareTo(dictionary[index].getKey())>0) {
			index++;
		}
		return index;
	}
	 private void makeRoom(int newPosition) {
		 if((newPosition>=0)&&(newPosition< numOfEntries)) {
			 int lastIndex= numOfEntries-1;
			 for(int index= lastIndex; index>= newPosition; index--) {
				 dictionary[index+1]= dictionary[index];
			 }
		 }
	 }
	
	public void ensureCapacity() {
		if(numOfEntries== dictionary.length) {
			dictionary= Arrays.copyOf(dictionary, 2*dictionary.length);
		}
	}
	
	public String textfile_indexes(String previous_names, String new_file_name) {
		
		previous_names= previous_names+" "+ new_file_name;

		return previous_names;
	}
	
	@SuppressWarnings("unchecked")
	public void add(K key, V value, F textFileName) {
		String keyStr= key.toString();		
		int keyIndex= locateIndex(key);
		
		if(keyIndex< numOfEntries && keyStr.equalsIgnoreCase(dictionary[keyIndex].getKey().toString())) {//key was added before
			
			int tempValue= (int) dictionary[keyIndex].getValue();
			Object newValue= ((int)tempValue)+1;		
			dictionary[keyIndex].setValue((V)newValue);		

			String previous_names= dictionary[keyIndex].getFile().toString();

			if(!previous_names.contains(textFileName.toString())) {//different file added
				
				String finalTextFiles= textfile_indexes(previous_names, textFileName.toString()+" " +1);
				dictionary[keyIndex].setFile((F)((Object)finalTextFiles));
			}
			else if(previous_names.contains(textFileName.toString())) {//same file added

				String[] changed= dictionary[keyIndex].getFile().toString().split("");
				String temp= "";
				int l= changed.length;
				int count= Integer.valueOf(changed[l-1])+1;	
								
				for(int i=0; i<l-1; i++) {				
						temp+=changed[i];						
				}
				temp+=count;
				
				dictionary[keyIndex].setFile((F)temp);				
			}	
		}
		else {//added key name is new
			ensureCapacity();
			makeRoom(keyIndex);
			dictionary[keyIndex]= new Entry<K,V,F>(key,value,textFileName);
			String finalTextFiles= textfile_indexes("", textFileName.toString()+" " +1);
			dictionary[keyIndex].setFile((F)(Object)finalTextFiles);
			numOfEntries++;			
		}	
	}

	
	public V getValue(K key) {
		V result= null;
		if(contains(key)) {
			int keyIndex= locateIndex(key);
			result= dictionary[keyIndex].getValue();
		}
		return result;
	}
	
	public boolean contains(K key) {
		int keyIndex= locateIndex(key);
		if((keyIndex< numOfEntries)&& key.equals(dictionary[keyIndex].getKey())) {
			return true;
		}
		return false;
	}
	
	public boolean isEmpty() {
		
		return numOfEntries==0;
	}
	
	public int getSize() {
		return numOfEntries;
	}
	
	public Iterator<K> getKeyIterator(){
		return new KeyIterator();
	}
	
	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}
	public Iterator<F> getFileIterator() {
		return new FileIterator();
	}
		
	private class Entry<K,V,F>{
		private K key;
		private V value;
		private F file;
		
		public Entry(K key, V value, F textFileName) {
			this.key= key;
			this.value=value;
			this.file=textFileName;
		}
		
		public K getKey() {
			return key;
		}
		public V getValue() {
			return value;
		}
		
		public void setValue(V value) {
			this.value=value;
		}
		
		public F getFile() {
			return file;
		}
		
		public void setFile(F file) {
			this.file= file;
		}
		
	}
	
	private class KeyIterator implements Iterator<K>{
		private Iterator<Entry<K,V,F>> traverser;
		private KeyIterator() {
			@SuppressWarnings("unchecked")
			Entry<K,V,F>[] tempDictionary=(Entry<K,V,F>[]) new Entry[numOfEntries];
			for(int i=0; i< numOfEntries; i++) {
				tempDictionary[i]= dictionary[i];
			}
			traverser= Arrays.asList(tempDictionary).iterator();
		}
		
		public boolean hasNext() {
			return traverser.hasNext();
		}
		
		public K next() {
			Entry<K,V,F> nextEntry= traverser.next();
			return nextEntry.getKey();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}

	private class ValueIterator implements Iterator<V> {
		private Iterator<Entry<K,V,F>> traverser;
		private ValueIterator() {
			@SuppressWarnings("unchecked")
			Entry<K,V,F>[] tempDictionary = (Entry<K,V,F>[]) new Entry[numOfEntries];
			for(int i = 0; i < numOfEntries; i++) {
				tempDictionary[i] = dictionary[i];
			}
			traverser = Arrays.asList(tempDictionary).iterator();
		}
		public boolean hasNext() {
			return traverser.hasNext();
		} // end hasNext

		public V next() {
			Entry<K,V,F> nextEntry = traverser.next();
			return nextEntry.getValue();
		} // end next

		public void remove() {
			throw new UnsupportedOperationException();
		} // end remove
	}
	
	private class FileIterator implements Iterator<F>{
		private Iterator<Entry<K,V,F>> traverser;
		private FileIterator() {
			@SuppressWarnings("unchecked")
			Entry<K,V,F>[] tempDictionary=(Entry<K,V,F>[]) new Entry[numOfEntries];
			for(int i=0; i< numOfEntries; i++) {
				tempDictionary[i]= dictionary[i];
			}
			traverser= Arrays.asList(tempDictionary).iterator();
		}
		
		public boolean hasNext() {
			return traverser.hasNext();
		}
		
		public F next() {
			Entry<K,V,F> nextEntry= traverser.next();
			return (F) nextEntry.getFile();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}
	


}
