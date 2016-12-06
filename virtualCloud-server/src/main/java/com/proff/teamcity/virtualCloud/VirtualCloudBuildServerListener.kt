package com.proff.teamcity.virtualCloud

import jetbrains.buildServer.clouds.InstanceStatus
import jetbrains.buildServer.serverSide.BuildServerAdapter
import jetbrains.buildServer.serverSide.SBuildAgent

class VirtualCloudBuildServerListener(val client: VirtualCloudClient) : BuildServerAdapter() {
    override fun agentRegistered(agent: SBuildAgent, currentlyRunningBuildId: Long) {
        super.agentRegistered(agent, currentlyRunningBuildId)
        if(!agent.isAuthorized)
            return
        val key: String? = agent.availableParameters[VirtualCloudConstants.CONFIG_KEY_PARAM_NAME] ?: return
        val image = client.myImages.firstOrNull { it.path == key } ?: return
        image.instance.start = agent.registrationTimestamp
        image.instance.state = InstanceStatus.RUNNING
    }

    /*override fun agentUnregistered(agent: SBuildAgent) {
        super.agentUnregistered(agent)
        val key: String? = agent.availableParameters[VirtualCloudConstants.CONFIG_KEY_PARAM_NAME] ?: return
        val image = client.myImages.firstOrNull { it.path == key } ?: return
        image.instance.state = InstanceStatus.STOPPED
    }*/
}