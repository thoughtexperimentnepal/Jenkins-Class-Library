package com.rajanpupa.jenkins.common.tests

import com.rajanpupa.jenkins.common.build.BuildTool

interface Test {

    void test(BuildTool executor)

}