package com.amazon.aocagent.enums;

import java.util.Arrays;
import lombok.Getter;


@Getter
public enum S3Package {
  AMAZON_LINUX_AMD64_RPM(
      SupportedOSDistribution.AMAZON_LINUX,
      LocalPackage.LINUX_AMD64_RPM),
  AMAZON_LINUX_ARM64_RPM(
      SupportedOSDistribution.AMAZON_LINUX,
      LocalPackage.LINUX_ARM64_RPM),
  REDHAT_AMD64_RPM(
      SupportedOSDistribution.REDHAT,
      LocalPackage.LINUX_AMD64_RPM),
  REDHAT_ARM64_RPM(
      SupportedOSDistribution.REDHAT,
      LocalPackage.LINUX_ARM64_RPM),
  SUSE_AMD64_RPM(
      SupportedOSDistribution.SUSE,
      LocalPackage.LINUX_AMD64_RPM),
  SUSE_ARM64_RPM(
      SupportedOSDistribution.SUSE,
      LocalPackage.LINUX_ARM64_RPM),
  CENTOS_AMD64_RPM(
      SupportedOSDistribution.CENTOS,
      LocalPackage.LINUX_AMD64_RPM),
  CENTOS_ARM64_RPM(
      SupportedOSDistribution.CENTOS,
      LocalPackage.LINUX_ARM64_RPM),
  UBUNTU_AMD64_DEB(
      SupportedOSDistribution.UBUNTU,
      LocalPackage.DEBIAN_AMD64_DEB
  ),
  UBUNTU_ARM64_DEB(
      SupportedOSDistribution.UBUNTU,
      LocalPackage.DEBIAN_ARM64_DEB
  ),
  DEBIAN_AMD64_DEB(
      SupportedOSDistribution.DEBIAN,
      LocalPackage.DEBIAN_AMD64_DEB
  ),
  DEBIAN_ARM64_DEB(
      SupportedOSDistribution.DEBIAN,
      LocalPackage.DEBIAN_ARM64_DEB
  ),
  WINDOWS_AMD64_MSI(
      SupportedOSDistribution.WINDOWS,
      LocalPackage.WINDOWS_AMD64_MSI
  ),
  ;

  private SupportedOSDistribution supportedOSDistribution;
  private LocalPackage localPackage;

  S3Package(
      SupportedOSDistribution supportedOSDistribution,
      LocalPackage localPackage) {
    this.supportedOSDistribution = supportedOSDistribution;
    this.localPackage = localPackage;
  }

  /**
   * getS3Key generates the S3Key for the packages.
   * @param packageVersion is used to construct the S3 Key
   * @return the S3 key of the package
   */
  public String getS3Key(String packageVersion) {
    return String.join(
        "/",
        Arrays.asList(
            supportedOSDistribution.name().toLowerCase(),
            this.localPackage.getArchitecture().name().toLowerCase(),
            packageVersion,
            this.getPackageName()));
  }

  /**
   * getPackageName return the ot collector install package name.
   * @return packageName
   */
  public String getPackageName() {
    return GenericConstants.PACKAGE_NAME_PREFIX.getVal() + localPackage.getPackageType().name().toLowerCase();
  }
}
