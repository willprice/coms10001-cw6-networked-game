package graph;


public class NEdge {

	private Edge edge;
	
	public NEdge(Edge edge) {
		this.edge = edge;
	}

	public void setWeight(double weight) {
		edge.weight = weight;
	}

	public String id1() {
		return edge.id1();
	}
	
	public String id2() {
		return edge.id2();
	}
	public double weight() {
		return edge.weight();
	}
	
	public NNode connectedTo(NNode node) {
		String connectedNodeId = edge.connectedTo(node.id());
		if (connectedNodeId != null)
			return new NNode(connectedNodeId);
		else
			return null;
	}
	
	public boolean connectsNode(NNode node) {
		return edge.connectsNode(node.id());
	}
}
