package com.proff.teamcity.virtualCloud

import jetbrains.buildServer.clouds.CloudErrorInfo
import jetbrains.buildServer.clouds.CloudImage
import jetbrains.buildServer.clouds.CloudInstance
import jetbrains.buildServer.clouds.InstanceStatus
import jetbrains.buildServer.serverSide.AgentDescription
import jetbrains.buildServer.serverSide.BuildServerListener
import jetbrains.buildServer.serverSide.SBuildAgent
import jetbrains.buildServer.serverSide.SBuildServer
import java.io.File
import java.util.*

class VirtualCloudInstance(val cloudImage: VirtualCloudImage, val myName: String, val server: SBuildServer) : CloudInstance {
    var start = Date()
    var state: InstanceStatus = InstanceStatus.STOPPED

    override fun toString(): String {
        return "name:${cloudImage.name} state: $state, start: $start"
    }

    init {
        val agent = server.buildAgentManager.getRegisteredAgents<SBuildAgent>(false).firstOrNull { it.availableParameters[VirtualCloudConstants.CONFIG_KEY_PARAM_NAME] == cloudImage.path }
        if (agent != null) {
            state = InstanceStatus.RUNNING
            start = agent.registrationTimestamp
        }
    }

    override fun getStatus(): InstanceStatus {
        return state
    }

    override fun getName(): String {
        return myName
    }

    override fun getStartedTime(): Date {
        return start
    }

    override fun getImage(): CloudImage {
        return cloudImage
    }

    override fun getNetworkIdentity(): String? {
        return "cloud.emulator.$imageId.$myName"
    }

    override fun getErrorInfo(): CloudErrorInfo? {
        return null
    }

    override fun getInstanceId(): String {
        return myName
    }

    override fun getImageId(): String {
        return cloudImage.id
    }

    override fun containsAgent(agentDescription: AgentDescription): Boolean {
        val agent = server.buildAgentManager.getRegisteredAgents<SBuildAgent>().firstOrNull { it.availableParameters[VirtualCloudConstants.CONFIG_KEY_PARAM_NAME] == cloudImage.path }
        return agent != null
    }
}