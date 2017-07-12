[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![download](https://api.bintray.com/packages/newnewcoder/generic/spring-batch-mssql-bulkcopy/images/download.svg) ](https://bintray.com/newnewcoder/generic/spring-batch-mssql-bulkcopy/_latestVersion)

### What's this
This lib provides spring-batch **tasklet** and **writer** for MSSQL **bulk copy**.

### How to use

~~~groovy
repositories {
    jCenter()
}

dependencies {
    compile "com.github.newnewcoder:springbatch-mssql-bulkcopy:<latest version>"
    // only support mssql-jdbc, not jTds ...etc.
}
~~~

**Tasklet** or **writter** usage see [test case](https://github.com/newnewcoder/spring-batch-mssql-bulkcopy/blob/master/src/test/java/com/github/newnewcoder/batch/BulkCopyTest.java)

### How to build project locally

First, install **docker** and **docker-compose**, then run below:

~~~groovy
./gradlew build
~~~