package gfx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import logic.Board;
import logic.Player;
import logic.WinState;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    public static final String VERSION = "1.0.3";

    private static boolean DEBUG = false;

    private JButton buttonArr[][];

    private JLabel player1_score, player2_score;
    private JLabel player1_label, player2_label;
    private JPanel game_panel;
    private final Player player1, player2;
    private final Board board;
    private int turnCnt = 0;
    private Object[] elements = new Object[2];

    public Object[] getElements() {
        return elements;
    }

    private boolean isBold = true;
    public void setBold(boolean bold) {
        isBold = bold;
    }

    public static void setDebug(boolean b) {
        DEBUG = b;
    }

    /**
     * Create the frame.
     */
    public MainWindow(Player player1, Player player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.setButtonArr(new JButton[board.getM()][board.getN()]);
        setForeground(Color.BLACK);
        setResizable(false);
        setTitle("TeSSA Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        getContentPane().add(menuBar, BorderLayout.NORTH);

        JMenu mnNewMenu = new JMenu("?");
        mnNewMenu.setHorizontalAlignment(SwingConstants.RIGHT);
        menuBar.add(mnNewMenu);

        JMenuItem configMenuItem = new JMenuItem("Settings");
        mnNewMenu.add(configMenuItem);
        // ToDO direkt im Test daraug zugreifen?
        elements[0] = configMenuItem;

        configMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrame();
            }
        });

        JSplitPane splitPane = new JSplitPane();
        splitPane.setEnabled(false);
        splitPane.setContinuousLayout(true);
        splitPane.setForeground(Color.BLACK);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel stats_panel = new JPanel();
        stats_panel.setForeground(Color.BLACK);
        splitPane.setLeftComponent(stats_panel);
        stats_panel.setLayout(new GridLayout(0, 2, 0, 0));

        player1_label = new JLabel(player1.getName() + " (" + player1.getIconString() + ") :");
        player1_label.setHorizontalAlignment(SwingConstants.CENTER);
        stats_panel.add(player1_label);

        player2_label = new JLabel(player2.getName() + " (" + player2.getIconString() + ") :");
        player2_label.setHorizontalAlignment(SwingConstants.CENTER);
        stats_panel.add(player2_label);

        player1_score = (new JLabel("0"));
        player1_score.setHorizontalAlignment(SwingConstants.CENTER);
        stats_panel.add(player1_score);

        player2_score = (new JLabel("0"));
        player2_score.setHorizontalAlignment(SwingConstants.CENTER);
        stats_panel.add(player2_score);

        game_panel = new JPanel();
        splitPane.setRightComponent(game_panel);

        markActivePlayer();

        resetBoard();
    }

    public JFrame settingsFrame() {
        final JFrame settingsFrame = new JFrame("Settings (Vers." + VERSION + ")");
        settingsFrame.setBounds(this.getX(), this.getY(), 418, 351);
        settingsFrame.setAlwaysOnTop(true);
        settingsFrame.setVisible(true);
        JPanel panel = new JPanel();
        settingsFrame.getContentPane().add(panel);
        // panel.setLayout(new GridLayout(0, 2));
        // JLabel l_m = new JLabel("Board Dimension M:");
        // final JSpinner spinner_m = new JSpinner();
        // spinner_m.setValue(board.getM());
        // JLabel l_n = new JLabel("Board Dimension N:");
        // final JSpinner spinner_n = new JSpinner();
        // spinner_n.setValue(board.getN());
        // JLabel l_winc = new JLabel("Win Condition (# icon in a row):");
        // final JSpinner spinner_k = new JSpinner();
        // spinner_k.setValue(board.getK());

        JLabel l_player1 = new JLabel("Player 1 Icon:");
        String[] items = { "X", "O", "tessa-red", "tessa-blue" };

        JComboBox<String> combo_p1 = new JComboBox<>(items);

        JLabel l_player2 = new JLabel("Player 1 Icon:");
        JComboBox<String> combo_p2 = new JComboBox<>(items);
        combo_p2.setSelectedIndex(1);

        JButton b_save = new JButton("Save changes");
        b_save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // int m = (int) spinner_m.getValue(), n = (int) spinner_n.getValue();
                // if (m < 0 && m > -8 && n < 0 && m > -8) {
                if (!isSelectionAllowed()) {
                    JLabel label = new JLabel();
                    Font font = label.getFont();
                    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
                    style.append("font-weight:" + (isBold ? "bold" : "normal") + ";");
                    style.append("font-size:" + font.getSize() + "pt;");
                    JEditorPane ep = new JEditorPane("text/plain", "Your selection is not allowed, two same colors is not allowed");
                    ep.setEditable(false);
                    ep.setBackground(label.getBackground());
                    JOptionPane.showMessageDialog(settingsFrame, ep);

                    combo_p2.setSelectedItem(getOppositeColor());
                }

                // board.setM(m);
                // board.setN(n);
                // board.setK((int) spinner_k.getValue());
                player1.setIcon(get_icon_for_player((String) combo_p1.getSelectedItem()));
                player2.setIcon(get_icon_for_player((String) combo_p2.getSelectedItem()));
                player1_label.setText(player1.getName() + " (" + player1.getIconString() + ") :");
                player2_label.setText(player2.getName() + " (" + player2.getIconString() + ") :");
                resetBoard();
                settingsFrame.dispose();
            }

            public ImageIcon get_icon_for_player(String selectedItem) {
                ImageIcon icon;
                switch (selectedItem) {
                    case "X":
                        icon = Ressources.icon_x;
                        break;
                    case "O":
                        icon = Ressources.icon_o;
                        break;
                    case "tessa-red":
                        icon = Ressources.icon_tessa_red;
                        break;
                    case "tessa-blue":
                        icon = Ressources.icon_tessa_blue;
                        break;
                    default:
                        icon = Ressources.icon_none;
                }
                return icon;
            }

            private boolean isSelectionAllowed() {
                List<String> items = List.of(combo_p1.getSelectedItem().toString(), combo_p2.getSelectedItem().toString());

                if (items.get(0) == null ||
                    items.get(1) == null ||
                    items.get(0).equals(items.get(1)) ||
                    (items.contains("tessa-red") && items.contains("X"))
                ) {
                    return false;
                }

                return true;
            }

            private String getOppositeColor() {
                String p1Color = combo_p1.getSelectedItem().toString();

                if (p1Color.equals("tessa-red") || p1Color.equals("X")) {
                    return "O";
                }

                return "X";
            }
        });

        // panel.add(l_m);
        // panel.add(spinner_m);
        // panel.add(l_n);
        // panel.add(spinner_n);
        // panel.add(l_winc);
        // panel.add(spinner_k);
        panel.add(l_player1);
        panel.add(combo_p1);
        panel.add(l_player2);
        panel.add(combo_p2);

        panel.add(b_save);
        settingsFrame.pack();
        return settingsFrame;
    }

    public void resetBoard() {
        game_panel.removeAll();
        setButtonArr(new JButton[board.getM()][board.getN()]);
        game_panel.setLayout(new GridLayout(0, board.getN()));
        for (int m = 0; m < board.getM(); m++) {
            for (int n = 0; n < board.getN(); n++) {
                final int im = m;
                final int in = n;
                JButton btn = new JButton("");
                btn.setIcon(Ressources.icon_none);
                // ToDO direkt im Test daraug zugreifen?
                elements[1] = btn;
                btn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WinState winner;
                        winner = turn(im, in);
                        checkWinner(winner);
                    }
                });
                game_panel.add(btn);
                getButtonArr()[m][n] = btn;
            }
        }
        reset();
        pack();
    }

    public void updateBoard() {
        for (int m = 0; m < board.getM(); m++) {
            for (int n = 0; n < board.getN(); n++) {
                Player p = board.getPlayer2d(m, n);
                JButton btn = getButtonArr()[m][n];
                if (p == null) {
                    btn.setIcon(Ressources.icon_none);
                } else {
                    btn.setIcon(p.getIcon());
                }
            }
        }

    }

    public WinState turn(int row, int col) {
        turnCnt++;

        board.setToken2d(row, col, board.getActivePlayer());

        WinState winner = board.checkWin();

        if (DEBUG) {
            printBoard();
            System.out.println("Who has won the match? " + winner);
        }

        updateBoard();
        board.nextTurn();
        markActivePlayer();
        return winner;
    }

    public void checkWinner(WinState winner) {
        if (WinState.none != winner) {
            String title = "";
            String msg = "";
            switch (winner) {
            case tie:
                msg = "The game ended with a draw!";
                title = "The game tied!";
                break;
            case player1:
            case player2:
                title = "A player won the match!";
                msg = ((WinState.player1 == winner) ? this.player1.getName() : this.player2.getName())
                        + " won the match!";
            default:
                break;
            }

            if (WinState.player1 == winner) {
                int currentScore = Integer.parseInt(player1_score.getText());
                currentScore++;
                this.player1_score.setText(currentScore + "");
            } else if (WinState.player2 == winner) {
                int currentScore = Integer.parseInt(player2_score.getText());
                currentScore++;
                this.player2_score.setText(currentScore + "");
            }

            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.PLAIN_MESSAGE);
            resetBoard();
        }
    }

    public void markActivePlayer() {
        Player active = board.getActivePlayer();
        Font p1f = player1_label.getFont();
        Font p2f = player2_label.getFont();
        if (player1 == active) {
            player1_label.setFont(p1f.deriveFont(Font.BOLD));
            player2_label.setFont(p2f.deriveFont(Font.PLAIN));
        } else {
            player1_label.setFont(p1f.deriveFont(Font.BOLD));
            player2_label.setFont(p2f.deriveFont(Font.PLAIN));
        }

    }

    public void reset() {
        board.resetGame();
        markActivePlayer();
        if (DEBUG)
            printBoard();
    }

    private void printBoard() {
        for (int m = 0; m < board.getM(); m++) {
            for (int n = 0; n < board.getN(); n++) {
                String player = board.getPlayerNameInField(m, n);
                System.out.print(player + " | ");
            }
            System.out.println();
        }
        System.out.println("################################");
    }

    public JButton[][] getButtonArr() {
        return buttonArr;
    }

    public void setButtonArr(JButton buttonArr[][]) {
        this.buttonArr = buttonArr;
    }

    public JLabel getPlayer1_score() {
        return player1_score;
    }

    public JLabel getPlayer2_score() {
        return player2_score;
    }
}
