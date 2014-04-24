package ai;

import java.util.ArrayList;
import java.util.List;

import graph.Node;

public class ShortestPathAI extends AI {
	private int maxDepth = 4;
	
	@Override
	public Move getMove(int playerId) {
		GameStateNode rootNode = new GameStateNode();
		setDetectiveAndMrXLocationsForNode(rootNode);
		createGameTree(rootNode, maxDepth);

		return null;
	}

	private GameStateNode createGameTree(GameStateNode rootNode, int depth) {
		if (depth > maxDepth) {
			return null;
		}

        if (((maxDepth - depth) % 2) == 0) {
        	int mrXLocation = rootNode.getMrXLocation();
        	int mrXId = aiReadable.getMrXIdList().get(0);
        	List<Move> possibleMoves = getPossibleMoves(mrXLocation, mrXId);

        	for (Move move : possibleMoves) {
        		GameStateNode node = new GameStateNode();
        		node.setDetectiveLocations(rootNode.getDetectiveLocations());
        		node.setMrXLocation(move.location);
        		
        		rootNode.addChild(createGameTree(node, depth-1));
        	}
        } else {
        	List<Integer> detectiveIds = aiReadable.getDetectiveIdList();
        	List<List<Move>> detectiveMovesLists = new ArrayList<>();
        	
        	for (Integer id : detectiveIds) {
        		int detectiveLocation = rootNode.getDetectiveLocation(id);
        		List<Move> possibleMoves = getPossibleMoves(detectiveLocation, id);
        		
        		detectiveMovesLists.add(possibleMoves);
        	}
        }
		return rootNode;
	}

	private void setDetectiveAndMrXLocationsForNode(GameStateNode node) {
		setMrXLocation(node);
		setDetectiveLocations(node);
	}
	
	private void setMrXLocation(GameStateNode node) {
		int mrXId = aiReadable.getMrXIdList().get(0);
		int mrXLocation = aiReadable.getNodeId(mrXId);
		node.setMrXLocation(mrXLocation);
	}

	private void setDetectiveLocations(GameStateNode node) {
		List<Integer> detectiveIdList = aiReadable.getDetectiveIdList();
		for (Integer id : detectiveIdList) {
			int detectiveLocation = aiReadable.getNodeId(id);
			node.addDetectiveLocation(id, detectiveLocation);
		} 
	}
	
}