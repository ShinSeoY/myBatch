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
                 // EC2 인스턴스에서 Jenkins 컨테이너로 my-exchange용 docker compose.yml 파일 복사 (jenkins용 docker-compose.yml 파일에 host_home/home/ubuntu와 호스트(ec2)의 폴더 volumes 설정 해놈)
                sh """
                docker cp /host_home/myExchange2/docker-compose.yml jenkins:/var/jenkins_home/workspace/docker-compose.yml
                docker cp /host_home/myExchange2/.env jenkins:/var/jenkins_home/workspace/.env
                cd /var/jenkins_home/workspace
                docker-compose down
                """
            }

            post {
                success {
                    sh 'echo "remove docker success"'
                }
                failure {
                    sh 'echo "remove docker failed"'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                pwd
                cd /var/jenkins_home/workspace
                docker-compose up -d --build
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