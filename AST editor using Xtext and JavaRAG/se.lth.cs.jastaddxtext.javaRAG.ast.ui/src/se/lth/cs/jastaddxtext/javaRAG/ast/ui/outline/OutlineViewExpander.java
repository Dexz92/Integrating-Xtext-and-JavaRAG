package se.lth.cs.jastaddxtext.javaRAG.ast.ui.outline;

import org.eclipse.xtext.ui.editor.outline.impl.OutlinePage;

/**
 * 
 * This class is used to expand the outline view as default when
 * the editor is started.
 *
 */
public class OutlineViewExpander extends OutlinePage{
	
	@Override
	protected int getDefaultExpansionLevel(){
		return 2;
	}

}
