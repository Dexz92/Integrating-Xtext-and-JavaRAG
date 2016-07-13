/*
 * generated by Xtext 2.9.0
 */
package se.lth.cs.jastaddxtext.ast.tests

import com.google.inject.Inject
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith
import se.lth.cs.jastaddxtext.ast.ast.AstPackage
import se.lth.cs.jastaddxtext.ast.ast.Model
import se.lth.cs.jastaddxtext.ast.validation.AstValidator

@RunWith(XtextRunner)
@InjectWith(AstInjectorProvider)
class AstParsingTest{

	@Inject	ParseHelper<Model> parser
	
	@Inject	extension ValidationTestHelper

	@Test 
	def void baseline() {
		val model = parser.parse('''
			A;
			B;
			C;
		''')
		model.assertNoErrors
	}
	
	@Test 
	def void superclassesSupported() {
		val model = parser.parse('''
			A;
			B : A;
			C : B;
		''')
		model.assertNoErrors
	}
	
	@Test 
	def void classNameWithoutCapitalFirstLetter() {
		val model = parser.parse('''
			a;
			B : C;
			C : B;
		''')
		model.assertWarning(
			AstPackage.Literals.CLASS_DECLARATION, 
			AstValidator::CLASS_NAME_CAPITAL_FIRST_LETTER,
			0,1,
			AstValidator::CLASS_NAME_CAPITAL_FIRST_LETTER_TEXT
		)
	}
	
	@Test 
	def void classWithChildren() {
		val model = parser.parse('''
			A ::= B;
			B ::= A C;
			C ::= A B;
		''')
		model.assertNoErrors
	}
	
	@Test 
	def void classWithChildrenWithSuperclass() {
		val model = parser.parse('''
			A ::= child1:B;
			B ::= child1:A;
			C ::= child1:B child2:A;
		''')
		model.assertNoErrors
	}
	
	@Test 
	def void tokenAsChild() {
		val model = parser.parse('''
			A ::= <token1>;
			B ::= <token1> <token2>;
			C ::= <token1> <token2> <token3>;
		''')
		model.assertNoErrors
	}
	
	@Test 
	def void tokenAsChildWithReferenceType() {
		val model = parser.parse('''
			A ::= <token1:B>;
			B ::= <token1:Integer> <token2:String>;
			C ::= <token1> <token2:String> <token3:Integer>;
		''')
		model.assertNoErrors()
	}
	
	@Test 
	def void tokenAsChildWithInvalidType() {
		val model = parser.parse('''
			A ::= <token1:C>;
			B ::= <token1:A>;
		''')
		model.assertError(
			AstPackage.Literals.TOKEN_COMPONENT,
			"org.eclipse.xtext.diagnostics.Diagnostic.Linking",
			14,1,
			"Couldn't resolve reference to ClassDeclaration 'C'"
		)
	}
	
