package com.proff.teamcity.virtualCloud

import jetbrains.buildServer.clouds.CloudErrorInfo
import jetbrains.buildServer.clouds.CloudImage
import jetbrains.buildServer.clouds.CloudInstance
import jetbrains.buildServer.serverSide.SBuildServer

class VirtualCloudImage(val imageName: String, val path: String, val server: SBuildServer) : CloudImage {
    var instance: VirtualCloudInstance = VirtualCloudInstance(this, name, server)
    override fun getAgentPoolId(): Int? {
        return null
    }

    override fun getName(): String {
        return imageName
    }

    override fun getId(): String {
        return imageName
    }

    override fun findInstanceById(name: String): CloudInstance? {
        if (instance.name == name) return instance
        return null
    }

    override fun getErrorInfo(): CloudErrorInfo? {
        return null
    }

    override fun getInstances(): List<VirtualCloudInstance> {
        val result = listOf(instance)
        return result
    }
}