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
            }
        }
    }
    catch (error) {
        currentDescription = error.message
        echo 'error = ${error.message}'
        throw error
    }
    finally {
        echo " Build finally completed!!!!! "
    }
}