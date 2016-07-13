package se.lth.cs.jastaddxtext.javaRAG.ast.attributes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javarag.Circular;
import javarag.Inherited;
import javarag.Module;
import javarag.Procedural;
import javarag.Synthesized;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Child;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.ClassDeclaration;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Component;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Declaration;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Model;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.TokenComponent;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.UnknownDeclaration;
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.impl.AstFactoryImpl;

/**
 * This class contains JavaRAG functionality, for examples and information
 * about JavaRAG, see: https://bitbucket.org/javarag/javarag
 */
public class NameAnalysis<T extends NameAnalysis.Interface> extends Module<T>{
	
	private AstFactoryImpl factory = new AstFactoryImpl();

	public interface Interface {
		@Inherited ClassDeclaration parent(Child self);

		@Inherited Declaration lookup(Child self, String name);
		@Inherited boolean lookup(ClassDeclaration self, String name);
		
		@Synthesized String name(Child self);
		@Synthesized String name(ClassDeclaration self);		
		
		@Synthesized String childType(Child self);
		@Synthesized boolean hasExplicitName(Child self);
		@Synthesized boolean hasType(Child self);

		@Synthesized ClassDeclaration superclass(ClassDeclaration self);
		@Synthesized boolean hasSuperclass(ClassDeclaration self);

		@Synthesized boolean isMultipleDeclaredInClass(Child self);
		@Synthesized boolean isMultipleDeclaredFromSuperclass(Child self);
		@Synthesized boolean isMultipleDeclared(ClassDeclaration self);
		
		@Synthesized List<Child> sameTypeChildrenWithoutNames(ClassDeclaration self);
		
		@Synthesized boolean cycleInSuperclassChain(ClassDeclaration self);
		
		@Procedural void printExtensionList(ClassDeclaration self);
		@Procedural Child getOriginalChild(Child duplicateChild);
	}
	
	/**
	 * Should be called on a Child object to get the ClassDeclaration
	 * acting as that child's parent node.
	 * 
	 * @param self
	 * @return the child's parent node
	 */
	public ClassDeclaration parent(ClassDeclaration self) {
		return self;
	}
	
	private Declaration localLookup(ClassDeclaration self, String name) {
		for (Child child : self.getChildren()) {
			if (e().name(child).equals(name)) {
				return child;
			}
		}
		return factory.createUnknownDeclaration();
	}
	
