package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K, V> implements DictionaryADT<K, V> {

	class Node<K, V> {
		public K key;
		public V value;
		public Node<K, V> leftChild;
		public Node<K, V> rightChild;

		public Node(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}	
	
	}

	private Node<K, V> root;
	private int currentSize;
	private int modCounter;


	public BinarySearchTree() {
		root = null;
		currentSize = 0;
	}

	// Returns true if the dictionary has an object identified by
	// key in it, otherwise false.
	public boolean contains(K key) {		
		return containsHelper(key, root);		
	}
	
	private boolean containsHelper(K key, Node<K,V> n) {
		if(n == null) return false;		
		if(((Comparable<K>)key).compareTo(n.key) < 0)
			return containsHelper(key, n.leftChild);
		else if(((Comparable<K>)key).compareTo(n.key) > 0)
			return containsHelper(key, n.rightChild);
		return true;	
	}

	// Adds the given key/value pair to the dictionary. Returns
	// false if the dictionary is full, or if the key is a duplicate.
	// Returns true if addition succeeded.
	public boolean add(K key, V value){
		if(contains(key))
			return false;
		if(root == null)
			root = new Node<K,V>(key, value);
		else
			insert(key,value,root,null,false);
		currentSize++;
		modCounter++;
		return true;
	}
	
	private void insert(K key, V value, Node<K,V> n, Node <K,V> parent, boolean wasLeft){
		if(n == null){		//at a leaf node so do the insert
			if(wasLeft) parent.leftChild = new Node<K,V>(key, value);
			else parent.rightChild = new Node<K,V>(key, value);			
		}
		else if(((Comparable<K>)key).compareTo((K)n.key) < 0)
			insert(key, value, n.leftChild, n, true); //go left
		else 
			insert(key, value, n.rightChild, n, false); //go right		
	}
			
	// Deletes the key/value pair identified by the key parameter.
	// Returns true if the key/value pair was found and removed,
	// otherwise false.
	public boolean delete(K key){
		if(!delete (key, root, null, false))
			return false;
		currentSize--;
		modCounter++;
		return true;
	}
	private boolean delete(K k, Node<K,V> n, Node<K,V> parent, boolean wasLeft) {
		if(n == null) return false;
		if(((Comparable<K>)k).compareTo(n.key) < 0)
			return delete(k, n.leftChild, n, true);
		else if(((Comparable<K>)k).compareTo(n.key) > 0)
			return delete(k, n.rightChild, n, false);
		else {
			if(n.leftChild == null && n.rightChild == null) { //no children case
				if(parent == null) root = null;
				else if(wasLeft) parent.leftChild = null;
				else parent.rightChild = null;
			}
			//one child case
			else if(n.leftChild == null){	//one right child
				if(parent == null) root = n.rightChild;
				else if(wasLeft) parent.leftChild = n.rightChild;
				else parent.rightChild = n.rightChild;
			}
			else if(n.rightChild == null){ //one left child 
				if(parent == null) root = n.leftChild;
				else if(wasLeft) parent.leftChild = n.leftChild;
				else parent.rightChild = n.leftChild;
			}
			
			
			//two children case
			else if(n.leftChild != null && n.rightChild != null){
			Node<K,V> tmp = getSuccessor(n.rightChild);
				if(isEmpty()) return false;
				if(tmp == null){
					if(parent == null){
					tmp = n.rightChild;
					root = tmp;					
					tmp.leftChild = n.leftChild;
					}
					else if(wasLeft){
						tmp = n.rightChild;
						parent.leftChild = tmp;
						tmp.leftChild = n.leftChild;
					}					
					else {
						tmp = n.rightChild;
						parent.rightChild = tmp;
						tmp.leftChild = n.leftChild;
					}
				}
				else {
					if(parent == null){
						root = tmp;
						tmp.leftChild = n.leftChild;
						tmp.rightChild = n.rightChild;
					}
					else if(wasLeft){
						parent.leftChild = tmp;
						tmp.leftChild = n.leftChild;
						tmp.rightChild = n.rightChild;
					}
					else{
						parent.rightChild = tmp;
						tmp.leftChild = n.leftChild;
						tmp.rightChild = n.rightChild;
					}									
				}							
		}			
		}		
		return true;		
	}	
	//two Children case
	//find the min and delete it
	private Node<K,V> getSuccessor(Node<K,V> n){
		Node<K,V> parent = null;
		while(n.leftChild != null){
			parent = n;
			n = n.leftChild;
		}
		if(parent == null) return null;
		else parent.leftChild = n.rightChild;
		return n;		
	}
	

	// Returns the value associated with the parameter key. Returns
	// null if the key is not found or the dictionary is empty.
	public V getValue(K key){
		return find(key, root);
	}
	
	private V find(K key, Node<K,V> n){
		if(n == null) return null;
		if(((Comparable<K>)key).compareTo(n.key) < 0) //go left
			return find(key, n.leftChild);
		else if(((Comparable<K>)key).compareTo(n.key) > 0) //go right
			return find(key, n.rightChild);
		return (V) n.value; //found
	}

	// Returns the key associated with the parameter value. Returns
	// null if the value is not found in the dictionary. If more
	// than one key exists that matches the given value, returns the
	// first one found.
	public K getKey(V value)
	{	
		Node<K,V> tmp = root;
		if(root == null) return null;
		while(tmp != null){
		if(((Comparable<V>)value).compareTo(tmp.value) < 0)
			tmp = tmp.leftChild;
		else if(((Comparable<V>)value).compareTo(tmp.value) > 0)
			tmp = tmp.rightChild;
		else if(((Comparable<V>)value).compareTo(tmp.value) == 0)
			return tmp.key;
		}
		if(tmp == null)
		return null;		
		return tmp.key;
	}	

	// Returns the number of key/value pairs currently stored
	// in the dictionary
	public int size() {
		return currentSize;
	}

	// Returns true if the dictionary is at max capacity
	public boolean isFull(){
		return false;
	}

	// Returns true if the dictionary is empty
	public boolean isEmpty() {
		return root == null;
	}

	// Returns the Dictionary object to an empty state.
	public void clear()
	{
		root = null;
		currentSize = 0;
		modCounter++;
	}
	
	abstract class IteratorHelper<E> implements Iterator<E>{
		   public Node<K,V>[] nodes;
		   public int idx, iterIndex;
		   public long modCheck;
		   
		   public IteratorHelper(){
			      idx = iterIndex = 0;
			      modCheck = modCounter;
			      nodes = new Node[currentSize];
			      inOrderedFillArrray(root);
			   }
		   
		   public void inOrderedFillArrray(Node<K,V> n){
			   if(n != null){
				   inOrderedFillArrray(n.leftChild);
		           nodes[idx++]= n;
		           inOrderedFillArrray(n.rightChild); 
		       }
		   }
		   
		   public boolean hasNext(){
		       	if (modCheck != modCounter){ 
		       		throw new ConcurrentModificationException();
		       		}		       	
		        return iterIndex < currentSize;
		   }
		   
		   public void remove(){
			   throw new UnsupportedOperationException();}
	}
	   
	class KeyIteratorHelper<K> extends IteratorHelper<K>{ 
		public KeyIteratorHelper(){
			super();
			}      
	    public K next(){
	    	if (!hasNext()) 
	    		throw new NoSuchElementException();
	        return (K) nodes[iterIndex++].key;
	    }
	}
	   
	class ValueIteratorHelper<V> extends IteratorHelper<V>{
	    public ValueIteratorHelper(){
	    	super();
	    	}  
	    public V next(){
	        if (!hasNext())
	        	throw new NoSuchElementException();
	        return (V) nodes[iterIndex++].value;
	      }
	   }

	// Returns an Iterator of the keys in the dictionary, in ascending
	// sorted order. The iterator must be fail-fast.
	public Iterator<K> keys()
	{
		return new KeyIteratorHelper();
	}

	// Returns an Iterator of the values in the dictionary. The
	// order of the values must match the order of the keys.
	// The iterator must be fail-fast.
	public Iterator<V> values()
	{
		return new ValueIteratorHelper();
	}
	
	
}