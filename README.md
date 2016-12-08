# TeamCity Virtual Cloud Plugin

TeamCity Virtual Cloud plugin provides support of virtual cloud (authorize/unauthorize real agents instead of run/stop cloud machine).


Can be used for:
* preview teamcity cloud integration without real cloud
* authorize agent on demand

# Compatibility

The plugin is compatible with [TeamCity](https://www.jetbrains.com/teamcity/download/) 10.0.x and greater.

# Requirements
configuration connected to virtual cloud agents should be writable for server 

# Build

execute *mvn package* in root folder

# How it works
1. Admin connects *Virtual Cloud* in *Agent Cloud* tab. In *Agent images* property add paths to configuration files of agents he want to connect to *Virtual Cloud*. This agent should be connected (authorized or not authorized)
2. Plugin returns to teamcity new virtual image with name of this agent
3. TeamCity starts that image to check of available properties
4. Plugin changes config of the agent (write instance hash to property *teamcity.cloud.instance.hash*)
5. Agent reconnects to TeamCity with this property value and TemCity treat it as cloud instance (authorize automatically as virtual agent).
6. After specific idle time TeamCity stops instance and plugin just unauthorize agent
7. When compatible build starts and all compatible authorized agents are busy, then TeamCity starts that instance and go to step 4