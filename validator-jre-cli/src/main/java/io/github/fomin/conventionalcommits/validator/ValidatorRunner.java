package io.github.fomin.conventionalcommits.validator;

import io.github.fomin.conventionalcommits.rules.GitRepositoryValidator;
import io.github.fomin.conventionalcommits.rules.RuleSet;
import io.github.fomin.conventionalcommits.rules.RulesParser;
import io.github.fomin.conventionalcommits.rules.ValidationResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import picocli.CommandLine;

public class ValidatorRunner {
  private final CommandLine cmd;
  private final CliOptions options;

  public ValidatorRunner(CommandLine cmd, CliOptions options) {
    this.cmd = cmd;
    this.options = options;
  }

  public int run() {
    if (options.isHelpRequested()) {
      cmd.usage(cmd.getOut());
      return 0;
    }
    String rawConfiguration;
    try {
      rawConfiguration = loadConfiguration();
    } catch (CommandLine.ParameterException ex) {
      cmd.getErr().println(ex.getMessage());
      return ex.getCommandLine().getCommandSpec().exitCodeOnInvalidInput();
    }

    RuleSet ruleSet = RulesParser.parse(rawConfiguration);
    List<ValidationResult> results =
        GitRepositoryValidator.validate(
            options.getRepoDir(), options.getStartRef(), options.getEndRef(), ruleSet);

    if (results.isEmpty()) {
      cmd.getOut().println("All validated commits are conventional.");
      return 0;
    } else {
      for (ValidationResult vr : results) {
        cmd.getErr()
            .println(
                "Commit " + vr.getCommitSha1() + " has invalid message: " + vr.getMessage().trim());
        for (String error : vr.getErrors()) {
          cmd.getErr().println("  " + error);
        }
      }
      cmd.getErr().println(results.size() + " commit(s) failed validation.");
      return 1;
    }
  }

  private String loadConfiguration() {
    File configFile = options.getConfigFile();
    File configurationFileToLoad =
        configFile != null ? configFile : new File("conventional-commits.yaml");
    if (configurationFileToLoad.exists()) {
      try {
        return new String(
            Files.readAllBytes(configurationFileToLoad.toPath()), StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new RuntimeException(
            "Failed to read file: " + configurationFileToLoad.getAbsolutePath(), e);
      }
    } else {
      if (configFile != null) {
        throw new CommandLine.ParameterException(
            cmd, "Error: Configuration file not found: " + configFile.getAbsolutePath());
      }
      return "";
    }
  }
}
