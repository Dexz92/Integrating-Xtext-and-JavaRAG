package se.lth.cs.jastaddxtext.javaRAG.ast.ui;

import java.util.HashMap;
import java.util.TreeSet;

import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Child;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.ClassDeclaration;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Component;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.TokenComponent;

@SuppressWarnings("deprecation")
/**
 * This class calculates appropriate highlighting for a specific node element
 */
public class AstHighlightingCalculator implements ISemanticHighlightingCalculator {

	private TreeSet<String> set = new TreeSet<String>();
	private HashMap<String, TreeSet<String>> map = new HashMap<String, TreeSet<String>>();

	/**
	 * Provides highlighting for a node element
	 */
	@Override
	public void provideHighlightingFor(XtextResource resource,
			org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor acceptor) {
		if (resource == null || resource.getParseResult() == null) {
			return;
		}
		INode root = resource.getParseResult().getRootNode();
		for (INode node : root.getAsTreeIterable()) {

			optionalColor(node, acceptor);
			dotColor(node, acceptor);
			ntaColor(node, acceptor);

			if (node.getSemanticElement() instanceof ClassDeclaration) {
				ClassDeclaration classDecl = (ClassDeclaration) node.getSemanticElement();
				classDeclColor(classDecl, node, acceptor);
			}

			if (node.getSemanticElement() instanceof TokenComponent) {
				TokenComponent token = (TokenComponent) node.getSemanticElement();
				tokenCompColor(token, node, acceptor);
			}

			if (node.getSemanticElement() instanceof Component) {
				Component comp = (Component) node.getSemanticElement();
				componentColor(comp, node, acceptor);
			}

		}
	}

	/**
	 * Syntax highlighting for optional brackets
	 * 
	 * @param node - INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void optionalColor(INode node, IHighlightedPositionAcceptor acceptor) {
		if (node.getText().equals("[") || node.getText().equals("]")) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.OPTIONAL_ID);
		}
	}

	/**
	 * Syntax highlighting for assignment,list and extend symbols
	 * 
	 * @param node -INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void dotColor(INode node, IHighlightedPositionAcceptor acceptor) {
		if (node.getText().equals("::=") || node.getText().equals("*") || node.getText().equals(":")) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.DOT_ID);
		}
	}

	/**
	 * Syntax highlighting for NTA
	 * 
	 * @param node -INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void ntaColor(INode node, IHighlightedPositionAcceptor acceptor) {
		if (node.getText().equals("/") || node.getText().equals("/")) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.NTA_ID);
		}
	}

	/**
	 * Syntax highlighting for ClassDeclaration
	 * 
	 * @param classDecl - ClassDeclaration
	 * @param node -INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void classDeclColor(ClassDeclaration classDecl, INode node, IHighlightedPositionAcceptor acceptor) {
		if (node.getText().equals(classDecl.getName())) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.CLASSDECLARATION_ID);
		}
		if (classDecl.getSuperclass() == null) {
			for (Child c : classDecl.getChildren()) {
				set.add(NodeModelUtils.getNode(c).getText());
			}
		}
		map.put(classDecl.getName(), set);

		if (classDecl.getSuperclass() != null) {
			if (map.containsKey(NodeModelUtils.getTokenText(node))) {
				for (String superName : map.get(NodeModelUtils.getTokenText(node))) {
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
	}

	/**
	 * Syntax highlighting for TokenComponent
	 * 
	 * @param token - TokenComponent
	 * @param node -INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void tokenCompColor(TokenComponent token, INode node, IHighlightedPositionAcceptor acceptor) {
		if (node.getText().equals("<") || node.getText().equals(">")) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.TOKEN_ID);
		}
		if (NodeModelUtils.getTokenText(node).equals(token.getType())) {
			acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.TYPE_ID);

		}
	}

	/**
	 * Syntax highlighting for Component
	 * 
	 * @param comp - Component
	 * @param node - INode
	 * @param acceptor - IHighlightedPositionAcceptor
	 */
	public void componentColor(Component comp, INode node, IHighlightedPositionAcceptor acceptor) {
		if (comp.getName() != null) {
			if (node.getText().equals(comp.getType().getName())) {
				if (!comp.getName().equals(comp.getType().getName())) {
					acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.TYPE_ID);
				} else {
					if (!node.hasNextSibling()) {
						acceptor.addPosition(node.getOffset(), node.getLength(), AstHighlightingConfiguration.TYPE_ID);
					}
				}

			}
		}
	}

}
