import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This class represents a standard 40 space monopoly board.
 * In addition this class tracks the number of moves made.
 * Stores the name/type of each square.
 * Tracks the number of times the simulation lands on each square.
 * Represents Chance and Community Chest decks as shuffled collections with a discard pile 
 * and reshuffle when empty.
 */
public class Board{
    private int totalMoves;
    SpaceType spaceType;
    public static final int BOARD_SIZE = 40;
    //Constants to reference board positions
    public static final int GO_INDEX = 0;
    public static final int JAIL_INDEX = 10;
    public static final int GO_TO_JAIL_INDEX = 30;

    //This class represents a single square on the board
    private static class Space{
        int index;
        String name;
        SpaceType type;

        //Constructor initializes the properties of the square object
        public Space(int index, String name, SpaceType type){
            this.index = index;
            this.name = name;
            this.type = type;
        }
        //Getters and setters for space objects
        public int getIndex(){
            return index;
        }
        public String getName(){
            return name;
        }
        public SpaceType getType(){
            return type;
        }
    }

    //intializes array for the 40 squares
    private Space[] spaces = new Space[BOARD_SIZE];

    //Counter to keep track total number of unique space landings
    private int[] landingCounter = new int[BOARD_SIZE];

    //Represent Chance and Community Chest decks as shuffled collections with a discard pile and reshuffle when empty
    private List<ChanceCard> chanceDeck = new ArrayList<ChanceCard>();
    private List<ChanceCard> chanceDiscardPile = new ArrayList<ChanceCard>();
    private List<CommunityChestCard> communityChestDeck = new ArrayList<CommunityChestCard>();
    private List<CommunityChestCard> communityChestDiscardPile = new ArrayList<CommunityChestCard>();

    //Initialize board and decks
    public Board(){
        initializeBoard();
        initializeDecks();
    }

    //Initialization of each space on the board with its index, name and space type
    private void initializeBoard(){
        spaces[0] = new Space(0, "Go", SpaceType.GO);
        spaces[1] = new Space(1, "Mediterranean Avenue", SpaceType.PROPERTY);
        spaces[2] = new Space(2, "Community Chest", SpaceType.COMMUNITY_CHEST);
        spaces[3] = new Space(3, "Baltic Avenue", SpaceType.PROPERTY);
        spaces[4] = new Space(4, "Income Tax", SpaceType.TAX);
        spaces[5] = new Space(5, "Reading Railroad", SpaceType.RAILROAD);
        spaces[6] = new Space(6, "Oriental Avenue", SpaceType.PROPERTY);
        spaces[7] = new Space(7, "Chance", SpaceType.CHANCE);
        spaces[8] = new Space(8, "Vermont Avenue", SpaceType.PROPERTY);
        spaces[9] = new Space(9, "Connecticut Avenue", SpaceType.PROPERTY);
        spaces[10] = new Space(10, "Jail", SpaceType.JAIL);
        spaces[11] = new Space(11, "St. Charles Place", SpaceType.PROPERTY);
        spaces[12] = new Space(12, "Electric Company", SpaceType.UTILITY);
        spaces[13] = new Space(13, "States Avenue", SpaceType.PROPERTY);
        spaces[14] = new Space(14, "Virginia Avenue", SpaceType.PROPERTY);
        spaces[15] = new Space(15, "Pennsylvania Railroad", SpaceType.RAILROAD);
        spaces[16] = new Space(16, "St. James Place", SpaceType.PROPERTY);
        spaces[17] = new Space(17, "Community Chest", SpaceType.COMMUNITY_CHEST);
        spaces[18] = new Space(18, "Tennessee Avenue", SpaceType.PROPERTY);
        spaces[19] = new Space(19, "New York Avenue", SpaceType.PROPERTY);
        spaces[20] = new Space(20, "Free Parking", SpaceType.FREE_PARKING);
        spaces[21] = new Space(21, "Kentucky Avenue", SpaceType.PROPERTY);
        spaces[22] = new Space(22, "Chance", SpaceType.CHANCE);
        spaces[23] = new Space(23, "Indiana Avenue", SpaceType.PROPERTY);
        spaces[24] = new Space(24, "Illinois Avenue", SpaceType.PROPERTY);
        spaces[25] = new Space(25, "B. & O. Railroad", SpaceType.RAILROAD);
        spaces[26] = new Space(26, "Atlantic Avenue", SpaceType.PROPERTY);
        spaces[27] = new Space(27, "Ventnor Avenue", SpaceType.PROPERTY);
        spaces[28] = new Space(28, "Water Works", SpaceType.UTILITY);
        spaces[29] = new Space(29, "Marvin Gardens", SpaceType.PROPERTY);
        spaces[30] = new Space(30, "Go To Jail", SpaceType.GO_TO_JAIL);
        spaces[31] = new Space(31, "Pacific Avenue", SpaceType.PROPERTY);
        spaces[32] = new Space(32, "North Carolina Avenue", SpaceType.PROPERTY);
        spaces[33] = new Space(33, "Community Chest", SpaceType.COMMUNITY_CHEST);
        spaces[34] = new Space(34, "Pennsylvania Avenue", SpaceType.PROPERTY);
        spaces[35] = new Space(35, "Short Line", SpaceType.RAILROAD);
        spaces[36] = new Space(36, "Chance", SpaceType.CHANCE);
        spaces[37] = new Space(37, "Park Place", SpaceType.PROPERTY);
        spaces[38] = new Space(38, "Luxury Tax", SpaceType.TAX);
        spaces[39] = new Space(39, "Boardwalk", SpaceType.PROPERTY);
    }

