package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import state.Initialisable.TicketType;
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

	private GameStateNode createGameTree(GameStateNode parentNode, int depth) {
		if (depth > maxDepth) {
			return null;
		}

        if (((maxDepth - depth) % 2) == 0) {
        	int mrXLocation = parentNode.getMrXLocation();
        	int mrXId = aiReadable.getMrXIdList().get(0);
        	List<Move> possibleMoves = getPossibleMoves(mrXLocation, mrXId);

        	for (Move move : possibleMoves) {
        		GameStateNode node = new GameStateNode();
        		node.setDetectiveLocations(parentNode.getDetectiveLocations());
        		node.setMrXLocation(move.location);
        		
        		parentNode.addChild(createGameTree(node, depth-1));
        	}
        } else {
        	List<Integer> detectiveIds = aiReadable.getDetectiveIdList();
        	List<Set<Move>> detectivesMoves = new ArrayList<>();
        	
        	for (Integer id : detectiveIds) {
        		int detectiveLocation = parentNode.getDetectiveLocation(id);
        		List<Move> possibleMoves = getPossibleMoves(detectiveLocation, id);
        		detectivesMoves.add(new TreeSet<>(possibleMoves));
        	}
        	
        	Set<List<Move>> finalPossibleMoves = Sets.cartesianProduct(detectivesMoves);
        	for (List<Move> move : finalPossibleMoves) {
        		GameStateNode node = new GameStateNode();
        		node.setMrXLocation(parentNode.getMrXLocation());
        		node.setDetectiveLocations(move);
        		parentNode.addChild(createGameTree(node, depth-1));
        	}
        }
		return parentNode;
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
	
	public List<List<List<Integer>>> cartesianProduct(List<List<Integer>> xs, 
			List<List<Integer>> ys) {
		List<List<List<Integer>>> product = new ArrayList<>();
		for (List<Integer> x : xs) {
			for (List<Integer> y : ys) {
				List<List<Integer>> moveCombination = new ArrayList<>();
				moveCombination.add(x);
				moveCombination.add(y);
				product.add(moveCombination);
			}

		}
		return product;
	}
	
	public static void main(String[] args) {
		ShortestPathAI ai = new ShortestPathAI();
		
		List<Pair<Integer, List<Integer>>> egData = new ArrayList<>();
		egData.add(new Pair(1, Arrays.asList(2, 3)));
		egData.add(new Pair(2, Arrays.asList(4, 7)));
		egData.add(new Pair(3, Arrays.asList(8, 9)));
	
		List<List<List<Integer>>> moves = new ArrayList<>();
		for (Pair<Integer, List<Integer>> pair : egData) {
			List<List<Integer>> combination = new ArrayList<>();
			for (Integer move : pair.y){ 
				combination.add(Arrays.asList(pair.x, move));
			}
			moves.add(combination);
		}
		
		List<List<List<Integer>>> p1 = ai.cartesianProduct(moves.get(0), moves.get(1));
		System.out.println(moves);
		System.out.print(p1);
		for (int i=2; i < moves.size(); ++i) {

			//p1 = ai.cartesianProduct(p1, moves.get(i));
		}
	}
	
}