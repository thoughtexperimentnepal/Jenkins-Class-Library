package com.rajanpupa.jenkins.common.tests

import com.rajanpupa.jenkins.common.build.BuildTool

class JUnit implements Test {
    protected def jenkins
    BuildTool executor
    protected static String check = 'check'

    JUnit(jenkins) {
        this.jenkins = jenkins
    }

    void test(BuildTool executor) {
        this.executor = executor
        if(executor.taskExists(check)){
            try {
                executor.task(check)
            } catch (e) {
                jenkins.echo 'JUnit Tests Failed'
                jenkins.currentBuild.result = 'FAILURE'
                throw e
            } finally {
                jenkins.junit '**/build/test-results/test/*.xml'
            }
        } else {
            jenkins.echo 'No check tests to execute'
        }

    }

}