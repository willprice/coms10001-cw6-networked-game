package state;


import game.TextOutput;

import java.io.File;
import java.io.IOException;
import java.util.*;

import state.Player.PlayerType;

public class GameData {
	
	private HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	private List<Integer> playerIds          = new ArrayList<Integer>();
	private List<Integer> detectiveIds       = new ArrayList<Integer>();
	private List<Integer> mrXIds             = new ArrayList<Integer>();
	private int currentPlayerIndex;
	private int numberOfPlayers;
	private String sessionName;
	private int sessionId;
	
	
	
	@Override
	public String toString() {
		String output= "GameData [playerIds=" + playerIds
				+ ", detectiveIds=" + detectiveIds + ", mrXIds=" + mrXIds
				+ ", currentPlayerIndex=" + currentPlayerIndex
				+ ", numberOfPlayers=" + numberOfPlayers + "]";
		
		for(int k : players.keySet())
		{
			output += players.get(k).toString() + "\n";
		}
		
		return output;
		
	}

	public static GameData loadFromFile(String filename) throws IOException
	{
		GameData data = new GameData();
		
		// start reading the file
		Scanner in = new Scanner(new File(filename));
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			String [] parts = line.split(":");
			String key = parts[0];
			
			if(key.equals("session_id")) data.sessionId = Integer.parseInt(parts[1]);
			else if(key.equals("session_name")) data.sessionName = parts[1];
			else if(key.equals("player")) data.addPlayer(parts[1]);
			else if(key.equals("move")) data.addMove(parts[1]);
			else continue;
			
		}
		
		in.close();
		
		return data;
	}
	
	public void addMove(String info)
	{
		String[] parts = info.split(",");
		int playerId = Integer.parseInt(parts[parts.length-1]);
		int location = Integer.parseInt(parts[2]);
		String type = parts[3];
		Initialisable.TicketType t= null;
		if(type.equals("Taxi")) t= Initialisable.TicketType.Taxi;
		else if(type.equals("Bus")) t= Initialisable.TicketType.Bus;
		else if(type.equals("Underground")) t= Initialisable.TicketType.Underground;
		else if(type.equals("DoubleMove")) t= Initialisable.TicketType.DoubleMove;
		else if(type.equals("SecretMove")) t= Initialisable.TicketType.SecretMove;
		
		this.getPlayer(playerId).addMove(t, location);
	}
	

	public GameData() 
	{
		numberOfPlayers = 0;
		currentPlayerIndex = 0;
	}
	
	
	boolean isMrX(int playerId)
	{
		if(mrXIds.contains(playerId)) return true;
		return false;
	}
	
	public Player mrX()
	{
		//TextOutput.printDebug(this.toString() + "\n");
		return players.get(mrXIds.get(0));
	}
	
	boolean isDetective(int playerId)
	{
		if(detectiveIds.contains(playerId)) return true;
		return false;
	}
	
	
	public void addPlayer(String playerInfo)
	{

		String[] parts = playerInfo.split(",");
		int id = Integer.parseInt(parts[0]);
		
		PlayerType type = PlayerType.Detective;
		if(parts[1].equals("X")) type = PlayerType.MrX;
		
		int location = Integer.parseInt(parts[2]);
		
		
		
		// parse the tickets out
		HashMap<Initialisable.TicketType, Integer> tickets = new HashMap<Initialisable.TicketType, Integer>();
		tickets.put(Initialisable.TicketType.Taxi, Integer.parseInt(parts[3]));
		tickets.put(Initialisable.TicketType.Bus, Integer.parseInt(parts[4]));
		tickets.put(Initialisable.TicketType.Underground, Integer.parseInt(parts[5]));
		tickets.put(Initialisable.TicketType.DoubleMove, Integer.parseInt(parts[6]));
		tickets.put(Initialisable.TicketType.SecretMove, Integer.parseInt(parts[7]));
		
		Player player = new Player(id, type, location, tickets);
		addPlayer(player);
	
	}
	
	/**
	 * Add a new player to the game
	 * @param player
	 */
	public void addPlayer(Player player)
	{
		players.put(player.playerId(), player);
		
		if(player.type() == Player.PlayerType.MrX)
		{
			mrXIds.add(player.playerId());
		}
		else
		{
			detectiveIds.add(player.playerId());
		}
		
		playerIds.add(player.playerId());
		numberOfPlayers++;
		currentPlayerIndex = numberOfPlayers-1;
	}
	
	public void removePlayer(int playerId)
	{
		Player player = players.get(playerId);
		
		TextOutput.printDebug(String.format(
				"Removing player %d\n", playerId));
		
		
		int index = 0;
		for(int i = 0; i < playerIds.size(); i++)
		{
			if(playerId == playerIds.get(i)) index = i;
		}
		
		
		playerIds.remove(index);
		
		if(isMrX(playerId))
		{
			mrXIds.remove(playerId);
		}
		else
		{
			detectiveIds.remove(new Integer(playerId));
		}
		
		numberOfPlayers = playerIds.size();
		TextOutput.printDebug(String.format(
				"Player %d removed, remaining players = %d, current index = %d\n", playerId, numberOfPlayers, currentPlayerIndex));
	}
	
	public List<Integer> detectiveIds()
	{
		return detectiveIds;
	}
	
	public List<Integer> mrXIds()
	{
		return mrXIds;
	}
	
	public int currentPlayer()
	{
		return players.get(playerIds.get(currentPlayerIndex)).playerId();
	}
	
	public int nextTurn()
	{
		if(currentPlayerIndex == numberOfPlayers-1)
		{
			currentPlayerIndex = 0;
		}
		else
		{
			currentPlayerIndex++;
		}
		
	
		int playerId = players.get(playerIds.get(currentPlayerIndex)).playerId();
		//TextOutput.printDebug(String.format("Current Player %d, index %d\n", playerId, currentPlayerIndex));
		return playerId;
	}
	
	public void currentPlayer(int id)
	{
		
		for(int i = 0; i < playerIds.size(); i++)
		{
			if(playerIds.get(i) == id)
			{
				currentPlayerIndex = i;
			}
		}
	}
	
	public int numberOfPlayers()
	{
		return numberOfPlayers;
	}
	
	
	boolean hasPlayer(int id)
	{
		if(players.containsKey(id))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public Player getPlayer(int id)
	{
		if(hasPlayer(id))
		{
			return players.get(id);
		}
		else
		{
			return null;
		}
	}
	
	public List<Integer> playerIds()
	{
		return playerIds;
	}
	
	
	
	
}