	private boolean localLookup(Model self, String name) {
		int counter = 0;
		for (ClassDeclaration decl : self.getClassDeclaration()) {
			if (decl.getName().equals(name)) {
				counter++;
				if (counter > 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Tries to find a Child with the same name as the parameter
	 * "name" in self. If a Child with this name isn't
	 * found and self has a superclass, the superclass' lookup
	 * method will be called. If a matching Child is found, it is 
	 * returned as a Declaration. If a matching Child isn't found
	 * and self doesn't have a superclass an UnknownDeclaration 
	 * will be returned.
	 * 
	 * @param self
	 * @param name, the name of the sought Child
	 * @return the sought Child cast as a Declaration if found,
	 *         UnknownDeclaration otherwise
	 */
	public Declaration lookup(ClassDeclaration self, String name) {
		Declaration decl = localLookup(self, name);
		
		if (decl instanceof UnknownDeclaration
				&& hasSuperclass(self)
				&& superclass(self) != self
				&& !cycleInSuperclassChain(self)) {
			return lookup(superclass(self), name);
		} else {
			return decl;
		}
	}
	
	/**
	 * Tries to find occurrences of ClassDelcarations that has
	 * a name equal to the parameter "name".
	 * @param self
	 * @param name
	 * @return if more than one ClassDeclaration has the same name
	 */
	public boolean lookup(Model self, String name) {
		return localLookup(self, name);
	}
	
	public String name(Child self) {
		String name;
		if (self instanceof TokenComponent) {
			name = ((TokenComponent) self).getName();
		} else {
			Component tempComponent = (Component) self;
			name = tempComponent.getName();
			if (name == null && tempComponent.getType() != null) {
				name = tempComponent.getType().getName();
			}
		}
		return name;
	}
	
	public String name(ClassDeclaration self) {
		return self.getName();
	}
	
	public String childType(Child self) {
		if (self instanceof TokenComponent) {
			return ((TokenComponent) self).getType().toString();
		} else {
			return hasType(self) ? ((Component) self).getType().getName() : null;
		}
	}
	
	public boolean hasExplicitName(Child self) {
		if (self instanceof TokenComponent) {
			return true;
		} else {
			return ((Component) self).getName() != null;
		}
	}
	
	public boolean hasType(Child self) {
		if (self instanceof TokenComponent) {
			return true;
		} else {
			return ((Component) self).getType() != null;
		}
	}

	public ClassDeclaration superclass(ClassDeclaration self) {
		return self.getSuperclass();
	}
	
	public boolean hasSuperclass(ClassDeclaration self) {
		return self.getSuperclass() != null;
	}

	public boolean isMultipleDeclaredInClass(Child self) {
		
		// Lookup other child with same name
		Declaration temp = e().lookup(self, name(self));
		
		/* Check whether the found child has the same parent
		 * as the child that the lookup was made on */
		if (temp != self && temp instanceof Child) {
			ClassDeclaration currentParent = e().parent(self);
			ClassDeclaration lookupParent = e().parent((Child) temp);
			return name(currentParent).equals(name(lookupParent));
		}
		
		return temp != self;
	}
	
	public boolean isMultipleDeclaredFromSuperclass(Child self) {
		ClassDeclaration currentParent = e().parent(self);
		if (hasSuperclass(currentParent)) {
			Declaration foundDecl = lookup(superclass(currentParent), name(self));
			if (foundDecl != self && foundDecl instanceof Child) {
				ClassDeclaration lookupParent = e().parent((Child) foundDecl);
				boolean differentType = !childType(self).equals(childType((Child) foundDecl));
				boolean differentParent = !name(currentParent).equals(name(lookupParent));
				return differentParent && differentType;
			}
		}
		return false;
	}

	public boolean isMultipleDeclared(ClassDeclaration self) {
		return e().lookup(self, self.getName());
	}
	
	public List<Child> sameTypeChildrenWithoutNames(ClassDeclaration self) {
		HashMap<String, List<Child>> childrenWithoutNames = new HashMap<String, List<Child>>();
		for (Child child : self.getChildren()) {
			if (!hasExplicitName(child)) {
				String childType = childType(child);
				if (childType == null) {
					// Do nothing, so error for unknown type isn't propagated
				} else if (childrenWithoutNames.containsKey(childType)) {
					childrenWithoutNames.get(childType).add(child);
				} else {
					LinkedList<Child> childList = new LinkedList<Child>();
					childList.add(child);
					childrenWithoutNames.put(childType, childList);
				}
			}
		}
		
		LinkedList<Child> errorChildren = new LinkedList<Child>();
		for (Entry<String, List<Child>> listEntry : childrenWithoutNames.entrySet()) {
			if (listEntry.getValue().size() > 1) {
				errorChildren.addAll(listEntry.getValue());
			}
		}

		return errorChildren;
	}
	
	public boolean cycleInSuperclassChain(ClassDeclaration self) {
		boolean cycleFound = false;
		
		if (hasSuperclass(self)) {
			Set<String> extensionList = new TreeSet<String>();
			extensionList.add(self.getName());
			ClassDeclaration tempSuperclass = superclass(self);
			
			boolean keepLooking = true;
			while (keepLooking) {
				cycleFound = !extensionList.add(tempSuperclass.getName());
				if (!cycleFound && hasSuperclass(tempSuperclass)) {
					tempSuperclass = superclass(tempSuperclass);
				} else {
					keepLooking = false;
				}
			}
		}
		
		return cycleFound;
	}

	/**
	 * Slightly hacky solution to retrieve the first occurrence of a
	 * Child in case it has been declared multiple times with the same name
	 * 
	 * @param duplicateChild
	 * @return the declared first occurrence of duplicateChild 
	 */
	public Child getOriginalChild(Child duplicateChild) {
		ClassDeclaration parentOfDuplicate = e().parent(duplicateChild);
		return (Child) lookup(superclass(parentOfDuplicate), name(duplicateChild));
	}
}