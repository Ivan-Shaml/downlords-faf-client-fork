package com.faforever.client.fa;

import org.springframework.util.StringUtils;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaunchCommandBuilder {

  private Float mean;
  private Float deviation;
  private String country;
  private String clan;
  private String username;
  private Integer uid;
  private Path executable;
  private List<String> additionalArgs;
  private Integer localGpgPort;
  private Path logFile;
  private Path replayFile;
  private Integer replayId;
  private URL replayUrl;

  private LaunchCommandBuilder() {
    // Private
  }

  public LaunchCommandBuilder localGpgPort(int localGpgPort) {
    this.localGpgPort = localGpgPort;
    return this;
  }

  public LaunchCommandBuilder executable(Path executable) {
    this.executable = executable;
    return this;
  }

  public LaunchCommandBuilder uid(Integer uid) {
    this.uid = uid;
    return this;
  }

  public LaunchCommandBuilder mean(Float mean) {
    this.mean = mean;
    return this;
  }

  public LaunchCommandBuilder deviation(Float deviation) {
    this.deviation = deviation;
    return this;
  }

  public LaunchCommandBuilder country(String country) {
    this.country = country;
    return this;
  }

  public LaunchCommandBuilder clan(String clan) {
    this.clan = clan;
    return this;
  }

  public LaunchCommandBuilder username(String username) {
    this.username = username;
    return this;
  }


  public LaunchCommandBuilder logFile(Path logFile) {
    this.logFile = logFile;
    return this;
  }

  public LaunchCommandBuilder additionalArgs(List<String> additionalArgs) {
    this.additionalArgs = additionalArgs;
    return this;
  }

  public LaunchCommandBuilder replayId(Integer replayId) {
    this.replayId = replayId;
    return this;
  }

  public LaunchCommandBuilder replayFile(Path replayFile) {
    this.replayFile = replayFile;
    return this;
  }

  public LaunchCommandBuilder replayUrl(URL replayUrl) {
    this.replayUrl = replayUrl;
    return this;
  }

  public List<String> build() {
    if (executable == null) {
      throw new IllegalStateException("executable has not been set");
    }
    if (logFile == null) {
      throw new IllegalStateException("logFile has not been set");
    }

    List<String> command = new ArrayList<>(Arrays.asList(
        executable.toAbsolutePath().toString(),
        "/init", "init_faf.lua",
        "/nobugreport"
    ));

    if (logFile != null) {
      command.add("/log");
      command.add(logFile.toAbsolutePath().toString());
    }

    if (localGpgPort != null) {
      command.add("/gpgnet");
      command.add("127.0.0.1:" + localGpgPort);
    }

    if (mean != null) {
      command.add("/mean");
      command.add(String.valueOf(mean));
    }

    if (deviation != null) {
      command.add("/deviation");
      command.add(String.valueOf(deviation));
    }

    if (replayFile != null) {
      command.add("/replay");
      command.add(replayFile.toAbsolutePath().toString());
    }

    if (replayUrl != null) {
      command.add("/replay");
      command.add(replayUrl.toExternalForm());
    }

    if (uid != null && username != null) {
      command.add("/savereplay");
      command.add("gpgnet://localhost/" + uid + "/" + username + ".SCFAreplay");
    }

    if (country != null) {
      command.add("/country");
      command.add(country);
    }

    if (!StringUtils.isEmpty(clan)) {
      command.add("/clan");
      command.add(clan);
    }

    if (replayId != null) {
      command.add("/replayid");
      command.add(String.valueOf(replayId));
    }

    if (additionalArgs != null) {
      command.addAll(additionalArgs);
    }

    return command;
  }

  public static LaunchCommandBuilder create() {
    return new LaunchCommandBuilder();
  }
}
