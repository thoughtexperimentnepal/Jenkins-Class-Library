import com.rajanpupa.jenkins.common.sourcecontrol.Git
import com.rajanpupa.jenkins.common.build.Gradle
import com.rajanpupa.jenkins.common.tests.Test

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Git git
    Gradle gradle
    Test testJunit

    try {
        node() {
            stage('Setup') {
                init {}
                git = new Git(this)
                gradle = new Gradle(this)
            }
            stage('SCM Checkout') {
                git.checkout()
                echo "=== Git Checkout Completed ==="
            }
            stage('Build') {
                if ( !gradle.build("clean build") ) {
                    currentDescription = ' :( Build failed :('
                    error currentDescription
                }else {
                    echo ' :) Build Passed :) :) '
                }
            }
            stage('Publish JUnit Results') {
                testJunit.test(new Gradle(this))
            }
            stage('Archive') {
                jenkinsArchive.archive()
            }
        }
    }
    catch (error) {
        currentDescription = error.message
        echo "error = ${error.message}"
        throw error
    }
    finally {
        echo " Build finally completed!!!!! "
    }
}