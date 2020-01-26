/**
 * general utilties concerning GUI
 */
package de.hab.ev3plugin.util;

import ilg.gnuarmeclipse.managedbuild.cross.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Gui {
	public static void MsgBox(String title, String value) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		MessageDialog.openInformation(win.getShell(), title, value);
	}

	public static void MsgBox(String value) {
		MsgBox("", value);
	}

	public static IProject getActiveProject() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		IProject proj = null;

		/* ===== This targets the project with the currently active window ===== */
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor != null) {
			IEditorInput inp = editor.getEditorInput();
			proj = inp.getAdapter(IProject.class);
			if (proj == null) {
				IResource res = inp.getAdapter(IResource.class);
				if (res != null) {
					proj = res.getProject();
				}
			}
		}

		/* ===== This targets project selected in project explorer ===== */
		 
		IWorkbenchWindow iworkbenchwindow = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
		ISelectionService ss = iworkbenchwindow.getSelectionService();
		String projExpID = "org.eclipse.ui.navigator.ProjectExplorer";
		ISelection sel = ss.getSelection(projExpID);
		Object selectedObject = sel;
		if (sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
		} else {

			IWorkbenchPage iworkbenchpage = iworkbenchwindow.getActivePage();
			IEditorInput ieditorpart = iworkbenchpage.getActiveEditor().getEditorInput();

			if (!(ieditorpart instanceof IFileEditorInput))
				return proj;
			selectedObject = ((IFileEditorInput) ieditorpart).getFile();
		}
		if (selectedObject instanceof IAdaptable) {
			IResource res = (IResource) ((IAdaptable) selectedObject).getAdapter(IResource.class);
			if (res != null)
				return res.getProject();
		}

		return proj;
	}

}
