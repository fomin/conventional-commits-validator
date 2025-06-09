package io.github.fomin.conventionalcommits.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

public class ValidatorRunnerTest {
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;

  private CliOptions options;
  private CommandLine cmd;

  @BeforeEach
  void setUp(@TempDir Path tempDir) {
    options = new CliOptions();
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    cmd = new CommandLine(options);
    cmd.setOut(new PrintWriter(new OutputStreamWriter(outContent, StandardCharsets.UTF_8), true));
    cmd.setErr(new PrintWriter(new OutputStreamWriter(errContent, StandardCharsets.UTF_8), true));
  }

  @Test
  void testHelpOption() {
    cmd.parseArgs("--help");
    int exitCode = new ValidatorRunner(cmd, options).run();
    assertEquals(0, exitCode, "Help should exit with code 0");
    String output = outContent.toString(StandardCharsets.UTF_8);
    assertTrue(output.contains("Usage:"), "Help output should contain Usage");
  }

  @Test
  void testNonExistentConfigFile_UserSpecified(@TempDir Path tempDir) {
    File nonExistent = tempDir.resolve("non-existent-config.yaml").toFile();
    cmd.parseArgs("--configFile", nonExistent.getAbsolutePath());
    int exitCode = new ValidatorRunner(cmd, options).run();
    assertEquals(
        CommandLine.ExitCode.USAGE,
        exitCode,
        "Non-existent config file should exit with USAGE code");
    String err = errContent.toString(StandardCharsets.UTF_8).trim();
    String expected = "Error: Configuration file not found: " + nonExistent.getAbsolutePath();
    assertTrue(
        err.contains(expected),
        "Error output should mention missing config file. Actual: '" + err + "'");
  }
}
