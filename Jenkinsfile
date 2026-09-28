pipeline {
    agent any

    tools {
        maven 'Maven3'
        allure 'Allure'
    }

    stages {

        stage('Testes') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Relatorio Allure') {
            steps {
                allure([
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}