import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dependencies<NodeType> {
	private final HashMap<NodeType, Set<NodeType>> dependencyMap = new HashMap<NodeType, Set<NodeType>>();

	public void add_direct(NodeType o, Set<NodeType> dependencies) {
		for (NodeType dependency : dependencies)
			add(o, dependency);
	}

	private void add(NodeType o, NodeType dependency) {
		getDirectDependencies(o).add(dependency);
	}

	private Set<NodeType> getDirectDependencies(NodeType o) {
		Set<NodeType> set = dependencyMap.get(o);
		if (set == null) {
			set = new HashSet<NodeType>();
			dependencyMap.put(o, set);
		}
		return set;
	}

	private void accumulateDependencies(Set<NodeType> accumulator, NodeType dependent) {
		if (accumulator.contains(dependent))
			return;

		accumulator.add(dependent);

		accumulateDirectDependencies(accumulator, dependent);
	}

	public Set<NodeType> getTransitiveDependenciesFor(NodeType o) {
		final Set<NodeType> dependencies = new HashSet<NodeType>();
		accumulateDirectDependencies(dependencies, o);
		return dependencies;
	}

	private void accumulateDirectDependencies(final Set<NodeType> dependencies, NodeType o) {
		for (NodeType directDependency : getDirectDependencies(o)) {
			accumulateDependencies(dependencies, directDependency);
		}
	}
	
	public static <NodeType> Dependencies<NodeType> create() {
		return new Dependencies<NodeType>();
	}
}
