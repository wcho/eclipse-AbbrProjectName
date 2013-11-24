package cho.abbrprojectname;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.viewsupport.DecoratingJavaLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

@SuppressWarnings("restriction")
public class ToggleAbbrProjectNamesDelegate implements IViewActionDelegate {

	public static boolean isOn = false;
	public static boolean decoratorAdded = false;
	
	@Override
	public void run(IAction action) {
		System.out.println("run");
		PackageExplorerPart pev = getPackageExplorer();
		if(pev == null) return;
		
		if(isOn) {
			isOn = false;
		} else {
			if(!decoratorAdded) {
				DecoratingJavaLabelProvider dlp =(DecoratingJavaLabelProvider) pev.getTreeViewer().getLabelProvider();
				JavaUILabelProvider jlp = ((JavaUILabelProvider) dlp.getStyledStringProvider());
				jlp.addLabelDecorator(new AbbrProjectNameLabelDecorator());
				decoratorAdded= true;
			}
			isOn = true;
		}
		refreshPackageExplorer();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println("selectionChanged");
	}

	@Override
	public void init(IViewPart view) {
		System.out.println("init: "+view);
	}
	
	public static PackageExplorerPart getPackageExplorer() {
		IViewPart v2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
				findView(JavaUI.ID_PACKAGES);
		if(v2 == null) return null;
		return (PackageExplorerPart) v2;
	}
	
	public static void labelDecoratorDispose() {
		decoratorAdded = false;
		if(isOn) {
			runCommand();
		}
	}
	
	public static void refreshPackageExplorer() {
		PackageExplorerPart pev = getPackageExplorer();
		if(pev == null) return;
		pev.getTreeViewer().refresh();
	}
	
	public static void runCommand() {
		IViewPart v2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
				findView(JavaUI.ID_PACKAGES);
		if(v2 == null) return;
		ICommandService cmdService = (ICommandService) v2.getSite().getService(ICommandService.class);
		Command cmd = cmdService.getCommand("cho.AbbrProjectName.command1");
		try {
			cmd.executeWithChecks(new ExecutionEvent());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnabledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
