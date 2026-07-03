#!/usr/bin/env bash
# `mvn install` publica no repositório Maven local (~/.m2) exatamente como
# uma release real faria — depois um projeto separado consome via
# groupId:artifactId:version normal, sem caminho relativo ao repo.
set -e
cd "$(dirname "$0")/.."
mvn -q -DskipTests install

rm -rf /tmp/velix-install-test-java
mkdir -p /tmp/velix-install-test-java/src/main/java
cd /tmp/velix-install-test-java

VERSION=$(grep -m1 '<version>' /sdk/sdk-velix-java/pom.xml | sed 's/.*<version>\(.*\)<\/version>.*/\1/')

cat > pom.xml <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.velix.installtest</groupId>
  <artifactId>install-test</artifactId>
  <version>1.0</version>
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>com.velix</groupId>
      <artifactId>velix-sdk</artifactId>
      <version>$VERSION</version>
    </dependency>
  </dependencies>
</project>
EOF

cat > src/main/java/Main.java <<'EOF'
import com.velix.sdk.VelixClient;

public class Main {
  public static void main(String[] args) {
    VelixClient client = VelixClient.builder().apiUrl("http://localhost").apiKey("test").build();
    if (client.onboarding() == null) throw new RuntimeException("client.onboarding() null no pacote instalado");
    System.out.println("INSTALL_TEST:java:PASS: resolvido via repositório Maven local, client.onboarding() existe");
  }
}
EOF

mvn -q dependency:build-classpath -Dmdep.outputFile=/tmp/cp.txt
CP=$(cat /tmp/cp.txt)
javac -cp "$CP" -d target src/main/java/Main.java
java -cp "target:$CP" Main
