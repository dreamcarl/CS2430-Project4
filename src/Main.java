/**
 * Team Name - Team SIX!
 * Team Members - Carlos Recinos, Nelson Long, & Christopher Reynolds
 * CS-2430-502-Spring 2026
 * Programming Project 4 - Capstone
 * @author Carlos Recinos (Primary Author)
 * @author Nelson Long (Secondary Author)
 * @author Christopher Reynolds (Secondary Author)
 */
package src;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Entry point for the Monopoly movement simulator.
 * Execution order:
 *   1. Run 10 independent simulations under Strategy A (immediate jail exit).
 *   2. Run 10 independent simulations under Strategy B (try for doubles in jail).
 *   3. Write full CSV data files for both strategies (Part 4).
 *   4. Print the comparative summary to stdout (Part 5).
 *
 * Output files produced are put into a result directory.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("=== Monopoly Movement Simulator - Team SIX! ===");
        System.out.println();

        // Run all Strategy A simulations (IMMEDIATE_EXIT)
        System.out.println("Running Strategy A (Immediate Exit) - 10 runs x 1,000,000 turns each...");
        List<SimulationResult> stratAResults = Simulator.runBatch(Player.JailExitStrategy.IMMEDIATE_EXIT);

        System.out.println();

        // Run all Strategy B simulations (TRY_FOR_DOUBLES)
        System.out.println("Running Strategy B (Try for Doubles) - 10 runs x 1,000,000 turns each...");
        List<SimulationResult> stratBResults = Simulator.runBatch(Player.JailExitStrategy.TRY_FOR_DOUBLES);

        System.out.println();
        String outputDir = "results";

        // Create the results directory if it does not already exist
        new File(outputDir).mkdirs();

        // Write CSV results files into the results directory
        System.out.println("Writing CSV results files...");
        Reporter.writeCSV(stratAResults, outputDir + "/strategy_a_results.csv");
        Reporter.writeCSV(stratBResults, outputDir + "/strategy_b_results.csv");

        // Print all datasets
        Reporter.printAllDatasets(stratAResults, "Strategy A (Immediate Exit)");
        Reporter.printAllDatasets(stratBResults, "Strategy B (Try for Doubles)");

        //Print Summary
        Reporter.printSummary(stratAResults, stratBResults);
    }
}
