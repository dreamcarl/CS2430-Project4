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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.List;

/**
 * Handles all simulation results by writing CSV data files and printing
 * the comparative summary for both jail-exit strategies.
 */
public class Reporter {

    private static final int TOP_N = 5;

    private static final int JAIL_INDEX = 10;

    /**
     * Writes all simulation results to a CSV file with columns:
     * Strategy, Run, N, SpaceIndex, SpaceName, Count, Percentage.
     *
     * @param results  simulation data from Simulator.runBatch()
     * @param filename path to the results CSV file
     * @throws IOException if the file cannot be written
     */
    public static void writeCSV(List<SimulationResult> results, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {

            writer.println("Strategy,Run,N,SpaceIndex,SpaceName,Count,Percentage");

            // One data row per square per snapshot
            for (SimulationResult r : results) {
                // Convert enum to a short label for the report
                String stratLabel = (r.strategy == Player.JailExitStrategy.IMMEDIATE_EXIT) ? "A" : "B";

                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    writer.printf("%s,%d,%d,%d,\"%s\",%d,%.4f%n",
                            stratLabel,
                            r.runNumber,
                            r.turnsCompleted,
                            i,
                            r.spaceNames[i],
                            r.landingCounts[i],
                            r.getPercentage(i));
                }
            }
        }
        System.out.println("CSV written: " + filename);
    }

    /**
     * Prints all 40 datasets for one strategy (10 runs x 4 n-values) to stdout.
     *
     * @param results        simulation data from Simulator.runBatch()
     * @param strategyLabel  strategy name used in section headers
     */
    public static void printAllDatasets(List<SimulationResult> results, String strategyLabel) {
        System.out.println();
        System.out.println("##########################################################");
        System.out.println(" PART 4 - ALL DATASETS: " + strategyLabel);
        System.out.println("##########################################################");

        // Iterate through every run number and every n-value to print all 40 datasets
        for (int run = 1; run <= Simulator.NUM_RUNS; run++) {
            for (int n : Simulator.N_VALUES) {

                // Find the SimulationResult that matches this run and n-value
                SimulationResult match = null;
                for (SimulationResult r : results) {
                    if (r.runNumber == run && r.turnsCompleted == n) {
                        match = r;
                        break;
                    }
                }
                if (match == null) continue;

                // Print the header for this individual dataset
                System.out.printf("%n--- %s | Run %d of %d | n = %,d ---%n", strategyLabel, run, Simulator.NUM_RUNS, n);
                System.out.printf("  %-5s  %-26s  %10s  %10s%n", "Idx", "Space Name", "Count", "Percentage");
                System.out.println("  " + "-".repeat(57));

                // Print one row per board square
                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    System.out.printf("  %-5d  %-26s  %10d  %9.4f%%%n",
                            i,
                            match.spaceNames[i],
                            match.landingCounts[i],
                            match.getPercentage(i));
                }
            }
        }
    }

    /**
     * Prints the comparative summary: top-5 squares per strategy and
     * a convergence check between Strategy A and B at n=1,000,000.
     *
     * @param stratA results from Strategy A simulations
     * @param stratB results from Strategy B simulations
     */
    public static void printSummary(List<SimulationResult> stratA, List<SimulationResult> stratB) {
        System.out.println();
        System.out.println("==========================================================");
        System.out.println(" COMPARATIVE OUTPUT / RUN SUMMARY");
        System.out.println("==========================================================");

        // Top-5 per strategy per n-value
        printTopSquaresSection("Strategy A (Immediate Exit)", stratA);
        printTopSquaresSection("Strategy B (Try for Doubles)", stratB);

        // Convergence comparison at n = 1,000,000
        printConvergenceSection(stratA, stratB);
        
        System.out.println("==========================================================");
        System.out.println(" END OF SUMMARY");
        System.out.println("==========================================================");
    }

    /**
     * Prints the top-5 most-landed squares at each n-value, averaged across all runs.
     *
     * @param label   strategy display label
     * @param results simulation data for this strategy
     */
    private static void printTopSquaresSection(String label, List<SimulationResult> results) {
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println(" " + label + " - Top " + TOP_N + " Squares by Average Landing %");
        System.out.println("----------------------------------------------------------");
        System.out.printf("  %-8s  %-5s  %-26s  %s%n", "Rank", "Idx", "Space Name", "Avg %");

        for (int n : Simulator.N_VALUES) {
            System.out.printf("%n  [n = %,d]%n", n);

            // Average landing percentages across all 10 runs for this n-value
            double[] avgPercent = getAveragedPercentages(results, n);

            // Get indices sorted by average percentage (highest first)
            int[] ranked = rankSpaces(avgPercent);

            for (int rank = 0; rank < TOP_N; rank++) {
                int idx = ranked[rank];
                // Use space names from the first matching result (names are constant)
                String name = getSpaceNameForIndex(results, n, idx);
                System.out.printf("  %-8d  %-5d  %-26s  %.4f%%%n",
                        rank + 1, idx, name, avgPercent[idx]);
            }
        }
    }

    /**
     * Prints a side-by-side top-5 comparison between strategies at n=1,000,000
     * and verdicts whether they have converged.
     *
     * @param stratA results for Strategy A
     * @param stratB results for Strategy B
     */
    private static void printConvergenceSection(List<SimulationResult> stratA, List<SimulationResult> stratB) {
        final int largestN = Simulator.N_VALUES[Simulator.N_VALUES.length - 1];

        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.printf(" Convergence Check at n = %,d%n", largestN);
        System.out.println("----------------------------------------------------------");

        double[] avgA = getAveragedPercentages(stratA, largestN);
        double[] avgB = getAveragedPercentages(stratB, largestN);

        int[] rankedA = rankSpaces(avgA);
        int[] rankedB = rankSpaces(avgB);

        System.out.printf("  %-5s  %-26s  %-8s   %-26s  %-8s%n",
                "Rank", "Strategy A Space", "Avg %", "Strategy B Space", "Avg %");

        // Collect top indices for overlap count
        List<Integer> topA = new ArrayList<>();
        List<Integer> topB = new ArrayList<>();

        for (int rank = 0; rank < TOP_N; rank++) {
            int idxA = rankedA[rank];
            int idxB = rankedB[rank];
            topA.add(idxA);
            topB.add(idxB);

            String nameA = getSpaceNameForIndex(stratA, largestN, idxA);
            String nameB = getSpaceNameForIndex(stratB, largestN, idxB);

            System.out.printf("  %-5d  %-26s  %-8.4f   %-26s  %.4f%%%n",
                    rank + 1, nameA, avgA[idxA], nameB, avgB[idxB]);
        }

        // Count how many of the top-5 squares appear in both lists
        int overlap = 0;
        for (int idx : topA) {
            if (topB.contains(idx)) overlap++;
        }

        System.out.println();
        System.out.printf("  Squares shared in top-%d: %d/5%n", TOP_N, overlap);

        // Compare Jail landing % directly between strategies
        System.out.printf("  Jail (idx %d) avg %%: Strategy A = %.4f%%  |  Strategy B = %.4f%%%n",
                JAIL_INDEX, avgA[JAIL_INDEX], avgB[JAIL_INDEX]);

        if (overlap >= 4) {
            System.out.println("  VERDICT: Strategies appear to converge at n = 1,000,000.");
        } else {
            System.out.println("  VERDICT: Strategies show noticeable divergence at n = 1,000,000.");
        }
    }

    // ------------------------------------------------------------------
    //  Private utility methods
    // ------------------------------------------------------------------

    /**
     * Returns the average landing percentage for each square across all runs matching n.
     *
     * @param results simulation data to average over
     * @param n       the turn-count milestone to filter by
     * @return double[40] of average landing percentages
     */
    private static double[] getAveragedPercentages(List<SimulationResult> results, int n) {
        double[] sum = new double[Board.BOARD_SIZE];
        int count = 0;

        // Accumulate percentages from every run that matches this milestone
        for (SimulationResult r : results) {
            if (r.turnsCompleted == n) {
                for (int i = 0; i < Board.BOARD_SIZE; i++) {
                    sum[i] += r.getPercentage(i);
                }
                count++;
            }
        }

        // Divide by the number of matching runs to get the average
        double[] avg = new double[Board.BOARD_SIZE];
        if (count > 0) {
            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                avg[i] = sum[i] / count;
            }
        }
        return avg;
    }

    /**
     * Returns board indices 0-39 sorted by landing percentage, highest first.
     *
     * @param avgPercent average landing percentages for all 40 squares
     * @return sorted array of board indices
     */
    private static int[] rankSpaces(double[] avgPercent) {
        // Build a list of indices, then sort by percentage descending using a comparator
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < avgPercent.length; i++) {
            indices.add(i);
        }
        
        indices.sort((a, b) -> Double.compare(avgPercent[b], avgPercent[a]));

        int[] ranked = new int[avgPercent.length];
        for (int i = 0; i < ranked.length; i++) {
            ranked[i] = indices.get(i);
        }
        return ranked;
    }

    /**
     * Returns the space name for a given board index from the first result matching n.
     *
     * @param results simulation data to search
     * @param n       turn-count milestone to match
     * @param index   board position 0-39
     * @return space name, or "Unknown" if not found
     */
    private static String getSpaceNameForIndex(List<SimulationResult> results, int n, int index) {
        for (SimulationResult r : results) {
            if (r.turnsCompleted == n) {
                return r.spaceNames[index];
            }
        }
        return "Unknown";
    }
}