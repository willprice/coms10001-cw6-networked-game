package game;

import net.RemoteControllable;
import net.RemoteInitialisable;

import net.TCPClient;
import state.ClientControllable;

import gui.TriggeredGUI;


import java.util.List;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import ai.AI;
import ai.AIReadable;
import ai.AI.Move;


public class GameController {


	private TriggeredGUI gui;
	private List<Integer> aiIDS = new ArrayList<Integer>();
	private List<Integer> guiIDS = new ArrayList<Integer>();
	private AI ai;
	private boolean visualise;
	private boolean useServer;
	public enum PlayerAssignments { AllGui, AllAI, MrXAI };

	protected AIReadable aiReadable;
	protected ClientControllable clientControllable;
	protected RemoteInitialisable remoteInitialisable;
	protected RemoteControllable remoteControllable;


	protected PlayerAssignments assignments = PlayerAssignments.MrXAI;


	/**
	 * Decide if you want the game to run on the server
	 * @param val
	 */
	public void setUseServer(boolean val)
	{
		useServer = val;
	}

	/**
	 * Decide if you want to visualise the game. NOTE, this is
	 * only really going to work in the full AI controlled game as the gui 
	 * is needed to input player moves in other modes
	 * @param vis
	 */
	public void setVisualise(boolean vis)
	{
		visualise = vis;
	}


	/**
	 * This function allows you to set different player assignments. This can either
	 * be Full AI, Mr X AI or Full GUI
	 * @param assignment
	 */
	public void setPlayerAssignments(PlayerAssignments assignment)
	{
		this.assignments = assignment;
	}

	/**
	 * Function to set an AI object
	 * @param a
	 */
	public void setAI(AI a)
	{
		this.ai = a;
	}


	

	/**
	 * Function to register a triggered AI
	 * @param gui
	 */
	public void registerGUI(TriggeredGUI gui)
	{
		this.gui = gui;
	}




	/**
	 * Here we check that the set up is ok depending on
	 * the different mode we have selected the GameController to use
	 * @return if setup was ok
	 */
	private boolean setupOK()
	{
		// check that if we are using an AI, an AI has been assigned
		if(this.assignments != PlayerAssignments.AllGui)
		{
			if(this.ai == null) 
			{
				TextOutput.printDebug("No AI Set\n");
				return false;
			}
		}
		
		
		// check that the needed objects have been set
		if(clientControllable == null)
		{
			TextOutput.printDebug("CLient Controllable Not Set\n");
			return false;
		}
		if(aiReadable == null)
		{
			TextOutput.printDebug("AI Readable Not Set\n");
			return false;
		}

		// depending on if we are using the server or not, we
		// do a different set of checks
		if(useServer)
		{
			return serverSetUpOk();
		}
		else
		{
			return offlineSetUpOk();
		}
	}

	/**
	 *  here we check that the GUI (no server) set up is ok
	 * @return is setup was ok
	 */
	private boolean offlineSetUpOk()
	{
		// obviously we need a valid gui object
		if(gui == null)
		{
			TextOutput.printDebug("No Gui Set\n");
			return false;
		}

		return true;
	}

	/**
	 * Function to check that the server setup is ok
	 * @return is setup was ok
	 */
	private boolean serverSetUpOk()
	{
		if(remoteControllable == null)
		{
			TextOutput.printDebug("Remote Controllable Not Set\n");
			return false;
		}


		return true;
	}




	/**
	 * Main function to run the game
	 */
	public void run(String sessionName)
	{
//		if(!setupOK())
//		{
//			TextOutput.printError("Setup Not Ok\n");
//		}

		// set up AI if we are using it
		if(this.assignments != PlayerAssignments.AllGui)
		{
			ai.registerAiReadable(aiReadable);
		}


		// run either offline or online
		if(!useServer)
		{
			runOffline();
		}
		else
		{
			runServer();
		}

	}
	
