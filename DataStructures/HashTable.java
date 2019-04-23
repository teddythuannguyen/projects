package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTable<K, V> implements DictionaryADT<K, V> {
	private int currentSize;
	private int maxSize;
	private int tableSize;
	private long modCounter;

	private UnorderedList<DictionaryNode<K, V>>[] list;

	private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>> {
		K key;
		V value;

		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public int compareTo(DictionaryNode<K, V> node) {
			return ((Comparable<K>)key).compareTo((K)node.key);
		}
	}

	public HashTable(int n) {
		currentSize = 0;
		maxSize = n;
		modCounter = 0;
		tableSize = (int) (maxSize * 1.3f);
		list = new UnorderedList[tableSize];
		for (int i = 0; i < tableSize; i++)
			list[i] = new UnorderedList<DictionaryNode<K, V>>();
	}

	private int getIndex(K key) {
		return (key.hashCode() & 0x7FFFFFFF) % tableSize;
	}

	// Returns true if the dictionary has an object identified by
	// key in it, otherwise false.
	public boolean contains(K key) {
		return list[getIndex(key)].contains(new DictionaryNode<K, V>(key, null));
	}

	// Adds the given key/value pair to the dictionary. Returns
	// false if the dictionary is full, or if the key is a duplicate.
	// Returns true if addition succeeded.
	public boolean add(K key, V value) {
		if (isFull())
			return false;
		if (list[getIndex(key)].contains(new DictionaryNode<K, V>(key, value)))
			return false;
		list[getIndex(key)].insert(new DictionaryNode<K, V>(key, value));
		currentSize++;
		modCounter++;
		return true;
	}

	// Deletes the key/value pair identified by the key parameter.
	// Returns true if the key/value pair was found and removed,
	// otherwise false.
	public boolean delete(K key) {
		if(isEmpty()) return false;
		DictionaryNode<K,V> node = new DictionaryNode<K,V>(key,null);
		if(list[getIndex(key)].remove(node) == null)
			return false;
		// update your size and mod
		currentSize--;
		modCounter++;
		return true;
		 
	}

	// Returns the value associated with the parameter key. Returns
	// null if the key is not found or the dictionary is empty.
	public V getValue(K key) {
		DictionaryNode<K, V> temp = list[getIndex(key)].get(new DictionaryNode<K, V>(key, null));
		if (temp == null)
			return null;
		return temp.value;
	}

	// Returns the key associated with the parameter value. Returns
	// null if the value is not found in the dictionary. If more
	// than one key exists that matches the given value, returns the
	// first one found.
	public K getKey(V value) {
		// double for loop to look through every double node until you find the
		// value
		// outer loop : array
		// inner loop : double nodes
		for (int i = 0; i < tableSize; i++)
			for (DictionaryNode n : list[i])
				if (((Comparable<V>) value).compareTo((V) n.value) == 0)
					return (K) n.key;
		return null;
	}

	// Returns the number of key/value pairs currently stored
	// in the dictionary
	public int size() {
		return currentSize;
	}

	// Returns true if the dictionary is at max capacity
	public boolean isFull() {
		return false;
	}

	// Returns true if the dictionary is empty
	public boolean isEmpty() {
		if(currentSize == 0) return true;
		return false;
	}

	// Returns the Dictionary object to an empty state.
	public void clear() {
		if(currentSize == 0) return;
		for(int i=0; i<list.length; i++){
			list[i].clear();
	}
		currentSize = 0;
		modCounter++;
	}

	abstract class IteratorHelper<E> implements Iterator<E> {
		protected DictionaryNode<K, V>[] nodes, n;
		protected int idx;
		protected long modCheck;

		private DictionaryNode<K, V>[] shellSort(DictionaryNode<K,V> array[]) {
			n = array;
			int in, out, h = 1;
			DictionaryNode<K, V> temp;
			int size = n.length;

			while (h <= size / 3)
				h = h * 3 + 1;
			while (h > 0) {
				for (out = h; out < size; out++) {
					temp = n[out];
					in = out;
					while (in > h - 1 && ((Comparable<K>) n[in - h].key).compareTo(temp.key) >= 0) {
						n[in] = n[in - h];
						in -= h;
					}
					n[in] = temp;
				}
				h = (h - 1) / 3;
			}
			return n;
		}

		public IteratorHelper() {
			nodes = new DictionaryNode[currentSize];
			idx = 0;
			int j = 0;
			modCheck = modCounter;
			for (int i = 0; i < tableSize; i++)
				for (DictionaryNode n : list[i])
					nodes[j++] = n;
			shellSort(nodes);
		}

		public boolean hasNext() {
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return idx < currentSize;
		}

		public abstract E next();

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	class KeyIteratorHelper<K> extends IteratorHelper<K> {
		public KeyIteratorHelper() {
			super();
		}

		public K next() {
			if(!hasNext())
			throw new NoSuchElementException();
			return (K) nodes[idx++].key;
		}
	}

	class ValueIteratorHelper<V> extends IteratorHelper<V> {
		public ValueIteratorHelper() {
			super();
		}

		public V next() {
			if(!hasNext())
				throw new NoSuchElementException();
			return (V) nodes[idx++].value;
		}
	}

	// Returns an Iterator of the keys in the dictionary, in ascending
	// sorted order. The iterator must be fail-fast.
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	// Returns an Iterator of the values in the dictionary. The
	// order of the values must match the order of the keys.
	// The iterator must be fail-fast.
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
}