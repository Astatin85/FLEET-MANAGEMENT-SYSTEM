AP Assignment 3: Fleet Highway Simulator
=========================================================

1. PROJECT OVERVIEW
-------------------
This project represents the evolution of the Fleet Management System into a concurrent, graphical application. It moves beyond static data storage to simulate a real-time highway environment where multiple Vehicle entities operate simultaneously. The core objectives were to implement multithreading using the Runnable interface, manage a shared resource (the Highway distance counter), demonstrate the occurrence of race conditions due to unsynchronized access, and resolve them using Java's intrinsic locking mechanisms.

2. COMPILATION AND EXECUTION STEPS
----------------------------------
The source code is structured into modular packages: `app`, `fleet`, `vehicles`, `interfaces`, and `exceptions`.

Prerequisites: Ensure you are in the root source directory (e.g., `src/`) where the package folders are visible.

Step 1: Compile the System
Run the following command to compile all dependencies:
javac app/*.java fleet/*.java vehicles/*.java interfaces/*.java exceptions/*.java

Step 2: Run the Simulation
Execute the main GUI class:
java app.fleetSimGUI

Step 3: Testing
- To test the Race Condition: Uncheck "Enable Synchronization" and click "Start". Observe the "Shared Highway Distance" vs. the sum of vehicle mileages.
- To test the Fix: Check "Enable Synchronization", Reset/Restart, and observe the correct calculation.

3. DESIGN AND GUI LAYOUT
------------------------
The application interface is built using Java Swing, utilizing a `BorderLayout` to organize components logically:

- **North Panel (Controls):** Houses the primary user controls. The "Start Simulation" and "Stop All" buttons manage the thread lifecycle. The "Enable Synchronization" checkbox is the toggle switch for the assignment's core experiment (Safe vs. Unsafe mode).
- **Center Panel (Visualization):** A scrollable `JPanel` using a `GridLayout`. Each vehicle is rendered as a distinct row containing its real-time status (ID, Model, Mileage) and a "Refuel" button. The row changes color (e.g., to red) when a vehicle runs out of fuel/pauses, providing immediate visual feedback.
- **South Panel (Shared Resource):** Displays the "Shared Highway Distance." This label represents the critical section of data that all threads compete to update.

4. THREAD CONTROL IMPLEMENTATION
--------------------------------
The simulation does not run on the main application thread. Instead, we utilize a custom `VehicleThread` class that implements `Runnable`.
- **Initialization:** When "Start" is clicked, the GUI wraps each `Vehicle` object into a `VehicleThread`.
- **Execution:** We call `thread.start()`, which triggers the `run()` loop. This loop handles the movement logic (`vehicle.move()`), updates the shared highway, and sleeps for a set interval (simulating travel time).
- **Pausing/Resuming:** Threads check a boolean `paused` flag. If a vehicle runs out of fuel, the flag is set to true, and the thread enters a wait state. The "Refuel" button on the GUI resets this flag, allowing the thread to resume execution.

5. RACE CONDITION AND SYNCHRONIZATION FIX
-----------------------------------------
**The Problem (Unsynchronized):**
As demonstrated in the attached screenshot `[incorrect_behavior.png]`, when synchronization is disabled, the "Shared Highway Distance" is significantly lower than the sum of the individual vehicle mileages.
This occurs because the operation `totalDistance += distance` is not atomic. It consists of three steps:
1. Read `totalDistance`.
2. Add `distance` to value.
3. Write new value to `totalDistance`.
Without locks, two vehicles may read the value "100" simultaneously. Both add "5" and write "105". One update is effectively lost (overwritten), leading to data inconsistency.
*Note: To capture this behavior clearly, thread sleep times were adjusted to increase collision frequency.*

**The Fix (Synchronized):**
As shown in screenshot `[correct_behavior.png]`, enabling synchronization corrects the total.
We resolved the issue by wrapping the update logic in a `synchronized(this)` block within the `Highway` class. This creates a "monitor lock." Only one thread can hold the lock at a time; others must wait their turn. This forces the updates to happen sequentially (100 -> 105 -> 110), guaranteeing data integrity.

6. GUI THREAD SAFETY (EDT)
--------------------------
Java Swing is not thread-safe. Modifying UI components (like `JLabel.setText`) directly from a background `VehicleThread` can cause race conditions in the rendering engine or application crashes.
To address this, all GUI updates within `VehicleThread` are wrapped in:
`SwingUtilities.invokeLater(() -> { ... });`
This method places the update task onto the Event Dispatch Thread (EDT), ensuring that the UI remains responsive and draws correctly while the heavy simulation logic runs in the background.
