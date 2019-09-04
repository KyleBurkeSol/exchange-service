pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew assemble'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
        stage('Javadoc') {
            steps {
                sh './gradlew javadoc'
            }
        }
        stage('Deploy'){
            steps{
                withCredentials([[
                    $class              : 'UsernamePasswordMultiBinding',
                    credentialsId       : 'PCF_LOGIN',
                    usernameVariable    : 'USERNAME',
                    passwordVariable    : 'PASSWORD'
                    ]]){
                        sh 'cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD'
                        sh 'cf target -o solstice-org -s sshanthi_cnt'
                        sh 'cf push exchange-service'
                }
            }
        }
    }
}