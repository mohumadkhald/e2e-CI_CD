pipeline
{
    agent any
    tools {
        jdk 'Java17'
        maven 'Maven'
    }
    stages {
          stage("Cleanup Workspace"){
              steps {
                  cleanWs()
              }

          }

          stage("Checkout from SCM"){
              steps {
                  git branch: 'main', credentialsId: 'Github', url: 'https://github.com/mohumadkhald/e2e-CI_CD'
              }

          }
    }
}
