package se.lth.cs.jastaddxtext.aspect.attributes;

import org.eclipse.emf.ecore.EObject;
import javarag.TreeTraverser;

public class AspectTreeTraverser implements TreeTraverser<EObject> {
	public Iterable<? extends EObject> getChildren(EObject node) {
		return node.eContents();
	}
}
