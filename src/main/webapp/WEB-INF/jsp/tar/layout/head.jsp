<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<link type="text/css" rel="stylesheet" href="<c:url value="/styles/bootstrap.min.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/styles/font-awesome.min.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/styles/silver/jquery-ui.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/styles/envize.css"/>" />
<link type="text/css" rel="stylesheet" href="<c:url value="/styles/admin.css"/>" />
<link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,300,700" />

<script type="text/javascript" src="<c:url value="/scripts/jquery.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery-ui.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery.blockUI.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/bootstrap.min.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/highcharts.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/highcharts-more.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/validation/jquery.validate.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/underscore-min.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/util.js"/>" ></script>
<script type="text/javascript" src="<c:url value="/scripts/date.js"/>"></script>