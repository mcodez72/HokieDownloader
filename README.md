# HokieDownloader
A VT / Hokie themed download manager simulator that showcases the implementation of the Queue data structure.

# Hokie Downloader - Deliverable 3

## How to Run
*   **To run the Thin View:** Execute the throwaway `main` method located in `ConsoleView.java`. It will display the MVP menu, wait for input, and print fake data to prove the UI runs independently of the backend.
*   **To run the Model:** Run LinkedQueueTest, DownloadItemTest, and DownloadManagerTest as JUnit tests; they use the model classes directly with no view, and DownloadManagerTest calls tick() to skip the real 60-second wait.

## Reflection

### How did your design absorb the change?
*   **Classes Touched:** Our priority feature touched `ConsoleView` (to display the new menu option)[cite: 43, 55], `DownloadController` (to add the `handleAddPriorityDownload` routing)[cite: 44, 56], and `DownloadManager` (to implement the separate `priorityQueue` and update `tick()`, `getQueued()`, and `getCurrent()`)[cite: 59]. 
*   **Comparison to D3 Prediction:** This matched our D3 prediction perfectly. We successfully avoided modifying `UrlParser`, `DownloadItem`, `ConsoleView`'s `readLine()`, and our core `LinkedQueue`[cite: 51, 53, 57]. We were pleasantly surprised by how easily `DownloadManager` absorbed the dual-queue logic without needing completely new data structures.

### Did the change cross the model/view line?
No, the change did not cross the model/view line. The View did not have to learn anything new about the Model. Because we updated `DownloadManager.getQueued()` to merge the `priorityQueue` and standard `queue` behind the scenes[cite: 59], the `ConsoleView` was able to display urgent files jumping the line using its exact same existing display logic[cite: 55].

### What changed from your D3 plan?
Our plan remained almost entirely intact. The only minor deviation was how we implemented the priority routing in the model. Instead of passing a priority integer dynamically to a single `addDownload` method as originally planned, we overloaded the method (`addDownload(int level, String url)`) and utilized constants (`NORMAL` and `URGENT`) to make the controller's implementation cleaner[cite: 56, 59].

### What doesn't work yet?
We have fully implemented the MVP and the Priority feature, but we still need to polish the console output slightly to ensure the live countdown doesn't leave visual artifacts in certain edge cases (like transitioning from a very long filename to a short one). 

### Design Improvement for Future Features
**To make adding the next feature cheaper**, we should extract the controller's menu routing into a Command pattern or map structure. This design change would allow us to add new menu options (like pausing/canceling a download) without having to manually modify the `if/else` block inside `DownloadController.handleMenuChoice()` every time.

---

### What doesn't work yet?
The complete MVP path end-to-end. While the Model logic and View UI work in isolation, the `DownloadController` integration (Checkpoint 1) is not fully wired yet. Bad menu inputs won't actively re-prompt the user until the controller's main loop is finished.

---

## Next Steps

### What is left for the final build?
1.  **Final Polish:** Clean up any remaining console formatting artifacts when the live countdown ticks down.
2.  **Robustness Check:** Do a final sweep of adversarial inputs (like massive strings or weird symbols) to guarantee the `UrlParser` catches absolutely everything.
3.  **Demo Prep:** Script and rehearse the final presentation.


### Our Change Feature (Deliverable 4)
**Feature:** Priority Downloads. Users will be able to designate a URL as "urgent," allowing it to bypass the standard queue and start downloading immediately after the current file finishes.
*   **Classes it WILL touch:** `DownloadController` (to parse the new menu option), `ConsoleView.showMessage()` (to display the new option), and `DownloadManager.addDownload(priority integer, URL)` (to handle the routing of priority items). 
*   **Classes it SHOULD NOT touch:** `UrlParser`, `DownloadItem`, `ConsoleView`'s `readLine()` method, and the core `LinkedQueue` (we will likely add a separate queue structure for priority items to keep the original FIFO queue intact).

### Stretch Goals
*   **Committed:** Priority downloads (successfully implemented).
*   **Dropped:** GUI implementation and actual Java networking protocols (dropped to focus on ensuring our MVC decoupling and Priority logic were perfect).


### New Tests Needed
*   Tests for the new Controller menu input (Option 4).
*   Tests in `DownloadManager` to verify priority files correctly jump ahead of standard queued files.
*   Tests to ensure bad inputs on the priority prompt are handled gracefully.

### What will your 3-minute demo show?
**Our demo path is timed to fit 3 minutes.** We will walk through:
1.  Adding a normal download to the queue.
2.  Adding an URGENT priority download.
3.  Displaying the queue to watch the live countdown, demonstrating the urgent file correctly bypassing the normal queue to start immediately after the active file finishes.
4.  Entering bad inputs (like an empty URL or invalid menu option) to show the program surviving without crashing.


### Owner for Each Remaining Item
*   **Mikey:** Final UI string polish and ensuring console countdown formatting works cleanly.
*   **Allycia:** Final sweep of JUnit test coverage and edge-case execution.
*   **Shafiur:** Leading the live demo presentation and walking through the user path.


## Attribution

| Class / Test File | Team Member | Contribution (How) |
| :--- | :--- | :--- |
| `UrlParser.java` | Mikey | Wrote logic, validated URI substring index math, GenAI-assisted. |
| `ConsoleView.java` | Mikey | Wrote class, stubbed UI, implemented throwaway driver, GenAI-assisted. |
| `UrlParserTest.java` | Mikey | Wrote JUnit tests for UrlParser covering normal URLs, empty, null, and too-long input, missing file names, and invalid URLs. |
| `LinkedQueue.java` | Allycia | Wrote our own linked queue with head and tail pointers. |
| `DownloadManager.java` | Allycia | Wrote the model class that holds the queue and completed list, adds downloads, and counts down the front file. |
| `DownloadItem.java` | Allycia | Wrote the class for one download with its URL, file name, 60-second timer, and status.|
| Model JUnit Tests | Shafiur | Completed the test classes for the project. GenAI-Assisted. |
| `DownloadController.java`| Shafiur | Wrote the menu loop, the menu input checks, and the handlers that connect the view to the model. |
| `Main.java` | Shafiur | Wrote main, which creates the model, view, and controller and starts the program, but this was integrated into DownloadController. |

**GenAI Note:** 
Mikey used GenAI as a sounding board to debug the `String.substring` index math when extracting filenames from the `URI.getPath()` method, and to verify the `ConsoleView` met the "thin view" decoupling requirements of the spec. GenAI was also used to help format this README.md template based on our Deliverable 1 & 2 planning documents.
