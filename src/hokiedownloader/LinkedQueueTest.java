package hokiedownloader;

import java.util.List;
import java.util.NoSuchElementException;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests LinkedQueue for FIFO order and the empty queue cases.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class LinkedQueueTest
    extends TestCase
{

    private LinkedQueue<String> queue;

    /**
     * Creates an empty queue before each test.
     */
    public void setUp()
    {
        queue = new LinkedQueue<String>();
    }


    /**
     * Tests that items come out in the same order they went in.
     */
    public void testEnqueueAndDequeue()
    {
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals(2, queue.size());
        assertEquals("a", queue.dequeue());
        assertEquals("b", queue.dequeue());
        assertTrue(queue.isEmpty());
    }


    /**
     * Tests that adding null is rejected.
     */
    public void testEnqueueNull()
    {
        try
        {
            queue.enqueue(null);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Cannot enqueue null", e.getMessage());
        }
    }


    /**
     * Tests that dequeue on an empty queue throws.
     */
    public void testDequeueEmpty()
    {
        try
        {
            queue.dequeue();
            fail("Should have thrown NoSuchElementException");
        }
        catch (NoSuchElementException e)
        {
            assertEquals("Queue is empty", e.getMessage());
        }
    }


    /**
     * Tests that peek shows the front without removing it.
     */
    public void testPeek()
    {
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.peek());
        assertEquals(2, queue.size());
    }


    /**
     * Tests that peek on an empty queue throws.
     */
    public void testPeekEmpty()
    {
        try
        {
            queue.peek();
            fail("Should have thrown NoSuchElementException");
        }
        catch (NoSuchElementException e)
        {
            assertEquals("Queue is empty", e.getMessage());
        }
    }


    /**
     * Tests isEmpty and size on a new queue and after adding.
     */
    public void testIsEmptyAndSize()
    {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        queue.enqueue("a");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
    }


    /**
     * Tests that the queue still works after being emptied, thus tail resets
     * correctly.
     */
    public void testReuseAfterEmpty()
    {
        queue.enqueue("a");
        queue.dequeue();
        queue.enqueue("b");
        assertEquals("b", queue.peek());
        assertEquals(1, queue.size());
    }


    /**
     * Tests that toList gives the items from front to back.
     */
    public void testToList()
    {
        assertEquals(0, queue.toList().size());
        queue.enqueue("a");
        queue.enqueue("b");
        List<String> list = queue.toList();
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
    }
}
