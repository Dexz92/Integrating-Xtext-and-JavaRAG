package se.lth.cs.jastaddxtext.ast.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class AstHighlightingConfiguration implements IHighlightingConfiguration {
	
	
	public static final String CLASSDECLARATION_ID = "classdeclaration";

	public static final String COMMENT_ID = "comment";

	public static final String KEYWORD_ID = "keyword";

	public static final String OPTIONAL_ID = "optional";

	public static final String TOKEN_ID = "token";

	public static final String ITALICNAME_ID = "italic";

	public static final String OVERRIDE_ID = "override";

	public static final String NTA_ID = "nta";

	public static final String DOT_ID = "dots";

	public void configure(IHighlightingConfigurationAcceptor acceptor) {

		acceptor.acceptDefaultHighlighting(CLASSDECLARATION_ID, "ClassDeclaration", classDeclarationTextStyle());
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword", keywordTextStyle());
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment", commentTextStyle());
		acceptor.acceptDefaultHighlighting(OPTIONAL_ID, "Optional", optionalTextStyle());
		acceptor.acceptDefaultHighlighting(TOKEN_ID, "Token", tokenTextStyle());
		acceptor.acceptDefaultHighlighting(ITALICNAME_ID, "Italic", italicTextStyle());
		acceptor.acceptDefaultHighlighting(OVERRIDE_ID, "Override", overrideTextStyle());
		acceptor.acceptDefaultHighlighting(NTA_ID, "NTA", ntaTextStyle());
		acceptor.acceptDefaultHighlighting(DOT_ID, "Dot", dotTextStyle());
	}

	public TextStyle classDeclarationTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 51, 255));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle keywordTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle commentTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 204, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle optionalTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle tokenTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle italicTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(255, 0, 0));
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	public TextStyle overrideTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(204, 255, 0));
		return textStyle;
	}

	public TextStyle ntaTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(255, 0, 255));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle dotTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

}
