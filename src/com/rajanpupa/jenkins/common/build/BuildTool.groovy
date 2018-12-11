package com.rajanpupa.jenkins.common.build

interface BuildTool {
    Boolean build(String args)

    Boolean task(String args)

    Boolean taskExists(String taskName)
}