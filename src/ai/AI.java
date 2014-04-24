package ai;

import java.util.ArrayList;
import java.util.List;

import graph.Edge;
import state.Initialisable;

public abstract class AI {
	AIReadable aiReadable;

	/**
	 * Small class to hold a move object. A move is simply
	 * a target location and a ticket type
	 */
	public class Move implements Comparable<Move>
	{
		public Move(Initialisable.TicketType type, int location)
		{
			this.type = type;
			this.location = location;
		}
		
		public Initialisable.TicketType type;
		public int location;
		
		public String toString()
		{
			return String.format("(%s %d)", type, location);
		}

		private AI getOuterType() {
			return AI.this;
		}

		@Override
		public int compareTo(Move o) {
			return Integer.compare(location, o.location);
		}
	}
	
	
	/**
	 * Helper function to convert from edge type to ticket type
	 * @param edgeType
	 * @return The ticket type corresponding to the edge type
	 */
	public Initialisable.TicketType convertEdgeTypeToTicketType(Edge.EdgeType edgeType)
	{
		Initialisable.TicketType ticketType = null;

		switch(edgeType)
		{
		case Underground: 
			ticketType = Initialisable.TicketType.Underground;
			break;
		case Taxi: 
			ticketType = Initialisable.TicketType.Taxi;
			break;
		case Bus: 
			ticketType = Initialisable.TicketType.Bus;
			break;
		default:
			break;
		}
		
		return ticketType;
	}
	
	/**
	 * Registration of the AIReadable object
	 * @param a
	 */
	public void registerAiReadable(AIReadable a)
	{
		this.aiReadable = a;
	}
	
	/**
	 * Abstract method for getting the AI to suggest a move
	 * @param playerId
	 * @return The move propsed by the AI
	 */
	public abstract Move getMove(int playerId);

	protected List<Move> getPossibleMoves(int currentLocation, int playerId) {
		
		List<Move> possibleMoves = new ArrayList<Move>();
		String locationName = Integer.toString(currentLocation);
		List<Edge> edges = aiReadable.getGraph().edges(locationName);
		
		for(Edge e: edges)
		{
			int connectingNode = Integer.parseInt(e.connectedTo(locationName));
			Move move = new Move(convertEdgeTypeToTicketType(e.type()), connectingNode);
			possibleMoves.add(move);
			
			if(aiReadable.getNumberOfTickets(Initialisable.TicketType.SecretMove, playerId) > 0)
			{
				Move anotherMove = new Move(Initialisable.TicketType.SecretMove, connectingNode);
				possibleMoves.add(anotherMove);
			}
		
		}
		
	
		// if player has double or secret moves...
		if(aiReadable.getNumberOfTickets(Initialisable.TicketType.DoubleMove, playerId) > 0)
		{
			possibleMoves.add(new Move(Initialisable.TicketType.DoubleMove, currentLocation));
		}
		
		
		
		return possibleMoves;
	}
}
