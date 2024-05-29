pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ShinSeoY/myBatch.git'
            }

            post {
                success {
                    sh 'echo "Cloned Repository success"'
                }
                failure {
                    sh 'echo "Cloned Repository failed"'
                }
            }
        }

        stage('Build') {
            steps {
                echo '1. start build project step'
                sh '''
                pwd
                chmod +x ./gradlew
                ./gradlew clean bootJar
                '''
            }
            post {
                success {
                    echo 'build project success'
                }

                failure {
                    echo 'build project failed'
                }
            }
        }
        stage('Test') {
            steps {
                echo '2. start test project step'
            }
        }
        stage('Docker Rm') {
            steps {
                sh 'echo "3. start remove previous docker step"'
                sh """
                """
            }

            post {
                success {
                    sh 'echo "remove docker img success"'
                }
                failure {
                    sh 'echo "remove docker img failed"'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                pwd
                '''
            }
            post {
                success {
                    sh 'echo "docker build success"'
                }
                failure {
                    sh 'echo "docker build failed"'
                }
            }
        }
    }
}