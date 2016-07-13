/*
 */
package se.lth.cs.jastaddxtext.javaRAG.ast.validation

import java.util.List
import java.util.TreeSet
import javarag.impl.reg.BasicAttributeRegister
import javax.inject.Inject
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.resource.IContainer
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.eclipse.xtext.validation.Check
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.AstPackage
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.Child
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.ClassDeclaration
import se.lth.cs.jastaddxtext.javaRAG.ast.ast.TokenComponent
import se.lth.cs.jastaddxtext.javaRAG.ast.attributes.ASTTreeTraverser
import se.lth.cs.jastaddxtext.javaRAG.ast.attributes.NameAnalysis

/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class AstValidator extends AbstractAstValidator {
	public static val CLASS_NAME_CAPITAL_FIRST_LETTER = "se.lth.cs.jastaddxtext.ast.validation.CapitalFirstLetter"
	public static val CLASS_NAME_CAPITAL_FIRST_LETTER_TEXT = "Class names should start with a capital letter"
	
	public static val MULTIPLE_CLASSES_WITH_SAME_NAME = "se.lth.cs.jastaddxtext.ast.validation.DuplicateClassNames"
	public static val MULTIPLE_CLASSES_WITH_SAME_NAME_TEXT = "Duplicate class names"
	
	public static val MULTIPLE_CHILDREN_WITH_SAME_NAME = "se.lth.cs.jastaddxtext.ast.validation.DuplicateChildNames"
	public static val MULTIPLE_CHILDREN_WITH_SAME_NAME_IN_CLASS_TEXT = "Duplicate child name"

	public static val MULTIPLE_CHILDREN_WITH_SAME_TYPE = "se.lth.cs.jastaddxtext.ast.validation.ExplicitChildNamesOnSameType"
	public static val MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT = "Multiple children with the same type must have explicit names"

	public static val CLASS_CANNOT_EXTEND_SELF = "se.lth.cs.jastaddxtext.ast.validation.ClassExtendingSelf"
	
	public static val CHILD_SAME_NAME_AS_PARENT = "se.lth.cs.jastaddxtext.ast.validation.ChildSameNameAsParent"
	public static val CHILD_SAME_NAME_AS_PARENT_TEXT = "Child has same name as its parent class"
	
	public static val CHILD_SAME_TYPE_AS_PARENT = "se.lth.cs.jastaddxtext.ast.validation.ChildSameTypeAsParent"
	public static val CHILD_SAME_TYPE_AS_PARENT_TEXT = "A child node should not have the same name as its parent class"
	
	public static val CHILD_NAME_SAME_AS_TYPE = "se.lth.cs.jastaddxtext.ast.validation.ChildNameSameAsType"
	public static val CHILD_NAME_SAME_AS_TYPE_TEXT = "A childnode's name should not be the same as its type"
	
	public static val CIRCULAR_INHERITANCE = "se.lth.cs.jastaddxtext.ast.validation.CircularInheritance"
	
	public static val COMMON_KEYWORD_AS_NAME = "se.lth.cs.jastaddxtext.ast.validation.KeywordAsName"
	
	@Inject ResourceDescriptionsProvider rdp
	@Inject IContainer.Manager cm
	
	private def getVisibleEObjectDescriptions(EObject o, EClass type) {
		o.getVisibleContainers.map[
			container | container.getExportedObjectsByType(type)
		].flatten
	}
	
	private def getVisibleContainers(EObject o) {
		val index = rdp.getResourceDescriptions(o.eResource)
		val rd = index.getResourceDescription(o.eResource.URI)
		cm.getVisibleContainers(rd, index)
	}
	
	/**
	 * Looks for multiple declarations of classes. Currently it uses a relatively
	 * expensive method to do this, but it makes it possible to check in several
	 * files at the same time. To have the same functionality but only for within
	 * a single file, the "lookup" method from JavaRAG can be used instead.
	 */
	@Check
	def checkClassNameIsUnique(ClassDeclaration classDecl) {
		val descriptionItr = getVisibleEObjectDescriptions(
			EcoreUtil2.getRootContainer(classDecl), AstPackage.Literals::CLASS_DECLARATION
		).iterator
		var counter = 0
		var continue = true
		var IEObjectDescription description = null
		while (continue && descriptionItr.hasNext) {
			description = descriptionItr.next
			if ((description.name.toString).equals(classDecl.name)) {
				counter++
			}
			
			if (counter > 1) {
				error(
				MULTIPLE_CLASSES_WITH_SAME_NAME_TEXT,
				AstPackage.Literals::CLASS_DECLARATION__NAME,
				MULTIPLE_CLASSES_WITH_SAME_NAME
				)
				continue = false
			}
		}
	}	
	
	private def getEvaluator(EObject object) {		
		val model = EcoreUtil2.getRootContainer(object)
		val registry = new BasicAttributeRegister
		registry.register(NameAnalysis)
		return registry.getEvaluator(model, new ASTTreeTraverser())
	}
	
	private def componentNameHighlight(Child component) {
		if (component instanceof TokenComponent) {
			return AstPackage.Literals::TOKEN_COMPONENT__NAME
		} else {
			return AstPackage.Literals::COMPONENT__NAME
		}
	}
	
	private def componentTypeHighlight(Child component) {
		if (component instanceof TokenComponent) {
			return AstPackage.Literals::TOKEN_COMPONENT__TYPE
		} else {
			return AstPackage.Literals::COMPONENT__TYPE
		}
	}
	
	private def keywordAsName(String name, EAttribute componentHighlight) {
		if (name.equals("String")) {
			warning(
				"It is unadvised to use 'String' as a name due to it being a common keyword",
				componentHighlight,
				COMMON_KEYWORD_AS_NAME
			)
		} else if (name.equals("Integer")) {
			warning(
				"It is unadvised to use 'Integer' as a name due to it being a common keyword",
				componentHighlight,
				COMMON_KEYWORD_AS_NAME
			)
		}
	}
	
	/**
	 * Checks that ClassDeclarations start with a uppercase letter
	 * 
	 * @Warning if a ClassDeclaration doesn't start with a uppercase letter
	 */
	@Check
	def checkClassStartsWithCapital(ClassDeclaration classDecl){
		if(!Character.isUpperCase(classDecl.name.charAt(0))) {
			warning(
				CLASS_NAME_CAPITAL_FIRST_LETTER_TEXT,
				AstPackage.Literals.CLASS_DECLARATION__NAME,
				CLASS_NAME_CAPITAL_FIRST_LETTER
			)
		}
	}

	/**
	 * Checks that child nodes aren't declared with the same name
	 * multiple times. The check extends to any superclass the parent
	 * to the child may have.
	 * 
	 * @Error if any multiple declarations are found
	 */
	@Check
	def checkValidChildNames(Child child) {
		val evaluator = getEvaluator(child)
		if (evaluator.evaluate("isMultipleDeclaredInClass", child)) {
			error(
				MULTIPLE_CHILDREN_WITH_SAME_NAME_IN_CLASS_TEXT,
				child, componentNameHighlight(child),
				MULTIPLE_CHILDREN_WITH_SAME_NAME
			)
		}
		
		if (evaluator.evaluate("isMultipleDeclaredFromSuperclass", child)) {
			val Child originalChild = evaluator.evaluate("getOriginalChild", child)
			val ClassDeclaration originalParent = evaluator.evaluate("parent", originalChild)
			val String originalParentName = evaluator.evaluate("name", originalParent)
			error(
				"Duplicate name from inherited child (in class: " + originalParentName + ") of different type",
				child, componentNameHighlight(child),
				MULTIPLE_CHILDREN_WITH_SAME_NAME
			)
		}
	}
	
	/**
	 * Makes sure that child nodes have explicit names in case there is more
	 * than one child of a certain type
	 * 
	 * @Error if more than one child has the same type, and more than one of
	 *        these child nodes lack an explicit name
	 */
	@Check
	def checkExplicitChildNamesWhenMultipleOfSameType(ClassDeclaration classDecl) {
		val evaluator = getEvaluator(classDecl)
		val List<Child> list = evaluator.evaluate("sameTypeChildrenWithoutNames", classDecl)
		for (child : list) {
			error(
				MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT,
				child, componentNameHighlight(child),
				MULTIPLE_CHILDREN_WITH_SAME_TYPE
			)
		}
	}
	
	/**
	 * Checks that a class does not specify itself as its superclass
	 * 
	 * @Error if classDecl has itself as a superclass
	 */
	@Check
	def checkClassNotExtendingSelf(ClassDeclaration classDecl) {
		val evaluator = getEvaluator(classDecl)
		if (evaluator.evaluate("hasSuperclass", classDecl)) {
			val className = evaluator.evaluate("name", classDecl)
			val superclass = evaluator.evaluate("superclass", classDecl)
			val superclassName = evaluator.evaluate("name", superclass)
			
			if (className.equals(superclassName)) {
				error(
					className + " cannot have itself as superclass",
					AstPackage.Literals::CLASS_DECLARATION__SUPERCLASS,
					CLASS_CANNOT_EXTEND_SELF
				)
			}
		}
	}
	
	/**
	 * Checks for if a child has the same name as its parent class
	 * 
	 * @Warning if child has the same name as its parent class
	 */
	@Check
	def childNameNotSameAsParentName(Child child) {
		val evaluator = getEvaluator(child)
		val childName = evaluator.evaluate("name", child)
		val parent = evaluator.evaluate("parent", child)
		val parentName = evaluator.evaluate("name", parent)
		
		if (childName.equals(parentName)) {
			warning(
				CHILD_SAME_NAME_AS_PARENT_TEXT,
				componentNameHighlight(child),
				CHILD_SAME_NAME_AS_PARENT
			)
		}
	}

	/**
	 * Checks for if a child has the same type as its parent class
	 * 
	 * @Error if child has the same type as its parent class
	 */
	@Check
	def checkChildNotOfParentType(Child child) {
		val evaluator = getEvaluator(child)
		if (evaluator.evaluate("hasType", child)) {
			val childType = evaluator.evaluate("childType", child)
			// A ClassDeclaration's name is the same as its type
			val parent = evaluator.evaluate("parent", child)
			val parentType = evaluator.evaluate("name", parent)
			if (childType.equals(parentType)) {
				error(
					CHILD_SAME_TYPE_AS_PARENT_TEXT,
					child, componentTypeHighlight(child),
					CHILD_SAME_TYPE_AS_PARENT
				)
			}
		}
	}
	
	/**
	 * Checks for if a child has the same name as its type
	 * 
	 * @Warning if child has the same name as its type
	 */
	@Check
	def checkChildNameNotSameAsType(Child child) {
		val evaluator = getEvaluator(child)
		val boolean hasExplicitName = evaluator.evaluate("hasExplicitName", child)
		val name = evaluator.evaluate("name", child)
		val type = evaluator.evaluate("childType", child)
		if (hasExplicitName && name.equals(type)) {
			warning(
				CHILD_NAME_SAME_AS_TYPE_TEXT,
				componentNameHighlight(child),
				CHILD_NAME_SAME_AS_TYPE
			)
		}
	}
	
	/**
	 * Checks for circular inheritance on a ClassDeclaration
	 * 
	 * @Error if classDecl depends on itself
	 */
	@Check
	def checkForCircularInheritance(ClassDeclaration classDecl) {
		val evaluator = getEvaluator(classDecl)
		if (evaluator.evaluate("cycleInSuperclassChain", classDecl)) {
			error(
				"The superclass chain for " + classDecl.name + " contains a cycle",
				AstPackage.Literals::CLASS_DECLARATION__SUPERCLASS,
				CIRCULAR_INHERITANCE
			)
		}
	}
	
	/**
	 * Checks if a Child has a reserved keyword as its name
	 * 
	 * @Warning if the name of child is a keyword
	 */
	@Check
	def checkForKeywordAsName(Child child) {
		val evaluator = getEvaluator(child)
		val name = evaluator.evaluate("name", child)
		keywordAsName(name, componentNameHighlight(child))
	}

	/**
	 * Checks if a Child has a reserved keyword as its name
	 * 
	 * @Warning if the name of child is a keyword
	 */
	@Check
	def checkForKeywordAsName(ClassDeclaration classDecl) {
		val evaluator = getEvaluator(classDecl)
		val name = evaluator.evaluate("name", classDecl)
		keywordAsName(name, AstPackage.Literals::CLASS_DECLARATION__NAME)
	}
}