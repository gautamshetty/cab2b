<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ page isErrorPage="true" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/failure.css" type="text/css">
</HEAD>
<BODY>
	<jsp:include page="header.jsp"/>
	<jsp:include page="leftpanel.jsp"/>
	<DIV id="content">
		<DIV class="titlebar">
			<DIV class="titlebarheader title">
				<DIV class="titlebarleftcurve">
					<DIV class="titlebarrightcurve">
						<DIV class="titlebarcollapsed" onClick="document.getElementById('contentbody').style.display=(document.getElementById('contentbody').style.display=='none'?'block':'none');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
							<bean:message key="title.unexpectederror"/>
						</DIV>						
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		<DIV class="error" id="contentbody">
			<IMG alt="<bean:message key="img.alt.error"/>" src="images/esclamation.jpg" align="middle">
			<bean:message key="error.unexpected"/>
		</DIV>
	</DIV>
	<jsp:include page="footer.jsp"/>
</BODY>
</HTML>