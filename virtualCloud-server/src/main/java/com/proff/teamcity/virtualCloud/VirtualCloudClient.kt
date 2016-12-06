package com.proff.teamcity.virtualCloud

import com.proff.teamcity.virtualCloud.VirtualCloudConstants.Companion.CONFIG_KEY_PARAM_NAME
import jetbrains.buildServer.clouds.*
import jetbrains.buildServer.serverSide.AgentDescription
import jetbrains.buildServer.serverSide.BuildServerListener
import jetbrains.buildServer.serverSide.SBuildAgent
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.util.PropertiesUtil
import java.io.File
import java.util.*

class VirtualCloudClient(val server: SBuildServer, val state: CloudState, val parameters: CloudClientParameters) : CloudClientEx {
    val myImages = mutableListOf<VirtualCloudImage>()
    val listener = VirtualCloudBuildServerListener(this)

    init {
        val agents = parameters.getParameter(VirtualCloudConstants.AGENTS_PROFILE_SETTING)
        if (agents != null) {
            agents.split("\n")
                    .map { it.trim().split(";") }
                    .takeWhile { it.count() >= 2 }
                    .mapTo(myImages) { VirtualCloudImage(it[0], it[1], server) }
        }
        server.addListener(listener)
    }


    override fun findInstanceByAgent(agentDescription: AgentDescription): CloudInstance? {
        val result = myImages.firstOrNull { it.path == agentDescription.availableParameters[CONFIG_KEY_PARAM_NAME] }?.instance
        /*if (result != null && result.state == InstanceStatus.STOPPED) {
            result.state = InstanceStatus.RUNNING
            result.start = Date()
        }*/
        return result
    }

    override fun findImageById(imageId: String): VirtualCloudImage? {
        return myImages.firstOrNull { it.id == imageId }
    }

    override fun getErrorInfo(): CloudErrorInfo? {
        return null
    }

    override fun canStartNewInstance(image: CloudImage): Boolean {
        val img = (image as VirtualCloudImage)
        return img.instance.state == InstanceStatus.STOPPED
    }

    override fun isInitialized(): Boolean {
        return true
    }

    override fun generateAgentName(agentDescription: AgentDescription): String? {
        return null
    }

    override fun getImages(): Collection<CloudImage> {
        return myImages.toList()
    }

    override fun startNewInstance(image: CloudImage, data: CloudInstanceUserData): CloudInstance {
        val img = (image as VirtualCloudImage)
        val agent = server.buildAgentManager.getRegisteredAgents<SBuildAgent>(true).firstOrNull { it.availableParameters[CONFIG_KEY_PARAM_NAME] == img.path }

        //if (agent == null) {
            val myBaseDir = img.path
            val inConfigFile: File = if (myBaseDir.endsWith("buildAgent.properties")) File(myBaseDir) else File(File(myBaseDir, "conf"), "buildAgent.properties")
            val outConfigFile = inConfigFile
            val config = PropertiesUtil.loadProperties(inConfigFile)
            data.setAgentRemovePolicy(CloudConstants.AgentRemovePolicyValue.Unauthorize)
            config.put(CONFIG_KEY_PARAM_NAME, img.path)
            config.put("authorizationToken", data.authToken)
            for ((key, value) in data.customAgentConfigurationParameters) {
                config.put(key, value)
            }
            PropertiesUtil.storeProperties(config, outConfigFile, null)
        /*} else {
            agent.setAuthorized(true, null, "Authorized by virtual cloud")
        }*/

        img.instance.state = InstanceStatus.RUNNING
        img.instance.start = Date()
        return img.instance
    }

    override fun terminateInstance(instance: CloudInstance) {
        val ins = (instance as VirtualCloudInstance)
        val agent = server.buildAgentManager.getRegisteredAgents<SBuildAgent>(false).firstOrNull { it.availableParameters[CONFIG_KEY_PARAM_NAME] == ins.cloudImage.path }
        agent?.setAuthorized(false, null, "Unauthorized by virtual cloud")
        ins.state = InstanceStatus.STOPPED
    }

    override fun restartInstance(instance: CloudInstance) {
    }

    override fun dispose() {
        server.removeListener(listener)
    }
}