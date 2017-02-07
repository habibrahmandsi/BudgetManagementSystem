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

    <script type="text/javascript">
        $(function() {
            var ctxRoot = '${pageContext.request.contextPath}';
            var projectId = '${envityProject.id}';
            Recenseo.Tar.init({
                contextRoot: ctxRoot,
                projectId:projectId
            });
        });
    </script>
</body>
</html>