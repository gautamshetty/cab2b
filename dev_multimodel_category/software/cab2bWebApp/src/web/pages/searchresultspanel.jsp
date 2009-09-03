<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<logic:present name="searchResultsView">
	<logic:notEqual name="searchResultsView" value="processing">
		<display:table class="simple" name="${sessionScope.searchResultsView}" cellspacing="1" cellpadding="4" uid="row" htmlId="searchresultstable">
			<logic:iterate name="row" id="column" type="edu.wustl.cab2bwebapp.dvo.SearchResultDVO">
				<display:column title="${column.title}" value="${column.value}" media="${column.media}" sortable="false" headerClass="unsortable"/>
			</logic:iterate>
		</display:table>
	</logic:notEqual>
</logic:present>
<logic:notPresent name="searchResultsView">
	<logic:present name="stopAjax">
		<DIV class="text" align="center"><bean:message key="text.resultsempty" arg0="${sessionScope.selectedQueryName}"/></DIV>
	</logic:present>
</logic:notPresent>
<logic:present name="savedQueries">
	<logic:iterate name="savedQueries" id="savedSearch">
		<DIV id="resultcountAJAX" style="display:none;"><bean:write name="savedSearch" property="resultCount"/></DIV>
	</logic:iterate>
</logic:present>
<logic:present name="failedServices">
	<DIV style="display:none" id="failedservicesAJAX">
		<logic:iterate name="failedServices" id="failedServices" indexId="indexId" type="edu.wustl.cab2b.common.user.ServiceURLInterface">
			<DIV class="${indexId%2==0?"odd":"even"}">
				<DIV class="failedservice text"><bean:write name="failedServices" property="hostingCenter"/>
				<BR/><bean:write name="failedServices" property="urlLocation"/></DIV>
			</DIV>
		</logic:iterate>
		<BR style="line-height:0.4em"/>
	</DIV>
	<DIV style="display:none" id="failedservicescountAJAX"><bean:write name="failedServicesCount"/></DIV>
</logic:present>
<DIV style="display:none">
	<logic:present name="queryUIPartialCount">
		<DIV id="partialresultsmessage"><bean:message key="message.partialresults" arg0="${savedSearch.resultCount}"/></DIV>
	</logic:present>
	<logic:present name="keyword">
		<DIV style="font-size:0.85em;" id="resultsmessageKeyword"><IMG SRC="images/PageLoading.gif" style="height:1.2em;width:1.2em;"> <bean:message key="message.partialresults.keyword" arg0="${sessionScope.keyword}" arg1="${sessionScope.selectedQueryName}"/></DIV>
	</logic:present>
	<logic:present name="stopAjax">
		<logic:present name="transformationMaxLimit">
			<logic:present name="queryResultCount">
				<logic:lessEqual name="transformationMaxLimit" value="${queryResultCount}">
					<DIV id="completeresultsmessage"><bean:message key="message.completeresults.morethan.uilimit" arg0="${queryResultCount}"/></DIV>
				</logic:lessEqual>
				<logic:greaterThan name="transformationMaxLimit" value="${queryResultCount}">
					<DIV id="completeresultsmessage"><bean:message key="message.completeresults.lessthan.uilimit" arg0="${queryResultCount}"/></DIV>
				</logic:greaterThan>
			</logic:present>
		</logic:present>
	</logic:present>
</DIV>