package io.github.fomin.conventionalcommits.validator;

import java.io.File;
import picocli.CommandLine.Option;

@SuppressWarnings("NullAway.Init") // Fields are initialized by picocli
public class CliOptions {
  @Option(
      names = {"-s", "--startRef"},
      description = "Start git ref",
      defaultValue = "HEAD")
  private String startRef;

  @Option(
      names = {"-e", "--endRef"},
      description = "End git ref",
      defaultValue = "HEAD^")
  private String endRef;

  @Option(
      names = {"-r", "--repoDir"},
      description = "Repository directory",
      defaultValue = ".")
  private File repoDir;

  @Option(
      names = {"-c", "--configFile"},
      description = "Configuration file")
  private File configFile;

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Display this help message")
  private boolean helpRequested;

  public String getStartRef() {
    return startRef;
  }

  public String getEndRef() {
    return endRef;
  }

  public File getRepoDir() {
    return repoDir;
  }

  public File getConfigFile() {
    return configFile;
  }

  public boolean isHelpRequested() {
    return helpRequested;
  }
}
