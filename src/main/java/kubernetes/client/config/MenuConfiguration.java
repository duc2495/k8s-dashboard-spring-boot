package kubernetes.client.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MenuConfiguration {
	private static Map<String, String> MENU_URL_PATTERN_MAP = new HashMap<>();
	
	static
	{
		MENU_URL_PATTERN_MAP.put("/home", "Home");
		MENU_URL_PATTERN_MAP.put("/projects", "Projects");
		MENU_URL_PATTERN_MAP.put("/overview", "Overview");
		MENU_URL_PATTERN_MAP.put("/apps", "Applications");
		MENU_URL_PATTERN_MAP.put("/deployments", "Deployments");
		MENU_URL_PATTERN_MAP.put("/pods", "Pods");
		MENU_URL_PATTERN_MAP.put("/services", "Services");
		MENU_URL_PATTERN_MAP.put("/templates", "Templates");
		MENU_URL_PATTERN_MAP.put("/storage", "Storage");	
	}
	
	public static Map<String, String> getMenuUrlPatternMap() {
		return Collections.unmodifiableMap(MENU_URL_PATTERN_MAP);
	}

	public static String getMatchingMenu(String uri) {
		Set<String> keySet = MENU_URL_PATTERN_MAP.keySet();
		for (String key : keySet) {
			if(uri.contains(key)){
				return MENU_URL_PATTERN_MAP.get(key);
			}
		}
		return "";
	}
}
