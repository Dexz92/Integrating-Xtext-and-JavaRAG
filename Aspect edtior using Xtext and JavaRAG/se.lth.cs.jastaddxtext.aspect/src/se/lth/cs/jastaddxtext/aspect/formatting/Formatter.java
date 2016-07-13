package se.lth.cs.jastaddxtext.aspect.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

import com.google.inject.Inject;

import se.lth.cs.jastaddxtext.aspect.services.AspectGrammarAccess;

public class Formatter extends AbstractDeclarativeFormatter {
	
	@Inject
	AspectGrammarAccess g;
	
	/**
	 * Format configuration for each grammar declaration
	 * @param config - FormattingConfig
	 */
	@Override
	protected void configureFormatting(FormattingConfig config) {
		
		//ImportSection
		config.setLinewrap(1).after(g.getXImportSectionAccess().getImportDeclarationsAssignment());
		
		//AspectDeclaration
		config.setLinewrap(1).before(g.getAspectDeclarationAccess().getAspectKeyword_0());
		config.setLinewrap(1).after(g.getAspectDeclarationAccess().getLeftCurlyBracketKeyword_2());
		config.setLinewrap().after(g.getAspectDeclarationAccess().getStatementAssignment_3());
		config.setLinewrap().before(g.getAspectDeclarationAccess().getRightCurlyBracketKeyword_4());
		config.setLinewrap(2).after(g.getAspectDeclarationAccess().getRightCurlyBracketKeyword_4());
		config.setLinewrap(2).after(g.getSynthesizedDeclarationAccess().getSemicolonKeyword_11_1_2());
		config.setLinewrap(2).after(g.getSynthesizedDeclarationAccess().getSemicolonKeyword_11_2());
		
		//SynthesizedDeclaration
		config.setNoSpace().after(g.getSynthesizedDeclarationAccess().getClassRefAssignment_4_0());
		config.setNoSpace().before(g.getSynthesizedDeclarationAccess().getNameAssignment_6());
		config.setNoSpace().before(g.getSynthesizedDeclarationAccess().getLeftParenthesisKeyword_7());
		config.setNoSpace().before(g.getSynthesizedDeclarationAccess().getRightParenthesisKeyword_9());
		config.setNoSpace().before(g.getSynthesizedDeclarationAccess().getSemicolonKeyword_11_2());
		config.setNoSpace().before(g.getSynthesizedDeclarationAccess().getSemicolonKeyword_11_1_2());

		//XBlockExpression
		config.setLinewrap().after(g.getXBlockExpressionAccess().getLeftCurlyBracketKeyword_1());
		config.setLinewrap().before(g.getXBlockExpressionAccess().getRightCurlyBracketKeyword_3());
		config.setLinewrap(2).after(g.getXBlockExpressionAccess().getRightCurlyBracketKeyword_3());

		//InheritedDeclaration
		config.setNoSpace().after(g.getInheritedDeclarationAccess().getClassRefAssignment_4_0());
		config.setNoSpace().before(g.getInheritedDeclarationAccess().getNameAssignment_6());
		config.setNoSpace().before(g.getInheritedDeclarationAccess().getLeftParenthesisKeyword_7());
		config.setNoSpace().after(g.getInheritedDeclarationAccess().getLeftParenthesisKeyword_7());
		config.setNoSpace().before(g.getInheritedDeclarationAccess().getRightParenthesisKeyword_9());
		config.setNoSpace().before(g.getInheritedDeclarationAccess().getSemicolonKeyword_10());
		config.setLinewrap(2).after(g.getInheritedDeclarationAccess().getSemicolonKeyword_10());

		
		//Equation
		config.setNoSpace().after(g.getEquationAccess().getClassRefAssignment_1_0());
		config.setNoSpace().before(g.getEquationAccess().getNameAssignment_4());
		config.setNoSpace().before(g.getEquationAccess().getLeftParenthesisKeyword_5());
		config.setNoSpace().after(g.getEquationAccess().getLeftParenthesisKeyword_5());
		config.setNoSpace().before(g.getEquationAccess().getRightParenthesisKeyword_7());
		config.setNoSpace().before(g.getEquationAccess().getSemicolonKeyword_8_1_2());
		
		//CollectionDeclaration
		config.setNoSpace().after(g.getCollectionDeclarationAccess().getClassRefAssignment_2_0());
		config.setNoSpace().before(g.getCollectionDeclarationAccess().getNameAssignment_4());
		config.setNoSpace().before(g.getCollectionDeclarationAccess().getLeftParenthesisKeyword_5());
		config.setNoSpace().after(g.getCollectionDeclarationAccess().getLeftParenthesisKeyword_5());
		config.setNoSpace().before(g.getCollectionDeclarationAccess().getRightParenthesisKeyword_6());
		config.setNoSpace().after(g.getCollectionDeclarationAccess().getLeftSquareBracketKeyword_7_0());
		config.setNoSpace().before(g.getCollectionDeclarationAccess().getRightSquareBracketKeyword_7_2());
		config.setNoSpace().before(g.getCollectionDeclarationAccess().getSemicolonKeyword_10());
		config.setLinewrap(2).after(g.getCollectionDeclarationAccess().getSemicolonKeyword_10());

		//CollectionContribution
		config.setNoSpace().before(g.getCollectionContributionAccess().getClassRefClassDeclarationCrossReference_0_0_0());
		config.setNoSpace().after(g.getCollectionContributionAccess().getTargetClassRefAssignment_6_0());
		config.setNoSpace().after(g.getCollectionContributionAccess().getTargetNameAssignment_8());
		config.setNoSpace().before(g.getCollectionContributionAccess().getTargetNameAssignment_8());
		config.setNoSpace().after(g.getCollectionContributionAccess().getLeftParenthesisKeyword_9());
		config.setNoSpace().before(g.getCollectionContributionAccess().getLeftParenthesisKeyword_9());
		config.setNoSpace().before(g.getCollectionContributionAccess().getRightParenthesisKeyword_10());
		config.setNoSpace().before(g.getCollectionContributionAccess().getSemicolonKeyword_12());
		config.setLinewrap(2).after(g.getCollectionContributionAccess().getSemicolonKeyword_12());


		//NTAContribution
		config.setNoSpace().after(g.getNTAContributionAccess().getClassRefAssignment_0_0());
		config.setNoSpace().before(g.getNTAContributionAccess().getChildNTAAssignment_3());
		config.setNoSpace().before(g.getNTAContributionAccess().getLeftParenthesisKeyword_4());
		config.setNoSpace().after(g.getNTAContributionAccess().getLeftParenthesisKeyword_4());
		config.setNoSpace().before(g.getNTAContributionAccess().getRightParenthesisKeyword_5());
		config.setNoSpace().before(g.getNTAContributionAccess().getTargetClassRefAssignment_7_0());
		config.setNoSpace().before(g.getNTAContributionAccess().getTargetNameAssignment_9());
		config.setNoSpace().before(g.getNTAContributionAccess().getTargetClassRefAssignment_7_0());
		config.setNoSpace().before(g.getNTAContributionAccess().getLeftParenthesisKeyword_10());
		config.setNoSpace().after(g.getNTAContributionAccess().getLeftParenthesisKeyword_10());
		config.setNoSpace().before(g.getNTAContributionAccess().getRightParenthesisKeyword_11());
		
		//Rewrite
		config.setLinewrap(1).after(g.getRewriteAccess().getLeftCurlyBracketKeyword_2());
		config.setLinewrap(1).after(g.getRewriteAccess().getRightCurlyBracketKeyword_4());
		config.setLinewrap(1).after(g.getRewriteAccess().getRightParenthesisKeyword_3_0_3());

		
		//CacheOrUncache
		config.setNoSpace().after(g.getCacheOrUncacheAccess().getClassRefAssignment_1_0());
		config.setNoSpace().after(g.getCacheOrUncacheAccess().getNameAssignment_3());
		config.setNoSpace().before(g.getCacheOrUncacheAccess().getNameAssignment_3());
		config.setNoSpace().after(g.getCacheOrUncacheAccess().getLeftParenthesisKeyword_4());
		config.setNoSpace().before(g.getCacheOrUncacheAccess().getRightParenthesisKeyword_6());
		config.setLinewrap().after(g.getCacheOrUncacheAccess().getSemicolonKeyword_7());
		
		//IntertypeDeclaration
		config.setNoSpace().after(g.getInterTypeDeclarationAccess().getClassRefAssignment_3_0());
		config.setNoSpace().before(g.getInterTypeDeclarationAccess().getNameAssignment_5());
		config.setNoSpace().before(g.getInterTypeDeclarationAccess().getLeftParenthesisKeyword_6());
		config.setNoSpace().after(g.getInterTypeDeclarationAccess().getLeftParenthesisKeyword_6());
		config.setNoSpace().before(g.getInterTypeDeclarationAccess().getRightParenthesisKeyword_8());
		config.setNoSpace().before(g.getInterTypeDeclarationAccess().getSemicolonKeyword_9_2());
		config.setLinewrap(2).after(g.getInterTypeDeclarationAccess().getSemicolonKeyword_9_2());
		config.setNoSpace().before(g.getInterTypeDeclarationAccess().getSemicolonKeyword_9_1_2());
		config.setLinewrap(2).after(g.getInterTypeDeclarationAccess().getSemicolonKeyword_9_1_2());
		
		//ImlpementStatement
		config.setNoSpace().before(g.getImplementStatementAccess().getSemicolonKeyword_3());
		config.setLinewrap(2).after(g.getImplementStatementAccess().getSemicolonKeyword_3());
				
	}

}