package org.xtext.hipie.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.xtext.hipie.views.DesignModeView;

public class HipiePerspective implements IPerspectiveFactory {

	public static String perspec_id = "org.xtext.hipie.ui.hipie_perspec" ;
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addShowViewShortcut(DesignModeView.ID) ;
        layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.26, layout.getEditorArea());
        left.addView(IPageLayout.ID_PROJECT_EXPLORER);
        IFolderLayout bot_left = layout.createFolder("bot_left", IPageLayout.BOTTOM, (float) 0.5, IPageLayout.ID_PROJECT_EXPLORER);
        bot_left.addView(IPageLayout.ID_OUTLINE);
        layout.addPlaceholder(DesignModeView.ID , IPageLayout.RIGHT, 0.6f, layout.getEditorArea()) ;
        IFolderLayout bot = layout.createFolder("bot", IPageLayout.BOTTOM, (float) 0.75, layout.getEditorArea());
        bot.addView(IPageLayout.ID_PROBLEM_VIEW);
	}
	
}