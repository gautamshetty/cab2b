<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<LINK rel="shortcut icon" href="../images/favicon.ico">
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/home.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/multiselect.js"></SCRIPT>
<SCRIPT language="JavaScript">
 function setPage()
{
  <html:messages id="successUserLogout" message="true">
	alert('<bean:message key="success.user.logout"/>');
  </html:messages>
  <logic:present name="errorSessionTimeout">
	alert('<bean:message key="alert.sessiontimeout"/>');
  </logic:present>
  <logic:present name="invalidRequest">
	alert('<bean:message key="alert.invalidRequest"/>');
  </logic:present>
  <logic:notPresent name="modelGroupDVOList">
	document.forms[0].action = 'Home.do';
    document.forms[0].submit();
  </logic:notPresent>
  setSelection(document.getElementsByName('modelGroups'));
   if(selectedItemsCount>0)
  {
    processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + document.getElementsByName('modelGroups')[singleSelectIndex].value, 'savedsearchespanelbody');
	if(selectedItemsCount==0)
    document.getElementById('savedsearchespanelbody').innerHTML = '';	
  }
  <html:messages id="serviceInstancesNotConfigured">  
  	if(confirm('<bean:message key="message.serviceinstancesnotconfigured"/>'))
   {
     validateDataTypeSelection();
   }
  </html:messages>  
}

 function validateDataTypeSelection()
{
   if(selectedItemsCount>0)
  {
    document.forms[0].action='DisplayServiceInstances.do';
    document.forms[0].submit()
  }
   else
  {
    alert('<bean:message key="alert.selectdatatype"/>');
  }
}

 function showSavedSearches(chkBoxObj, signInRequired)
{
   if(signInRequired)
  {
	chkBoxObj.checked = false;
	 if(confirm('<bean:message key="confirmation.signinrequired"/>'))
	{
	  document.location = 'Login.do';
	}
    return;
  }
   else
  {
	if(selectedItemsCount==1)
	document.getElementsByName('modelGroups')[singleSelectIndex].checked = false;
  }
  setSelection(document.getElementsByName('modelGroups'));
  processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + (chkBoxObj.checked?chkBoxObj.value:'null'), 'savedsearchespanelbody');
}

 function setKeywordSearchTextBox(textBoxObj, keywordSearchExampleText, option)
{  
   if(option=='focus')
  {
    if(keywordSearchExampleText=='')
    keywordSearchExampleText = textBoxObj.value;
    if(textBoxObj.value==keywordSearchExampleText)
    textBoxObj.value = '';
    textBoxObj.className = 'textbox'
  }
   else
  {
	if(textBoxObj.value=='')
	textBoxObj.value = keywordSearchExampleText;
	if(textBoxObj.value==keywordSearchExampleText)
	textBoxObj.className = 'textbox exampleValue';
  }
}
</SCRIPT>
<BODY onLoad="setPage()">
	<FORM method="post" action="KeywordSearch.do" onsubmit="return checkEmptyTextFileld('keyword', keywordSearchExample.value, true, '<bean:message key="error.keywordsearch.empty"/>')">
		<jsp:include page="header.jsp"/>
		<jsp:include page="leftpanel.jsp"/>				
		<DIV id="content">
			<DIV id="toppanel">
				<DIV class="label">
					<bean:message key="label.searchdatatype"/>
				</DIV>
				<DIV>
					<DIV class="myselect" style="float:left" onmouseover="setDropDown(1);" onmouseout="setDropDown(0);">		
						<DIV class="myselectbox" onclick="setDropDown(1)">
							<DIV id="selectshow" class="selectshow"><bean:message key="select.datatype"/></DIV>
							<INPUT type="hidden" name="selectinfo" id="selectinfo"/>							
						</DIV>
						<DIV class="myselectboxitems" id="myselectboxitems">
							<logic:present name="modelGroupDVOList">							
								<logic:iterate name="modelGroupDVOList" id="modelGroupDVO" type="edu.wustl.cab2bwebapp.dvo.ModelGroupDVO" indexId="index">
									<DIV class="myselectboxitem">
										<INPUT type="checkbox" name="modelGroups" id="<bean:write name="modelGroupDVO" property="modelGroupName"/>" value="<bean:write name="modelGroupDVO" property="modelGroupName"/>" onClick="showSavedSearches(this, <bean:write name="modelGroupDVO" property="secured"/> <logic:notPresent name="userName">&& true</logic:notPresent><logic:present name="userName">&& false</logic:present>)" <logic:equal name="modelGroupDVO" property="selected" value="true">checked</logic:equal>>
										<LABEL><bean:write name="modelGroupDVO" property="modelGroupName"/><logic:equal name="modelGroupDVO" property="secured" value="true">&nbsp;<IMG src="images/lock.gif" alt="<bean:message key="img.alt.secureservice"/>"></logic:equal></LABEL>										
									</DIV>
								</logic:iterate>
							</logic:present>						
						</DIV>						
					</DIV>		
					<DIV>
						<A href="#this" class="link" onClick="validateDataTypeSelection()"><bean:message key="link.databasestosearch"/></A>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="keywordsearchpanel">
				<DIV class="label"><bean:message key="label.keywordsearch"/></DIV> <INPUT type="text" class="textbox examplevalue" name="keyword" value="<bean:message key="textbox.keywordsearch.examplevalue"/>" onFocus="setKeywordSearchTextBox(this, keywordSearchExample.value, 'focus')" onBlur="setKeywordSearchTextBox(this, keywordSearchExample.value, 'blur')"><INPUT type="hidden" name="keywordSearchExample" value="<bean:message key="textbox.keywordsearch.examplevalue"/>"> <INPUT type="submit" class="button" value="<bean:message key="button.keywordsearch"/>">
			</DIV>			
			<DIV id="savedsearchespanel">
				<DIV class="titlebar collapsible">
					<DIV class="titlebarheader title">
						<DIV class="titlebarleftcurve">
							<DIV class="titlebarrightcurve">
								<DIV class="titlebarcollapsed" onClick="document.getElementById('savedsearchespanelbody').style.display=(document.getElementById('savedsearchespanelbody').style.display=='none'?'block':'none');this.className=(this.className=='titlebarexpanded'?'titlebarcollapsed':'titlebarexpanded');">
								<bean:message key="title.savedsearches"/>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV id="savedsearchespanelbody">
					<jsp:include page="savedsearches.jsp"/>
				</DIV>
			</DIV>
		</DIV>
		<jsp:include page="footer.jsp"/>
	</FORM>
</BODY>
</HTML>