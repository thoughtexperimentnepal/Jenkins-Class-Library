import com.rajanpupa.jenkins.common.nexus.Nexus
import com.rajanpupa.jenkins.common.nexus.Semver

/*
 * Finds the latest version of the service in nexus
 * Bump the major version
 * Deploys the current build with the bumped version
 */
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    echo "==========================_publishToNexus.groovy======================================"
	echo "config = ${config}"
	
	// Get the versions from nexus
	// nexus = new Nexus("private_snapshot_repository", "private_release_repository", env.NEXUS_USERNAME, env.NEXUS_PASSWORD, config.projectName)
	// nexus.setJenkins(this)
	
	// echo "nexus object created"
	// lastRelease = nexus.getLastRelease(config.projectName)
	// echo "last release: ${lastRelease.toString()}"
	
	// Semver semver = new Semver(lastRelease.toString());
	
	// Determine the latest version
	
	// Do a major version increase
	// bumpedMajorVersion = semver.previewBumpMajor()
	// echo "=== bumpedMajorVersion= ${bumpedMajorVersion}"
	// env.BUMP_VERSION=bumpedMajorVersion
	// echo "=== env bumpedMajorVersion= ${env.BUMP_VERSION}"
	// return;
	
    // pass version to gradlew publish 
    // try {
    //     sh '''
    //       ./gradlew publish -PbuildVersion=$BUMP_VERSION
    //     '''
    // } catch (error) {
    //     echo 'Push to NEXUS Failed'
    //     echo("Publish to nexus failed with root cause : ${error}")
    //     currentBuild.result = 'UNSTABLE'
    // }
}