    //This enum class will help identify the type of chance card for later resolving
    public enum ChanceCard{
        ADVANCE_TO_BOARDWALK,              // Advance to Boardwalk
        ADVANCE_TO_GO,                     // Advance to Go 
        ADVANCE_TO_ILLINOIS_AVENUE,        // Advance to Illinois Avenue
        ADVANCE_TO_ST_CHARLES_PLACE,       // Advance to St. Charles Place
        ADVANCE_TO_NEAREST_RAILROAD_ONE,   // Advance to nearest Railroad
        ADVANCE_TO_NEAREST_RAILROAD_TWO,   // Advance to nearest Railroad pt.2
        ADVANCE_TO_NEAREST_UTILITY,        // Advance token to nearest Utility
        BANK_DIVIDEND,                     // Bank pays you dividend of $0
        GET_OUT_OF_JAIL_FREE,              // Get Out of Jail Free Card
        GO_BACK_THREE_SPACES,              // Go Back 3 Spaces
        GO_TO_JAIL,                        // Go to jail immediately
        GENERAL_REPAIRS,                   // Make general repairs on all your property
        SPEEDING_FINE,                     // Speeding fine $0
        ADVANCE_TO_READING_RAILROAD,       // Take a trip to Reading Railroad
        CHAIRMAN_OF_THE_BOARD,             // Pay each player $0
        BUILDING_LOAN_MATURES              // Your building loan matures
    }

    //This enum class will help identify the type of community chest card for later keepResolving
    public enum CommunityChestCard{
        ADVANCE_TO_GO,                     // Advance to Go 
        BANK_ERROR,                        // Bank error in your favor
        DOCTORS_FEE,                       // Doctor's fee
        SALE_OF_STOCK,                     // From sale of stock you get $0
        GET_OUT_OF_JAIL_FREE,              // Get Out of Jail Free
        GO_TO_JAIL,                        // Go to jail immediately
        HOLIDAY_FUND,                      // Holiday fund matures
        INCOME_TAX_REFUND,                 // Income tax refund
        BIRTHDAY,                          // It is your birthday
        LIFE_INSURANCE,                    // Life insurance matures
        HOSPITAL_FEES,                     // Pay hospital fees
        SCHOOL_FEES,                       // Pay school fees
        CONSULTANCY_FEE,                   // Receive $0 consultancy fee
        STREET_REPAIRS,                    // You are assessed for street repair
        BEAUTY_CONTEST,                    // You have won second prize in a beauty contest
        INHERITANCE                        // You inherit $0
    }

    //Fills both decks with their respective cards
    private void initializeDecks(){
         //Add every Chance card to the Chance deck
        for(ChanceCard card : ChanceCard.values()){
            chanceDeck.add(card);
        }

        //Add every Community Chest card to the Community Chest deck
        for(CommunityChestCard card : CommunityChestCard.values()){
            communityChestDeck.add(card);
        }

        //Shuffles the decks after populating the card values
        Collections.shuffle(chanceDeck);
        Collections.shuffle(communityChestDeck);
    }
    
    //method to draw from the Chance deck
    public ChanceCard drawChance(){
        if(chanceDeck.isEmpty()){
            reshuffleChanceDeck();
        }
        return chanceDeck.remove(chanceDeck.size() - 1);
    }

    //Helper method to hadle discarding after card resolving
    public void discardChance(ChanceCard card){
        if(card != null){
            chanceDiscardPile.add(card);
        }
    }

    //method to draw from the Community Chesk deck
    public CommunityChestCard drawCommunityChest(){
        if(communityChestDeck.isEmpty()){
            reshuffleCommunityChestDeck();
        }
        return communityChestDeck.remove(communityChestDeck.size() - 1);
    }

    //Helper method to handle discarding after card resolving
    public void discardCommunityChest(CommunityChestCard card){
        if(card != null){
            communityChestDiscardPile.add(card);
        }
    }

    //Helper method to reshuffle Chance Deck
    private void reshuffleChanceDeck(){
        chanceDeck.addAll(chanceDiscardPile);
        chanceDiscardPile.clear();
        Collections.shuffle(chanceDeck);
    }

    //helper method to reshuffle Community Chest card
    private void reshuffleCommunityChestDeck(){
        communityChestDeck.addAll(communityChestDiscardPile);
        communityChestDiscardPile.clear();
        Collections.shuffle(communityChestDeck);
    }

    //Adds landing spot to running total for tracking
    public void recordLanding(int index){
        int safeIndex = checkBound(index);
        landingCounter[safeIndex]++;
        totalMoves++;
    }

    //This method ensures no out of bound moves are completed
    public int checkBound(int index){
        int validIndex = index % BOARD_SIZE;

        if(validIndex < 0){
            validIndex += BOARD_SIZE;
        }
        return validIndex;
    }

    //Getters for the class
    public Space getSpace(int index){
        return spaces[checkBound(index)];
    }

    public String getSpaceName(int index){
        return getSpace(index).getName();
    }

    public SpaceType getSpaceType(int index){
        return getSpace(index).getType();
    }

    public int getLandingCount(int index){
        return landingCounter[checkBound(index)];
    }

    public int getTotalMoves(){
        return totalMoves;
    }

    //Allows to observe the number of times a space has been landed on without modifying the running total 
    public int[] getSpaceCounts(){
        int[] copy = new int[landingCounter.length];

        for(int i = 0; i < landingCounter.length; i++){
            copy[i] = landingCounter[i];
        }
        return copy;
    }
}