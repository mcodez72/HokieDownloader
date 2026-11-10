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

    private LinkedQueue<DownloadItem> queue;
    private List<DownloadItem> completed;

    /**
     * Creates an empty queue and an empty completed list.
     */
    public DownloadManager()
    {
        queue = new LinkedQueue<DownloadItem>();
        completed = new ArrayList<DownloadItem>();
    }


    /**
     * Parses the URL, makes a file with a 1 minute timer and adds it to the
     * queue, and if it is the only file its timer starts right away.
     *
     * @param url
     *            the URL the user pasted
     * @return the new file
     * @throws IllegalArgumentException
     *             if the URL is bad or already queued
     */
    public DownloadItem addDownload(String url)
    {
        String fileName = UrlParser.parseFileName(url);

        for (DownloadItem item : queue.toList())
        {
            if (item.getUrl().equals(url))
            {
                throw new IllegalArgumentException("Already in queue");
            }
        }

        DownloadItem newItem = new DownloadItem(url, fileName);
        queue.enqueue(newItem);
        if (queue.size() == 1)
        {
            newItem.startTimer();
        }
        return newItem;
    }


    /**
     * Counts down the front file, and when it hits 0 it moves to completed and
     * the next file's timer starts.
     *
     * @param seconds
     *            how many seconds passed
     * @throws IllegalArgumentException
     *             if seconds is negative
     */
    public void tick(int seconds)
    {
        if (seconds < 0)
        {
            throw new IllegalArgumentException("Seconds cannot be negative");
        }
        if (queue.isEmpty())
        {
            return;
        }

        DownloadItem front = queue.peek();
        front.tick(seconds);
        if (front.isComplete())
        {
            completed.add(queue.dequeue());
            if (!queue.isEmpty())
            {
                queue.peek().startTimer();
            }
        }
    }


    /**
     * Gives the file whose timer is counting down right now.
     *
     * @return the front file, or null if the queue is empty
     */
    public DownloadItem getCurrent()
    {
        if (queue.isEmpty())
        {
            return null;
        }
        return queue.peek();
    }


    /**
     * Gives a copy of every file in the queue from front to back.
     *
     * @return the queued files
     */
    public List<DownloadItem> getQueued()
    {
        return queue.toList();
    }


    /**
     * Gives a copy of the finished files in the order they finished.
     *
     * @return the completed files
     */
    public List<DownloadItem> getCompleted()
    {
        return new ArrayList<DownloadItem>(completed);
    }


    /**
     * Checks if the queue is empty and at least one file finished.
     *
     * @return true if all files were downloaded
     */
    public boolean isAllDone()
    {
        return queue.isEmpty() && !completed.isEmpty();
    }
}
