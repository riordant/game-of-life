import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.lang.Math;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.*;

public class GameOfLife 
{
	public static int PUZZLE_WIDTH;
	public static int PUZZLE_HEIGHT = PUZZLE_WIDTH;
	public static int GENERATION = 0;
	public static int MIN_VARIABLE_DIM = 8;
	public static final int MAX_GRIDS = 2500;
	public static final int PERSON = 1;
	public static final int MIN_PUZZLE_DIM = 8;
	public static final int MIN_WINDOW_DIM = 18;
	public static final int ALIVE = 3;
	public static final int UNDER_POP = 2;
	public static final int OVER_POP = 3;
	public static final int BAL_POP1 = 2;
	public static final int BAL_POP2 = 3;
	public static final int DEAD = 0;
	public static final int LIVING = 1;

	
	public static void main(String[] args) 
	{
		
		
		JFrame frame = new JFrame();
		
	    String gridDimList[] = new String[(int)Math.sqrt(MAX_GRIDS)-(MIN_PUZZLE_DIM-1)]; //8x8 - 50x50
	    String gridList[]; // array of dimensions of the grid for printing to screen
	    String sqrdStr;    // the string for these dimensions.
		boolean boardCheck = true;  // until the board is all zeros
	    int i,rootGrid;
		int board[][] = new int[0][0];    // first board
		Scanner scanner;
		String input;
		
		
		for(i=0; i<gridDimList.length;i++){		//sets each position of gridDimList to a grid dimension
	    	sqrdStr = MIN_VARIABLE_DIM + " x " + MIN_VARIABLE_DIM;
	    	gridDimList[i] = sqrdStr;
	    	MIN_VARIABLE_DIM++;
	    }
		
		try{
		
		JOptionPane.showMessageDialog(frame, "Conway's Game of Life - 1970\nProgrammed by: Tadhg Riordan", 
											 "Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
		JOptionPane.showMessageDialog(frame, "Rules:\n1. Select a game size.\n2. Select live game cells (Hold CMD/CTRL to select multiple cells).\n\n" 
											 + "The game continues through a set of generations.\nA living cell with less than two neighbours dies of " 
											 + "underpopulation.\n" + "A living cell with two or three neighbours lives to the next generation.\n" +
											 "A living cell with more than three neighbours dies of overpopulation." +
											 "\nAny dead cell with exactly three neighbours comes alive.", 
											 "Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
		
		input = (String) JOptionPane.showInputDialog(frame,"Choose the number of grids for the game:", 
													"Conway's Game Of Life",JOptionPane.PLAIN_MESSAGE, null, gridDimList, gridDimList[0]);
		scanner = new Scanner(input);
		rootGrid = scanner.nextInt();
		PUZZLE_HEIGHT = rootGrid;
		PUZZLE_WIDTH = rootGrid;
		
		board = new int[PUZZLE_WIDTH][PUZZLE_HEIGHT];
		board = makeBlankBoard(board);
		
		gridList = new String[rootGrid*rootGrid];
		for(i=0; i<gridList.length;i++)
		{
				gridList[i] = "\u2B1C "; 
		}
		
		
		JList list = new JList(gridList);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(PUZZLE_HEIGHT);
		JScrollPane listScroller = new JScrollPane(list);
		
		JOptionPane.showMessageDialog(null, listScroller,"Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
		int[] selected = list.getSelectedIndices(); //creates an array of the indexes of grids highlighted
		for(i=0; i<selected.length;i++)
		{
			board = setUpPeople(board,selected[i]);
		}
		
		printBoard(board);
		
		}catch(NoSuchElementException noElem){}
		catch(NullPointerException cancel){
			System.exit(0);
		}
		
		while(boardCheck == true)
		{
			
			GENERATION++;
			board = checkChanges(board); 
			printBoard(board);
			
			boardCheck = false;
			for(int x=0; x<PUZZLE_WIDTH;x++)
			{
				for(int y=0; y<PUZZLE_HEIGHT; y++)
				{
					if(board[x][y] == LIVING)
					{
						boardCheck = true; 
					}
				}
			}
		}
		
		if(GENERATION == 1) JOptionPane.showMessageDialog(frame, "Every person has died. The game lasted for 1 generation.",
																 "Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
		else JOptionPane.showMessageDialog(frame, "Every person has died. The game lasted for " + GENERATION + " generations.",
																"Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
	}
	
	public static int[][] makeBlankBoard(int[][] board){ //sets board to zero.
		for(int x=0; x<PUZZLE_WIDTH;x++)
		{
			
			for(int y=0; y<PUZZLE_HEIGHT; y++)
			{
				board[x][y] = DEAD;
			}
		}
		return board;
		
	}
	
	public static int[][] setUpPeople(int[][] board, int num){ //Sets up person on board.
		
		int row;
		int col;
		
		row = ((int) num / PUZZLE_HEIGHT);
		col = (num % PUZZLE_WIDTH);
		
		board[row][col] = PERSON;
		return board;
	}
	
	public static int[][] checkChanges(int[][] board)
	{
		int x,y,count;
		int newBoard[][] = new int[PUZZLE_WIDTH][PUZZLE_HEIGHT]; //This is the board that will be returned.
		newBoard = makeBlankBoard(newBoard);
		for(x=0; x<PUZZLE_WIDTH;x++)
		{
			for(y=0; y<PUZZLE_HEIGHT;y++)
			{
				count = 0;
					if(x==0 && y==0) 		//top left
					{					
						if(board[x][y+1] == 1) count++; 
						if(board[x+1][y] == 1) count++; 
						if(board[x+1][y+1] == 1) count++; 
					}
					
					else if(x==(PUZZLE_WIDTH-1) && y==0) //bottom left
					{		
						if(board[x-1][y] == 1) count++; 
						if(board[x][y+1] == 1) count++; 
						if(board[x-1][y+1] == 1) count++; 
					}
					
					else if(x==0 && y==(PUZZLE_HEIGHT-1))//top right
					{     
						if(board[x+1][y] == 1) count++; 
						if(board[x][y-1] == 1) count++; 
						if(board[x+1][y-1] == 1) count++; 
					}
					
					else if(x==(PUZZLE_WIDTH-1) && y==(PUZZLE_HEIGHT-1)) //bottom right
					{     
						if(board[x-1][y] == 1) count++; 
						if(board[x][y-1] == 1) count++; 
						if(board[x-1][y-1] == 1) count++; 
					}
					
					else if(x==0) //Top Row
					{     
						if(board[x][y-1] == 1) count++; 
						if(board[x][y+1] == 1) count++; 
						if(board[x+1][y] == 1) count++; 
						if(board[x+1][y+1] == 1) count++;  
						if(board[x+1][y-1] == 1) count++; 
					}
					
					else if(y==0) //Left Column
					{     
						if(board[x-1][y] == 1) count++;
						if(board[x+1][y] == 1) count++; 
						if(board[x][y+1] == 1) count++; 
						if(board[x+1][y+1] == 1) count++; 
						if(board[x-1][y+1] == 1) count++; 
					}
					
					else if(x==(PUZZLE_HEIGHT-1)) //Bottom Row
					{     
						if(board[x][y-1] == 1) count++; 
						if(board[x][y+1] == 1) count++; 
						if(board[x-1][y] == 1) count++; 
						if(board[x-1][y+1] == 1) count++; 
						if(board[x-1][y-1] == 1) count++; 
					}
					
					else if(y==(PUZZLE_WIDTH-1)) //Right Column
					{     
						if(board[x-1][y] == 1) count++; 
						if(board[x+1][y] == 1) count++; 
						if(board[x][y-1] == 1) count++; 
						if(board[x-1][y-1] == 1) count++; 
						if(board[x+1][y-1] == 1) count++; 
					}
					
					else //any other square
					{
						if(board[x][y-1] == 1) count++;
						if(board[x][y+1] == 1) count++; 
						if(board[x-1][y] == 1) count++;
						if(board[x+1][y] == 1) count++; 
						if(board[x-1][y-1] == 1) count++; 
						if(board[x-1][y+1] == 1) count++;
						if(board[x+1][y-1] == 1) count++;
						if(board[x+1][y+1] == 1) count++;
					}
					
					if(board[x][y] == LIVING) //terms for alive people
					{ 	
					
						if(count < UNDER_POP) newBoard[x][y] = DEAD; 				 // Under-Population
						if(count == BAL_POP1 || count == BAL_POP2) newBoard[x][y] = LIVING; // Unbalanced Population
						if(count > OVER_POP) newBoard[x][y] = DEAD; 				 // Over Population
					
					}
					
					else if(board[x][y] == DEAD) //terms for dead people
					{  
						if(count == ALIVE) newBoard[x][y] = LIVING; // Colonisation
					}
				}
		}
		
		return newBoard;
	}
	
	public static void printBoard(int[][] board)
	{
		JFrame frame = new JFrame();
		String string = "";
		String ultimateString = "";
	
		for(int x=0; x<PUZZLE_WIDTH;x++)
		{
			
			for(int y=0; y<PUZZLE_HEIGHT; y++)
			{
				if(board[x][y] == DEAD) string+= " " + "\u2B1C";
				else string+= " " + "\u2B1B";
			}
			ultimateString += string + "\n";
			string = "";
		}
		JOptionPane.showMessageDialog(frame,"Generation: " + GENERATION + "\n" + ultimateString, "Conway's Game Of Life", JOptionPane.PLAIN_MESSAGE);
	}
}