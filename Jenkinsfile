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
                    passwordVariable    : 'PASSWORD',
                    organization        : 'solstice-org'
                    ]]){
                        sh '/usr/local/bin/cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD'
                        sh '/usr/local/bin/cf push'
                }
            }
        }
    }
}