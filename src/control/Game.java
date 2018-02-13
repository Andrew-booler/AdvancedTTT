package control;

import model.Action;
import model.State;
import view.Interaction;

public class Game {
	private State currentState;
	private int roleSelection;
	public static void main(String[] args) {
		while (true) {
			//Initial a new game
			Game game = new Game();
			if (game.getRoleSelection()==1) {
				//Select the role
				while(true) {
					//User picks X
					//Ask user for the next move
					Interaction.displayBoard(game.getCurrentState());
					Action act = Interaction.play();
					//Validate the move
					while (!game.getCurrentState().isValidAction(act)) {
						Interaction.displayMsg("The Action is invalid");
						act= Interaction.play();
					}
					//Display and perform the move
					Interaction.displayMove(act);
					game.getCurrentState().update(act);
					//Check if the game is over
					if (game.getCurrentState().isTerminal()) {
						game.getCurrentState().calEvaluation();
						if(game.getCurrentState().getxEvaluation()>game.getCurrentState().getoEvaluation()) {
						Interaction.displayBoard(game.getCurrentState());
						Interaction.displayMsg("You win!");
						break;
						}else {
							game.getCurrentState().calEvaluation();
							if(game.getCurrentState().isTie()) {
								Interaction.displayBoard(game.getCurrentState());
								Interaction.displayMsg("Tie.");
								break;
							}
						}
					}
					//Same steps for computer player
					Interaction.displayBoard(game.getCurrentState());
					act = Computer.play(game.getCurrentState(),-game.getRoleSelection());
					while (!game.getCurrentState().isValidAction(act)) {
						Interaction.displayMsg("The Action is invalid");
						act= Computer.play(game.getCurrentState(),-game.getRoleSelection());
					}
					Interaction.displayMove(act);
					game.getCurrentState().update(act);
					if (game.getCurrentState().isTerminal()) {
						game.getCurrentState().calEvaluation();
						if(game.getCurrentState().getxEvaluation()<game.getCurrentState().getoEvaluation()) {
							Interaction.displayBoard(game.getCurrentState());
							Interaction.displayMsg("You lose!");
							break;
							}
					}
				}
			}else {
				//User picks O
				while(true) {
					Interaction.displayBoard(game.getCurrentState());
					Action act = Computer.play(game.getCurrentState(),-game.getRoleSelection());
					while (!game.getCurrentState().isValidAction(act)) {
						Interaction.displayMsg("The Action is invalid");
						act= Computer.play(game.getCurrentState(),-game.getRoleSelection());
					}
					Interaction.displayMove(act);
					game.getCurrentState().update(act);
					if (game.getCurrentState().isTerminal()) {
						game.getCurrentState().calEvaluation();
						if(game.getCurrentState().getoEvaluation()<game.getCurrentState().getxEvaluation()) {
						Interaction.displayBoard(game.getCurrentState());
						Interaction.displayMsg("You lose!");
						break;
						}else {
							if(game.getCurrentState().getoEvaluation()==0) {
								Interaction.displayBoard(game.getCurrentState());
								Interaction.displayMsg("Tie.");
								break;
							}
						}
					}
					Interaction.displayBoard(game.getCurrentState());
					act = Interaction.play();
					while (!game.getCurrentState().isValidAction(act)) {
						Interaction.displayMsg("The Action is invalid");
						act= Interaction.play();
					}
					Interaction.displayMove(act);
					game.getCurrentState().update(act);
					if(game.getCurrentState().isTerminal()) {
						game.getCurrentState().calEvaluation();
						if(game.getCurrentState().getoEvaluation()>game.getCurrentState().getxEvaluation()) {
							Interaction.displayBoard(game.getCurrentState());
							Interaction.displayMsg("You win!");
							break;
							}
					}
					
				}
			}
		}
	}
	public Game() {
		currentState = new State();
		roleSelection = Interaction.selectRole();
	}
	/**
	 * @return roleSelection
	 */
	public int getRoleSelection() {
		return roleSelection;
	}
	/**
	 * @param roleSelection  roleSelection
	 */
	public void setRoleSelection(int roleSelection) {
		this.roleSelection = roleSelection;
	}
	public State getCurrentState() {
		return currentState;
	}
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

}
