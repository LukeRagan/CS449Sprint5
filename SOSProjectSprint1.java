package guiguy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class SOSProjectSprint1 {
    public static ButtonGroup buttonGroupRed;
    public static ButtonGroup buttonGroupBlue;
    public static JRadioButton horizontalRadioButton1;
    public static JRadioButton horizontalRadioButton2;
    public static boolean isRedSClicked = false; //tracks S is selected for red player
    public static boolean isRedOClicked = false; // tracks O is selected for red player
    public static boolean isBlueSClicked = false; //tracks S is selected for blue player
    public static boolean isBlueOClicked = false; // tracks O is selected for blue player
    public static int boardSize = 7; // Default board size
    public static boolean isRedTurn = true; // Red player's turn initially
    public static JLabel turnLabel; // Label to display the current player's turn
    public static int redScore = 0;
    public static int blueScore = 0;
    public static String[][] board;
    public static JPanel gameBoardPanel; 	
    public static String computerSymbol;
    
    public static void recordGame(String[][] board, int row, int col, String playerSymbol) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_record.txt", true))) {
            // Append the player's move to the text file
            writer.write("Player: " + (isRedTurn ? "Red" : "Blue") + "\n");
            writer.write("Move: " + playerSymbol + " at Row " + (row + 1) + ", Col " + (col + 1) + "\n");
            writer.write("Board:\n");

            // Write the current state of the game board to the text file
            for (String[] rowArr : board) {
                for (String cell : rowArr) {
                    if (cell == null) {
                        writer.write("_ ");
                    } else {
                        writer.write(cell + " ");
                    }
                }
                writer.write("\n");
            }

            writer.write("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
    	String[] options = {"Singleplayer (You are Red)", "Singleplayer (You are Blue)", "Multiplayer"};
        int choice = JOptionPane.showOptionDialog(null, "Select Game Mode", "Game Mode",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // Set up variables to track game mode
        final boolean isSinglePlayer = (choice == JOptionPane.YES_OPTION || choice == JOptionPane.NO_OPTION);
        boolean isYouRed = (choice == JOptionPane.YES_OPTION);
        
        JFrame frame = new JFrame("SOS Game");

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JLabel blueLabel = new JLabel("Blue Player");
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(blueLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        //blue button group
        buttonGroupBlue = new ButtonGroup();
        JRadioButton radioButtonBlue1 = new JRadioButton("S");
        JRadioButton radioButtonBlue2 = new JRadioButton("O");

        buttonGroupBlue.add(radioButtonBlue1);
        buttonGroupBlue.add(radioButtonBlue2);

        // makes the buttons stay in the middle of the right side of the screen
        leftPanel.add(radioButtonBlue1);
        leftPanel.add(radioButtonBlue2);
        leftPanel.add(Box.createVerticalGlue());

        JCheckBox checkBox = new JCheckBox("Record??");
        leftPanel.add(checkBox);;
        
        //makes right panel and sets border
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        //title buttons
        JPanel horizontalRadioButtonsPanel = new JPanel();
        horizontalRadioButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // creates label for the title of the game
        JLabel label = new JLabel("SOS Game");
        horizontalRadioButtonsPanel.add(label);

        horizontalRadioButton1 = new JRadioButton("Simple Game");
        horizontalRadioButton2 = new JRadioButton("General Game");

        ButtonGroup horizontalButtonGroup = new ButtonGroup();
        horizontalButtonGroup.add(horizontalRadioButton1);
        horizontalButtonGroup.add(horizontalRadioButton2);

        horizontalRadioButtonsPanel.add(horizontalRadioButton1);
        horizontalRadioButtonsPanel.add(horizontalRadioButton2);

        // making panel for radio buttons on the right of the screen
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));

        // label for board size adjuster
        JLabel boardSizeLabel = new JLabel("Board Size");
        radioButtonsPanel.add(boardSizeLabel);

        // making buttons for red player
        JLabel redLabel = new JLabel("Red Player");
        radioButtonsPanel.add(Box.createVerticalGlue());
        radioButtonsPanel.add(redLabel);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonGroupRed = new ButtonGroup(); // Changed from local variable to class variable
        JRadioButton radioButtonRed1 = new JRadioButton("S");
        JRadioButton radioButtonRed2 = new JRadioButton("O");

        buttonGroupRed.add(radioButtonRed1);
        buttonGroupRed.add(radioButtonRed2);

        // Add action listeners to radio buttons for red player
        radioButtonRed1.addActionListener(new ActionListener() {
        	 @Override
        	 public void actionPerformed(ActionEvent e) {
        	     isRedSClicked = true;
        	     isRedOClicked = false;
        	     recordGame(board, -1, -1, "S"); // Adjust the row and col parameters as needed
            }
        });

        radioButtonRed2.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                isRedSClicked = false;
                isRedOClicked = true;
                recordGame(board, -1, -1, "O");
            }
        });
     // Add action listeners to radio buttons for blue player
        radioButtonBlue1.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                isBlueSClicked = true;
                isBlueOClicked = false;
                recordGame(board, -1, -1, "S");
            }
        });

        radioButtonBlue2.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                isBlueSClicked = false;
                isBlueOClicked = true;
                recordGame(board, -1, -1, "O");
            }
        });

        // makes the buttons stay in the middle of the right side of the screen
        radioButtonsPanel.add(radioButtonRed1);
        radioButtonsPanel.add(radioButtonRed2);
        radioButtonsPanel.add(Box.createVerticalGlue());

        // making game board size
        final JPanel gameBoardPanel = new JPanel();
        updateGameBoard(gameBoardPanel, boardSize, isSinglePlayer); // Initialize the game board

        // making board size adjuster
        final JSpinner boardSizeSpinner = new JSpinner();
        boardSizeSpinner.setModel(new SpinnerNumberModel(boardSize, 3, 8, 1)); //default, minimum, and maximum
        boardSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boardSize = (int) boardSizeSpinner.getValue();
                updateGameBoard(gameBoardPanel, boardSize, isSinglePlayer);
            }
        });
        radioButtonsPanel.add(boardSizeSpinner);

        // Create a label to display the current player's turn
        turnLabel = new JLabel("Red Player's Turn");
        rightPanel.add(turnLabel, BorderLayout.SOUTH); // Add the label to the bottom of rightPanel

        rightPanel.add(radioButtonsPanel, BorderLayout.CENTER);
        rightPanel.add(gameBoardPanel, BorderLayout.CENTER);
        rightPanel.add(horizontalRadioButtonsPanel, BorderLayout.NORTH);

        // makes the panels move to the respective side of the screen
        JPanel mainPanel = new JPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        mainPanel.add(radioButtonsPanel, BorderLayout.EAST);

        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeftPanel.add(checkBox);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(bottomLeftPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(containerPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton newGameButton = new JButton ("New Game");
        newGameButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		clearGameBoard(gameBoardPanel, boardSize);
        	}
        });
        leftPanel.add(newGameButton);
        
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        if (isSinglePlayer) {
            board = new String[boardSize][boardSize];
            if (!isYouRed) {
                // Computer is the Red player, and it makes the first move
                isRedTurn = false;
                computerMove();
            }
        }
    }	
    public static void computerMove() {
        if (!isRedTurn) {
            // Implement a simple AI for the computer's move
            Random random = new Random();
            int row, col;
            do {
                row = random.nextInt(boardSize);
                col = random.nextInt(boardSize);
            } while (board[row][col] != null);

            // Simulate a button click for the chosen position
            simulateButtonClick(row, col);

            // Check for a win condition after the computer's move
            if (checkForWin(board, row, col)) {
                turnLabel.setText("you win");
                disableButtons(gameBoardPanel);
            }
        }
    }
    public static void simulateButtonClick(int row, int col) {
        // Find the button at the specified position and trigger its ActionListener
        Component[] buttons = gameBoardPanel.getComponents();
        for (Component button : buttons) {
            if (button instanceof JButton) {
                JButton jButton = (JButton) button;
                int buttonRow = (int) jButton.getClientProperty("row");
                int buttonCol = (int) jButton.getClientProperty("col");
                if (buttonRow == row && buttonCol == col) {
                    jButton.doClick();
                    break;
                }
            }
        }
    }

 // Method to clear the game board
    public static void clearGameBoard(JPanel gameBoardPanel, int size) {
        Component[] buttons = gameBoardPanel.getComponents();
        for (Component button : buttons) {
            if (button instanceof JButton) {
                JButton jButton = (JButton) button;
                jButton.setText("");
                jButton.setForeground(Color.BLACK); // Reset button color
            }
        }
        isRedTurn = true; // Reset the turn to Red Player's Turn
        turnLabel.setText("Red Player's Turn");
    }
 //method to check for a draw condition
    public static boolean checkForDraw(String[][] board) {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell == null || cell.isEmpty()) {
                    // There are empty cells, the game is not a draw
                    return false;
                }
            }
        }
        // All cells are filled with "S" or "O," and no win condition is satisfied
        return true;
    }
    public static void updateGameBoard(final JPanel gameBoardPanel, int size, final boolean isSinglePlayer) {
        gameBoardPanel.removeAll(); // Clear previous components
        gameBoardPanel.setLayout(new GridLayout(size, size)); // Set new grid layout

        int cellSize = 50;

        // Initialize the board array
        board = new String[size][size];	
        // Initialize the gameBoardPanel variable
        SOSProjectSprint1.gameBoardPanel = gameBoardPanel;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final JButton button = new JButton();
                button.setPreferredSize(new Dimension(cellSize, cellSize));
                button.setBackground(Color.GREEN);
                button.setBorder(BorderFactory.createLineBorder(Color.black));
                gameBoardPanel.add(button);

                final int row = i;
                final int col = j;
                
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isRedTurn) {
                        	button.setForeground(Color.RED);
                            if (isRedSClicked) {
                                button.setText("S");
                                board[row][col] = "S";
                            } else if (isRedOClicked) {
                                button.setText("O");
                                board[row][col] = "O";
                            }
                            turnLabel.setText("Blue Player's Turn"); // Set label text for Blue Player's Turn
                            if (checkForWin(board,row,col)) {
                            	redScore++;
                            }
                        } else {
                        	button.setForeground(Color.BLUE);
                            if (isBlueSClicked) {
                                button.setText("S");
                                board[row][col] = "S";
                            } else if (isBlueOClicked) {
                                button.setText("O");
                                board[row][col] = "O";
                            }
                            turnLabel.setText("Red Player's Turn"); // Set label text for Red Player's Turn
                            if (checkForWin(board,row,col)) {
                            	blueScore++;
                            }
                        }
                        isRedTurn = !isRedTurn; // Switch turn
                        
                        //check for win condition
                        if (horizontalRadioButton1.isSelected()) {
                        	if (checkForWin(board, row, col)) {
                            	turnLabel.setText("Blue is the Winner");
                            	disableButtons(gameBoardPanel);
                            } else if (checkForDraw(board)) {
                            	turnLabel.setText("Draw");
                            	disableButtons(gameBoardPanel);
                            }
                        }
                        if (horizontalRadioButton2.isSelected()) {
                        	if (isBoardFull(board)) {
                            	determineWinner();
                            	disableButtons(gameBoardPanel);
                            }
                        }
                        if (isSinglePlayer && !checkForWin(board, row, col) && !checkForDraw(board)) {
                            computerMove();
                        }
                    }
                });
                
                button.putClientProperty("row", i);
                button.putClientProperty("col", j);
            }
        }

        gameBoardPanel.revalidate();
        gameBoardPanel.repaint();
    }
