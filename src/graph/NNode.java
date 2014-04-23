package graph;


public class NNode {
	private Node node;

	public NNode(Node node) {
		this.node = node;
	}

	public NNode(String id) {
		this.node = new Node(id);
	}

	public String id() {
		return node.name();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : id().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NNode other = (NNode) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!id().equals(other.id()))
			return false;
		return true;
	}

}
