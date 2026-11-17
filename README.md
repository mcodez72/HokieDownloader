# HokieDownloader
A VT / Hokie themed download manager simulator that showcases the implementation of the Queue data structure.

# Hokie Downloader - Deliverable 4

## How to Run
*   **To run the Program:** Run the `main` method located in `DownloadController.java`. This will launch the interactive console application where you can test normal downloads, urgent priority downloads, and view the live queue.
*   **To run the Tests:** Run `LinkedQueueTest`, `DownloadItemTest`, `DownloadManagerTest`, and `UrlParserTest` as JUnit tests. They execute the model classes directly without any UI dependencies, covering all core methods, interleaved enqueues/dequeues, and bad-input cases.

## Reflection

### How did your design absorb the change?
*   **Classes Touched:** Our priority feature touched `ConsoleView` (to display the new menu option), `DownloadController` (to add the `handleAddPriorityDownload` routing), and `DownloadManager` (to implement the separate `priorityQueue` and update `tick()`, `getQueued()`, and `getCurrent()`). 
*   **Comparison to D3 Prediction:** This matched our D3 prediction perfectly. We successfully avoided modifying `UrlParser`, `DownloadItem`, `ConsoleView`'s `readLine()`, and our core `LinkedQueue`. We were pleasantly surprised by how easily `DownloadManager` absorbed the dual-queue logic without needing completely new data structures.

### Did the change cross the model/view line?
No, the change did not cross the model/view line. The View did not have to learn anything new about the Model. Because we updated `DownloadManager.getQueued()` to merge the `priorityQueue` and standard `queue` behind the scenes, the `ConsoleView` was able to display urgent files jumping the line using its exact same existing display logic.

### What changed from your D3 plan?
Our plan remained almost entirely intact. The only minor deviation was how we implemented the priority routing in the model. Instead of passing a priority integer dynamically to a single `addDownload` method as originally planned, we overloaded the method (`addDownload(int level, String url)`) and utilized constants (`NORMAL` and `URGENT`) to make the controller's implementation cleaner.

### What doesn't work yet?
We have fully implemented the MVP and the Priority feature, but we still need to polish the console output slightly to ensure the live countdown doesn't leave visual artifacts in certain edge cases (like transitioning from a very long filename to a short one). 

### Design Improvement for Future Features
**To make adding the next feature cheaper**, we should extract the controller's menu routing into a Command pattern or map structure. This design change would allow us to add new menu options (like pausing/canceling a download) without having to manually modify the `if/else` block inside `DownloadController.handleMenuChoice()` every time.

---

## Next Steps

### What is left for the final build?
1.  **Final Polish:** Clean up any remaining console formatting artifacts when the live countdown ticks down.
2.  **Robustness Check:** Do a final sweep of adversarial inputs (like massive strings or weird symbols) to guarantee the `UrlParser` catches absolutely everything.
3.  **Demo Prep:** Script and rehearse the final presentation.

### Stretch Goals
*   **Committed:** Priority downloads (successfully implemented).
*   **Dropped:** GUI implementation and actual Java networking protocols (dropped to focus on ensuring our MVC decoupling and Priority logic were perfect).

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
| `ConsoleView.java` | Mikey | Wrote class, stubbed UI, implemented single-line `\r` refresh logic, GenAI-assisted. |
| `UrlParserTest.java` | Mikey | Wrote JUnit tests for UrlParser covering normal URLs, empty, null, too-long input, missing file names, directory traversal, and invalid URLs. |
| `LinkedQueue.java` | Allycia | Wrote our own linked queue with head and tail pointers. |
| `DownloadManager.java` | Allycia | Implemented the two-queue Priority structure, tick logic, and queue merging. |
| `DownloadItem.java` | Allycia | Wrote the class for one download with its URL, file name, 60-second timer, and status.|
| Model JUnit Tests | Shafiur | Completed the test classes for the project, including interleaved enqueues/dequeues and priority tests. GenAI-Assisted. |
| `DownloadController.java`| Shafiur | Wrote the menu loop, input checks, `handleAddPriorityDownload`, and handlers connecting the view to the model. |
| `Main.java` | Shafiur | Wrote main, which creates the model, view, and controller and starts the program (integrated directly into DownloadController). |

**GenAI Note:** 
Mikey used GenAI as a sounding board to debug the `String.substring` index math when extracting filenames from the `URI.getPath()` method, to verify the `ConsoleView` met the "thin view" decoupling requirements of the spec, and to troubleshoot the Carriage Return (`\r`) logic for the single-line live countdown updates. GenAI was also used to help format this README.md template based on our Deliverable 1 & 2 planning documents and the Deliverable 4 rubric.
