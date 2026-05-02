/**
 * This class represents a standard 40 space monopoly board.
 * In addition this class tracks the number of moves made.
 * Stores the name/type of each square.
 * Tracks the number of times the simulation lands on each square.
 * Represents Chance and Community Chest decks as shuffled collections with a discard pile 
 * and reshuffle when empty.
 */
package monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    int totalMoves;
    SpaceType spaceType;
    static int BOARD_SIZE = 40;

    //Define unique object properties
    private static class Space{
        int index;
        String name;
        SpaceType type;

        //Constructor initializes the fields
        public Space(int index, String name, SpaceType type){
            this.index = index;
            this.name = name;
            this.type = type;
        }
        //Getters and setters for space objects
        public int getIndex() {
            return index;
        }
        public String getName() {
            return name;
        }
        public SpaceType getType() {
            return type;
        }
    }

    //intialize array for the squares
    private Space[] board = new Space[BOARD_SIZE];

    //Counter to keep track total space landings
    private int[] spaceCounter = new int[BOARD_SIZE];

    //Represent Chance and Community Chest decks as shuffled collections with a discard pile and reshuffle when empty
    private List<Integer> chanceDeck = new ArrayList<>();
    private List<Integer> chanceDiscardPile = new ArrayList<>();
    private List<Integer> communityChestDeck = new ArrayList<>();
    private List<Integer> communityChestDiscardPile = new ArrayList<>();

    //Initialize board and decks
    public Board(){
        initializeBoard();
        initializeDecks();
    }

    private void initializeBoard(){
        board[0] = new Space(0, "Go", SpaceType.GO);
        board[1] = new Space(1, "Mediterranean Avenue", SpaceType.PROPERTY);
        board[2] = new Space(2, "Community Chest", SpaceType.COMMUNITY_CHEST);
        board[3] = new Space(3, "Baltic Avenue", SpaceType.PROPERTY);
        board[4] = new Space(4, "Income Tax", SpaceType.TAX);
        board[5] = new Space(5, "Reading Railroad", SpaceType.RAILROAD);
        board[6] = new Space(6, "Oriental Avenue", SpaceType.PROPERTY);
        board[7] = new Space(7, "Chance", SpaceType.CHANCE);
        board[8] = new Space(8, "Vermont Avenue", SpaceType.PROPERTY);
        board[9] = new Space(9, "Connecticut Avenue", SpaceType.PROPERTY);
        board[10] = new Space(10, "Jail", SpaceType.JAIL);
        board[11] = new Space(11, "St. Charles Place", SpaceType.PROPERTY);
        board[12] = new Space(12, "Electric Company", SpaceType.UTILITY);
        board[13] = new Space(13, "States Avenue", SpaceType.PROPERTY);
        board[14] = new Space(14, "Virginia Avenue", SpaceType.PROPERTY);
        board[15] = new Space(15, "Pennsylvania Railroad", SpaceType.RAILROAD);
        board[16] = new Space(16, "St. James Place", SpaceType.PROPERTY);
        board[17] = new Space(17, "Community Chest", SpaceType.COMMUNITY_CHEST);
        board[18] = new Space(18, "Tennessee Avenue", SpaceType.PROPERTY);
        board[19] = new Space(19, "New York Avenue", SpaceType.PROPERTY);
        board[20] = new Space(20, "Free Parking", SpaceType.FREE_PARKING);
        board[21] = new Space(21, "Kentucky Avenue", SpaceType.PROPERTY);
        board[22] = new Space(22, "Chance", SpaceType.CHANCE);
        board[23] = new Space(23, "Indiana Avenue", SpaceType.PROPERTY);
        board[24] = new Space(24, "Illinois Avenue", SpaceType.PROPERTY);
        board[25] = new Space(25, "B. & O. Railroad", SpaceType.RAILROAD);
        board[26] = new Space(26, "Atlantic Avenue", SpaceType.PROPERTY);
        board[27] = new Space(27, "Ventnor Avenue", SpaceType.PROPERTY);
        board[28] = new Space(28, "Water Works", SpaceType.UTILITY);
        board[29] = new Space(29, "Marvin Gardens", SpaceType.PROPERTY);
        board[30] = new Space(30, "Go To Jail", SpaceType.GO_TO_JAIL);
        board[31] = new Space(31, "Pacific Avenue", SpaceType.PROPERTY);
        board[32] = new Space(32, "North Carolina Avenue", SpaceType.PROPERTY);
        board[33] = new Space(33, "Community Chest", SpaceType.COMMUNITY_CHEST);
        board[34] = new Space(34, "Pennsylvania Avenue", SpaceType.PROPERTY);
        board[35] = new Space(35, "Short Line", SpaceType.RAILROAD);
        board[36] = new Space(36, "Chance", SpaceType.CHANCE);
        board[37] = new Space(37, "Park Place", SpaceType.PROPERTY);
        board[38] = new Space(38, "Luxury Tax", SpaceType.TAX);
        board[39] = new Space(39, "Boardwalk", SpaceType.PROPERTY);
    }

    //Initializes the decks of cards
    private void initializeDecks(){
        chanceDeck.add(0);
        chanceDeck.add(1);
        chanceDeck.add(2);
        chanceDeck.add(3);
        chanceDeck.add(4);
        chanceDeck.add(5);
        chanceDeck.add(6);
        chanceDeck.add(7);
        chanceDeck.add(8);
        chanceDeck.add(9);
        chanceDeck.add(10);
        chanceDeck.add(11);
        chanceDeck.add(12);
        chanceDeck.add(13);
        chanceDeck.add(14);
        chanceDeck.add(15);

        communityChestDeck.add(0);
        communityChestDeck.add(1);
        communityChestDeck.add(2);
        communityChestDeck.add(3);
        communityChestDeck.add(4);
        communityChestDeck.add(5);
        communityChestDeck.add(6);
        communityChestDeck.add(7);
        communityChestDeck.add(8);
        communityChestDeck.add(9);
        communityChestDeck.add(10);
        communityChestDeck.add(11);
        communityChestDeck.add(12);
        communityChestDeck.add(13);
        communityChestDeck.add(14);
        communityChestDeck.add(15);

        //Shuffle the decks
        Collections.shuffle(chanceDeck);
        Collections.shuffle(communityChestDeck);
    }
    
    //method to draw from the Chance deck and shuffle if pile empty
    public int drawChance() {
        if(chanceDeck.isEmpty()){
            chanceDeck.addAll(chanceDiscardPile);
            chanceDiscardPile.clear();
            Collections.shuffle(chanceDeck);
        }

        int card = chanceDeck.remove(chanceDeck.size() - 1);
        chanceDiscardPile.add(card);
        return card;
    }
    //method to draw from the Community Chesk deck and suffle if pile empty
    public int drawCommunityChestyDeck() {
        if(communityChestDeck.isEmpty()){
            communityChestDeck.addAll(communityChestDeck);
            communityChestDiscardPile.clear();
            Collections.shuffle(communityChestDeck);
        }

        int card = communityChestDeck.remove(communityChestDeck.size() - 1);
        communityChestDiscardPile.add(card);
        return card;
    }

    //getters for total moves and total landings
    public int getTotalMoves(){
        return totalMoves;
    }
    public int[] getSpaceCounts(){
        return spaceCounter;
    }
}
