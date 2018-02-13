package model;

import java.util.ArrayList;

public class State {

	private Board[][] grid;	// containing 9 Board
	private int turn;		// +1 for X, -1 for O; set to 1 by default
	private int maxDepth;	// limited depth, set by constructor parameter
	private int xEvaluation;	// the higher the value, the more likely X will win; set to 0 by default
	private int oEvaluation;	// the higher the value, the more likely O will win; set to 0 by default
	private Action lastAction;	// last action
	
	// default constructor
	public State() {
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
		this.maxDepth = 10;
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
		lastAction = (s.getLastAction() == null ? null : new Action(s.getLastAction()));

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
			
			if (grid[lastBoardRow][lastBoardCol].isTie()) {	// corresponding board is tie
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
		if (lastAction == null) {
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					actions.addAll(grid[i][j].getAvlActions());
				}
			}
		} else {
			int lastBoardPos = lastAction.getBoardPos();
			int lastBoardRow = (lastBoardPos - 1) / 3;
			int lastBoardCol = (lastBoardPos - 1) % 3;
			
			if (grid[lastBoardRow][lastBoardCol].isTie()) {
				for (int i = 0; i < grid.length; i++) {
					for (int j = 0; j < grid[i].length; j++) {
						actions.addAll(grid[i][j].getAvlActions());
					}
				}
			} else {
				actions.addAll(grid[lastBoardRow][lastBoardCol].getAvlActions());
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
			lastAction = action;
			maxDepth--;	// more deeper
			turn = -1;
		} else if (turn == -1) {
			grid[gridRow][gridCol].update(action, turn);
			lastAction = action;
			maxDepth--;	// more deeper
			turn = 1;
		} else {
			try {
				throw new Exception("No such turn: " + turn);
			} catch (Exception e) {
				System.err.print(e.getMessage());
			}
		}
	}

	// check whether this state is tie
	public boolean isTie() {
		boolean tieFlag = true;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].findWinner()) {
					return false;
				} else {
					tieFlag &= grid[i][j].isTie();
				}
			}
		}
	
		return tieFlag;
	}
		
	// check whether this state is terminal when the action is taken
	public boolean isTerminal() {
		boolean tieFlag = true;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].findWinner()) {
					return true;
				} else {
					tieFlag &= grid[i][j].isTie();
				}
			}
		}

		return tieFlag;
	}
	
	// check whether this state is cutoff
	public boolean isCutoff() {
		return isTerminal() || maxDepth == 0;
	}
	
	// compute evaluation value when the state is terminal
	public void calEvaluation() {
		int grade[][] = new int[3][3];	// record the state of small board

		xEvaluation = 0;
		oEvaluation = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j].calEvaluation();
				xEvaluation += grid[i][j].getxEvaluation();
				oEvaluation += grid[i][j].getoEvaluation();
				if (grid[i][j].isTerminal && grid[i][j].findWinner()) {	// a small board has a winner
					if (grid[i][j].getxEvaluation() > grid[i][j].getoEvaluaton()) {
						grade[i][j] = 1;	// X win
					} else if (grid[i][j].getxEvaluation() < grid[i][j],getoEvaluation()) {
						grade[i][j] = -1;	// Y win
					} else {
						grade[i][j] = 0;	// not finished
					}
				} else {	// a small board is tie or has not been terminated
					grade[i][j] = 0;
				}
			}
		}

		int xTmp = 0;
		int oTmp = 0;

		// compute row value
		for (int i = 0; i < 3; i++) {
			switch(getRowSum(grade, i)) {
				case 3:
					xTmp += 100;	
					break;
				case 2:
					xTmp += 10;	
					break;
				case -3:
					xTmp -= 100;	
					break;
				case -2:
					xTmp -= 10;
					break;
				default:
					break;
			}
		}

		// compute column value
		for (int i = 0; i < 3; i++) {
			switch(getColSum(i)) {
				case 3:
					xTmp += 100;
					break;
				case 2:
					xTmp += 10;
					break;
				case -3:
					xTmp -= 100;
					break;
				case -2:
					xTmp -= 10;
					break;
				default:
					break;
			}
		}
		
	// compute diagonal value
		switch(board[0][0] + board[1][1] + board[2][2]) {
			case 3:
				xTmp += 100;
				break;
			case 2:
				xTmp += 10;
				break;
			case -3:
				xTmp -= 100;
				break;
			case -2:
				xTmp -= 10;
				break;
			default:
				break;
		}

	// compute inverse diagonal value
		switch(board[0][2] + board[1][1] + board[2][0]) {
			case 3:
				xTmp += 100;
				break;
			case 2:
				xTmp += 10;
				break;
			case -3:
				xTmp -= 100;
				break;
			case -2:
				xTmp -= 10;
				break;
			default:
				break;
		}

		xEvaluation = xTmp - oTmp;
		oEvaluation = oTmp - xTmp;
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
	
	public Action getLastAction() {
		return lastAction;
	}

	// private functions
	private int getRowSum(int board[][], int row) {
		return board[row][0] + board[row][1] + board[row][2];
	}
	
	private int getColSum(int board[][], int col) {
		return board[0][col] + board[1][col] + board[2][col];
	}
}
