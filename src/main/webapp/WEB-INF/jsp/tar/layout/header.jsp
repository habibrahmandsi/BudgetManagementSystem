<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.macrosoft.bms.util.Constants" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<sec:authentication var="principal" property="principal"/>

<div class="header-wrapper">
    <div class="header-left-part">
        <div class="cursorPointer" onclick="window.location='/admin/landing.do'" id="logo"><strong><spring:message
                code="project.name"/></strong>
        </div>
    </div>

    <div class="cssmenu header-right-part">
        <sec:authorize access="isAuthenticated()">
	        <ul>
	            <li class="dropdown">
	                <a style="padding: 10px 5px;" href="#" class="dropbtn"> Welcome, ${principal.fullName}
	                    &nbsp;<img class="downArrow" src="/images/down-arrow-icon.png">&nbsp;
	                </a>
	                <div class="dropdown-content">
	                    <a class="logout"><spring:message code="header.menu.logout"/></a>
	                    <a class=""><spring:message code="header.menu.about"/></a>
	                </div>
	            </li>
	        </ul>
	    </sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
            <ul class="menuSeparator">
                <li class="dropdown">
                    <a style="padding: 10px 5px;" href="#" class="dropbtn ">
                        <img class="downArrow" src="/images/share-holder-icon.png"> Share Holders
                        <img class="downArrow" src="/images/down-arrow-icon.png">
                    </a>
                    <div class="dropdown-content">
                        <a href="/admin/upsertShareHolder.do"><spring:message code="header.menu.shareHolder.add"/></a>
                        <a href="/admin/shareHolderList.do"><spring:message code="shareHolder.list.header"/></a>
                    </div>
                </li>
                <li class="dropdown">
                    <a style="padding: 10px 5px;" href="#" class="dropbtn ">
                        <img class="downArrow" src="/images/cash-icon.png">&nbsp;Transaction
                        <img class="downArrow" src="/images/down-arrow-icon.png">
                    </a>
                    <div class="dropdown-content">
                        <a href="/admin/upsertInstallment.do"><spring:message code="header.menu.new.installment"/></a>
                        <a href="/admin/installmentList.do"><spring:message code="header.menu.installment.list"/></a>
                        <a href="/admin/upsertDeposit.do"><spring:message code="header.menu.credit"/></a>
                        <a href="/admin/depositList.do"><spring:message code="header.menu.deposit.list"/></a>
                        <a href="/admin/upsertExpense.do"><spring:message code="header.menu.new.debit"/></a>
                        <a href="/admin/expenseList.do"><spring:message code="header.menu.debit"/></a>
                    </div>
                </li>
                <li class="dropdown">
                    <a style="padding: 10px 5px;" href="#" class="dropbtn ">
                        <img class="downArrow" src="/images/report-icon.png">Reports
                        <img class="downArrow" src="/images/down-arrow-icon.png">
                    </a>
                    <div class="dropdown-content">
                        <a href="/admin/depositList.do"><spring:message code="header.menu.credit"/></a>
                        <a href="/admin/userList.do"><spring:message code="header.menu.debit"/></a>
                    </div>
                </li>
            </ul>
        </sec:authorize>
        <%--<div class="loggedUserFullname"> Welcome, ${principal.fullName}</div>--%>
        <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
            <c:if test="${not empty globalMessages}">
            <div style="width:70px; float: right; margin-top: 8px;">
                <a href="/admin/services"><img class="icon"
                                               style="border: 2px solid #6c6c6c; border-radius: 50%;padding: 1px;"
                                               src="/images/notification-icon.png">
                    <span style="background-color:rgba(255, 0, 0, 0.54);margin-left: -9px;border-radius: 50%;padding: 4px 5px;"
                          class="badge">${globalMessages.size()}</span></a>

            </div>
            </c:if>
        </sec:authorize>
    </div>

    <div style="clear: both;"></div>
    <form id="logout-form" action="/logout" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<%-- Header 2 implementation --%>


<%--    <div class="projectHeadWrapper">
        <div class="header-left-part" style="width: 50%;">
            <div class="ui-helper-clearfix">
                <h3 style="margin: 0px; display:inline-block;">BMS</h3>
            </div>
        </div>

        <div class="header-right-part">
            <div class="cssmenu" style="text-align: right;">
                <ul>
                    <li class="dropdown">
                        <a href="#" style="padding: 5px 5px;border-bottom-width: 0px;" class="dropbtn"><i class="icon-list icon-large"></i></a>
                        <div class="dropdown-content">
                            &lt;%&ndash;<a href="javascript:void(0)" onclick="Recenseo.Tar.addToPopulation()"><spring:message-->
                                    code="header.menu.addToPopulation"/></a>
                            <a href="javascript:void(0)" onclick="Recenseo.Tar.createJudgementalSample()"><spring:message
                                    code="header.menu.createJudgementalSample"/></a>
                            <a href="javascript:void(0)" onclick="Recenseo.Tar.removeFromPopulation()"><spring:message
                                    code="header.menu.removeFromPopulation"/></a>
                            <a href="javascript:void(0)" onclick="Recenseo.Tar.syncingDocument()"><spring:message
                                    code="header.menu.sync"/></a>
                            <a href="javascript:void(0)"><spring:message code="header.menu.index"/></a>&ndash;%&gt;
                        </div>
                    </li>
                </ul>
            </div>

        </div>
    </div>--%>

<script>
    $(document).ready(function () {
        $(".logout").click(function () {
            $("#logout-form").submit();
        });
    })
</script>
