package hokiedownloader;

import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests DownloadItem for its 1 minute timer and status changes.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class DownloadItemTest
    extends TestCase
{

    private DownloadItem item;

    /**
     * Creates a new file before each test.
     */
    public void setUp()
    {
        item = new DownloadItem("https://vt.edu/notes.pdf", "notes.pdf");
    }


    /**
     * Tests that a new file has a full timer and is waiting.
     */
    public void testNewItem()
    {
        assertEquals("https://vt.edu/notes.pdf", item.getUrl());
        assertEquals("notes.pdf", item.getFileName());
        assertEquals(60, item.getTimer());
        assertEquals(DownloadItem.Status.WAITING, item.getStatus());
        assertFalse(item.isComplete());
    }


    /**
     * Tests that startTimer sets the file to downloading.
     */
    public void testStartTimer()
    {
        item.startTimer();
        assertEquals(DownloadItem.Status.DOWNLOADING, item.getStatus());
    }


    /**
     * Tests that tick counts down and the file completes at 0.
     */
    public void testTick()
    {
        item.startTimer();
        item.tick(10);
        assertEquals(50, item.getTimer());
        assertFalse(item.isComplete());

        item.tick(50);
        assertEquals(0, item.getTimer());
        assertTrue(item.isComplete());
        assertEquals(DownloadItem.Status.COMPLETE, item.getStatus());
    }


    /**
     * Tests that the timer stops at 0 instead of going negative.
     */
    public void testTickPastZero()
    {
        item.startTimer();
        item.tick(100);
        assertEquals(0, item.getTimer());
        assertTrue(item.isComplete());
    }
}
