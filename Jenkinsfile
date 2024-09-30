pipeline
{
    agent any
    tools {
        jdk 'Java17'
        maven 'Maven'
    }
    parameters {
      booleanParam(name: "executeTest", defaultValue: true, description: "Default will Make Test")
    }
    environment {
        APP_NAME = "e2e-pipeline-ci_cd"
        RELEASE = "1.0.0"
        DOCKER_USER = "mohumadkhald"
        DOCKER_PASS = 'Dockerhub'
        IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
        IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}"
        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")

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
             when {
               expression {
                 params.executeTest
               }
             }
              steps {
                  sh 'mvn test'
              }
          }
          stage("Sonarqube Analysis") {
              when {
                expression {
                  params.executeTest
                }
              }
              steps {
                  script {
                      withSonarQubeEnv(credentialsId: 'sonarqube-token') {
                          sh "mvn sonar:sonar"
                      }
                  }
              }
          }

          stage("Quality Gate") {
              when {
                expression {
                  params.executeTest
                }
              }
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

//           stage("Trivy Scan") {
//               steps {
//                   script {
//                     sh ('docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${IMAGE_NAME} --no-progress --scanners vuln  --exit-code 0 --severity HIGH,CRITICAL --format table')
//                   }
//               }
//
//         }

        stage ('Cleanup Artifacts') {
            steps {
                script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker rmi ${IMAGE_NAME}:latest"
                }
            }
        }


        stage("Trigger CD Pipeline") {
            steps {
                script {
                    sh "curl -v -k --user jenkins:${JENKINS_API_TOKEN} -X POST -H 'cache-control: no-cache' -H 'content-type: application/x-www-form-urlencoded' --data 'IMAGE_TAG=${IMAGE_TAG}' 'http://54.146.248.142/job/gitops-complete-pipeline/buildWithParameters?token=gitops-token'"
                }
            }

        }

    }
}