	/**
	 * Function that will be running the game on the server
	 */
	public void runServer()
	{
		// first we establish the TCP connection and we 
		// assign it to the relevant objects
		TCPClient tcp = new TCPClient();
		registerRemoteControllable(tcp);
		registerRemoteInitialisable(tcp);
		
		// assign the tcp connection the local game state so that it
		// is able to push changes to it
		tcp.registerClientControllable(clientControllable);
		
		// register the relevant objects with the GUI
		gui.registerVisualisable(aiReadable);
		gui.registerControllable(tcp);

		if(!setupOK())
		{
			TextOutput.printError("Setup Not Ok\n");
		}

		// now we send a message over the network to initialise a new game
		if(!remoteInitialisable.initialiseServerGame("Test", 5, 1))
		{
			TextOutput.printError("Game Cannot Be Initialised. Quitting\n");
			System.exit(0);
		}




		// we then get the files needed for the game to operate and initialise the local
		// game state with them
		String gameFilename = remoteInitialisable.getServerGame();
		String graphFilename = remoteInitialisable.getServerGraph();
		String posFilename = remoteInitialisable.getServerNodePositions();
		String mapFilename = remoteInitialisable.getServerMap();
		this.clientControllable.initialiseGame(mapFilename, graphFilename, posFilename, gameFilename);







		// try and get the players from the server
		List<Integer> playerIds = remoteControllable.joinServerGame();
		if(playerIds == null)
		{
			TextOutput.printError("Couldn't Get the Player IDS\n");
			System.exit(1);
		}


		// split the players between the gui and ai
		setUpAIandGUIIds(playerIds);



		// initialise the gui
		if(gui != null)
		{
			gui.setUpGui();
			gui.triggerUpdate();
			if(visualise) SwingUtilities.invokeLater(gui);

		}

		// while game is not over
		while(!remoteControllable.getServerGameOver())
		{

			// get the current player from the server
			int currentPlayer  = remoteControllable.getServerNextPlayer();


			// if the current player is gui controlled, we tell the GUI to make
			// the move
			if(guiControlled(currentPlayer))
			{
				// gui make move
				gui.takeMove();
			}
			else
			{
				// ai take move
				Move move = ai.getMove(currentPlayer);
				
				// send the AI's move to the server
				boolean success = remoteControllable.makeServerMove(currentPlayer, move.location, move.type);
				if(success)
				{
					// update the move on the local game state
					clientControllable.movePlayer(currentPlayer, move.location, move.type);
				}



			}
			
			// trigger an update
			gui.triggerUpdate();
		}

		// game has been won, get the winning player
		int winner = remoteControllable.getServerWinningPlayer();
		TextOutput.printStandard(String.format("The winning Player is: %d\n", winner));
		gui.close();
		System.exit(0);


	}
	
	
	/**
	 * Here we have the function that runs offline mode
	 */
	public void runOffline()
	{
		//  register the visualisable and controllable objects with the gui
		gui.registerControllable(clientControllable);
		gui.registerVisualisable(aiReadable);
		

		// initialise a new game in the game state
		// and set up the gui
		clientControllable.initialiseGame(5);
		gui.setUpGui();


		// get the set of ids from the game state
		List<Integer> dIds = aiReadable.getDetectiveIdList();
		List<Integer> xIds  = aiReadable.getMrXIdList();
		
		List<Integer> allIds = new ArrayList<Integer>();
		for(int id : dIds)
		{
			allIds.add(id);
		}
		
		for(int id : xIds)
		{
			allIds.add(id);
		}

		// split the players between the gui and ai
		setUpAIandGUIIds(allIds);

		// do we want the gui?
		if(visualise) SwingUtilities.invokeLater(gui);


		// while game is not over
		while(!aiReadable.isGameOver())
		{

			// get the current player
			int currentPlayer = aiReadable.getNextPlayerToMove();



			// if the player is controlled by the gui tell the gui
			// to take a go
			if(guiControlled(currentPlayer))
			{
				// gui make move
				gui.takeMove();
			}
			else
			{
				// ai take move
				Move move = ai.getMove(currentPlayer);
				
				// Use the suggested Move by the ai and push it to the game state
				clientControllable.movePlayer(currentPlayer, move.location, move.type);
			}

			// trigger the gui to redraw itself
			gui.triggerUpdate();
		}

		// game has been won, get the winning player
		int winner = aiReadable.getWinningPlayerId();
		TextOutput.printStandard(String.format("The winning Player is: %d\n", winner));
		gui.close();
		System.exit(0);

	}

	/**
	 * This function determines if a given id is controlled by the gui
	 * @param id
	 * @return true if gui controlled
	 */
	private boolean guiControlled(int id)
	{
		for(int t : guiIDS)
		{
			if(t == id) return true;
		}
		return false;
	}


	/**
	 * This function takes all the ids of the players and splits
	 * them by the choice of player assignment
	 * and given the 
	 * @param playerIds
	 */
	private void setUpAIandGUIIds(List<Integer> playerIds)
	{
		for(int id : playerIds)
		{
			if(assignments == PlayerAssignments.AllAI)
			{
				aiIDS.add(id);
			}
			else if(assignments == PlayerAssignments.AllGui)
			{
				guiIDS.add(id);
			}
			else
			{
				if(isMrx(id)) aiIDS.add(id);
				else guiIDS.add(id);
			}
		}
	}

	/**
	 * Function to check if a player id is of mr x
	 * @param id
	 * @return true if mr x
	 */
	private boolean isMrx(int id)
	{
		for(int t : aiReadable.getMrXIdList())
		{
			if(t == id) return true;
		}
		return false;
	}

	
	// registration functions
	public void registerAIReadable(AIReadable aiReadable)
	{
		this.aiReadable = aiReadable;
	}

	public void registerClientControllable(ClientControllable con)
	{
		this.clientControllable = con;
	}

	public void registerRemoteInitialisable(RemoteInitialisable in)
	{
		this.remoteInitialisable = in;
	}

	public void registerRemoteControllable(RemoteControllable con)
	{
		this.remoteControllable = con;
	}


}
