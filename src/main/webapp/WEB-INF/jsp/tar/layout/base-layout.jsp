<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <tiles:insertAttribute name="head" />
    <script type="text/javascript" src="<c:url value="/scripts/tar.js"/>" ></script>
</head>
<body style="padding: 5px;">
	<tiles:insertAttribute name="header" />
    <%@ include file="/WEB-INF/jsp/tar/layout/message.jsp" %>
	<tiles:insertAttribute name="body" />
    <div id="footer">
        <table style="width: 100%; margin-top: 200px;">
            <tbody>
            <tr>
                <td style="text-align: right !important;color: grey; font-style: italic;">
                    All right reserve @<img style="height: 20px;" src="/images/Macrosoft-logo.png">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</body>
</html>