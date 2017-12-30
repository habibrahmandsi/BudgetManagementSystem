<%@ page import="com.dsoft.entity.OccupationType" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="shareHolder.details.header"/></title>
<script type="text/javascript" src="<c:url value="/jquery.PrintArea.js"/>"></script>
<!-- /.row -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="shareHolder.details.header"/>
            </div>
            <div class="panel-body" id="divPrint">
                <div class="row">
                    <div class="col-lg-1"></div>
                    <div class="col-lg-10">
                        <div class="shareHolderDetailsWrapperDiv">
                            <table class="table detailsInfoTable">
                                <thead>
                                <th colspan="4" class="headerFont">Basic Information</th>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><spring:message code="shareHolder.form.name"/> :</td>
                                    <td>${shareHolder.name}</td>
                                    <td rowspan="6">
                                        <div class="shareHolderPicWrapperDiv">
                                            <img src="../shareHolder/getImage.do?imgId=${shareHolder.photoName}&sex=${shareHolder.sex}&imgSize=5">
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.father.name"/> :</td>
                                    <td>${shareHolder.fatherName}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.mother.name"/> :</td>
                                    <td>${shareHolder.motherName}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.sex"/> :</td>
                                    <td>${shareHolder.sex}</td>
                                    <td></td>
                                </tr>
                                <c:if test="${shareHolder.maritalStatus == 'Married'}">
                                    <tr>
                                        <td><spring:message code="shareHolder.form.spouse.name"/> :</td>
                                        <td>${shareHolder.spouseName}</td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><spring:message code="shareHolder.form.occupation.spouse"/> :</td>
                                        <td>${shareHolder.spouseOccupation}</td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td><spring:message code="shareHolder.form.spouse.home.district"/> :</td>
                                        <td>${shareHolder.spouseHomeDistrict.name}</td>
                                        <td></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td><spring:message code="shareHolder.form.cdesgJoinDate"/> :</td>
                                    <td><fmt:formatDate value="${shareHolder.currentDesgJoinDate}"
                                                        pattern="${dateFormateForJstlTag}"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.nationalIdNo"/> :</td>
                                    <td>${shareHolder.nationalId}</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.status"/> :</td>
                                    <td>${shareHolder.jobStatus}</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.cell"/> :</td>
                                    <td>${shareHolder.mobile}</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.present.address"/> :</td>
                                    <td>${shareHolder.presentAddress}</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td><spring:message code="shareHolder.form.permanent.address"/> :</td>
                                    <td>${shareHolder.permanentAddress}</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td><spring:message code="shareHolder.form.email"/> :</td>
                                    <td>${shareHolder.email}</td>
                                    <td></td>
                                </tr>

                                <c:forEach items="${shareHolder.extraInfoList}" var="obj" varStatus="idx">
                                    <tr>
                                        <td>${obj.labelName}&nbsp;:</td>
                                        <td>${obj.value}</td>
                                        <td></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-lg-1"></div>
                </div>
            </div>
            <div class="row" style="margin-bottom: 10px;">
                <div class="col-lg-4"></div>
                <div class="col-lg-4">
                    <button class="btn btn-success btn-block printBtn">
                        Print
                    </button>
                </div>
                <div class="col-lg-4"></div>
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
</div>
<!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

<script>
    $(document).ready(function () {
        $(".printBtn").click(function () {
            var options = {
                "mode": "popup",
                "popClose": true,
                "extraCss": contextPath + '/resources/css/print.css',
                "retainAttr": ["class", "id", "style", "on"],
                "extraHead": '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>'

            };
            var printDiv = $('<div></div>');
            var data = $("#divPrint").html();
            var head = '<div style="text-align: center;padding-bottom: 10px;"><table style="display: inline-block;"><tbody> <tr> <td rowspan = "2"><img src="' + contextPath + '/resources/images/bangladesh_govt_logo.png" style="width:50px; height:50px;" class="headerGovtLogo"></td> <td> <a style="font-size: 16px !important;" class="navbar-brand shopName">shareHolder Management System</a></td> </tr> <tr> <td> <a title="Home" style="font-size: 12px !important;font-weight: 600;" class="navbar-brand">বহিরাগমন ও পাসপোর্ট অধিদপ্তর</a></td> </tr> </tbody></table></div>'

            data = data.replace("undefined", "");

            $(printDiv).append(head);

            console.log("SMNLOG:printDiv:" + printDiv);
            $(printDiv).append(data);

            $(printDiv).printArea(options);
//            $("#divPrint").printArea( options );


        })

    });
</script>
