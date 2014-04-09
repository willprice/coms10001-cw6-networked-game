package state;

import ai.AIReadable;
import game.TextOutput;
import graph.Graph;

public class ClientGameState extends GameState implements AIReadable, ClientControllable {
	
		
	public Graph getGraph() 
	{
		return map.getGraph();
	}


	public Boolean initialiseGame(String mapFilename, String graphFilename,
			String positionsFilename, String gameFilename) {
		
		// initialise the Map object from the map file
		this.initialiseMap(mapFilename, positionsFilename, graphFilename);
		
		// call the gamestate's load game function. This will
		// create a new set of game data that matches the game data in the database
		if(!this.loadGame(gameFilename))
		{
			TextOutput.printError("Couldn't load the Game Data\n");
			System.exit(1);
		}
		
		// create a new game logic object based on the map and game data
		gameLogic = GameLogic.newGame(map, gameData);
		
		return true;
	}
}
