package hokiedownloader;

import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class ConsoleView
{

    private Scanner scanner = new Scanner(System.in);

    public void displayMenu()
    {
        System.out.println("\n--- Hokie Downloader ---");
        System.out.println("1. Download a file");
        System.out.println("2. Display download queue");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
    }


    public String readLine()
    {
        try
        {
            return scanner.nextLine();
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }


    public void showMessage(String message)
    {
        System.out.println(message);
    }


    /**
     * Prints the completed files, the file downloading now and the files still
     * waiting.
     *
     * @param current
     *            the file downloading now, or null
     * @param queued
     *            every file in the queue from front to back
     * @param completed
     *            the finished files
     */
    public void showQueue(
        DownloadItem current,
        List<DownloadItem> queued,
        List<DownloadItem> completed)
    {
        System.out.println("------------------------------");
        for (DownloadItem item : completed)
        {
            System.out.println(item.getFileName() + " - Complete");
        }
        for (DownloadItem item : queued)
        {
            if (item == current)
            {
                System.out.println(
                    "Downloading: " + item.getFileName() + " - "
                        + item.getTimer() + "s remaining");
            }
            else
            {
                System.out.println("Waiting: " + item.getFileName());
            }
        }
        System.out.println("------------------------------");
    }


    public static void main(String[] args)
    {
        ConsoleView view = new ConsoleView();

        view.displayMenu();

        String choice = view.readLine();

        view.showMessage("You CHOSE: " + choice);
        // next pass it to shafiur's controller so it can
        // handle bad input cases
        System.out.println("\n[Testing Fake Data]");
        view.showMessage("Downloading: notes.pdf - 45s remaining");
        view.showMessage("Complete");
        view.showMessage("All files downloaded");
    }
}
