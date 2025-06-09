package io.github.fomin.conventionalcommits.validator;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class Main {

  private Main() {
    // Prevent instantiation
  }

  public static void main(String[] args) {
    CliOptions options = new CliOptions();
    CommandLine cmd = new CommandLine(options);
    try {
      cmd.parseArgs(args);
    } catch (ParameterException ex) {
      cmd.getErr().println(ex.getMessage());
      System.exit(cmd.getCommandSpec().exitCodeOnInvalidInput());
    }
    int exitCode = new ValidatorRunner(cmd, options).run();
    System.exit(exitCode);
  }
}
