package graph;
import java.util.ArrayList;
import java.util.List;



public class NGraph {
	protected List<NNode> nodes;
	protected List<NEdge> edges;
	
	public NGraph(Graph graph) {
		this();
		for (Node node : graph.nodes()) {
			nodes.add(new NNode(node));
		}
		
		for (Edge edge : graph.edges()) {
			edges.add(new NEdge(edge));
		}
	}
	
	public NGraph() {
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
	}

	public void add(NNode... nodes) {
		for (NNode node : nodes) {
			this.nodes.add(node);
		}
	}

	public void changeAllWeights(double weight) {
		for (NEdge edge : edges()) {
			edge.setWeight(weight);
		}
	}

	public List<NEdge> edges() {
		return edges;
	}

	public void add(NEdge edge) {
		edges.add(edge);
	}

	public static NGraph createGraph(Object[]...edgeInfoArray) {
		NGraph graph = new NGraph();
		for (Object[] edgeInfo :  edgeInfoArray) {
			if (edgeInfo[0] == null) return null;
			
			String startNodeId = (String) edgeInfo[0];
			String endNodeId = (String) edgeInfo[1];
			double weight = (double) edgeInfo[2];
			
			NNode startNode = graph.findNodeInGraph(startNodeId);
			NNode endNode = graph.findNodeInGraph(endNodeId);
			if(startNode == null)
				startNode = graph.createNode(startNodeId);
			if (endNode == null)
				endNode = graph.createNode(endNodeId);
			
			NEdge edge = graph.createEdge(startNodeId, endNodeId, weight);
			graph.add(startNode, endNode);
			graph.add(edge);
		}
		return graph;
	}
	private NNode findNodeInGraph(String nodeId) {
		for (NNode node : nodes) {
			if (nodeId.equals(node.id()))
				return node;
		}
		return null;
	}

	private NEdge createEdge(String id1, String id2, double weight) {
		Edge.EdgeType defaultEdgeType = Edge.EdgeType.Bus;
		return createEdge(id1, id2, weight, defaultEdgeType);
	}

	private NEdge createEdge(String id1, String id2, double weight,
			Edge.EdgeType edgeType) {
		return new NEdge(new Edge(id1, id2, weight, edgeType));
	}

	private NNode createNode(String id) {
		return new NNode(new Node(id));
	}

	public List<NNode> nodes() {
		return nodes;
	}

	public List<NNode> findNeighbours(NNode node) {
		List<NNode> neighbours = new ArrayList<>();
		
		for (NEdge edge : edges) {
			if (edge.connectsNode(node)) {
				neighbours.add(edge.connectedTo(node));
			}
		}
		return neighbours;
	}
	
}
