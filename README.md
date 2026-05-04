# CS2430-Project4-Capstone

## Project Overview
A single-player Monopoly movement simulator that answers: *Which squares are landed on most, and does the jail-exit strategy change the distribution?*

The simulator runs 10 independent simulations per strategy at n = 1,000 / 10,000 / 100,000 / 1,000,000 turns and outputs raw landing counts, percentages, and a comparative summary.

### Project Details                                                                                                                                                                                                                                          
- **Course**: CS 2430                                                                                                                                                                                                                                       
- **Team**: SIX!                                                                                                                                                                                                                                            
- **Implementation Lead**: Carlos Recinos                                                                                                                                                                                                                   
- **Verification Lead**: Christopher Reynolds                                                                                                                                                                                                               
- **Communications Lead**: Nelson Long

### File Structure

```
CS2430-Project4/
├── src/
│   ├── Main.java             # Entry point  
│   ├── Board.java            # 40-space board, card decks, and landing counters
│   ├── Player.java           # Turn engine, movement rules, and jail-exit strategies
│   ├── SpaceType.java        # Enum of the 10 board space categories
│   ├── Simulator.java        # Batch runner  10 runs x 4 milestones per strategy
│   ├── SimulationResult.java # Data container for one simulation snapshot
│   └── Reporter.java         # CSV export and printed summary output
├── results/                  # Generated output (created at runtime, gitignored)
│   ├── strategy_a_results.csv
│   └── strategy_b_results.csv
├── docs/
│   ├── pseudoCode.md
│   ├── CSIS2430_TeamSIX_ProjectPlan_A4.md
│   └── Project4 UML Diagram.pdf
└── contributions.md
```

---

## Prerequisites
- Java Development Kit (JDK) 25 or higher
- IDE: IntelliJ IDEA or any Java compatible IDE

---

## Building and Running

Compile and run from the **project root directory**:

```bash
javac src/*.java
java -cp . src.Main
```

Output files are written to `results/` (created automatically):
- `results/strategy_a_results.csv`  Strategy A landing data
- `results/strategy_b_results.csv` Strategy B landing data

All 80 datasets and the Part 5 comparative summary are also printed to stdout.

---

*Last Updated: May 3rd, 2026*
