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

          stage("Build App"){
              steps {
                  sh 'mvn clean package'
              }
          }

          stage("Test App"){
              steps {
                  sh 'mvn test'
              }
          }
          stage("Sonarqube Analysis") {
              steps {
                  script {
                      withSonarQubeEnv(credentialsId: 'sonarqube-token') {
                          sh "mvn sonar:sonar"
                      }
                  }
              }
          }
    }
}
