package fitnesse.testrunner;

import fitnesse.testsystems.Descriptor;
import fitnesse.wiki.ReadOnlyPageData;

/**
 * Define a (hashable) extract of the test page, to be used as input for building the test system.
 */
public class WikiPageDescriptor implements Descriptor {
  public static final String COMMAND_PATTERN = "COMMAND_PATTERN";
  public static final String TEST_RUNNER = "TEST_RUNNER";
  public static final String TEST_SYSTEM = "TEST_SYSTEM";
  public static final String IN_PROCESS = "^inprocess";

  private final ReadOnlyPageData data;
  private final boolean inProcess;
  private final boolean remoteDebug;
  private final String classPath;

  public WikiPageDescriptor(ReadOnlyPageData data, boolean inProcess, boolean remoteDebug, String classPath) {
    this.data = data;
    this.inProcess = inProcess;
    // Debug property should move to ClientBuilder
    this.remoteDebug = remoteDebug;
    this.classPath = classPath;
  }

  @Override
  public String getTestSystemType() {
    String type = getRawTestSystemType();
    if (inProcess) type += IN_PROCESS;
    return type;
  }

  private String getRawTestSystemType() {
    return getTestSystem().split(":")[0];
  }

  @Override
  public String getClassPath() {
    return classPath;
  }

  @Override
  public boolean isDebug() {
    return remoteDebug;
  }

  // Generic entry point for everything the test system needs to know.
  @Override
  public String getVariable(String name) {
    return data.getVariable(name);
  }

  @Override
  public String getTestSystem() {
    String testSystemName = getVariable(TEST_SYSTEM);
    if (testSystemName == null)
      return "fit";
    return testSystemName;
  }

  private String testRunner() {
    String program = getVariable(TEST_RUNNER);
    if (program == null)
      program = "";
    return program;
  }

  private String commandPattern() {
    String testRunner = getVariable(COMMAND_PATTERN);
    if (testRunner == null)
      testRunner = "";
    return testRunner;
  }

  @Override
  public int hashCode() {
    return getTestSystem().hashCode() ^ testRunner().hashCode() ^ commandPattern().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;

    WikiPageDescriptor descriptor = (WikiPageDescriptor) obj;
    return descriptor.getTestSystem().equals(getTestSystem()) &&
            descriptor.testRunner().equals(testRunner()) &&
            descriptor.commandPattern().equals(commandPattern());
  }

}
