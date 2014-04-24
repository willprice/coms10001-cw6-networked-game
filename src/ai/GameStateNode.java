package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
