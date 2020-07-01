/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package com.amazon.aocagent;

import com.amazon.aocagent.enums.Stack;
import com.amazon.aocagent.enums.TestAMI;
import com.amazon.aocagent.exception.BaseException;
import com.amazon.aocagent.models.Context;
import com.amazon.aocagent.tasks.ITask;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;




@CommandLine.Command(
    name = "aocintegtest",
    mixinStandardHelpOptions = true,
    version = "aocintegtest 1.0",
    description = "use for integtest and releases of the aocagent")
@Log4j2
public class App implements Callable<Integer> {
  @CommandLine.Option(
      names = {"-t", "--task"},
      description = "EC2Test, ECSTest, EKSTest, ...",
      defaultValue = "S3Release"
  )
  private String taskName;

  @CommandLine.Option(
      names = {"-s", "--stack"},
      description = "TEST, RELEASE, HKG_RELEASE...",
      defaultValue = "TEST"
  )
  private String stackName = "TEST";

  @CommandLine.Option(
      names = {"-l", "--local-packages-dir"},
      description =
          "read packages, version file from this directory, default value is build/packages",
      defaultValue = "build/packages"
  )
  private String localPackagesDir;

  @CommandLine.Option(
      names = {"-r", "--region"},
      description =
          "region will be used to create the testing resource like EC2 Instance,"
              + " and be used to perform regionlized release, the default value is us-west-2",
      defaultValue = "us-west-2"
  )
  private String region;

  @CommandLine.Option(
      names = {"-a", "--ami"},
      description = "the ami used for ec2 integ-test, default value is AMAZON_LINUX2",
      defaultValue = "AMAZON_LINUX2"
  )
  private String testingAMI;

  @CommandLine.Option(
      names = {"-c", "--ssh-cert-path"},
      description = "the path of ssh cert, default val is build/packages/sshkey.pem",
      defaultValue = "build/packages/sshkey.pem"
  )
  private String sshCertPath;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    ITask task = (ITask) Class.forName("com.amazon.aocagent.tasks." + taskName).newInstance();
    task.init(this.buildContext());

    try {
      task.execute();
    } catch (BaseException ex) {
      log.error(ex.getMessage());
      return 1;
    }
    return 0;
  }

  private Context buildContext() throws IOException {
    Context context = new Context();

    Stack stack = Stack.valueOf(this.stackName);
    context.setStack(stack);

    context.setLocalPackagesDir(this.localPackagesDir);

    // get aoc version from the current working directory: "local-packages/VERSION"
    String version =
        new String(
            Files.readAllBytes(Paths.get(this.localPackagesDir + "/VERSION")),
            StandardCharsets.UTF_8);
    context.setAgentVersion(version);

    context.setRegion(this.region);

    context.setTestingAMI(TestAMI.valueOf(this.testingAMI));

    context.setSshCertPath(this.sshCertPath);

    return context;
  }
}
