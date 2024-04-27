// Version für JUnit 5

import gfx.MainWindow;
import gfx.Ressources;
import logic.Board;
import logic.Player;
import logic.WinState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TeSSA_Tac_Toe_Tests {
    private Player p1, p2;
    private Board board;
    private MainWindow frame;

    private static final int TIME_OUT = 0;

    @BeforeEach
    public void beforeEach() {
        p1 = new Player("Player 1", Ressources.icon_x);
        p2 = new Player("Player 2", Ressources.icon_o);
        board = new Board(4, 5, 3, p1, p2);
        frame = new MainWindow(p1, p2, board);
        frame.setVisible(true);
        MainWindow.setDebug(true);
    }

    @AfterEach
    public void tearDown() {
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

    //Fehler 1
    @Test
    public void testClickRightBottomCorner() {
        frame.turn(3, 4);

        int actual1 = board.get2d(3, 4);
        assertEquals(1, actual1);

        int actual2 = board.get2d(3, 3);
        assertEquals(0, actual2);
    }

    //Fehler 2
    @Test
    public void testWinOverCorner() {
        frame.turn(2, 0);
        frame.turn(2, 1);
        frame.turn(2, 2);
        frame.turn(3, 1);
        frame.turn(2, 3);
        WinState winner = frame.turn(3, 2);
        assertSame(WinState.none, winner);
    }

    //Fehler 3
    @Test
    public void testPlayer1ScoreIncrementedAfterTurns() {
        frame.turn(0, 1);
        frame.turn(0, 2);
        frame.turn(1, 1);
        frame.turn(0, 3);
        WinState winner = frame.turn(2, 1);

        frame.checkWinner(winner);
        String actualjlabel = frame.getPlayer1_score().getText();
        int actual = Integer.parseInt(actualjlabel);
        assertEquals(1, actual);
    }

    @Test
    public void testPlayer2ScoreIncrementedAfterTurns() {
        frame.turn(1, 2);
        frame.turn(0, 2);
        frame.turn(1, 1);
        frame.turn(0, 3);
        frame.turn(2, 1);
        WinState winner = frame.turn(0, 1);


        frame.checkWinner(winner);
        String actualjlabel = frame.getPlayer2_score().getText();
        int actual = Integer.parseInt(actualjlabel);
        assertEquals(1, actual);
    }

    //Fehler 4.1
    @Test
    public void testIgnoreEmptyCellsFromTop() {
        frame.turn(0, 0);
        frame.turn(1, 2);
        frame.turn(1, 0);
        frame.turn(2, 2);
        WinState winner = frame.turn(3, 0);
        assertSame(WinState.none, winner);
    }

    //Fehler 4.2
    @Test
    public void testIgnoreEmptyCellsFromBottom() {
        frame.turn(3, 0);
        frame.turn(1, 2);
        frame.turn(2, 0);
        frame.turn(2, 2);
        WinState winner = frame.turn(0, 0);
        assertSame(WinState.none, winner);
    }

    //Fehler 5
    @Test
    public void twoRedPlayersShouldNotBePossible() {
        Object[] components = getComboBoxesAndSaveButton(frame.settingsFrame().getContentPane());
        List<JComboBox> comboBoxes = (List<JComboBox>) components[0];
        JButton saveButton = (JButton) components[1];

        comboBoxes.get(0).setSelectedItem("tessa-red");
        comboBoxes.get(1).setSelectedItem("X");

        saveButton.doClick();

        assertEquals("tessa-red", p1.getIconString());
        assertEquals("O", p2.getIconString());
    }

    //Fehler 6
    @Test
    public void testFieldSymbolAppearsOnClickedCell() {
        for (int i = 1; i < 5; i++) {
            frame.turn(0, i);
        }
        for (int i = 0; i < 5; i++) {
            frame.turn(1, i);
        }
        frame.turn(2, 1);
        frame.turn(2, 0);
        frame.turn(2, 3);
        frame.turn(2, 2);
        frame.turn(3, 0);
        frame.turn(2, 4);
        frame.turn(3, 2);
        frame.turn(3, 1);
        frame.turn(3, 4);
        frame.turn(3, 3);
        frame.turn(3, 4);

        int actual = board.get2d(0, 0);
        assertEquals(0, actual);
    }

    //Fehler 7
    @Test
    public void testVFormationNotWinning() {
        frame.turn(0, 1);
        frame.turn(0, 2);
        frame.turn(1, 2);
        frame.turn(1, 3);
        WinState winner = frame.turn(0, 3);

        assertSame(WinState.none, winner);
    }

    //Fehler 8
    @Test
    public void testDrawAfter20Turns() {
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

    //Fehler 9
    @Test
    public void BackSlashPreventsWinning() {
        frame.turn(1, 4);//PLayer1
        frame.turn(2, 2);//Player2
        frame.turn(2, 3);//PLayer1
        frame.turn(3, 1);//Player2
        frame.turn(3, 2);//PLayer1
        WinState winner = board.checkWin();
        assertSame(WinState.player1, winner);
    }

    //Fehler 10
    @Test
    public void testFieldTopRightNotEvaluated() {
        frame.turn(2, 2);
        frame.turn(1, 2);
        frame.turn(1, 3);
        frame.turn(0, 3);
        WinState winner = frame.turn(0, 4);
        assertSame(WinState.player1, winner);
    }

    @Test
    public void testSetIcon() {
        p1.setIcon(Ressources.icon_tessa_blue);
        p2.setIcon(Ressources.icon_tessa_red);

        assertEquals("tessa-blue", p1.getIconString());
        assertEquals("tessa-red", p2.getIconString());
    }

    @Test
    public void testSetIconToNull() {
        p1.setIcon(null);
        assertEquals("", p1.getIconString());
    }

    @Test
    public void testToString() {
        assertEquals("[Player 1]", p1.toString());
    }

    @Test
    public void checkBoard() {
        assertEquals(board.getM() * board.getN(), board.getSize());
    }

    @Test
    public void testActionPerformed() {
        //TODO: Besser an die Elemente kommen
        Object[] elements = frame.getElements();
        for (Object element : elements) {
            if (element instanceof JButton) {
                ((JButton) element).doClick();
            } else if (element instanceof JMenuItem) {
                ((JMenuItem) element).doClick();
            }
        }
    }

    @Test
    public void testDebug() {
        MainWindow.setDebug(false);
        frame.turn(0, 0);
    }

    @Test
    public void testCheckWinner() {
        frame.checkWinner(WinState.tie);
        frame.checkWinner(WinState.unknown);
    }

    @Test
    public void testSelectAllPlayers() {
        Object[] components = getComboBoxesAndSaveButton(frame.settingsFrame().getContentPane());
        List<JComboBox> comboBoxes = (List<JComboBox>) components[0];
        JButton saveButton = (JButton) components[1];

        String[] testCases = {"X", "O", "tessa-red", "tessa-blue", "unknown"};
        comboBoxes.get(0).addItem("unknown");
        comboBoxes.get(1).addItem("unknown");

        for (String testCase1 : testCases) {
            for (String testCase2 : testCases) {
                if (testCase1.equals(testCases[1]) && testCase2.equals(testCases[1])) {
                    frame.setBold(false);
                }
                comboBoxes.get(0).setSelectedItem(testCase1);
                comboBoxes.get(1).setSelectedItem(testCase2);
                saveButton.doClick();
            }
        }
    }


    private Object[] getComboBoxesAndSaveButton(Container container) {
        Component[] containerComponents = container.getComponents();
        JPanel panel = (JPanel) containerComponents[0];
        Component[] components = panel.getComponents();

        List<JComboBox> comboBoxes = new ArrayList<>();
        JButton saveButton = new JButton();

        for (Component component : components) {
            if (component instanceof JComboBox) {
                comboBoxes.add((JComboBox) component);
            }

            if (component instanceof JButton && ((JButton) component).getText().equals("Save changes")) {
                saveButton = (JButton) component;
            }
        }

        return new Object[]{comboBoxes, saveButton};
    }
}