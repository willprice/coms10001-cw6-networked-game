package game;
import game.GameController.PlayerAssignments;
import gui.TriggeredGUI;
import ai.AI;
import ai.RandomAI;
import ai.ShortestPathAI;
import state.*;


public class Game {

	public static void main(String[] args) {
		Game game = new Game();
		TextOutput.setDebugMode(true);
		
		int visualise = 0; //Integer.parseInt(args[1]);
		String name = args[0];
		
		if(visualise == 0) game.run(name, true);
		else game.run(name,false);
	}
	

	
	public void run(String sessionName, boolean visualise)
	{
		// create all the object for the game
		ClientGameState state = new ClientGameState();
		GameController controller = new GameController();
		TriggeredGUI gui = new TriggeredGUI();
		AI ai = new ShortestPathAI();
		
		
		
		// register the Ai readable and client controllable objects (local game state)
		controller.registerAIReadable(state);
		controller.registerClientControllable(state);
		
		// register the gui and the ai
		controller.setAI(ai);
		controller.registerGUI(gui);
		
		
		// specify the mode of the game we are playing
		controller.setVisualise(visualise);
		controller.setPlayerAssignments(PlayerAssignments.AllGui);
		controller.setUseServer(true);
		
		// run the game
		controller.run(sessionName);
	}
}
