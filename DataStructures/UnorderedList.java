/* Thuan Nguyen
   masc2190
 */

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedList<E> implements Iterable<E>{
	public static final int DEFAULT_MAX_CAPACITY = 1000;
	int currentSize, maxSize;
	Node <E> head;
	class Node <E> {
		E data;
		Node <E> next;
		public Node(E obj) {
			data = obj;
			next = null;
		}    		
	}
	public UnorderedList(){
		currentSize = 0;
		head = null;		
	}

	//  Inserts a new object into the priority queue.  Returns true if
	//  the insertion is successful.  If the PQ is full, the insertion
	//  is aborted, and the method returns false.
	public boolean insert(E obj) {
		Node <E> newNode = new Node<E> (obj);
		if (isEmpty())		
			head = newNode;		
		else {
			newNode.next = head;
			head = newNode;
		}
		currentSize++;
		return true;
	}

	//  Removes the object of highest priority that has been in the
	//  PQ the longest, and returns it.  Returns null if the PQ is empty.
	public E remove(E obj){
		if(isEmpty()) 
			return null;		
		//	head = head.next;
		//	currentSize--;
		// search for the obj, store it, delete it, then return it
		
			
			Node<E> prev = null, current = head;			
			//look the node to delete
			while(current != null && ((Comparable<E>)obj).compareTo(current.data) != 0){
				prev = current;
				current = current.next;	
			}				
			//list is not empty, first node is the one to delete
			if(current == head){
				head = head.next;
			}
			else{  //node to remove is in the middle
				prev.next = current.next;
			}		
		currentSize--;
		return current.data;
	}

	//  Returns the object of highest priority that has been in the
	//  PQ the longest, but does NOT remove it. 
	//  Returns null if the PQ is empty.
	public E get(E obj){
		if(isEmpty()) 
			return null;		
		Node <E> tmp = head;		
		if(((Comparable<E>)obj).compareTo(tmp.data) == 0)
			return tmp.data;
		return null;	
	}

	//  Returns true if the priority queue contains the specified element
	public boolean contains(E obj){
		Node <E> tmp = head;
		while(tmp != null){
			if(((Comparable<E>)obj).compareTo(tmp.data) == 0)
				return true;
			tmp = tmp.next;
		}
		return false;		
	}

	//  Returns the number of objects currently in the PQ.
	public int size(){
		return currentSize;
	}

	//  Returns the PQ to an empty state.
	public void clear(){
		currentSize = 0;
		head = null;
	}

	//  Returns true if the PQ is empty, otherwise false
	public boolean isEmpty(){
		return head == null;
	}

	//  Returns true if the PQ is full, otherwise false.  List based
	//  implementations should always return false.
	public boolean isFull(){
		return false;
	}

	//  Returns an iterator of the objects in the PQ, in no particular
	//  order.
	public Iterator<E> iterator()
	{
		return new LinkedListIterator();
	}

	private class LinkedListIterator implements Iterator<E>
	{
		private Node<E> nextNode;

		public LinkedListIterator()
		{
			nextNode = head;
		}

		public boolean hasNext()
		{
			return nextNode != null;
		}

		public E next()
		{
			if (!hasNext()) 
				throw new NoSuchElementException();
			E tmp = nextNode.data;
			nextNode = nextNode.next;
			return tmp;
		}
		public void remove() { 
			throw new UnsupportedOperationException(); 
		}
	}
}
