package se.lth.cs.jastaddxtext.javaRAG.ast.attributes;

import org.eclipse.emf.ecore.EObject;
import javarag.TreeTraverser;

public class ASTTreeTraverser implements TreeTraverser<EObject> {
	public Iterable<? extends EObject> getChildren(EObject node) {
		return node.eContents();
	}
}