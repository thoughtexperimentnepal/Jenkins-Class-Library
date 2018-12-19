import com.rajanpupa.jenkins.common.build.Gradle
import com.rajanpupa.jenkins.common.tests.Test
import com.rajanpupa.jenkins.common.tests.JUnit

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Test testJunit

    try {
        node() {
            stage('Setup') {
                init {}
                gradle = new Gradle(this)
                testJunit = new JUnit(this)
            }
            stage('SCM Checkout') {
                __gitCheckout{}
            }
            stage('Build') {
                __gradleBuild{}
            }
            stage('Publish JUnit Results') {
                testJunit.test(new Gradle(this))
            }
            stage('Archive') {
                __jenkinsArchive{}
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