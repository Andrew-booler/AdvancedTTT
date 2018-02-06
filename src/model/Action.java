package model;

public class Action {

	private int gridPos;
	private int boardPos;
	
	Action(){
		setGridPos(0);	// set to invalid positions by default
		setBoardPos(0);
	}

	Action(int gridPos, int boardPos) {
		this.setGridPos(gridPos);
		this.setBoardPos(boardPos);
	}

	Action(Action act) {
		gridPos = act.getGridPos();
		boardPos = act.getBoardPos();
	}
	
	public int getGridPos() {
		return gridPos;
	}

	public void setGridPos(int gridPos) {
		this.gridPos = gridPos;
	}

	public int getBoardPos() {
		return boardPos;
	}

	public void setBoardPos(int boardPos) {
		this.boardPos = boardPos;
	}
	
	
}
