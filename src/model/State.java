package model;

import java.util.ArrayList;
import java.util.Arrays;

public class State {

	private Board[][] grid;	// containing 9 Board
	private int turn;		// +1 for X, -1 for O; set to 1 by default
	private int maxDepth;	// limited depth, set by constructor parameter
	private int xEvaluation;	// the higher the value, the more likely X will win; set to 0 by default
	private int oEvaluation;	// the higher the value, the more likely O will win; set to 0 by default
	private Action lastAction;	// last action
	
	// default constructor
	public State(int maxDepth) {
		grid = new Board[3][3];
		// initialize board
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Board();
				grid[i][j].setGridIdx(3 * i + j + 1);	// set board position
			}
		}
		// initialize turn
		turn = 1;
		// initialize max depth
		this.maxDepth = maxDepth;
		// initialize evaluation value
		xEvaluation = 0;
		oEvaluation = 0;
		// initialize last action to null
		lastAction = null;

	}
	
	// copy constructor
	public State(State s) {
		// copy grid
		grid = new Board[3][3];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Board(s.getGrid()[i][j]);
				grid[i][j].setGridIdx(s.getGrid()[i][j].getGridIdx());
			}
		}
		// copy trun
		turn = s.getTurn();
		// copy max depth
		maxDepth = s.getMaxDepth();
		// copy evaluation value
		xEvaluation = s.getxEvaluation();
		oEvaluation = s.getoEvaluation();
		// copy last action
		lastAction = new Action(s.getLastAction());

	}
	
	// check whether an action is valid
	public boolean isValidAction(Action action) {
		int gridPos = action.getGridPos();	// board position in grid
		int gridRow = (gridPos - 1) / 3;
		int gridCol = (gridPos - 1) % 3;
		
		if (lastAction == null) {	// first move
			return grid[gridRow][gridCol].isValidAction(action);
		} else {
			int lastBoardPos = lastAction.getBoardPos();
			int lastBoardRow = (lastBoardPos - 1) / 3;
			int lastBoardCol = (lastBoardPos - 1) % 3;
			
			if (grid[lastBoardRow][lastBoardCol].isFull()) {	// corresponding board is full
				return grid[gridRow][gridCol].isValidAction(action);
			} else {
				if (gridPos != lastBoardPos) {
					return false;
				} else {
					return grid[gridRow][gridCol].isValidAction(action);
				}
			}
		}
	}
	
	// get all applicable actions under this state
	public ArrayList<Action> getAvlActions() {
		ArrayList<Action> actions = new ArrayList<Action>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				actions.addAll(grid[i][j].getAvlActions());
			}
		}
		
		return actions;
	}
	
	// update state according to the action
	public void update (Action action) {
		int gridPos = action.getGridPos();
		int gridRow = (gridPos - 1) / 3;
		int gridCol = (gridPos - 1) % 3;
		
		if (turn == 1) {
			grid[gridRow][gridCol].update(action, turn);
			turn = -1;
		} else if (turn == -1) {
			grid[gridRow][gridCol].update(action, turn);
			turn = 1;
		} else {
			try {
				throw new Exception("No such turn: " + turn);
			} catch (Exception e) {
				System.err.print(e.getMessage());
			}
		}
	}
	
	// check whether this state is terminal when the action is taken
	public boolean isTerminal() {
		if (isFull()) {
			return true;
		} else if (getRowSum(0) == 3 || getRowSum(1) == 3 || getRowSum(2) == 3 || getColSum(0) == 3 || getColSum(1) == 3 || getColSum(2) == 3 ||
					board[0][0] + board[1][1] + board[2][2] == 3 || board[0][2] + board[1][1] + board[2][0] == 3 ||
					getRowSum(0) == -3 || getRowSum(1) == -3 || getRowSum(2) == -3 || getColSum(0) == -3 || getColSum(1) == -3 || getColSum(2) == -3 ||
					board[0][0] + board[1][1] + board[2][2] == -3 || board[0][2] + board[1][1] + board[2][0] == -3) {
			return true;
		} else {
			return false;
		}
	}
	
	// compute utility when the state is terminal
	public void calUtility() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				xEvaluation += grid[i][j].getxEvaluation();
				oEvaluation += grid[i][j].getoEvaluation();
			}
		}
	}

	// getters
	public Board[][] getGrid() {
		return grid;
	}

	public int getTurn() {
		return turn;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public int getxEvaluation() {
		return xEvaluation;
	}

	public int getoEvaluation() {
		return oEvaluation;
	}

	// private functions
	private int getRowSum(int row) {
		return board[row][0] + board[row][1] + board[row][2];
	}
	
	private int getColSum(int col) {
		return board[0][col] + board[1][col] + board[2][col];
	}
	
	public Action getLastAction() {
		return lastAction;
	}

	// check whether the board is full
	private boolean isFull() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0) {
					return false;
				}
			}
		}
		
		return true;
	}
}