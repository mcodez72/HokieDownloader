package hokiedownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// -------------------------------------------------------------------------
/**
 * MODEL. Our own first in first out queue made of linked nodes, which keeps a
 * head and a tail thus adding and removing are both O(1).
 *
 * @param <T>
 *            the type of item stored in the queue
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class LinkedQueue<T>
{

    /**
     * One link in the queue that holds an item and the node behind it.
     *
     * @param <T>
     *            the type of item stored in the node
     */
    private static class Node<T>
    {
        private T data;
        private Node<T> next;

        /**
         * Creates a node that holds the given item and points to nothing.
         *
         * @param data
         *            the item to store
         */
        public Node(T data)
        {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Creates an empty queue.
     */
    public LinkedQueue()
    {
        head = null;
        tail = null;
        size = 0;
    }


    /**
     * Adds an item to the back of the line.
     *
     * @param item
     *            the item to add
     * @throws IllegalArgumentException
     *             if the item is null
     */
    public void enqueue(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Cannot enqueue null");
        }
        Node<T> newNode = new Node<T>(item);
        if (isEmpty())
        {
            head = newNode;
        }
        else
        {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }


    /**
     * Removes and returns the item at the front of the line.
     *
     * @return the front item
     * @throws NoSuchElementException
     *             if the queue is empty
     */
    public T dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = head.data;
        head = head.next;
        if (head == null)
        {
            tail = null;
        }
        size--;
        return item;
    }


    /**
     * Returns the item at the front without removing it.
     *
     * @return the front item
     * @throws NoSuchElementException
     *             if the queue is empty
     */
    public T peek()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }
        return head.data;
    }


    /**
     * Checks if the queue has no items.
     *
     * @return true if the queue is empty
     */
    public boolean isEmpty()
    {
        return size == 0;
    }


    /**
     * Gives the number of items in the queue.
     *
     * @return how many items are in the queue
     */
    public int size()
    {
        return size;
    }


    /**
     * Copies the items from front to back so the view can show them.
     *
     * @return a new list of the items in queue order
     */
    public List<T> toList()
    {
        List<T> list = new ArrayList<T>();
        Node<T> current = head;
        while (current != null)
        {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
