package se.lth.cs.jastaddxtext.ast.ui;

import java.util.HashMap;
import java.util.TreeSet;

import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;

import se.lth.cs.jastaddxtext.ast.ast.Child;
import se.lth.cs.jastaddxtext.ast.ast.ClassDeclaration;
import se.lth.cs.jastaddxtext.ast.ast.Component;
import se.lth.cs.jastaddxtext.ast.ast.NTA;
import se.lth.cs.jastaddxtext.ast.ast.TokenComponent;

public class AstHighlightingCalculator implements ISemanticHighlightingCalculator {

	
	@Override
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor,
			CancelIndicator cancelInd) {
		if (resource == null || resource.getParseResult() == null) {
			return;
		}
		INode root = resource.getParseResult().getRootNode();
		
		TreeSet<String> set = new TreeSet<String>();
		HashMap<String, TreeSet<String>> map = new HashMap<String, TreeSet<String>>();
		for (INode node : root.getAsTreeIterable()) {

			if (node.getText().equals("::=") || node.getText().equals("*") || node.getText().equals(":")) {
				acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.DOT_ID);
			}

			if (node.getSemanticElement() instanceof ClassDeclaration) {
				ClassDeclaration classDecl = (ClassDeclaration) node.getSemanticElement();
				if (classDecl.getSuperclass() == null) {
					for (Child c : classDecl.getChildren()) {
						set.add(NodeModelUtils.getNode(c).getText());
					}
					map.put(classDecl.getName(), set);
				}

				if (node.getText().equals(classDecl.getName())) {
					acceptor.addPosition(node.getOffset(), node.getLength(),
							AstHighlightingConfiguration.CLASSDECLARATION_ID);
				}

				if (node.getText().equals("[") || node.getText().equals("]")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.OPTIONAL_ID);
				}
				if (classDecl.getSuperclass() != null) {
					if (map.containsKey(NodeModelUtils.getTokenText(node))) {
						for (String superName : map.get(classDecl.getSuperclass().getName())) {
							if (node.getText().equals(superName)) {
								ClassDeclaration decl = (ClassDeclaration) node.getParent().getSemanticElement();
								for (Child c : decl.getSuperclass().getChildren()) {
									if (NodeModelUtils.getNode(c).getText().equals(superName)) {
										acceptor.addPosition(node.getOffset(), node.getLength(),
												AstHighlightingConfiguration.OVERRIDE_ID);
									}
								}
							}
						}

					}
				}
				// -------
			}

			if (node.getSemanticElement() instanceof TokenComponent) {
				TokenComponent token = (TokenComponent) node.getSemanticElement();
				if (node.getText().equals("<") || node.getText().equals(">")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.TOKEN_ID);
				}
				if (NodeModelUtils.getTokenText(node).equals(token.getType())) {
					acceptor.addPosition(node.getOffset(), node.getLength(),
							AstHighlightingConfiguration.ITALICNAME_ID);

				}
			}

			if (node.getSemanticElement() instanceof Component) {
				Component comp = (Component) node.getSemanticElement();

				if (comp.getName() != null) {
					if (node.getText().equals(comp.getType().getName())) {
						if (!comp.getName().equals(comp.getType().getName())) {
							acceptor.addPosition(node.getOffset(), node.getLength(),
									AstHighlightingConfiguration.ITALICNAME_ID);
						} else {
							if (!node.hasNextSibling()) {
								acceptor.addPosition(node.getOffset(), node.getLength(),
										AstHighlightingConfiguration.ITALICNAME_ID);
							}
						}

					}
				}
			}
			// -----
			if (node.getSemanticElement() instanceof NTA) {
				if (NodeModelUtils.getTokenText(node).equals("/") || NodeModelUtils.getTokenText(node).equals("/")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.NTA_ID);
				}
			}
		}
	}
}
