pipeline
{
    agent:any
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    stages {
          stage("Cleanup Workspace"){
              steps {
                  cleanWs()
              }

          }

          stage("Checkout from SCM"){
              steps {
                  git branch: 'main', credentialsId: 'Github', url: 'https://github.com/dmancloud/complete-prodcution-e2e-pipeline'
              }

          }
    }
}
