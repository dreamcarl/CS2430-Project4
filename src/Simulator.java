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

import java.util.ArrayList;
import java.util.List;

/**
 * Runs 10 independent simulations per jail-exit strategy, snapshotting
 * landing counts at n = 1,000 / 10,000 / 100,000 / 1,000,000 turns.
 * Each run uses a fresh Board and Player to keep results isolated.
 */
public class Simulator {

    // Number of independent simulation runs to execute per strategy
    public static final int NUM_RUNS = 10;

    // Turn-count milestones at which landing snapshots are taken
    public static final int[] N_VALUES = {1_000, 10_000, 100_000, 1_000_000};

    /**
     * Runs a full batch of simulations and returns all snapshots as a flat list
     * of (NUM_RUNS x N_VALUES.length) SimulationResult objects.
     *
     * @param strategy jail-exit strategy to simulate
     * @return all landing-count snapshots for this strategy
     */
    public static List<SimulationResult> runBatch(Player.JailExitStrategy strategy) {
        List<SimulationResult> results = new ArrayList<>();


        for (int run = 1; run <= NUM_RUNS; run++) {

            System.out.printf("  Run %d/%d...%n", run, NUM_RUNS);

            // Initialize new board
            Board board = new Board();
            Player player = new Player(board, strategy);

            // Index into N_VALUES tracking which milestone we are waiting for next
            int milestoneIdx = 0;

            // Inner loop: advance one turn at a time up to the final milestone
            for (int turn = 1; turn <= N_VALUES[N_VALUES.length - 1]; turn++) {
                player.takeTurn();

                // Check if the current turn count matches the next milestone
                if (milestoneIdx < N_VALUES.length && turn == N_VALUES[milestoneIdx]) {
                    // Snapshot the board's landing counts at this milestone
                    int[] counts = board.getSpaceCounts();

                    String[] names = new String[Board.BOARD_SIZE];
                    for (int i = 0; i < Board.BOARD_SIZE; i++) {
                        names[i] = board.getSpaceName(i);
                    }

                    results.add(new SimulationResult(strategy, run, turn, counts, names));
                    milestoneIdx++;
                }
            }
        }
        return results;
    }
}
