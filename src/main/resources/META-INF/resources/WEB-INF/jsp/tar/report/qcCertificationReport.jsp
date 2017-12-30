<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html style="height:90%">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/reset.css"/>" />
	<link type="text/css" rel="stylesheet" href="<c:url value="/styles/silver/jquery-ui.css"/>" />
	<style type="text/css">
		html {
			box-sizing: border-box;
		}
		body, .ui-widget {
			font-family: Roboto Slab,serif; font-size:14px; font-weight:400; color:#333333;
			line-height:1.2;
		}
		.ui-widget-header {
			background: #d8d8d8;
		}
		td { padding: 6px 8px; }
	</style>
</head>	
<body>
<div>
	<div style="position:relative">
		<table class="ui-widget" style="margin-top:12px;">
		<tbody>
			<tr>
				<td class="ui-widget-header">Documents in Population</td>
				<td class="ui-widget-content"><fmt:formatNumber value="${qcReport.docCount}"/></td>
			</tr>
			<tr>
				<td class="ui-widget-header">Training Documents</td>
				<td class="ui-widget-content"><fmt:formatNumber value="${qcReport.totalTrainCount}"/></td>
			</tr>
		</tbody>
		</table>

	<!-- 		
		<table class="ui-widget" style="margin-top:24px;">			
		<tbody>
			<tr>
				<td class="ui-widget-header">Expected Recall from Prediction</td>
				<td class="ui-widget-content"><fmt:formatNumber type="PERCENT" maxFractionDigits="2" value="${qcReport.prediction.recall}"/></td>
			</tr>
			<tr>
				<td class="ui-widget-header">Estimated Recall from QC</td>
				<td class="ui-widget-content"><fmt:formatNumber type="PERCENT" maxFractionDigits="2" value="${qcReport.estimatedRecall}"/></td>
			</tr>
		</tbody>
		</table>
		
		<table class="ui-widget" style="margin-top:12px;">			
		<tbody>
			<tr>
				<td class="ui-widget-header">Expected Yield from Prediction</td>
				<td class="ui-widget-content"><fmt:formatNumber type="PERCENT" maxFractionDigits="2" value="${qcReport.prediction.yield}"/></td>
			</tr>
			<tr>
				<td class="ui-widget-header">Actual Yield</td>
				<td class="ui-widget-content"><fmt:formatNumber type="PERCENT" maxFractionDigits="2" value="${qcReport.actualYield}"/></td>
			</tr>
			<tr>
				<td class="ui-widget-header">Actual Yield with Families</td>
				<td class="ui-widget-content"><fmt:formatNumber type="PERCENT" maxFractionDigits="2" value="${qcReport.actualYieldWithFamilies}"/></td>
			</tr>
		</tbody>
		</table>
	-->
		<div style="position:absolute; top:0; right:0">
			<!--  Pie chart with population estimates -->
		</div>
	</div>
</div>