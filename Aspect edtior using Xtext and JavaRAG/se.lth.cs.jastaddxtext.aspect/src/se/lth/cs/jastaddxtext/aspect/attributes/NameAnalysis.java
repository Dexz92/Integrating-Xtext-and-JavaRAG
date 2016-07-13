package se.lth.cs.jastaddxtext.aspect.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.xtext.EcoreUtil2;

import javarag.Collected;
import javarag.Module;
import javarag.Synthesized;
import javarag.coll.CollectionBuilder;
import javarag.coll.Collector;
import se.lth.cs.jastaddxtext.aspect.aspect.ClassDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.CollectionDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.Declaration;
import se.lth.cs.jastaddxtext.aspect.aspect.DeclarativeDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.Equation;
import se.lth.cs.jastaddxtext.aspect.aspect.ImperativeDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.InterTypeDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.InterfaceDeclaration;
import se.lth.cs.jastaddxtext.aspect.aspect.Model;
import se.lth.cs.jastaddxtext.aspect.aspect.SynOrInhDeclaration;

/**
 * This class contains JavaRAG functionality, for examples and information
 * about JavaRAG, see: https://bitbucket.org/javarag/javarag
 */
public class NameAnalysis<T extends NameAnalysis.Interface> extends Module<T> {

	public interface Interface {

		@Synthesized
		Map<String, Integer> attributeDeclMap(Model self);

		@Synthesized
		Map<String, Integer> equationDeclMap(Model self);

		@Synthesized
		Map<String, Integer> classAndInterfaceDeclMap(Model self);

		@Collected
		List<Declaration> attributes(Model self);

		@Collected
		List<Declaration> equations(Model self);

		@Collected
		List<Declaration> classesAndInterfaces(Model self);
	}

	public Map<String, Integer> attributeDeclMap(Model self) {
		return buildAttributeMap(e().attributes(self), new HashMap<String, Integer>());
	}

	public Map<String, Integer> equationDeclMap(Model self) {
		return buildEquationMap(e().equations(self), new HashMap<String, Integer>());
	}

	public Map<String, Integer> classAndInterfaceDeclMap(Model self) {
		return buildClassAndInterfaceMap(e().classesAndInterfaces(self), new HashMap<String, Integer>());
	}

	public CollectionBuilder<List<Declaration>, Declaration> attributes(Model self) {
		return new CollectionBuilder<List<Declaration>, Declaration>(new ArrayList<Declaration>());
	}

	public CollectionBuilder<List<Declaration>, Declaration> equations(Model self) {
		return new CollectionBuilder<List<Declaration>, Declaration>(new ArrayList<Declaration>());
	}

	public CollectionBuilder<List<Declaration>, Declaration> classesAndInterfaces(Model self) {
		return new CollectionBuilder<List<Declaration>, Declaration>(new ArrayList<Declaration>());
	}
	
	private void calculateDeclMap(Map<String, Integer> declMap, String qualifiedName) {
		int declCount = 0;
		if (declMap.containsKey(qualifiedName)) {
			declCount = declMap.get(qualifiedName);
		}
		declMap.put(qualifiedName, declCount + 1);
	}

	private Map<String, Integer> buildAttributeMap(List<Declaration> declList, Map<String, Integer> declMap) {
		String classRef = "";
		String qualifiedName = "";
		for (Declaration decl : declList) {
			if (decl instanceof DeclarativeDeclaration) {
				DeclarativeDeclaration tempDecl = (DeclarativeDeclaration) decl;
				if (tempDecl.getClassRef() != null && !tempDecl.getClassRef().equals("null")
						|| tempDecl.isOnASTNode()) {
					classRef = tempDecl.isOnASTNode() ? "ASTNode" : tempDecl.getClassRef().getName();
					qualifiedName = classRef + "." + tempDecl.getName();
				}
			} else {
				InterTypeDeclaration tempDecl = (InterTypeDeclaration) decl;
				if (tempDecl.getClassRef() != null && !tempDecl.getClassRef().equals("null")
						|| tempDecl.isOnASTNode()) {
					classRef = tempDecl.isOnASTNode() ? "ASTNode" : tempDecl.getClassRef().getName();
					qualifiedName = classRef + "." + tempDecl.getName();
				}
			}
			
			calculateDeclMap(declMap, qualifiedName);
		}
		return declMap;
	}
	
	private Map<String, Integer> buildEquationMap(List<Declaration> declList, Map<String, Integer> declMap) {
		String classRef = "";
		String qualifiedName = "";
		for (Declaration decl : declList) {
			Equation tempDecl = (Equation) decl;
			if (tempDecl.getGetName() != null) {
				// To strip the "get" part of the name. If the name doesn't contain "get
				// there is a separate check for that
				classRef = tempDecl.getGetName().length() < 4 ? 
						tempDecl.getGetName() : tempDecl.getGetName().substring(3);
				qualifiedName = classRef + "." + tempDecl.getName();
			} else {
				classRef = tempDecl.isOnASTNode() ? "ASTNode" : tempDecl.getClassRef().getName();
				qualifiedName = classRef + "." + tempDecl.getName();
			}

			calculateDeclMap(declMap, qualifiedName);
		}
		return declMap;
	}

	private Map<String, Integer> buildClassAndInterfaceMap(List<Declaration> declList, Map<String, Integer> declMap) {
		for (Declaration decl : declList) {
			ImperativeDeclaration tempDecl = (ImperativeDeclaration) decl;
			String qualifiedName = tempDecl.getName();

			calculateDeclMap(declMap, qualifiedName);
		}
		return declMap;
	}

	public void attributes(SynOrInhDeclaration self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}
	
	public void attributes(CollectionDeclaration self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}

	public void attributes(InterTypeDeclaration self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}

	public void equations(Equation self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}

	public void classesAndInterfaces(ClassDeclaration self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}

	public void classesAndInterfaces(InterfaceDeclaration self, Collector<Declaration> col) {
		Model node = (Model) EcoreUtil2.getRootContainer(self);
		col.add(node, self);
	}
}