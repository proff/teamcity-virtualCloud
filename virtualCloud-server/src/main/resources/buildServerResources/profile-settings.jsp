
<%@ page import="com.proff.teamcity.virtualCloud.VirtualCloudConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="paramName" value="<%=VirtualCloudConstants.Companion.getAGENTS_PROFILE_SETTING()%>"/>

<tr>
    <th><label for="${paramName}">Agent images:</label></th>
    <td>
        <props:multilineProperty name="${paramName}" className="longField" linkTitle="Agent images to run" cols="55" rows="5" expanded="${true}"/>
        <span class="smallNote">
      List of agent names and paths to config, each on new line.
      <br/>
      format: <strong>&lt;Agent name&gt;;&lt;network path&gt;</strong>
      <br/>
      Network path should be to agent root folder or buildAgent.properties file. buildAgent.properties should be writable to server
    </span>
    </td>
</tr>
