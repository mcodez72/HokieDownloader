package hokiedownloader;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class ConsoleView {
    
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n--- Hokie Downloader ---");
        System.out.println("1. Download a file");
        System.out.println("2. Display download queue");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
    }

    public String readLine() {
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
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
