package io.github.fomin.conventionalcommits;

import java.io.File;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

public interface ConventionalCommitsExtension {
  Property<String> getStartRef();

  Property<String> getEndRef();

  Property<File> getRepoDir();

  RegularFileProperty getConfigurationFile();
}
