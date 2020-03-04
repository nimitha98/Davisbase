import java.io.RandomAccessFile;
import java.io.File;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class DavisBase {
  protected static final String masterTableName = "davisbase_tables";
  protected static String masterTableFilePath;
  protected static String masterColTableFilePath;
  public static final String masterColumnTableName = "davisbase_columns";
  protected static final String tableLocation = "data";
  protected static final String userDataFolder = "user_data";
  protected static final String systemDataFolder = "catalog";
  protected static final String indicesFolder = "indices";
  protected static final String seqFolder = "sequences";
  protected static final String tableFormat = ".tbl";
  private static final String prompt = "davisdbsql> ";
  protected static boolean isExit = false;
  protected static RandomAccessFile dbColumnFile;
  protected static RandomAccessFile dbMasterTableFile;
  static long pageSize = 512;
  static Scanner scanner = new Scanner(System.in).useDelimiter(";");

  public static void main(String[] args) {
    // File system setup
    File folder = new File(tableLocation);
    if (!folder.exists()) {
      folder.mkdir();
      folder = new File(Utils.getOSPath(new String[]{ tableLocation, userDataFolder }));
      folder.mkdir();
      folder = new File(Utils.getOSPath(new String[]{ tableLocation, systemDataFolder }));
      folder.mkdir();
      folder = new File(Utils.getOSPath(new String[]{ tableLocation, indicesFolder }));
      folder.mkdir();
      folder = new File(Utils.getOSPath(new String[]{ tableLocation, seqFolder }));
      folder.mkdir();
    }

    try {
      dbMasterTableFile = new RandomAccessFile(Utils.getFilePath("master", masterTableName), "rw");
      dbColumnFile = new RandomAccessFile(Utils.getFilePath("master", masterColumnTableName), "rw");

      BTree mDBtabletree = new BTree(dbMasterTableFile, DavisBase.masterTableName, false, true);
      BTree mDBColumnFiletree = new BTree(dbColumnFile, DavisBase.masterColumnTableName, true, false);

      buildDBBaseTable(dbMasterTableFile, mDBtabletree);
      buildDBColumnTable(dbColumnFile, mDBColumnFiletree);

    } catch (Exception e) {
      System.out.println("Unexpected Error: " + e.getMessage());
    }

    // Greet user!
    Prompt.splashScreen();

    String userCommand = "";
    while (!isExit) {
      System.out.print(prompt);
      userCommand = scanner.next().replace("\n", "").replace("\r", "").trim().toLowerCase();
      Parser.parse(userCommand);
    }
    System.out.println("Exiting...");
  }

  private static void buildDBColumnTable(RandomAccessFile mDBColumnFile, BTree mDBColumnFiletree) throws Exception {
    if (mDBColumnFile.length() > 0) return;

    LinkedHashMap<String, ArrayList<String>> token;

    token = Utils.buildInsertRecord(Arrays.asList("1", DavisBase.masterTableName, "rowid", "int", "1", "no", "na", "no", "no"));
    mDBColumnFiletree.createNewTableLeaf(token);

    token = Utils.buildInsertRecord(Arrays.asList("2", DavisBase.masterTableName, "table_name", "text", "2", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("3", DavisBase.masterColumnTableName, "rowid", "int", "1", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("4", DavisBase.masterColumnTableName, "table_name", "text", "2", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("5", DavisBase.masterColumnTableName, "column_name", "text", "3", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("6", DavisBase.masterColumnTableName, "data_type", "text", "4", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("7", DavisBase.masterColumnTableName, "ordinal_position", "tinyint", "5", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("8", DavisBase.masterColumnTableName, "is_nullable", "text", "6", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("9", DavisBase.masterColumnTableName, "default", "text", "7", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);

    token = Utils.buildInsertRecord(Arrays.asList("10", DavisBase.masterColumnTableName, "is_unique", "text", "8", "no", "na", "no", "no"));
    mDBColumnFiletree.insertNewRecord(token);
  }

  private static void buildDBBaseTable(RandomAccessFile mDBtableFile, BTree mDBtabletree) throws Exception {
    if (mDBtableFile.length() > 0) return;

    LinkedHashMap<String, ArrayList<String>> token = new LinkedHashMap<String, ArrayList<String>>();
    token.put("rowid", new ArrayList<String>(Arrays.asList("int", "1")));
    token.put("table_name", new ArrayList<String>(Arrays.asList("text", DavisBase.masterTableName)));
    mDBtabletree.createNewTableLeaf(token);

    token.clear();
    token.put("rowid", new ArrayList<String>(Arrays.asList("int", "2")));
    token.put("table_name", new ArrayList<String>(Arrays.asList("text", DavisBase.masterColumnTableName)));
    mDBtabletree.insertNewRecord(token);
  }
}
