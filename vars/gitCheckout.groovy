import com.rajanpupa.jenkins.common.sourcecontrol.Git;

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Git git

    try {
        node() {
            stage('Setup') {
                init {}
                git = new Git(this)
            }
            stage('SCM Checkout') {
                git.checkout()
                echo "=== Git Checkout Completed ==="
                echo '=== Providing execute permission to gradlew ==='
                this.sh "chmod +x gradlew"
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