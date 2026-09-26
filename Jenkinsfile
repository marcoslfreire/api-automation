pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {
        stage('Testes') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}