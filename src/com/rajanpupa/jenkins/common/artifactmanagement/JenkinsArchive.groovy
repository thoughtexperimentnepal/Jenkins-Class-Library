package com.rajanpupa.jenkins.common.artifactmanagement

class JenkinsArchive implements Archive {
    protected def jenkins

    JenkinsArchive(jenkins){
        this.jenkins = jenkins
    }

    void archive() {
        jenkins.archiveArtifacts artifacts: '**/*'
    }

}