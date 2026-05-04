package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import src.Board;
import src.Player;


public class ExperimentTests {

    // Declare board and players
    Board boardA;
    Board boardB;
    Player.JailExitStrategy stratA = Player.JailExitStrategy.IMMEDIATE_EXIT;
    Player.JailExitStrategy stratB = Player.JailExitStrategy.TRY_FOR_DOUBLES;
    Player playerA;
    Player playerB;

    @Before
    public void initializeBoardAndPlayers() {
        boardA = new Board();
        boardB = new Board();
        playerA = new Player(boardA,stratA);
        playerB = new Player(boardB,stratB);
    }

    @Test
    public void everythingStartsOffOk() {
        assertEquals(0, boardA.getTotalMoves());
        assertEquals(0,playerA.getCurrentPosition());
        assertEquals("Go", boardA.getSpaceName(playerA.getCurrentPosition()));
        assertEquals(0, boardB.getTotalMoves());
        assertEquals(0,playerB.getCurrentPosition());
        assertEquals("Go", boardB.getSpaceName(playerB.getCurrentPosition()));
    }

    @Test
    public void playerMovedAtLeastOnce() {

        playerA.takeTurn();
        assertTrue(boardA.getTotalMoves() > 0);
        playerB.takeTurn();
        assertTrue(boardB.getTotalMoves() > 0);

    }

    // helper function. Advances player turns until player is on a certain space
    private void keepGoingUntilOnSpace(Player player, int targetSpace) {
        do {
            player.takeTurn();
        } while (player.getCurrentPosition() != targetSpace);
    }

    @Test(timeout = 3000)
    public void allSpacesAreLandable() {
        for (int i=0; i<Board.BOARD_SIZE; i++) {
            if (i==30) {
                continue; // skip Go To Jail space
            }
            keepGoingUntilOnSpace(playerA,i);
        }

        playerB.takeTurn();
        for (int i=0; i<Board.BOARD_SIZE; i++) {
            if (i==30) {
                continue; // skip Go To Jail space
            }
            keepGoingUntilOnSpace(playerB,i);
        }
    }

    @Test(timeout = 3000)
    public void testJailStatuses() {
        do {
            keepGoingUntilOnSpace(playerA,Board.JAIL_INDEX);
        } while (playerA.isInJail());
        // Player is visiting jail
        do {
            keepGoingUntilOnSpace(playerA,Board.JAIL_INDEX);
        } while (!playerA.isInJail());
        // Player is in jail

        do {
            keepGoingUntilOnSpace(playerB,Board.JAIL_INDEX);
        } while (playerB.isInJail());
        // Player is visiting jail
        do {
            keepGoingUntilOnSpace(playerB,Board.JAIL_INDEX);
        } while (!playerB.isInJail());
        // Player is in jail
    }

    @Test
    public void whenPlayerIsInJailPlayerShouldBeOnThatSpot()  {

        // player is not in jail
        do {
            playerA.takeTurn();
        } while (!playerA.isInJail());
        // player is in jail
        assertEquals(Board.JAIL_INDEX,playerA.getCurrentPosition());

        // player is not in jail
        do {
            playerB.takeTurn();
        } while (!playerB.isInJail());
        // player is in jail
        assertEquals(Board.JAIL_INDEX,playerB.getCurrentPosition());
    }
}