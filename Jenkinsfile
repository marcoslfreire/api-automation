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
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'

            allure([
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}