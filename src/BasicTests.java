import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class BasicTests {
	private static final String A = "A";
	private static final String B = "B";
	private static final String C = "C";
	private static final String D = "D";
	private static final String E = "E";
	private static final String F = "F";
	private static final String G = "G";
	private static final String H = "H";

	@Test
	public void singleDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(A, asSet(B));
		
		assert_equal(asSet(B), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(this.<String>asSet(), dependencies.getTransitiveDependenciesFor(B));
	}

	@Test
	public void multipleDependenciesAddedSeparately() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(A, asSet(B));
		dependencies.add_direct(A, asSet(C));
		
		assert_equal(asSet(B, C), dependencies.getTransitiveDependenciesFor(A));
	}

	@Test
	public void singleTransitiveDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(B, asSet(C));
		dependencies.add_direct(A, asSet(B));
		
		assert_equal(asSet(B, C), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(asSet(C), dependencies.getTransitiveDependenciesFor(B));
		assert_equal(this.<String>asSet(), dependencies.getTransitiveDependenciesFor(C));
	}
	
	@Test
	public void looseCircularDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(A, asSet(B));
		dependencies.add_direct(B, asSet(C));
		dependencies.add_direct(C, asSet(A));
		
		assert_equal(asSet(A, B, C), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(asSet(A, B, C), dependencies.getTransitiveDependenciesFor(B));
		assert_equal(asSet(A, B, C), dependencies.getTransitiveDependenciesFor(C));
	}
	
	@Test
	public void looseCircularDependencyWithOtherStuff() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(A, asSet(B));
		dependencies.add_direct(B, asSet(C));
		dependencies.add_direct(C, asSet(A));
		dependencies.add_direct(C, asSet(D));
		
		assert_equal(asSet(A, B, C, D), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(asSet(A, B, C, D), dependencies.getTransitiveDependenciesFor(B));
		assert_equal(asSet(A, B, C, D), dependencies.getTransitiveDependenciesFor(C));
	}
	
	@Test
	public void singleCircularDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(B, asSet(A));
		dependencies.add_direct(A, asSet(B));
		
		assert_equal(asSet(B, A), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(asSet(A, B), dependencies.getTransitiveDependenciesFor(B));
	}
	
	@Test
	public void singleTightCircularDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		dependencies.add_direct(A, asSet(A));
		
		assert_equal(asSet(A), dependencies.getTransitiveDependenciesFor(A));
	}

	@Test
	public void askingForUnknownDependency() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();
		assert_equal(this.<String>asSet(), dependencies.getTransitiveDependenciesFor(A));
	}

	@Test
	public void works() throws Exception {
		final Dependencies<String> dependencies = Dependencies.create();

		dependencies.add_direct(A, asSet(B, C));
		dependencies.add_direct(B, asSet(C, E));
		dependencies.add_direct(C, asSet(G));
		dependencies.add_direct(D, asSet(A, F));
		dependencies.add_direct(E, asSet(F));
		dependencies.add_direct(F, asSet(H));

		assert_equal(asSet(B, C, E, F, G, H), dependencies.getTransitiveDependenciesFor(A));
		assert_equal(asSet(C, E, F, G, H), dependencies.getTransitiveDependenciesFor(B));
		assert_equal(asSet(G), dependencies.getTransitiveDependenciesFor(C));
		assert_equal(asSet(A, B, C, E, F, G, H), dependencies.getTransitiveDependenciesFor(D));
		assert_equal(asSet(F, H), dependencies.getTransitiveDependenciesFor(E));
		assert_equal(asSet(H), dependencies.getTransitiveDependenciesFor(F));
	}

	private <NodeType> Set<NodeType> asSet(NodeType... objects) {
		return new HashSet<NodeType>(Arrays.asList(objects));
	}

	private <NodeType> void assert_equal(Set<NodeType> expected, Set<NodeType> actual) {
		assertEquals(expected.size(), actual.size());
		assertTrue(actual.containsAll(expected));
		assertTrue(expected.containsAll(actual));
	}
}
