package hokiedownloader;

import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests DownloadManager without any view, thus it proves the model stands on
 * its own and tick() lets us skip waiting a real minute.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class DownloadManagerTest
    extends TestCase
{

    private static final String URL_A = "https://vt.edu/files/notes.pdf";
    private static final String URL_B = "https://vt.edu/files/lab3.zip";

    private DownloadManager manager;

    /**
     * Creates an empty manager before each test.
     */
    public void setUp()
    {
        manager = new DownloadManager();
    }


    /**
     * Tests that a new manager is empty and not done.
     */
    public void testNewManager()
    {
        assertNull(manager.getCurrent());
        assertEquals(0, manager.getQueued().size());
        assertEquals(0, manager.getCompleted().size());
        assertFalse(manager.isAllDone());
    }


    /**
     * Tests that the first file starts right away and the second one waits.
     */
    public void testAddDownload()
    {
        DownloadItem a = manager.addDownload(URL_A);
        DownloadItem b = manager.addDownload(URL_B);
        assertEquals("notes.pdf", a.getFileName());
        assertEquals(DownloadItem.Status.DOWNLOADING, a.getStatus());
        assertEquals(DownloadItem.Status.WAITING, b.getStatus());
        assertEquals(a, manager.getCurrent());
        assertEquals(2, manager.getQueued().size());
    }


    /**
     * Tests that a bad URL is rejected and not added.
     */
    public void testAddBadUrl()
    {
        try
        {
            manager.addDownload("hello");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Not a valid URL", e.getMessage());
        }
        assertEquals(0, manager.getQueued().size());
    }


    /**
     * Tests that the same URL can not be added twice.
     */
    public void testAddDuplicate()
    {
        manager.addDownload(URL_A);
        try
        {
            manager.addDownload(URL_A);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Already in queue", e.getMessage());
        }
        assertEquals(1, manager.getQueued().size());
    }


    /**
     * Tests that when the front file hits 0 it completes and the next file
     * starts, then all files are done.
     */
    public void testTick()
    {
        DownloadItem a = manager.addDownload(URL_A);
        DownloadItem b = manager.addDownload(URL_B);

        manager.tick(59);
        assertEquals(1, a.getTimer());

        manager.tick(1);
        assertTrue(a.isComplete());
        assertEquals(b, manager.getCurrent());
        assertEquals(DownloadItem.Status.DOWNLOADING, b.getStatus());
        assertEquals(1, manager.getCompleted().size());
        assertFalse(manager.isAllDone());

        manager.tick(60);
        assertNull(manager.getCurrent());
        assertEquals(2, manager.getCompleted().size());
        assertTrue(manager.isAllDone());
    }


    /**
     * Tests that ticking an empty queue does nothing.
     */
    public void testTickEmpty()
    {
        manager.tick(5);
        assertNull(manager.getCurrent());
        assertEquals(0, manager.getCompleted().size());
    }


    /**
     * Tests that a negative tick is rejected.
     */
    public void testNegativeTick()
    {
        try
        {
            manager.tick(-5);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Seconds cannot be negative", e.getMessage());
        }
    }
}
