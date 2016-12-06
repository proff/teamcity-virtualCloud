package com.proff.teamcity.virtualCloud

import jetbrains.buildServer.clouds.*
import jetbrains.buildServer.serverSide.AgentDescription
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.PluginDescriptor

class VirtualCloudClientFactory(cloudRegistrar: CloudRegistrar, var pluginDescriptor: PluginDescriptor, val server: SBuildServer) : CloudClientFactory {

    init {
        cloudRegistrar.registerCloudFactory(this)
    }

    override fun getInitialParameterValues(): MutableMap<String, String> {
        return mutableMapOf()
    }

    override fun canBeAgentOfType(agentDescription: AgentDescription): Boolean {
        return true
    }

    override fun getDisplayName(): String {
        return "Virtual cloud"
    }

    override fun getEditProfileUrl(): String? {
        return pluginDescriptor.getPluginResourcesPath("profile-settings.jsp")
    }

    override fun getPropertiesProcessor(): PropertiesProcessor {
        return PropertiesProcessor { listOf() }
    }

    override fun createNewClient(state: CloudState, parameters: CloudClientParameters): CloudClientEx {
        return VirtualCloudClient(server, state, parameters)
    }

    override fun getCloudCode(): String {
        return "virt"
    }

}