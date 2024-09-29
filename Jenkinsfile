pipeline
{
    agent any
    tools {
        jdk 'Java17'
        maven 'Maven'
    }

    environment {
        APP_NAME = "e2e-pipeline-CI_CD"
        RELEASE = "1.0.0"
        DOCKER_USER = "mohumadkhald"
        DOCKER_PASS = 'Dockerhub'
        IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
//         JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")

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

          stage("Quality Gate") {
              steps {
                  script {
                      waitForQualityGate abortPipeline: false, credentialsId: 'jenkins-sonarqube-token'
                  }
              }
          }

          stage("Build & Push Docker Image") {
              steps {
                  script {
                      docker.withRegistry('',DOCKER_PASS) {
                          docker_image = docker.build "${IMAGE_NAME}"
                      }

                      docker.withRegistry('',DOCKER_PASS) {
                          docker_image.push("${IMAGE_TAG}")
                          docker_image.push('latest')
                      }
                  }
              }
          }

    }
}
