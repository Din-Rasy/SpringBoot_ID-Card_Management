pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'test'
        BUILD_LOG = 'jenkins-build-output.txt'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    writeFile file: env.BUILD_LOG, text: "# Jenkins Build Output\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\nBranch: main\nStarted: ${new Date()}\n\n"
                }
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    echo "=== Checkout completed ===" | tee -a "$BUILD_LOG"
                    git status --short | tee -a "$BUILD_LOG"
                    git log --oneline -1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Verify Environment') {
            steps {
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    {
                      echo ""
                      echo "=== Verify Environment ==="
                      echo "Workspace: $WORKSPACE"
                      echo "Java version:"
                      java -version 2>&1
                      echo "Maven version:"
                      mvn -version
                      echo "Git version:"
                      git --version
                      echo "Ansible version:"
                      ansible --version
                    } 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Build with Maven') {
            steps {
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    {
                      echo ""
                      echo "=== Build with Maven ==="
                      mvn clean package -DskipTests
                    } 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Test with SQLite Test Database') {
            steps {
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    {
                      echo ""
                      echo "=== Test with SQLite Test Database ==="
                      echo "Command: mvn clean test -Dspring.profiles.active=test"
                      echo "SQLite test config: src/test/resources/application-test.properties"
                      grep -n "sqlite" src/test/resources/application-test.properties || true
                      mvn clean test -Dspring.profiles.active=test
                    } 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Run Ansible Playbook Deploy') {
            steps {
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    {
                      echo ""
                      echo "=== Run Ansible Playbook Deploy ==="
                      echo "Command: ansible-playbook -i inventory.ini playbook.yml"
                      ansible-playbook -i inventory.ini playbook.yml
                    } 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Archive Outputs') {
            steps {
                sh """#!/usr/bin/env bash
                    set -euo pipefail
                    {
                      echo ""
                      echo "=== Archive Outputs ==="
                      ls -lh "$BUILD_LOG" || true
                      ls -lh target/*.jar 2>/dev/null || true
                      ls -lh target/surefire-reports 2>/dev/null || true
                    } 2>&1 | tee -a "$BUILD_LOG"
                """
                archiveArtifacts artifacts: 'jenkins-build-output.txt,target/*.jar,target/surefire-reports/*.txt,ansible-output.txt,ANSIBLE_SUBMISSION.md,backups/*.sql', allowEmptyArchive: true
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            }
        }
    }

    post {
        success {
            echo 'Build, SQLite tests, and Ansible deployment completed successfully.'
        }

        failure {
            echo 'Build or test failed. Attempting to send email notification.'
            script {
                try {
                    emailext(
                        subject: "Jenkins Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        body: """
Jenkins build failed.

Job: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}
Build URL: ${env.BUILD_URL}
Branch: main

Please check the console output and archived jenkins-build-output.txt.
""",
                        to: '',
                        cc: 'srengty@gmail.com',
                        recipientProviders: [developers(), culprits()]
                    )
                } catch (err) {
                    echo "Email notification could not be sent. Install/configure Email Extension Plugin and SMTP settings. Error: ${err}"
                }
            }
        }

        always {
            archiveArtifacts artifacts: 'jenkins-build-output.txt,target/surefire-reports/*.txt,target/surefire-reports/*.xml', allowEmptyArchive: true
        }
    }
}
