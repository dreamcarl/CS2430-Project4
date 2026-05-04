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

/**
 * Immutable data container for a single simulation.
 * One SimulationResult is produced each time the turn counter hits a milestone
 * (1,000 / 10,000 / 100,000 / 1,000,000 turns) during a Simulator.runBatch() call.
 */
public class SimulationResult {
    public final Player.JailExitStrategy strategy;
    public final int runNumber;
    public final int turnsCompleted;
    public final int[] landingCounts;
    public final String[] spaceNames;

    /**
     * Constructs a SimulationResult from a board snapshot.
     *
     * @param strategy       the jail-exit strategy used during this run
     * @param runNumber      which independent run this came from (1-10)
     * @param turnsCompleted how many turns had been taken when this snapshot was taken
     * @param landingCounts  copy of the board's landing counter array (length 40)
     * @param spaceNames     copy of the board's space name array (length 40)
     */
    public SimulationResult(Player.JailExitStrategy strategy, int runNumber, int turnsCompleted, int[] landingCounts, String[] spaceNames) {
        this.strategy       = strategy;
        this.runNumber      = runNumber;
        this.turnsCompleted = turnsCompleted;
        this.landingCounts  = landingCounts.clone();
        this.spaceNames     = spaceNames.clone();
    }

    /**
     * Returns the landing percentage for a given board index.
     * Percentage = (times landed on this square / total turns) * 100.
     *
     * @param index board position 0-39
     * @return percentage as a value between 0.0 and 100.0
     */
    public double getPercentage(int index) {
        return (landingCounts[index] / (double) turnsCompleted) * 100.0;
    }
}
