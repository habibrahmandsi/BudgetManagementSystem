<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    final String contextPath = request.getContextPath();
%>

<link type="text/css" rel="stylesheet" href="<c:url value="/styles/datatable/datatables.bootstrap.css"/>"/>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/dataTables.responsive.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery.PrintArea.js"/>"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="deposit.list.title"/> of <b>${shareHolder.name}</b>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-5 shareHolderDetailsDiv" id="divPrint">
                <div class="shareHolderDetailsWrapperDiv">
                    <table class="table detailsInfoTable">
                        <thead>
                        <tr>
                            <th colspan="4" class="headerFont">Basic Information</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Name :</td>
                            <td>${shareHolder.name}</td>
                            <td rowspan="4">
                                <div class="shareHolderPicWrapperDiv"><img
                                        src="/admin/getImage.do?imgId=${shareHolder.photoName}&sex=${shareHolder.sex}&imgSize=5">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>Father's Name :</td>
                            <td> ${shareHolder.fathersName}</td>
                        </tr>
                        <tr>
                            <td>Mother's Name :</td>
                            <td>${shareHolder.mothersName}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Gender :</td>
                            <td>${shareHolder.sex}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>National Id :</td>
                            <td>${shareHolder.nationalId}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>E-mail :</td>
                            <td>${shareHolder.email}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Contact Number :</td>
                            <td>${shareHolder.mobile}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>No of installment :</td>
                            <td>${totalInstallment}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Total deposited amount :</td>
                            <td><font style="color: green">${totalAmount}</font></td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-lg-7" id="tableDiv">
                <table class="table customTable">
                    <thead>
                    <tr>
                        <th class="headerFont">SL#</th>
                        <%--<th class="headerFont">Name</th>--%>
                        <th class="headerFont">Payment Method</th>
                        <th class="headerFont">Reference No.</th>
                        <th class="headerFont">Installment Name</th>
                        <th class="headerFont">Added By</th>
                        <th class="headerFont">Date</th>
                        <th class="headerFont">Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${depositList}" var="obj" varStatus="idx">
                    <tr>
                        <td>${idx.index+1}</td>
                        <%--<td>${obj.name}</td>--%>
                        <td>${obj.method}</td>
                        <td>${obj.referenceNo}</td>
                        <td>${obj.installmentName}</td>
                        <td>${obj.createdBy}</td>
                        <td>${obj.date}</td>
                        <td>${obj.amount}</td>
                    </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="6" style="font-weight: 600; text-align: right;">Total Deposited Amount :&nbsp;</td>
                        <td>${totalAmount}</td>
                    </tr>
                    </tbody>
                </table>
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
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var contextPath = '<%= contextPath %>';
        $(".printBtn").click(function () {
            var options = {
                "mode": "popup",
                "popClose": true,
                "extraCss": contextPath + '/styles/print.css',
                "retainAttr": ["class", "id", "style", "on"],
                "extraHead": '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>'
            };

            var printDiv = $('<div></div>');
            var data = $("#divPrint").html();
            var head = '<div style="text-align: center;padding-bottom: 10px; margin-bottom: 30px;"><table style="display: inline-block;"><tbody> <tr> <td> <img style="height: 35px;" src="/images/bmsLogo.png"></td> </tr> <tr> </tr> </tbody></table></div>'

            data = data.replace("undefined", "");
            data += $("#tableDiv").html();
            data += $("#footer").show().html();

//            $("#footer").hide();
            $(printDiv).append(head);

            console.log("SMNLOG:printDiv:" + printDiv);
            $(printDiv).append(data);

            $(printDiv).printArea(options);
//            $("#divPrint").printArea( options );


        })
    });
</script>