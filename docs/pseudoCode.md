# Pseudocode – Project 4

A simulation of the classic monopoly game to answer probability questions such as: most landed spaces
and how different "get out of jail" strategies affect long-run landing probabilities.

---

## Part 1 - BOARD AND DATA STRUCTURES
```
//Track total moves
int totalMoves = 0;

//Define board size
int BOARD_SIZE = 40;

// Define space types
enum SpaceType {GO, JAIL, CHANCE . . .}

//Define space class and unique space identifiers
class space{
  int index;
  String name;
  SpaceType type;

  // Space type constructor
  Space(int index, String name, SpaceType type){
    this. index = index;
    this. name = name;
    this. type = type;
  }
}

//start the monopoly game board
Space[] board = new Space[BOARD_SIZE];

//add the spots/spaces according to the classic board
void boardFill(){
  board[0] = new Space(0, "GO", SpaceType.GO);
  . . .
  board[39] = new Space(39, "Board Walk" SpaceType.Property);

//Track total space landings
int spaceCounts = new;

//Represent Chance/Community Chest decks as shuffled collections
Collections collection;

```

## Part 2 - TURN ENGINE AND MOVEMENT RULES
```
//fields to keep track of stats
int currentPosition;
int consecutiveDoubles;
boolean inJail = false;
int jailTurns;
boolean hasGetOutOfJail = false;

//Complete player turn simulator and consecutive double tracker
void myTurn(){
  totalMoves++;
  int dice1 = rollDice();
  int dice2 = rollDice();
  int move = dice1 + dice2;

  if(dice1 == dice2){
    consecutiveDoubles++;
    if(ConsecutiveDoubles == 3){
      goToJail();
      return;
    }
  }else{
    consecutiveDoubles = 0
  }
  //Execute a valid move in the board
  currentPosition = (currentPosition + move); // reset to 0 if position = 0 and perform any requiered additional moves

  //Execute valid move results
  Space space = board[currentPosition];

  //Check for chance/community card
  resolvingSpace(space);

  //Check if in jail
  if(!inJail){
    return;
  }

  //Check if player can get out
  if(hasGetOutOfJailFreeCard){
    getOutOfJailFree();
  }else{
      //strategy A || B
  }

  //Helper methods to implement

  int rollDie(){
    return (math.random()) * 6;
  }

  void goToJail(){
    inJail = true;
    jailTurns = 0;
  }

  void resolvingSpace(Space space){
    //Space logic to implement
  }

  void drawChanceCard(){
    //Card logic to implement
  }

  void drawCommunityChestCard(){
    //Card logic to implement
  }

  int drawCard(Collection){
    //Card logic to implement
  }

```

## Part 3 – TWO JAIL-EXIT STRATEGIES
```
void strategyA(){
  if(hasGetOutOfJailFreeCard){
      getOutOfJail();
  }else{
    payFine();
    inJail = false;
  }

void strategyB(){
  if(hasGetOutOfJailFreeCard){
      getOutOfJail();
  }else{
  for(int i = 0; i < 3; i++){
    int dice1 = rollDice();
    int dice2 = rollDice();
    if(dice1 == dice2){
      inJail = false;
      return;
    }
  }
  //if no double roll pay fine and get out next turn
  payFine();
  inJail = false;

```

## Part 4 – BATCH SIMULATION AND DATA COLLECTION
```
//Run 10 independent simulations
void runSimulations(int numberOfTurns, String strategyName){
  //Reset the board state for each simulation and keep track of landing counts
  currentPosition = 0;
  totalMoves = 0;
  landingCounts = 0;
  . . .

  //Collect stats / landing counts after n = 1_000, 10_000, 100_000, and 1_000_000
  for(int i = 0; i < n; i++){
    myTurn();
    landingCounts++;
  }

  //Output of stats
  system.out.println(//stats format);

```

## Part 5 – COMPARATIVE OUTPUT / RUN SUMMARY
```
//Prints output summary to a file such as a table
void printSummary(){
  system.out.println("\n===== SUMMARY =====")

  //Find top 3 and 5 most landed on squares
  int topA = findTopSquares(landingCounts);
  int topB = findTopSquares(landingCounts);
  system.out.println(//Top landed squares results")

  //System.out.println(//Whether Strategy A and Strategy B appear to -
    converge to the same distribution at n = 1,000,000.)

  //System.out.println(//Any obvious anomalies (e.g., stuck in Jail, -
    card loops, unexpected non-convergence)

```

## Main
```
runSimulations();
print comparison

```
## EXTRA CREDIT
//Do one of the following: Generate a chart/plot (bar chart) of landing -
    percentages for n = 1,000,000 for both strategies and include it in -
    your report, or Compare your final distribution to a published/known -
    Monopoly probability distribution (cite your source) and discuss similarities/differences.
```
