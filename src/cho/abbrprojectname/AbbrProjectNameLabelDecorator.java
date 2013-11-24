package cho.abbrprojectname;

import java.util.HashMap;
import java.util.Map.Entry;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
public class AbbrProjectNameLabelDecorator implements ILabelDecorator {

	private static HashMap<String,String> nameAbbrMap = new HashMap<String,String>();
	
	public AbbrProjectNameLabelDecorator() {
		parseAbbreviationPattern();
	}
	
	public static HashMap<String, String> parseAbbreviationPattern()
	{
		String pattern = AbbrProjectNameActivator.getDefault().getPreferenceStore().getString("abbr");
		return parseAbbreviationPattern(pattern);
	}

	public static HashMap<String, String> parseAbbreviationPattern(String pattern) {
		System.out.println(pattern);
		nameAbbrMap.clear();
		String[] parts = pattern.split("\\s*,\\s*");

		for(int i = 0; i < parts.length; i++)
		{
			String part = parts[i].trim();
			if(part.length() == 0)
				continue;
			String[] parts2 = part.split("\\s*=\\s*", 2);
			if(parts2.length != 2)
				return null;
			String prefix = parts2[0].trim();
			String abbr = parts2[1].trim();
			if(prefix.startsWith("#"))
				continue;
			nameAbbrMap.put(prefix,  abbr);
		}
		System.out.println(nameAbbrMap);
		return nameAbbrMap;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		ToggleAbbrProjectNamesDelegate.labelDecoratorDispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		return image;
	}

	@Override
	public String decorateText(String text, Object element) {
		if(ToggleAbbrProjectNamesDelegate.isOn) {
			if(element instanceof JavaProject) {
				JavaProject proj = (JavaProject) element;
				element = proj.getProject();
			} 
			if(element instanceof IProject) {
				for(Entry<String, String> entry : nameAbbrMap.entrySet()) {
					String pattern = entry.getKey();
					if(text.startsWith(pattern)) {
						return entry.getValue()+text.substring(pattern.length());
					}
				}
			}
		}
		return text;
	}
}
