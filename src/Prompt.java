import java.util.HashMap;

public class Prompt {
  private static final String version = "v0.1.1";
  private static final String copyright = "@2019 Team Blue";

  public static HashMap<String, Integer> h;
  public static String getTableCurVal(String tableName) {
		if (h == null) {
			h = new HashMap<String, Integer>();
		}
		if (!h.containsKey(tableName)) {
			h.put(tableName,1);
			return Integer.toString(1);
		}
		int tmp = h.get(tableName);
		h.put(tableName,tmp+1);
		return Integer.toString(tmp+1);
	}
  
  public static void splashScreen() {
    System.out.println(Utils.repeat("-", 80));
    System.out.println("Welcome to DavisBase Lite"); // Display the string.
    System.out.println("DavisBase Lite Version " + getVersion());
    System.out.println(getCopyright());
    System.out.println("\nType \"help;\" to display supported commands.");
    System.out.println(Utils.repeat("-", 80));
  }

  public static String getVersion() {
    return version;
  }

  public static String getCopyright() {
    return copyright;
  }

  public static void displayVersion() {
    System.out.println("DavisBase Lite Version " + getVersion());
    System.out.println(getCopyright());
  }
}
