
import java.util.Random;

/**
 * This class defines the turn engine and movement rules
 * This class accurately simulates one complete player turn under classic rules (with no money)
 */
public class Player{
    //Enum for exit strategy A and B
    public enum JailExitStrategy{
        IMMEDIATE_EXIT,
        TRY_FOR_DOUBLES
    }

    //Fields for the class
    private int currentPosition;
    private int consecutiveDoubles;
    private boolean inJail;
    private int jailTurns;
    private boolean hasChanceGetOutOfJailFreeCard;
    private boolean hasCommunityChestGetOutOfJailFreeCard;
    private Board board;
    private Random rand;
    private JailExitStrategy jailExitStrategy;

    //Creates a player with a jail exit strategy
    public Player(Board board, JailExitStrategy jailExitStrategy){
        this.board = board;
        this.rand = new Random();
        this.currentPosition = Board.GO_INDEX;
        this.consecutiveDoubles = 0;
        this.inJail = false;
        this.jailTurns = 0;
        this.jailExitStrategy = jailExitStrategy;
    }

    //Simulates one complete turn
    public void takeTurn(){
        consecutiveDoubles = 0;

        if(inJail){
            takeTurnFromJail();
        }else{
            takeRegularTurn();
        }
        consecutiveDoubles = 0;
    }

    //This method represents a valid turn and jail if doubles are rolled 3 consecutive times 
    private void takeRegularTurn(){
        boolean rollAgain = true;

        while(rollAgain){
            DiceRoll roll = rollDice();

            if(roll.isDoubles()){
                consecutiveDoubles++;
            } else {
                consecutiveDoubles = 0;
            }
            if(consecutiveDoubles == 3){
                sendToJail();
                board.recordLanding(currentPosition);
                return;
            }
            //Move the total rolled amount
            moveForward(roll.getTotal());
            resolveCurrentSpace();
            board.recordLanding(currentPosition);

            //Doubles give another roll unless a card or square sent the player to jail
            rollAgain = roll.isDoubles() && !inJail;
        }
    }

    //Method to resolve turn if in jail
    private void takeTurnFromJail(){
        if(jailExitStrategy == JailExitStrategy.IMMEDIATE_EXIT) {
            strategyA();
        } else {
            strategyB();
        }
    }

    //Strategy A: use get out of jail card OR "pay" fine immediately on next turn 
    private void strategyA(){
        if(hasGetOutOfJailFreeCard()){
            useGetOutOfJailFreeCard();
        }else{
            payFine();
        }
        inJail = false;
        jailTurns = 0;
        takeRegularTurn();
    }

    //Strategy B: use get out of jail card OR attempt to roll doubles IF no doubles after 3 turns pay fine on the 4th turn
    private void strategyB(){
        if(hasGetOutOfJailFreeCard()){
            useGetOutOfJailFreeCard();
            inJail = false;
            jailTurns = 0;
            takeRegularTurn();
            return;
        }

        //After 3 failed attempts player pays fine to get out
        if(jailTurns >= 3){
            payFine();
            inJail = false;
            jailTurns = 0;
            takeRegularTurn();
            return;
        }
        //Attempt to roll doubles
        DiceRoll roll = rollDice();
        jailTurns++;

        //Checks for double rolls to get out of jail
        if(roll.isDoubles()){
            inJail = false;
            jailTurns = 0;
            moveForward(roll.getTotal());
            resolveCurrentSpace();
            board.recordLanding(currentPosition);
            return;
        }

        //A failed doubles attempt means the player remains in jail
        board.recordLanding(currentPosition);
    }

    //Allows the player to exit jail
    private void payFine(){
        //Assume the player paid to leave
    }

    //Method defines dice roll logic
    private DiceRoll rollDice() {
        int dice1 = rand.nextInt(6) + 1;
        int dice2 = rand.nextInt(6) + 1;

        return new DiceRoll(dice1, dice2);
    }

    //Executes a valid forward move
    private void moveForward(int spaces){
        currentPosition = board.checkBound(currentPosition + spaces);
    }

    //Sends the player to jail immediatly 
    private void sendToJail(){
        currentPosition = Board.JAIL_INDEX;
        inJail = true;
        jailTurns = 0;
        consecutiveDoubles = 0;
    }

    //Resolves the space for any debuffs that were landed on
    private void resolveCurrentSpace(){
        boolean resolving = true;

        while(resolving){
            resolving = false;
            SpaceType type = board.getSpaceType(currentPosition);

            if(type == SpaceType.CHANCE){
                resolving = resolveChanceCard(board.drawChance());
            } else if(type == SpaceType.COMMUNITY_CHEST){
                resolving = resolveCommunityChestCard(board.drawCommunityChest());
            } else if(type == SpaceType.GO_TO_JAIL){
                sendToJail();
            }
        }
    }

