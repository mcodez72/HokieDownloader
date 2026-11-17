package hokiedownloader;

import java.util.ArrayList;
import java.util.List;

// -------------------------------------------------------------------------
/**
 * MODEL. The brain of HokieDownloader that owns the queue and the completed
 * list, and it never prints or reads input thus it can be tested without a
 * view.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class DownloadManager
{

public static final int NORMAL = 0;
public static final int URGENT = 1;

private DownloadItem current;
private LinkedQueue<DownloadItem> priority;
private LinkedQueue<DownloadItem> queue;
private List<DownloadItem> completed;

public DownloadManager()
{
    current = null;
    priority = new LinkedQueue<DownloadItem>();
    queue = new LinkedQueue<DownloadItem>();
    completed = new ArrayList<DownloadItem>();
}


/**
 * Adds a normal download. Same as addDownload(NORMAL, url).
 *
 * @param url the URL the user pasted
 * @return the new file
 * @throws IllegalArgumentException if the URL is bad or already queued
 */
public DownloadItem addDownload(String url)
{
    return addDownload(NORMAL, url);
}


/**
 * Parses the URL and adds a file. If nothing is downloading it starts
 * right away. Otherwise an URGENT file waits in the priority line, which is
 * served before the normal line, and a NORMAL file waits in the normal line.
 *
 * @param level NORMAL (0) or URGENT (1)
 * @param url the URL the user pasted
 * @return the new file
 * @throws IllegalArgumentException if the level is not 0 or 1, the URL is
 *             bad, or the URL is already queued
 */
public DownloadItem addDownload(int level, String url)
{
    if (level != NORMAL && level != URGENT)
    {
        throw new IllegalArgumentException("Priority must be 0 or 1");
    }
    String fileName = UrlParser.parseFileName(url);
    url = url.trim();

    for (DownloadItem item : getQueued())
    {
        if (item.getUrl().equals(url))
        {
            throw new IllegalArgumentException("Already in queue");
        }
    }

    DownloadItem newItem = new DownloadItem(url, fileName);
    if (current == null)
    {
        current = newItem;
        current.startTimer();
    }
    else if (level == URGENT)
    {
        priority.enqueue(newItem);
    }
    else
    {
        queue.enqueue(newItem);
    }
    return newItem;
}

/**
 * Counts down the current file, and when it hits 0 it moves to completed and
 * the next file starts (urgent files first).
 *
 * @param seconds how many seconds passed
 * @throws IllegalArgumentException if seconds is negative
 */
public void tick(int seconds)
{
    if (seconds < 0)
    {
        throw new IllegalArgumentException("Seconds cannot be negative");
    }
    if (current == null)
    {
        return;
    }
    current.tick(seconds);
    if (current.isComplete())
    {
        completed.add(current);
        if (!priority.isEmpty())
        {
            current = priority.dequeue();
        }
        else if (!queue.isEmpty())
        {
            current = queue.dequeue();
        }
        else
        {
            current = null;
        }
        if (current != null)
        {
            current.startTimer();
        }
    }
}


public DownloadItem getCurrent()
{
    return current;
}


/**
 * Gives a copy of every unfinished file in the order it will run: the
 * current file, then urgent files, then normal files.
 *
 * @return the queued files
 */
public List<DownloadItem> getQueued()
{
    List<DownloadItem> all = new ArrayList<DownloadItem>();
    if (current != null)
    {
        all.add(current);
    }
    all.addAll(priority.toList());
    all.addAll(queue.toList());
    return all;
}


public boolean isAllDone() 
 {
    return current == null && !completed.isEmpty();
 }
    
}
