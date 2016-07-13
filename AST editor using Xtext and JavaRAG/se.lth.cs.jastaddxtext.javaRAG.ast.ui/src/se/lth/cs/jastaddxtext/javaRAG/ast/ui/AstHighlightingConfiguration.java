
package se.lth.cs.jastaddxtext.javaRAG.ast.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
/**
 * This class configurates styles
 *
 */
public class AstHighlightingConfiguration implements IHighlightingConfiguration {

	public static final String CLASSDECLARATION_ID = "classdeclaration";

	public static final String COMMENT_ID = "comment";

	public static final String KEYWORD_ID = "keyword";

	public static final String OPTIONAL_ID = "optional";

	public static final String TOKEN_ID = "token";

	public static final String TYPE_ID = "type";

	public static final String OVERRIDE_ID = "override";

	public static final String NTA_ID = "nta";

	public static final String DOT_ID = "dots";

	/**
	 * Applying styles for defined declarations
	 * 
	 * @param acceptor - IHighlightingConfigurationAcceptor
	 */
	public void configure(IHighlightingConfigurationAcceptor acceptor) {

		acceptor.acceptDefaultHighlighting(CLASSDECLARATION_ID, "ClassDeclaration", classDeclarationTextStyle());
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword", keywordTextStyle());
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment", commentTextStyle());
		acceptor.acceptDefaultHighlighting(OPTIONAL_ID, "Optional", optionalTextStyle());
		acceptor.acceptDefaultHighlighting(TOKEN_ID, "Token", tokenTextStyle());
		acceptor.acceptDefaultHighlighting(TYPE_ID, "Type", typeTextStyle());
		acceptor.acceptDefaultHighlighting(OVERRIDE_ID, "Override", overrideTextStyle());
		acceptor.acceptDefaultHighlighting(NTA_ID, "NTA", ntaTextStyle());
		acceptor.acceptDefaultHighlighting(DOT_ID, "Dot", dotTextStyle());
	}

	/**
	 * TextStyle for ClassDeclarations
	 * 
	 * @return TextStyle
	 */
	public TextStyle classDeclarationTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 76, 153));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for keywords
	 * 
	 * @return TextStyle
	 */
	public TextStyle keywordTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for comments
	 * 
	 * @return TextStyle
	 */
	public TextStyle commentTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 204, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for optionals
	 * 
	 * @return TextStyle
	 */
	public TextStyle optionalTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for tokens
	 * 
	 * @return TextStyle
	 */
	public TextStyle tokenTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for italic words
	 * 
	 * @return TextStyle
	 */
	public TextStyle typeTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(153, 0, 76));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for override notation
	 * 
	 * @return TextStyle
	 */
	public TextStyle overrideTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(156, 76, 0));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	/**
	 * TextStyle for NTAs
	 * 
	 * @return TextStyle
	 */
	public TextStyle ntaTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	/**
	 * TextStyle for assignments,list and extend symbols
	 * 
	 * @return TextStyle
	 */
	public TextStyle dotTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

}
