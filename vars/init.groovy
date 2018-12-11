// Adds git, nodeJS, /usr/local/bin to env.PATH
// This needs to run at the top level to be shared properly (not within other withEnv or such)
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    try {
        env.CF_TOOL = tool 'cf_cli_6_28_0'
        env.PATH = "${env.PATH}:${env.CF_TOOL}/bin"
        echo '=== Providing execute permission to gradlew ==='
        this.sh "chmod +x gradlew"
    }
    catch (all) {
        echo "No CF custom tool installed"
    }

    //env.GIT_HOME = tool name: 'Git_2_17_0', type: 'git'
    env.PATH = "${env.PATH}:/usr/local/bin"
    env.HTTPS_PROXY=""
    env.HTTP_PROXY=""
    env.https_proxy=""
    env.http_proxy=""
    env.NPM_CONFIG_PROXY=""
    env.NPM_CONFIG_HTTPS_PROXY=""
    env.NO_PROXY="localhost"
    env.CF_HOME = "${env.WORKSPACE}"
}