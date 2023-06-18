mvn -N install
mvn clean install -Dmaven.test.skip=true -f swiftboot-utils/pom.xml
mvn clean install -Dmaven.test.skip=true -f swiftboot-collections/pom.xml
mvn clean install -Dmaven.test.skip=true -f swiftboot-data/pom.xml
mvn jar:test-jar -f swiftboot-data/pom.xml
mvn install:install-file -Dfile=target/swiftboot-data-2.4-tests.jar -DgroupId=com.github.swiftech -DartifactId=swiftboot-data -Dclassifier=tests -Dpackaging=test-jar -f swiftboot-data/pom.xml
mvn clean install -Dmaven.test.skip=true