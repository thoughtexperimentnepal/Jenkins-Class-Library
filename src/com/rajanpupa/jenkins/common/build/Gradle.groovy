package com.rajanpupa.jenkins.common.build

class Gradle implements BuildTool {
    protected def jenkins

    Gradle(jenkins){
        this.jenkins = jenkins
    }

    Boolean build(String args){
        try {
            jenkins.echo 'Starting Gradle Build'
            jenkins.sh "./gradlew --no-daemon ${args} --parallel"

            return true
        }
        catch (ignored) {
            return false
        }
    }


    Boolean task(String args){
        return this.build(args)
    }

    Boolean taskExists(String taskName){
        String gradleTasks = jenkins.sh(script: './gradlew tasks --all', returnStdout: true).trim()
        return gradleTasks.contains(taskName)
    }

}