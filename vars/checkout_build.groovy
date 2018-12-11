import com.rajanpupa.jenkins.common.sourcecontrol.Git
import com.rajanpupa.jenkins.common.build.Gradle

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Git git
    Gradle gradle

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
                if ( !gradleBuild.build("clean build") ) {
                    currentDescription = ' :( Build failed :('
                    error currentDescription
                }else {
                    echo ' :) Build Passed :) :) '
                }
            }
        }
    }
    catch (error) {
        currentDescription = error.message
        echo error.message
        throw error
    }
    finally {
        echo " Build finally completed!!!!! "
    }
}