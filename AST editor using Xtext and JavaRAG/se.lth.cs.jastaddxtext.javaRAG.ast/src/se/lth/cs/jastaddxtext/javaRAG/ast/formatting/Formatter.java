package se.lth.cs.jastaddxtext.javaRAG.ast.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

import com.google.inject.Inject;

import se.lth.cs.jastaddxtext.javaRAG.ast.services.AstGrammarAccess;

/**
 * 
 * This class customizes the formatting
 *
 */
public class Formatter extends AbstractDeclarativeFormatter {
	@Inject
	AstGrammarAccess g;
	
	/**
	 * Format configuration for each grammar declaration
	 * @param config - FormattingConfig
	 */
	@Override
	protected void configureFormatting(FormattingConfig config) {

		// ClassDeclaration
		config.setNoLinewrap().after(g.getClassDeclarationAccess().getAbstractAbstractKeyword_0_0());

		config.setNoLinewrap().before(g.getClassDeclarationAccess().getSuperclassAssignment_2_1());
		config.setNoLinewrap().before(g.getClassDeclarationAccess().getColonKeyword_2_0());
		config.setNoLinewrap().after(g.getClassDeclarationAccess().getColonKeyword_2_0());

		config.setNoLinewrap().after(g.getClassDeclarationAccess().getSuperclassAssignment_2_1());
		config.setNoLinewrap().after(g.getClassDeclarationAccess().getSuperclassClassDeclarationCrossReference_2_1_0());

		config.setNoSpace().before(g.getClassDeclarationAccess().getSemicolonKeyword_4());
		config.setLinewrap().after(g.getClassDeclarationAccess().getSemicolonKeyword_4());

		// List
		config.setNoSpace().before(g.getListComponentAccess().getAsteriskKeyword_1());

		// Token
		config.setNoLinewrap().before(g.getTokenComponentAccess().getGreaterThanSignKeyword_3());
		config.setNoLinewrap().before(g.getTokenComponentAccess().getAlternatives_2_1());
		config.setNoLinewrap().after(g.getTokenComponentAccess().getColonKeyword_2_0());
		config.setNoLinewrap().after(g.getTokenComponentAccess().getNameAssignment_1());
		config.setNoLinewrap().after(g.getTokenComponentAccess().getLessThanSignKeyword_0());
		config.setNoLinewrap().before(g.getTokenComponentAccess().getLessThanSignKeyword_0());
		config.setNoSpace().after(g.getTokenComponentAccess().getLessThanSignKeyword_0());
		config.setNoSpace().before(g.getTokenComponentAccess().getGreaterThanSignKeyword_3());
		config.setNoSpace().after(g.getTokenComponentAccess().getNameAssignment_1());
		config.setNoSpace().before(g.getTokenComponentAccess().getTypeAssignment_2_1_0());
		config.setNoSpace().before(g.getTokenComponentAccess().getReferenceAssignment_2_1_1());

		// Optional
		config.setNoLinewrap().after(g.getOptionalComponentAccess().getLeftSquareBracketKeyword_0());
		config.setNoLinewrap().before(g.getOptionalComponentAccess().getLeftSquareBracketKeyword_0());

		config.setNoLinewrap().before(g.getOptionalComponentAccess().getRightSquareBracketKeyword_2());
		config.setNoLinewrap().before(g.getOptionalComponentAccess().getRightSquareBracketKeyword_2());

		// Component
		config.setNoLinewrap().after(g.getComponentAccess().getNameAssignment_0_0());
		config.setNoLinewrap().before(g.getComponentAccess().getNameAssignment_0_0());
		config.setNoLinewrap().before(g.getComponentAccess().getTypeAssignment_1());
		config.setNoLinewrap().before(g.getComponentAccess().getTypeClassDeclarationCrossReference_1_0());

		config.setNoLinewrap().after(g.getComponentAccess().getTypeAssignment_1());
		config.setNoLinewrap().after(g.getComponentAccess().getTypeClassDeclarationCrossReference_1_0());

		config.setNoSpace().before(g.getComponentAccess().getColonKeyword_0_1());
		config.setNoSpace().after(g.getComponentAccess().getColonKeyword_0_1());
	}

}
