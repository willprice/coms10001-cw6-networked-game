package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.AI.Move;

public class GameStateNode {
	private GameStateNode parent;
	private List<GameStateNode> children;
	private int rank;
	
	private Map<Integer, Integer> detectiveLocations;
	private Integer mrXLocation;
	
	public GameStateNode() {
		children = new ArrayList<>();
		detectiveLocations = new HashMap<>();
	}
	
	public GameStateNode(GameStateNode parentNode, Move move) {
		// TODO Auto-generated constructor stub
	}

	public void setParent(GameStateNode parent) {
		this.parent = parent;
	}
	
	public GameStateNode getParent() {
		return parent;
	}
	
	public List<GameStateNode> getChildren() {
		return children;
	}
	
	public void addChild(GameStateNode child) {
		children.add(child);
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void addDetectiveLocation(Integer detectiveId, Integer location) {
		detectiveLocations.put(detectiveId, location);
	}
	
	public Map<Integer, Integer> getDetectiveLocations() {
		return detectiveLocations;
	}
	
	public Integer getDetectiveLocation(Integer detectiveId) {
		return detectiveLocations.get(detectiveId);
	}
	
	public void setDetectiveLocations(Map<Integer, Integer> locations) {
		detectiveLocations = locations;
	}
	
	public void setDetectiveLocation(Integer detectiveId, Integer location) {
		detectiveLocations.put(detectiveId, location);
	}
	
	public void setMrXLocation(Integer location) {
		mrXLocation = location;
	}
	
	public Integer getMrXLocation() {
		return mrXLocation;
	}

	public void setDetectiveLocations(List<Move> detectiveMoves) {
		for(int i = 0; i < detectiveMoves.size(); i++) {
			// This should be i?
			// Also are these the correct id's for the players
			detectiveLocations.put(i, detectiveMoves.get(0).location);
		}
	}
	
	public String toString() {
		// TODO: Make this print stuff out for debugging
		return detectiveLocations.toString() + "\n";
	}

}
