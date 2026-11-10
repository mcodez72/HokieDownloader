package hokiedownloader;

// -------------------------------------------------------------------------
/**
 * MODEL. One file download with its own 1 minute timer, which only counts down
 * once the file reaches the front of the queue.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class DownloadItem
{

    /**
     * The three states a download can be in.
     */
    public enum Status
    {
        WAITING,
        DOWNLOADING,
        COMPLETE
    }

    /**
     * Every file gets the same 1 minute timer.
     */
    public static final int TIMER_SECONDS = 60;

    private String url;
    private String fileName;
    private int timer;
    private Status status;

    /**
     * Creates a download with a full 60 second timer that is WAITING.
     *
     * @param url
     *            the full URL the user pasted
     * @param fileName
     *            the file name parsed from the URL
     */
    public DownloadItem(String url, String fileName)
    {
        this.url = url;
        this.fileName = fileName;
        this.timer = TIMER_SECONDS;
        this.status = Status.WAITING;
    }


    /**
     * Sets the file to DOWNLOADING once it reaches the front of the queue.
     */
    public void startTimer()
    {
        status = Status.DOWNLOADING;
    }


    /**
     * Takes time off the timer without going below 0, and marks the file
     * COMPLETE once it hits 0.
     *
     * @param seconds
     *            how many seconds passed
     */
    public void tick(int seconds)
    {
        timer = timer - seconds;
        if (timer <= 0)
        {
            timer = 0;
            status = Status.COMPLETE;
        }
    }


    /**
     * Checks if the timer has reached 0.
     *
     * @return true if the file is complete
     */
    public boolean isComplete()
    {
        return status == Status.COMPLETE;
    }


    /**
     * Gives the full URL.
     *
     * @return the URL
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Gives the parsed file name.
     *
     * @return the file name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * Gives the seconds left on the timer.
     *
     * @return the seconds left
     */
    public int getTimer()
    {
        return timer;
    }


    /**
     * Gives the current status.
     *
     * @return the status
     */
    public Status getStatus()
    {
        return status;
    }
}
