def buildJar() {
    echo "building the application..."
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'Docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t aliaymanmohammed/demo-app:jma-1.0 .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push aliaymanmohammed/demo-app:jma-1.0'
    }
}

def deployApp() {
    echo 'deploying the application...'
    def dockerCmd = 'docker run -p 8082:8080 -d aliaymanmohammed/demo-app:jma-1.0 '
    sshagent(['ubuntu-server-key']) {
        sh "ssh -o StrictHostKeyChecking=no ubuntu@54.93.120.75 ${dockerCmd}"
    }
}

return this
