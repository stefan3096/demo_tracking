pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/stefan3096/demo_tracking.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t demo-tracking:latest .'
            }
        }

        stage('Deploy Container') {
            steps {
                sh '''
                  docker stop demo-tracking || true
                  docker rm demo-tracking || true
                  docker run -d --name demo-tracking -p 8087:8080 demo-tracking:latest
                '''
            }
        }
    }
}