/*
 * generated by Xtext 2.9.2
 */
package se.lth.cs.jastaddxtext.aspect.ui

import org.eclipse.ui.views.contentoutline.IContentOutlinePage
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import se.lth.cs.jastaddxtext.aspect.ui.outline.OutlineViewExpander

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
@FinalFieldsConstructor
class AspectUiModule extends AbstractAspectUiModule {
	override Class<? extends IContentOutlinePage> bindIContentOutlinePage() {
		return OutlineViewExpander
	}
}