//check if board is full
public static boolean isBoardFull(String[][] board) {
	for (String [] row : board) {
		for (String cell : row) {
			if (cell == null || cell.isEmpty()) {
				return false;
			}
		}
	}
	return true;
}
public static void determineWinner() {
	if (redScore > blueScore) {
		turnLabel.setText("Red Player Wins");
	} else if (redScore < blueScore) {
		turnLabel.setText("Blue Player Wins");
	} else {
		turnLabel.setText("Draw");
	}
}

//method for checking win condition
public static boolean checkForWin(String[][] board, int row, int col) {
	String[] sos = {"S", "O", "S"};
	for (int i = -1; i <= 1; i++) {
		for (int j = -1; j <= 1; j++) {
			if (i == 0 && j == 0) {
				continue;
			}
			int count = 0;
			for (int k =0; k < 3; k++) {
				int newRow = row + i * k;
				int newCol = col + j * k;
				if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length && board [newRow][newCol] != null && board[newRow][newCol].equals(sos[k])) {
					count++;
				}
			}
			if (count == 3) {
				return true; // found win condition
			}
		}
	}
	return false;
}

// method to disable all buttons after a win
public static void disableButtons(JPanel gameBoardPanel) {
	Component[] buttons = gameBoardPanel.getComponents();
	for (Component button : buttons) {
		if (button instanceof JButton) {
			JButton jButton = (JButton) button;
			jButton.setEnabled(false);
		}
	}
}
}