	@Test 
	def void usingUndeclaredClassAsSuperclass() {
		val model = parser.parse('''
			A;
			B;
			C : D;
		''')
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			"org.eclipse.xtext.diagnostics.Diagnostic.Linking",
			10,1,
			"Couldn't resolve reference to ClassDeclaration 'D'."
		)
	}
	
	@Test
	def void simpleNTA() {
		val model = parser.parse('''
			A ::= /B/ /C*/;
			B;
			C;
		''')
		model.assertNoErrors()
	}
	
	@Test
	def void NTAwithUndeclaredClass() {
		val model = parser.parse('''
			A ::= /B/ /C*/;
			B;
		''')
		model.assertError(
			AstPackage.Literals.NTA,
			"org.eclipse.xtext.diagnostics.Diagnostic.Linking",
			11,1,
			"Couldn't resolve reference to ClassDeclaration 'C'."
		)
	}
	
	@Test
	def void NTAsOfSameType() {
		val model = parser.parse('''
			A ::= /B/ /B*/;
			B;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE,
			7,1,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT
		)
		
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE,
			11,1,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT
		)
	}
	
	@Test
	def void multipleClassesSharingName() {
		val model = parser.parse('''
			A;
			A;
			B;
			C;
		''')
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION, 
			AstValidator::MULTIPLE_CLASSES_WITH_SAME_NAME,
			0,1,
			AstValidator::MULTIPLE_CLASSES_WITH_SAME_NAME_TEXT
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION, 
			AstValidator::MULTIPLE_CLASSES_WITH_SAME_NAME,
			3,1,
			AstValidator::MULTIPLE_CLASSES_WITH_SAME_NAME_TEXT
		)
	}
	
	@Test
	def void multipleChildrenSharingName() {
		val model = parser.parse('''
			A ::= child1:B child1:C;
			B;
			C;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME,
			15,6,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME_TEXT
		)
	}
	
	@Test
	def void multipleChildrenSharingTypeAndName() {
		val model = parser.parse('''
			A;
			B;
			C ::= child1:A child1:B;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME,
			21,6,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME_TEXT
		)
	}
	
	@Test
	def void multipleChildrenSharingTypeWithoutNames() {
		val model = parser.parse('''
			A;
			B ::= A A;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE,
			9,1,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT
		)
		
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE,
			11,1,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_TYPE_TEXT
		)
	}
	
	@Test
	def void multipleChildrenSharingTypeOneName() {
		val model = parser.parse('''
			A;
			B ::= child1:A A;
		''')
		model.assertNoErrors()
	}
	
	/*
	 * Must add stacking of errors to get this to work. Not only
	 * one error should be highlighted at a time.
	 */
	@Test
	def void multipleChildrenSharingTypeAndNameExtended() {
		val model = parser.parse('''
			A;
			B ::= child1:A A child2:A child1:A;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME,
			29,6,
			AstValidator::MULTIPLE_CHILDREN_WITH_SAME_NAME_TEXT
		)
	}
	
	@Test
	def void classHavingItselfAsSuperclass() {
		val model = parser.parse('''
			A;
			B : B;
			C;
		''')
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CLASS_CANNOT_EXTEND_SELF,
			7,1,
			AstValidator::CLASS_CANNOT_EXTEND_SELF_TEXT
		)
	}
	
	@Test
	def void childHavingSameTypeAsParent() {
		val model = parser.parse('''
			A ::= B:A;
			C ::= C;
		''')
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::CHILD_SAME_TYPE_AS_PARENT,
			8,1,
			AstValidator::CHILD_SAME_TYPE_AS_PARENT_TEXT
		)
		
		model.assertError(
			AstPackage.Literals.COMPONENT,
			AstValidator::CHILD_SAME_TYPE_AS_PARENT,
			17,1,
			AstValidator::CHILD_SAME_TYPE_AS_PARENT_TEXT
		)
	}
	
	@Test
	def void childHavingSameNameAsParent() {
		val model = parser.parse('''
			A ::= A:B;
			B;
		''')
		model.assertWarning(
			AstPackage.Literals.COMPONENT,
			AstValidator::CHILD_SAME_NAME_AS_PARENT,
			6,1,
			AstValidator::CHILD_SAME_NAME_AS_PARENT_TEXT
		)
	}
	
	@Test
	def void childNameSameAsType() {
		val model = parser.parse('''
			A ::= B:B <String:String>;
			B;
		''')
		model.assertWarning(
			AstPackage.Literals.CHILD,
			AstValidator::CHILD_NAME_SAME_AS_TYPE,
			6,1,
			AstValidator::CHILD_NAME_SAME_AS_TYPE_TEXT
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			"org.eclipse.xtext.diagnostics.Diagnostic.Syntax",
			11,6,
			"mismatched input 'String' expecting RULE_ID"
		)
	}
	
	@Test
	def void circularInheritance1() {
		val model = parser.parse('''
			A : B;
			B : A;
		''')
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			4,1,
			"Circular inheritance. A extends (->) the following classes: B, and B extends A"
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			11,1,
			"Circular inheritance. B extends (->) the following classes: A, and A extends B"
		)
	}
	
	@Test
	def void circularInheritance2() {
		val model = parser.parse('''
			A : B;
			B : C;
			C : D;
			D : A;
		''')
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			4,1,
			"Circular inheritance. A extends (->) the following classes: B -> C -> D, and D extends A"
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			11,1,
			"Circular inheritance. B extends (->) the following classes: C -> D -> A, and A extends B"
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			18,1,
			"Circular inheritance. C extends (->) the following classes: D -> A -> B, and B extends C"
		)
		
		model.assertError(
			AstPackage.Literals.CLASS_DECLARATION,
			AstValidator::CIRCULAR_INHERITANCE,
			25,1,
			"Circular inheritance. D extends (->) the following classes: A -> B -> C, and C extends D"
		)
	}
	
	@Test
	def void redundantTokenType() {
		val model = parser.parse('''
			A ::= <token1:Integer> <token2:String> <token3:B>;
			B;
		''')
		model.assertNoErrors()
		model.assertWarning(
			AstPackage.Literals.TOKEN_COMPONENT,
			AstValidator::REDUNDANT_TOKEN_TYPE,
			31,6,
			AstValidator::REDUNDANT_TOKEN_TYPE_TEXT
		)
	}

	@Test
	def void largerFilesAssertNoErrors() {
		val testDirectoryPath = "./src/testFiles"
		val testDirectory = new File(testDirectoryPath)
		val directoryListing = testDirectory.listFiles()
		if (directoryListing != null) {
			for (File testFile : directoryListing) {
				print("Checking file \"" + testFile.name + "\" for errors...")
				val testFilePath = Paths.get(testDirectoryPath + "/" + testFile.name)
				val reader = Files.newBufferedReader(testFilePath)
				val stringBuilder = new StringBuilder
				var line = reader.readLine()
				
				while (line != null) {
					stringBuilder.append(line)
					line = reader.readLine()
				}
				
				val model = parser.parse(stringBuilder.toString)
				model.assertNoErrors
				println(" no errors found")
			}
		} else {
			throw new AssertionError("Could not find directory containing test files")
		}
	}
}
