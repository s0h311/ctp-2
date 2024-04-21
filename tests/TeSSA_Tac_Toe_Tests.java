// Version für JUnit 5
import gfx.MainWindow;
import gfx.Ressources;
import logic.Board;
import logic.Player;
import logic.WinState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeSSA_Tac_Toe_Tests {
    private Player p1, p2;
    private Board board;
    private MainWindow frame;

    private static final int TIME_OUT = 0;

    @BeforeEach
    public void beforeEach() throws Exception {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(4, 5, 3, p1, p2);
        frame = new MainWindow(p1, p2, board);
        frame.setVisible(true);
        MainWindow.setDebugg(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        p1 = p2 = null;
        board = null;
        frame = null;
    }

    @Test
    public void test01() throws InterruptedException {
        frame.turn(0, 0);
        Thread.sleep(TIME_OUT);
        frame.turn(0, 1);
        Thread.sleep(TIME_OUT);
        frame.turn(1, 0);
        Thread.sleep(TIME_OUT);
        frame.turn(0, 2);
        Thread.sleep(TIME_OUT);
        WinState winner = frame.turn(2, 0);
        assertSame(WinState.player1, winner);
    }

    @Test
    public void test02() {
        frame.turn(0, 0);
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals("Player 1", retString);
    }

    @Test
    public void test03() {
        frame.turn(0, 0);
        frame.turn(0, 1);
        String retString = board.getPlayerNameInField(0, 1);
        assertEquals("Player 2", retString);
    }

    @Test
    public void test04() {
        String retString = board.getPlayerNameInField(0, 0);
        assertEquals("        ", retString);
    }

    @Test
    public void test05() {
        WinState wst = frame.turn(0, 0);
        frame.checkWinner(wst);
    }

    @Test
    public void test06() {
        frame.turn(0, 0);
        frame.turn(0, 1);
        frame.turn(1, 0);
        frame.turn(1, 1);
        frame.turn(2, 1);
        WinState winSt = frame.turn(1, 0);
        frame.checkWinner(winSt);
    }

    @Test
    public void test07() {
        frame.settingsFrame();
    }

    //i - Fehler
    @Test
    public void testIgnoirertLeerVonOben()  {
        frame.turn(0, 0);
        frame.turn(1, 2);
        frame.turn(1, 0);
        frame.turn(2, 2);
        WinState winner = frame.turn(3, 0);
        assertSame(WinState.none, winner);
    }

    @Test
    public void testIgnoirertLeerVonUnten()  {
        frame.turn(3, 0);
        frame.turn(1, 2);
        frame.turn(2, 0);
        frame.turn(2, 2);
        WinState winner = frame.turn(0, 0);
        assertSame(WinState.none, winner);
    }

    @Test
    public void testGewinnÜberEck()  {
        frame.turn(2, 0);
        frame.turn(2, 1);
        frame.turn(2, 2);
        frame.turn(3, 1);
        frame.turn(2, 3);
        WinState winner = frame.turn(3, 2);
        assertSame(WinState.none, winner);
    }

    @Test
    public void testUnentschieden()  {
        frame.turn(0, 0);
        frame.turn(1, 0);
        frame.turn(2, 0);
        frame.turn(3, 0);
        frame.turn(1, 1);
        frame.turn(0, 1);
        frame.turn(3, 1);
        frame.turn(2, 1);
        frame.turn(1, 2);
        frame.turn(0, 2);
        frame.turn(3, 2);
        frame.turn(2, 2);
        frame.turn(0, 3);
        frame.turn(1, 3);
        frame.turn(2, 3);
        WinState winner = frame.turn(3, 3);
        assertSame(WinState.none, winner);
    }

    @Test
    public void testFeld_4_0()  {
        frame.turn(2, 2);
        frame.turn(1, 2);
        frame.turn(1, 3);
        frame.turn(0, 3);
        WinState winner = frame.turn(0, 4);
        assertSame(WinState.player1, winner);
    }

    @Test
    public void rightBottomCorner() {
        frame.turn(3, 4);

        int actual1 = board.get2d(3, 4);
        assertEquals(1, actual1);

        int actual2 = board.get2d(3, 3);
        assertEquals(0, actual2);
    }

    @Test
    public void testSpieler1Punkte(){
        frame.turn(0,1);
        frame.turn(0,2);
        frame.turn(1,1);
        frame.turn(0,3);
        WinState winner = frame.turn(2,1);


        frame.checkWinner(winner);
        String actualjlabel = frame.getPlayer1_score().getText();
        int actual = Integer.parseInt(actualjlabel);
        assertEquals(1,actual);
    }
    @Test
    public void testSpieler2Punkte(){
        frame.turn(1,2);
        frame.turn(0,2);
        frame.turn(1,1);
        frame.turn(0,3);
        frame.turn(2,1);
        WinState winner = frame.turn(0,1);


        frame.checkWinner(winner);
        String actualjlabel = frame.getPlayer2_score().getText();
        int actual = Integer.parseInt(actualjlabel);
        assertEquals(1,actual);
    }
    @Test
    public void testVFormationKeinGewinn(){
        frame.turn(0,1);
        frame.turn(0,2);
        frame.turn(1,2);
        frame.turn(1,3);
        WinState winner = frame.turn(0,3);

        assertSame(WinState.none, winner);
    }
    @Test
        public void testFeldSymbolErscheintAufAngeklicktemFeld(){
        frame.turn(0,1);
        frame.turn(0,2);
        frame.turn(0,3);
        frame.turn(0,4);
        frame.turn(1,0);
        frame.turn(1,1);
        frame.turn(1,2);
        frame.turn(1,3);
        frame.turn(1,4);
        frame.turn(2,1);
        frame.turn(2,0);
        frame.turn(2,3);
        frame.turn(2,2);
        frame.turn(3,0);
        frame.turn(2,4);
        frame.turn(3,2);
        frame.turn(3,1);
        frame.turn(3,4);
        frame.turn(3,3);
        frame.turn(3,4);

        int actual = board.get2d(0, 0);
        assertEquals(0, actual);
    }
}
