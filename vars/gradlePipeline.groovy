import com.rajanpupa.jenkins.common.build.Gradle
import com.rajanpupa.jenkins.common.tests.Test
import com.rajanpupa.jenkins.common.tests.JUnit

def call(body){
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    echo "== Branch Name : ${env.BRANCH_NAME}"
	echo "== PR Target : ${env.CHANGE_TARGET}"

    // //skip if the branch name is not dev or master or PR
    if ( env.BRANCH_NAME != null && 
		 !"dev".equalsIgnoreCase(env.BRANCH_NAME) && 
		 !"master".equalsIgnoreCase(env.BRANCH_NAME) && 
		 !env.BRANCH_NAME.toUpperCase().startsWith("PR") 
	){                                  
	     echo " Skipping Build and Deploy for commit event of feature branch ${env.BRANCH_NAME} "
	     //return
	}

    // variable declaration
    Test testJunit;

    try {
        node() {
            // configurations
            // only keep 10 builds to prevent disk usage from growing out of control
            properties([
					buildDiscarder(
							logRotator(
									artifactDaysToKeepStr: '',
									artifactNumToKeepStr: '5',
									daysToKeepStr: '',
									numToKeepStr: '10',
							),
					)
			]);

            stage('Setup') {
                init {}
                gradle = new Gradle(this)
                testJunit = new JUnit(this)
            }
            stage('SCM Checkout') {
                echo "-- Checkout from Github "
                __gitCheckout{}
            }

            // TODO: REMOVE THIS: FOR TEST ONLY
            if("jenkins".equalsIgnoreCase(env.BRANCH_NAME)){
                    echo ("-- Calling publish to nexus")
                    stage('Publish to Nexus') {
                        __publishToNexus {projectName=config.projectName}
                    }

                    return;
            }

            if (!"master".equalsIgnoreCase(env.BRANCH_NAME)){
				// Event: ALL except MERGE TO MASTER

                // build
                stage('Build') {
                    __gradleBuild{}
                }

                // unit test
                stage('Publish JUnit Results') {
                    testJunit.test(new Gradle(this))
                }

                // sonar qube
                // stage("SonarQube"){
				// 	__gradleSonarQube{ skipStep=config.skipSonarQube }
				// }

                // unit integration test

                stage('Archive') {
                    __jenkinsArchive{}
                }

                if ("dev".equalsIgnoreCase(env.CHANGE_TARGET)) {
					// Event: PR TO DEV
					// stage("PI Test"){
					// 	__gradlePiTest{ skipStep=config.skipPiTest }
					// }
					
				} else if ( "dev".equalsIgnoreCase(env.BRANCH_NAME) ) {
					// Event: MERGE TO DEV

                    // deploy to Dev

                }else if ("master".equalsIgnoreCase(env.CHANGE_TARGET)) {
                    // PR TO MASTER

                    // deploy to Qat

                    // acceptance test

                    // publish to nexus
                    // stage('Publish to Nexus') {
					// 	__publishToNexus {projectName=config.projectName}
					// }

                } else{
                    // Event: merge to Master

                    // Pull from Nexus
                    // stage("Nexus get jar"){
                    //     __getJarFromNexus{projectName=config.projectName}
                    // }

                    // deploy to higher environments 

                    // deploy to production
                }
            
            }

            
            
            
        }
    }
    catch (error) {
        currentDescription = error.message
        echo "error = ${error.message}"
        throw error
    }
    finally {
        echo " Build finally completed!!!!! "
    }
}