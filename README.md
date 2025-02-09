# Key-Based Locking

TODO

## For Developers

### How to Release

Required permissions on GitHub:
* Push to `main`
* Push to `release`
* Create releases in GitHub

1. Create a new release using the gitflow-maven-plugin:
   ```
   ./mvnw gitflow:release
   ```
   You will be prompted for a version number. This will update the `main` and `release` branches and create a tag in the form `v1.2.3`.

2. Create a release in GitHub from this tag.

3. The release will be built and deployed to Maven Central.
