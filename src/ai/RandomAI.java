package ai;

import state.*;

import java.util.*;


import game.TextOutput;
import graph.*;

public class RandomAI extends AI {
	
	
	
	@Override
	public  Move getMove(int playerId)
	{
		TextOutput.printDebug(String.format("In AI: player id %d\n", playerId));
		
		int currentLocation = aiReadable.getNodeId(playerId);
		List<Move> possibleMoves = getPossibleMoves(currentLocation, playerId);
		
		int numberOfMoves = possibleMoves.size();
		
		// get a random move
		int choice = (int) (Math.random() * numberOfMoves);
		Move chosenMove = possibleMoves.get(choice);

		return chosenMove;
	}
	
	
}
