package se.lth.cs.jastaddxtext.aspect.ui.outline;

import org.eclipse.xtext.ui.editor.outline.impl.OutlinePage;

public class OutlineViewExpander extends OutlinePage {
	@Override
	protected int getDefaultExpansionLevel() {
		return 2;
	}
}
