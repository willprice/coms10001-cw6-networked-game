package state;
import java.util.*;

import state.Initialisable.TicketType;

/**
 * Class representing a player
 */
public class Player {
	
	enum PlayerType { Detective, MrX };
	
	private static final Boolean[] mrXAppearanceList = {
		false, false, true, 
		false, false, false, false, true,
		false, false, false, false, true,
		false, false, false, false, true,
		false, false, false, false, false, true
	};
	
	
	private int playerId;
	private PlayerType playerType;
	private HashMap<Initialisable.TicketType, Integer> playerTickets;
	private List<Integer> locationHistory = new ArrayList<Integer>();
	private List<Initialisable.TicketType> ticketHistory = new ArrayList<Initialisable.TicketType>();
	private List<Boolean> visualisableHistory = new ArrayList<Boolean>();
	private int currentLocation;
	private int turnNumber = 0;

	
	/**
	 * Player constructor
	 * @param id Id of the player
	 * @param type Type of the player
	 * @param location Location of the player
	 */
	public Player(int id, PlayerType type, int location, HashMap<Initialisable.TicketType, Integer> tickets)
	{
		playerId = id;
		playerType = type;
		currentLocation = location;
		
		// initialise the tickets
		playerTickets = tickets;	
		
		if(type == PlayerType.MrX)
		{
			visualisableHistory.add(false);
		}
		else
		{
			visualisableHistory.add(true);
		}
		
		locationHistory.add(location);
	}
	
	
	public void addMove(Initialisable.TicketType type, int location)
	{
		locationHistory.add(location);
		if(this.type() == PlayerType.MrX)
		{
			visualisableHistory.add(mrXAppearanceList[turnNumber]);
		}
		else
		{
			visualisableHistory.add(true);
		}
		ticketHistory.add(type);
	}
	
	public void useTicket(Initialisable.TicketType type)
	{
		if(playerTickets.get(type) > 0)
		{
			playerTickets.put(type, playerTickets.get(type)-1);
		}
	}
	
	public void addTicket(Initialisable.TicketType type)
	{
		playerTickets.put(type, playerTickets.get(type)+1);
	}
	
	public int getTicketNumber(TicketType type)
	{
		if(playerTickets.containsKey(type))
		{
			return playerTickets.get(type);
		}
		
		return 0;
	}
	
	public PlayerType type()
	{
		return playerType;
	}
	
	public int playerId()
	{
		return playerId;
	}
	
	/**
	 * Function to get the current location of a player
	 * @return The players current location
	 */
	int getCurrentLocation()
	{
		return currentLocation;
	}
	
	
	boolean isAtLocation(int loc)
	{
		if(this.currentLocation == loc) return true;
		return false;
	}
	
	boolean hasTickets(Initialisable.TicketType type)
	{
		if(this.playerTickets.get(type) > 0) return true;
		return false;
	}
	
	public int getNumberOfMoves()
	{
		return locationHistory.size();
	}
	
	/**
	 * Debug function to check player initialisation
	 */
	
	
	
	public void moveTo(int target, TicketType type)
	{
		addMove(type, target);
		currentLocation = target;
		turnNumber++;
	}
	
	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", playerType=" + playerType
				+ ", playerTickets=" + playerTickets + ", moveHistory="
				+ locationHistory + ", ticketHistory=" + ticketHistory
				+ ", visualisableHistory=" + visualisableHistory
				+ ", currentLocation=" + currentLocation + ", turnNumber="
				+ turnNumber + "]";
	}


	public void moveToSecret(int target)
	{
		locationHistory.add(target);
		if(this.type() == PlayerType.MrX)
		{
			visualisableHistory.add(mrXAppearanceList[turnNumber]);
		}
		else
		{
			visualisableHistory.add(true);
		}
		ticketHistory.add(TicketType.SecretMove);
		
		currentLocation = target;
		turnNumber++;
	}
	
	
	public List<Initialisable.TicketType> ticketHistory()
	{
		return ticketHistory;
	}
	
	public Boolean isVisible()
	{
		return visualisableHistory.get(turnNumber);
	}
	
	
	public List<Integer> getPositionList()
	{
		return locationHistory;
	}
	
	
}
