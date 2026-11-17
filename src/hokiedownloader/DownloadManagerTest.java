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
    private static final String URL_C = "https://vt.edu/files/song.mp3";
    private static final String URL_D = "https://vt.edu/files/essay.docx";

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


    /**
     * Tests that the same URL with extra spaces is still caught as a duplicate.
     */
    public void testAddDuplicateWithSpaces()
    {
        manager.addDownload(URL_A);
        try
        {
            manager.addDownload("  " + URL_A + "  ");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Already in queue", e.getMessage());
        }
        assertEquals(1, manager.getQueued().size());
    }


    /**
     * Tests that getCompleted gives the finished files in the order they
     * finished.
     */
    public void testGetCompleted()
    {
        DownloadItem a = manager.addDownload(URL_A);
        DownloadItem b = manager.addDownload(URL_B);
        manager.tick(60);
        manager.tick(60);
        assertEquals(2, manager.getCompleted().size());
        assertEquals(a, manager.getCompleted().get(0));
        assertEquals(b, manager.getCompleted().get(1));
    }


    /**
     * Tests that changing the list from getCompleted does not change the
     * manager.
     */
    public void testGetCompletedIsCopy()
    {
        manager.addDownload(URL_A);
        manager.tick(60);
        manager.getCompleted().clear();
        assertEquals(1, manager.getCompleted().size());
    }


    /**
     * Tests that an urgent file starts right away when nothing is downloading.
     */
    public void testAddUrgentWhenEmpty()
    {
        DownloadItem a = manager.addDownload(DownloadManager.URGENT, URL_A);
        assertEquals(a, manager.getCurrent());
        assertEquals(DownloadItem.Status.DOWNLOADING, a.getStatus());
    }


    /**
     * Tests that an urgent file goes after the current file but before the
     * normal files, thus it does not interrupt the current download.
     */
    public void testAddUrgentGoesNext()
    {
        DownloadItem a = manager.addDownload(URL_A);
        DownloadItem b = manager.addDownload(URL_B);
        DownloadItem c = manager.addDownload(DownloadManager.URGENT, URL_C);

        assertEquals(a, manager.getCurrent());
        assertEquals(DownloadItem.Status.WAITING, c.getStatus());
        assertEquals(a, manager.getQueued().get(0));
        assertEquals(c, manager.getQueued().get(1));
        assertEquals(b, manager.getQueued().get(2));

        manager.tick(60);
        assertEquals(c, manager.getCurrent());
        assertEquals(DownloadItem.Status.DOWNLOADING, c.getStatus());

        manager.tick(60);
        assertEquals(b, manager.getCurrent());
    }


    /**
     * Tests that two urgent files stay in FIFO order with each other.
     */
    public void testTwoUrgentFifo()
    {
        manager.addDownload(URL_A);
        DownloadItem c = manager.addDownload(DownloadManager.URGENT, URL_C);
        DownloadItem d = manager.addDownload(DownloadManager.URGENT, URL_D);

        manager.tick(60);
        assertEquals(c, manager.getCurrent());
        manager.tick(60);
        assertEquals(d, manager.getCurrent());
        manager.tick(60);
        assertNull(manager.getCurrent());
        assertTrue(manager.isAllDone());
    }


    /**
     * Tests that a NORMAL level works the same as the one parameter
     * addDownload.
     */
    public void testAddNormalLevel()
    {
        manager.addDownload(URL_A);
        DownloadItem b = manager.addDownload(DownloadManager.NORMAL, URL_B);
        DownloadItem c = manager.addDownload(DownloadManager.URGENT, URL_C);
        assertEquals(c, manager.getQueued().get(1));
        assertEquals(b, manager.getQueued().get(2));
    }


    /**
     * Tests that a priority level other than 0 or 1 is rejected and nothing is
     * added.
     */
    public void testAddBadLevel()
    {
        try
        {
            manager.addDownload(2, URL_A);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Priority must be 0 or 1", e.getMessage());
        }
        try
        {
            manager.addDownload(-1, URL_A);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Priority must be 0 or 1", e.getMessage());
        }
        assertNull(manager.getCurrent());
        assertEquals(0, manager.getQueued().size());
    }


    /**
     * Tests that a bad URL is rejected for an urgent file too.
     */
    public void testAddUrgentBadUrl()
    {
        manager.addDownload(URL_A);
        try
        {
            manager.addDownload(DownloadManager.URGENT, "hello");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Not a valid URL", e.getMessage());
        }
        assertEquals(1, manager.getQueued().size());
    }


    /**
     * Tests that a URL already in either line can not be added as urgent.
     */
    public void testAddUrgentDuplicate()
    {
        manager.addDownload(URL_A);
        manager.addDownload(URL_B);
        manager.addDownload(DownloadManager.URGENT, URL_C);

        try
        {
            manager.addDownload(DownloadManager.URGENT, URL_A);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Already in queue", e.getMessage());
        }
        try
        {
            manager.addDownload(DownloadManager.URGENT, URL_B);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Already in queue", e.getMessage());
        }
        try
        {
            manager.addDownload(DownloadManager.URGENT, URL_C);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Already in queue", e.getMessage());
        }
        assertEquals(3, manager.getQueued().size());
    }
}
