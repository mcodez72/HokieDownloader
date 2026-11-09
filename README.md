# HokieDownloader
A VT / Hokie themed download manager simulator that showcases the implementation of the Queue data structure.

# Hokie Downloader - Deliverable 3

## How to Run
*   **To run the Thin View:** Execute the throwaway `main` method located in `ConsoleView.java`. It will display the MVP menu, wait for input, and print fake data to prove the UI runs independently of the backend.
*   **To run the Model:** [Allycia: Add 1 sentence here on how to run your JUnit tests or throwaway driver for the Queue/DownloadManager]

## Reflection

### What changed from the spec?
*   **Classes & Signatures:** We largely stuck to the spec. For `UrlParser.parseFileName`, we ensured it explicitly catches `URISyntaxException` and throws `IllegalArgumentException` as planned, but we refined the substring index logic to ensure trailing slashes are dropped cleanly.
*   **Validation:** Validation remains exactly where we specified: `UrlParser` handles bad links, `DownloadManager` handles duplicates, and `DownloadController` handles menu inputs. 
*   **Tests:** [Allycia: Briefly mention if you had to change any of your JUnit test assertions from the original plan]

### Did the model really stand alone?
Yes. The model and view were built in parallel using stubs. The model is completely decoupled and runs its own JUnit tests without any awareness of the UI. To make the view run for this prototype, we temporarily added a throwaway `main` method to `ConsoleView` that feeds it hard-coded strings, proving the model doesn't need to push data for the view to compile and run.

### What happened to the unknowns?
*   **Figured out:** We figured out how to safely parse filenames and drop fragments/queries using `java.net.URI` in the `UrlParser`. We also figured out how to simulate a 1-minute timer without freezing the app by using a manual `tick(int seconds)` method rather than background threads.
*   **Still open:** We still need to figure out exactly how to integrate priority downloads (our Deliverable 4 change feature) without breaking our underlying FIFO architecture. 

### What doesn't work yet?
The complete MVP path end-to-end. While the Model logic and View UI work in isolation, the `DownloadController` integration (Checkpoint 1) is not fully wired yet. Bad menu inputs won't actively re-prompt the user until the controller's main loop is finished.

## Next Steps

### What is left to finish the MVP?
1.  **Integration:** Connect `DownloadController` to `ConsoleView` and `DownloadManager`.
2.  **Controller Loop:** Implement the `run()` loop in the controller to keep the menu active.
3.  **End-to-End Testing:** Run the 5-step checklist to ensure data flows correctly across the model/view boundary.

### Our Change Feature (Deliverable 4)
**Feature:** Priority Downloads. Users will be able to designate a URL as "urgent," allowing it to bypass the standard queue and start downloading immediately after the current file finishes.
*   **Classes it WILL touch:** `DownloadController` (to parse the new menu option), `ConsoleView` (to display the new option), and `DownloadManager` (to handle the routing of priority items).
*   **Classes it SHOULD NOT touch:** `UrlParser`, `DownloadItem`, `ConsoleView`'s `readLine()` method, and the core `LinkedQueue` (we will likely add a separate queue structure for priority items to keep the original FIFO queue intact).

### Stretch Goals
*   **Committing to:** Priority downloads.
*   **Dropping:** We are dropping the GUI implementation and actual Java networking protocols to ensure we have enough time to perfectly implement and test the Priority Queue logic.

### New Tests Needed
*   Tests for the new Controller menu input (Option 4).
*   Tests in `DownloadManager` to verify priority files correctly jump ahead of standard queued files.
*   Tests to ensure bad inputs on the priority prompt are handled gracefully.

### Owner for Each Remaining Item
*   **Mikey:** Update `ConsoleView` to display priority options; refine UI strings.
*   **Allycia:** Implement the Priority Queue logic in the model; write JUnit tests for the new routing.
*   **Shafiur:** Finish `DownloadController` integration; add routing logic for the new priority menu choice.

## Attribution

| Class / Test File | Team Member | Contribution (How) |
| :--- | :--- | :--- |
| `UrlParser.java` | Mikey | Wrote logic, validated URI substring index math, GenAI-assisted. |
| `ConsoleView.java` | Mikey | Wrote class, stubbed UI, implemented throwaway driver, GenAI-assisted. |
| `UrlParserTest.java` | Mikey | [Mikey/Allycia: Add who wrote tests for the parser] |
| `LinkedQueue.java` | Allycia | [Allycia: Fill in contribution] |
| `DownloadManager.java` | Allycia | [Allycia: Fill in contribution] |
| `DownloadItem.java` | Allycia | [Allycia: Fill in contribution] |
| Model JUnit Tests | Allycia | [Allycia: Fill in contribution] |
| `DownloadController.java`| Shafiur | [Shafiur: Fill in contribution] |
| `Main.java` | Shafiur | [Shafiur: Fill in contribution] |

**GenAI Note:** 
Mikey used GenAI as a sounding board to debug the `String.substring` index math when extracting filenames from the `URI.getPath()` method, and to verify the `ConsoleView` met the "thin view" decoupling requirements of the spec. GenAI was also used to help format this README.md template based on our Deliverable 1 & 2 planning documents.
