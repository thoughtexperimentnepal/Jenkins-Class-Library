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
                gitUtil = new Git(this)
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