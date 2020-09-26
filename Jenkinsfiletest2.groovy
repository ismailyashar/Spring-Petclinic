properties([
    parameters([
        string(defaultValue: '', description: 'Input node IP', name: 'SSHNODE', trim: true)
        ])
    ])

node {
    withCredentials([sshUserPrivateKey(credentialsId: 'Jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
    
        stage("Install git") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install git -y"
       
        }       
        stage("Initialize") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install epel-release -y "
        }
        stage("Install java") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } yum install java-1.8.0-openjdk-devel -y"
            
        }
        stage("Clone Repo") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } git clone https://github.com/spring-projects/spring-petclinic.git "
        }
        
        stage("Install pip3") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  ./mvnw package "
            
        }
        stage("Built Application") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  mv target/*.jar target/app.jar "
            
         stage("Built Application") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  java -jar spring-petclinic/target/app.jar"
            
        }
        }
        
    }
}

