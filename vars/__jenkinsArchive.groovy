import com.rajanpupa.jenkins.common.sourcecontrol.Git;

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Git git = new Git(this)

    git.checkout()

}