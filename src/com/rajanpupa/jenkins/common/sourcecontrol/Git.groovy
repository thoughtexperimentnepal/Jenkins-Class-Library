package com.rajanpupa.jenkins.common.sourcecontrol;

class Git implements Serializable {
    protected def jenkins

    Git(jenkins) {
        this.jenkins = jenkins
    }

    void checkout() {
        jenkins.echo 'Starting Git pull'
        jenkins.checkout( jenkins.scm).each { k, v -> jenkins.env.setProperty(k,v) }
    }
}