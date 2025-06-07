package io.github.fomin.conventionalcommits;

import java.io.File;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;

public interface CheckCommitsWorkParameters extends WorkParameters {
  Property<String> getStartRef();

  Property<String> getEndRef();

  Property<File> getRepoDir();

  RegularFileProperty getConfigurationFile();

  DirectoryProperty getProjectDir();
}
