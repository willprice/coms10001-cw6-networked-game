package ai;
import graph.NEdge;
import graph.NGraph;
import graph.NNode;

import java.awt.print.Printable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShortestPath {
	private NGraph graph;
	private Map<NNode, Double> currentBestEstimates;
	private NNode startNode;
	private NNode endNode;
	public List<NNode> unvisitedNodes;
	private Map<NNode, Double> tentativeDistancesToNodes;
	private Map<NNode, NNode> previous;

	public ShortestPath(NGraph graph) {
		this.graph = graph;
		currentBestEstimates = new HashMap<>();
		unvisitedNodes = new ArrayList<>(graph.nodes());
		previous = new HashMap<>();
	}

	public ShortestPath(NGraph graph, NNode startNode, NNode endNode) {
		this(graph);
		this.startNode = startNode;
		this.endNode = endNode;
		assignInitialDistances();
	}

	public double calculateShortestPath() {
		while (!(unvisitedNodes.isEmpty())) {
			NNode minimalBestEstimateNode = getUnvisitedNodeWithMinimalCurrentBestEstimate();
			unvisitedNodes.remove(minimalBestEstimateNode);
			updateCurrentBestEstimates(minimalBestEstimateNode);
		}
		return getCurrentBestEstimates().get(endNode);
	}

	public void assignInitialDistances() {
		for (NNode node : graph.nodes())
			currentBestEstimates.put(node, Double.POSITIVE_INFINITY);
		currentBestEstimates.put(startNode, 0.0);
	}

	public Map<NNode, Double> getCurrentBestEstimates() {
		return currentBestEstimates;
	}

	public Map<NNode, Double> calculateTentativeDistancesToNodes(NNode currentNode) {
		tentativeDistancesToNodes = new HashMap<>();
		for (NEdge edge : graph.edges()) {
			NNode oppositeNode = edge.connectedTo(currentNode);
			if (oppositeNode != null)
				tentativeDistancesToNodes.put(oppositeNode, edge.weight());
			else {
				updateCurrentBestDistanceForNodeNotConnectedToCurrentNode(new NNode(edge.id1()));
				updateCurrentBestDistanceForNodeNotConnectedToCurrentNode(new NNode(edge.id2()));
			}
		}
		tentativeDistancesToNodes.put(currentNode, 0.0);
		return tentativeDistancesToNodes;
	}
	
	public Map<NNode, Double> getTentativeDistancesToNodes() {
		return tentativeDistancesToNodes;
	}

	private void updateCurrentBestDistanceForNodeNotConnectedToCurrentNode(NNode node) {
		Double currentBestDistance = tentativeDistancesToNodes.get(node);
		if (currentBestDistance == null || currentBestDistance == Double.POSITIVE_INFINITY)
			tentativeDistancesToNodes.put(node, Double.POSITIVE_INFINITY);
	}

	public void updateCurrentBestEstimates(NNode currentNode) {
		Map<NNode, Double> tentativeDistances = calculateTentativeDistancesToNodes(currentNode);
		Double bestEstimateForCurrentNode = currentBestEstimates.get(currentNode);
		for (NNode node : unvisitedNodes) {
			Double updatedBestEstimate = Math.min(currentBestEstimates.get(node), bestEstimateForCurrentNode + tentativeDistances.get(node));
			if (bestEstimateForCurrentNode + tentativeDistances.get(node) < currentBestEstimates.get(node)) {
				previous.put(node, currentNode);
			}
			currentBestEstimates.put(node, updatedBestEstimate);
		}
	}
	
	private NNode getUnvisitedNodeWithMinimalCurrentBestEstimate() {
		NNode currentNodeWithMinimalBestEstimate = unvisitedNodes.get(0);
		for (NNode node : unvisitedNodes) {
				if (currentBestEstimates.get(node) < currentBestEstimates.get(currentNodeWithMinimalBestEstimate)) {
					currentNodeWithMinimalBestEstimate = node;
				}
		}
		return currentNodeWithMinimalBestEstimate;
	}
		
}
