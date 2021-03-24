#!/usr/bin/groovy

def showMSTeamsError(String credentialsId) {
    showMSTeamsMessage('failed', '#FF0000', 'Failure', credentialsId)
}

def showMSTeamsAlert(String message, String credentialsId) {
    showMSTeamsMessage(message, '#FFD700', 'Alert', credentialsId)
}

def showMSTeamsNotification(String message, String credentialsId) {
    showMSTeamsMessage(message, null, null, credentialsId)
}

def showMSTeamsMessage(String message, String color, String status, String credentialsId) {
    def serviceName = "${env.JOB_NAME}".tokenize('/')[1]
    def branchName = "${env.JOB_NAME}".tokenize('/').last()
    def consoleUrl = "${env.JENKINS_URL}"
    "${env.JOB_NAME}".tokenize('/').each { item -> consoleUrl = consoleUrl + "job/" + item + "/" }
    consoleUrl = consoleUrl + "${BUILD_NUMBER}/console"

    withCredentials([string(credentialsId: credentialsId, variable: 'HOOK')]) {
        if (color) {
            office365ConnectorSend color: color, message: "${serviceName}/${branchName} build [#${BUILD_NUMBER}](${consoleUrl}) " + message, status: status, webhookUrl: "$HOOK"
        } else {
            office365ConnectorSend message: "${serviceName}/${branchName} build [#${BUILD_NUMBER}](${consoleUrl}) " + message, webhookUrl: "$HOOK"
        }
    }
}
