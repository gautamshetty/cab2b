<%--L
  Copyright Georgetown University.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cab2b/LICENSE.txt for details.
L--%>

<%@ page errorPage="failure.jsp" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><bean:message key="application.title"/></TITLE>
<META http-equiv="Content-Type" content="text/html">
<LINK rel="stylesheet" href="stylesheet/home.css" type="text/css">
<SCRIPT language="JavaScript" src="javascript/validator.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/ajax.js"></SCRIPT>
<SCRIPT language="JavaScript" src="javascript/multiselect.js"></SCRIPT>
<BODY onLoad="<logic:present name="errorSessionTimeout">alert('<bean:message key="alert.sessioninvalidated"/>');</logic:present><logic:notPresent name="modelGroupDVOList">document.forms[0].action='Home.do';document.forms[0].submit();</logic:notPresent>setSelection(document.getElementsByName('modelGroups'));if(selectedItemsCount>0)processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + document.getElementsByName('modelGroups')[singleSelectIndex].value, 'savedsearchespanelbody');if(selectedItemsCount==0)document.getElementById('savedsearchespanelbody').innerHTML = '';">
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
										<INPUT type="checkbox" name="modelGroups" id="<bean:write name="modelGroupDVO" property="modelGroupName"/>" value="<bean:write name="modelGroupDVO" property="modelGroupName"/>" onClick="if(<bean:write name="modelGroupDVO" property="secured"/> <logic:notPresent name="userName">&& true</logic:notPresent><logic:present name="userName">&& false</logic:present>){this.checked=false;alert('<bean:message key="alert.signinrequired"/>');return;}else{if(selectedItemsCount==1)document.getElementsByName('modelGroups')[singleSelectIndex].checked=false;};setSelection(document.getElementsByName('modelGroups'));processAJAXRequest('DisplaySavedSearches.do?modelGroups=' + (this.checked?this.value:'null'), 'savedsearchespanelbody');" <logic:equal name="modelGroupDVO" property="selected" value="true">checked</logic:equal>>
										<LABEL><bean:write name="modelGroupDVO" property="modelGroupName"/></LABEL>										
									</DIV>
								</logic:iterate>
							</logic:present>						
						</DIV>						
					</DIV>		
					<DIV>
						<A href="#this" class="link" onClick="if(selectedItemsCount>0){document.forms[0].action='DisplayServiceInstances.do';document.forms[0].submit()}else{alert('<bean:message key="alert.selectdatatype"/>');}"><bean:message key="link.databasestosearch"/></A>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="keywordsearchpanel">
				<DIV class="label"><bean:message key="label.keywordsearch"/></DIV> <INPUT type="text" class="textbox examplevalue" name="keyword" value="<bean:message key="textbox.keywordsearch.examplevalue"/>" onFocus="if(keywordSearchExample.value=='')keywordSearchExample.value=this.value;if(this.value==keywordSearchExample.value)this.value='';this.className='textbox'" onBlur="if(this.value=='')this.value=keywordSearchExample.value;if(this.value==keywordSearchExample.value)this.className='textbox exampleValue'"><INPUT type="hidden" name="keywordSearchExample" value="<bean:message key="textbox.keywordsearch.examplevalue"/>"> <INPUT type="submit" class="button" value="<bean:message key="button.keywordsearch"/>">
			</DIV>			
			<DIV id="savedsearchespanel">
				<DIV class="titlebar">
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