    //Resolves the effects of a chance card 
    private boolean resolveChanceCard(Board.ChanceCard card){
        boolean moved = false;
        boolean cardIsHeldByPlayer = false;

        switch(card){
            case ADVANCE_TO_BOARDWALK:
                currentPosition = 39;
                moved = true;
                break;

            case ADVANCE_TO_GO:
                currentPosition = Board.GO_INDEX;
                moved = true;
                break;

            case ADVANCE_TO_ILLINOIS_AVENUE:
                currentPosition = 24;
                moved = true;
                break;

            case ADVANCE_TO_ST_CHARLES_PLACE:
                currentPosition = 11;
                moved = true;
                break;

            case ADVANCE_TO_NEAREST_RAILROAD_ONE:
            case ADVANCE_TO_NEAREST_RAILROAD_TWO:
                currentPosition = nearestRailroad(currentPosition);
                moved = true;
                break;

            case ADVANCE_TO_NEAREST_UTILITY:
                currentPosition = nearestUtility(currentPosition);
                moved = true;
                break;

            case GET_OUT_OF_JAIL_FREE:
                hasChanceGetOutOfJailFreeCard = true;
                cardIsHeldByPlayer = true;
                break;

            case GO_BACK_THREE_SPACES:
                currentPosition = board.checkBound(currentPosition - 3);
                moved = true;
                break;

            case GO_TO_JAIL:
                sendToJail();
                moved = true;
                break;

            case ADVANCE_TO_READING_RAILROAD:
                currentPosition = 5;
                moved = true;
                break;

            //Money cards are skipped over
            case BANK_DIVIDEND:
            case GENERAL_REPAIRS:
            case SPEEDING_FINE:         
            case CHAIRMAN_OF_THE_BOARD:
            case BUILDING_LOAN_MATURES:
                break;
        }
        //Used cards go to the discard pile, get out of jail free card stays with the player
        if(!cardIsHeldByPlayer){
            board.discardChance(card);
        }
        return moved && !inJail;
    }

    //Resolves the effects of Community Chest cards
    private boolean resolveCommunityChestCard(Board.CommunityChestCard card){
        boolean moved = false;
        boolean cardIsHeldByPlayer = false;

        switch(card){
            case ADVANCE_TO_GO:
                currentPosition = Board.GO_INDEX;
                moved = true;
                break;

            case GET_OUT_OF_JAIL_FREE:
                hasCommunityChestGetOutOfJailFreeCard = true;
                cardIsHeldByPlayer = true;
                break;

            case GO_TO_JAIL:
                sendToJail();
                moved = true;
                break;

            //Money cards are skipped over
            case BANK_ERROR:
            case DOCTORS_FEE:
            case SALE_OF_STOCK:
            case HOLIDAY_FUND:
            case INCOME_TAX_REFUND:
            case BIRTHDAY:
            case LIFE_INSURANCE:
            case HOSPITAL_FEES:
            case SCHOOL_FEES:
            case CONSULTANCY_FEE:
            case STREET_REPAIRS:
            case BEAUTY_CONTEST:
            case INHERITANCE:
                break;
        }
        //Used cards go to the discard pile, get out of jail free card stays with the player
        if(!cardIsHeldByPlayer){
            board.discardCommunityChest(card);
        }
        return moved && !inJail;
    }

    //Helper method to find the closes railroad available
    private int nearestRailroad(int position){
        if(position < 5 || position >= 35){
            return 5;
        }else if(position < 15){
            return 15;
        }else if(position < 25){
            return 25;
        }else{
            return 35;
        }
    }

    //Helper method to find the closest utility space available
     private int nearestUtility(int position){
        if(position < 12 || position >= 28){
            return 12;
        }else{
            return 28;
        }
    }

    //Checks if player holds a get out of jail free card
    private boolean hasGetOutOfJailFreeCard(){
        return hasChanceGetOutOfJailFreeCard || hasCommunityChestGetOutOfJailFreeCard;
    }

    //Uses a get out of jail free card
    private void useGetOutOfJailFreeCard(){
        if(hasChanceGetOutOfJailFreeCard){
            hasChanceGetOutOfJailFreeCard = false;
            board.discardChance(Board.ChanceCard.GET_OUT_OF_JAIL_FREE);
        } else if(hasCommunityChestGetOutOfJailFreeCard){
            hasCommunityChestGetOutOfJailFreeCard = false;
            board.discardCommunityChest(Board.CommunityChestCard.GET_OUT_OF_JAIL_FREE);
        }
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public boolean isInJail(){
        return inJail;
    }

    public int getConsecutiveDoubles(){
        return consecutiveDoubles;
    }

    //Represents the rolling of dice. checks for doubles and sums the total
    private static class DiceRoll {
        private int diceOne;
        private int diceTwo;

        public DiceRoll(int diceOne, int diceTwo){
            this.diceOne = diceOne;
            this.diceTwo = diceTwo;
        }

        public int getTotal(){
            return diceOne + diceTwo;
        }

        public boolean isDoubles(){
            return diceOne == diceTwo;
        }
    }
}