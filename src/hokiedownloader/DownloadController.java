package hokiedownloader;

// -------------------------------------------------------------------------
/**
 * CONTROLLER. Sits between the model and the view, where it checks the user's
 * input, calls the model, catches its exceptions and tells the view what to
 * show.
 *
 * @author shafiursarker
 * @version Nov 10, 2026
 */
public class DownloadController
{

    private DownloadManager model;
    private ConsoleView view;

    /**
     * Connects the model and the view.
     *
     * @param model
     *            the model to control
     * @param view
     *            the view to update
     */
    public DownloadController(DownloadManager model, ConsoleView view)
    {
        this.model = model;
        this.view = view;
    }


    /**
     * Starts the program by connecting the model, view and controller.
     *
     * @param args
     *            not used
     */
    public static void main(String[] args)
    {
        DownloadManager model = new DownloadManager();
        ConsoleView view = new ConsoleView();
        DownloadController controller = new DownloadController(model, view);
        controller.run();
    }


    /**
     * Shows the menu and handles choices until the user exits.
     */
    public void run()
    {
        boolean running = true;
        while (running)
        {
            view.displayMenu();
            String input = view.readLine();
            running = handleMenuChoice(input);
        }
        view.showMessage("Goodbye, Hokie!");
    }


    /**
     * Checks the menu choice and calls the matching handler, thus anything that
     * is not 1, 2 or 3 just shows an error and the menu again.
     *
     * @param input
     *            what the user typed, or null if the input ended
     * @return false when the program should exit
     */
    public boolean handleMenuChoice(String input)
    {
        if (input == null)
        {
            return false;
        }
        input = input.trim();
        if (input.equals("1"))
        {
            handleAddDownload();
            return true;
        }
        else if (input.equals("2"))
        {
            handleShowQueue();
            return true;
        }
        else if (input.equals("3"))
        {
            return false;
        }
        else
        {
            view.showMessage("Error: Please enter 1, 2, or 3.");
            return true;
        }
    }


    /**
     * Asks for a URL and adds it to the queue, or shows what went wrong.
     */
    public void handleAddDownload()
    {
        view.showMessage("Paste the file URL:");
        String url = view.readLine();
        if (url == null)
        {
            return;
        }
        try
        {
            DownloadItem item = model.addDownload(url);
            view.showMessage("Added " + item.getFileName() + " to the queue.");
        }
        catch (IllegalArgumentException e)
        {
            view.showMessage("Error: " + e.getMessage());
        }
    }


    /**
     * Moves the model forward 1 second at a time and shows the queue until
     * every file is downloaded.
     */
    public void handleShowQueue()
    {
        if (model.getCurrent() == null)
        {
            view.showMessage("Queue is empty.");
            return;
        }
        while (model.getCurrent() != null)
        {
            view.showQueue(
                model.getCurrent(),
                model.getQueued(),
                model.getCompleted());
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                return;
            }
            model.tick(1);
        }
        view.showQueue(null, model.getQueued(), model.getCompleted());
        view.showMessage("All files downloaded");
    }
}
