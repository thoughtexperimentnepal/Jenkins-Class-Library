import com.rajanpupa.jenkins.common.artifactmanagement.Archive
import com.rajanpupa.jenkins.common.artifactmanagement.JenkinsArchive

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    Archive jenkinsArchive = new JenkinsArchive(this)

    jenkinsArchive.archive()

}