<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%
    final String contextPath = request.getContextPath();
%>

<script type="text/javascript" src="<c:url value="/scripts/jquery.PrintArea.js"/>"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="landing.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-4"></div>
                <div class="col-lg-4" style="text-align: center; font-size: 15px;">
                    <label class="nonBoldFont"> Total Deposited amount: <font
                            style="font-weight: 600;">${allDeposit}</font> tk.</label><br>
                    <label class="nonBoldFont"> Total Expense amount: <font
                            style="font-weight: 600;">${totalExpenseAmount}</font> tk.</label><br>
                    <label class="nonBoldFont"> Total Available amount: <font
                            style="font-weight: 600;">${totalAvailableAmount}</font> tk.</label>
                </div>
                <div class="col-lg-4"></div>
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading">
        Share Holder Details
    </div>
    <div class="panel-body">
        <div class="row" style="margin-bottom: 10px;">
            <div class="col-lg-4"></div>
            <div class="col-lg-4"></div>
            <div class="col-lg-4">
                <button class="btn btn-warning btn-block printBtn">
                    Print
                </button>
            </div>
        </div>
        <div class="row landingPage">
            <div class="col-lg-12">
                <c:forEach items="${shareHolderList}" var="shareHolder">
                    <div class="col-lg-4 shareHolderDetailsDiv">
                        <div class="shareHolderDetailsWrapperDiv">
                            <table class="table detailsInfoTable">
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
                                    <td>${shareHolder.noOfInstallation}</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>Total deposited amount :</td>
                                    <td><font style="color: green">${shareHolder.amount}</font></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td colspan="3" style="text-align: center!important;"><a
                                            href="/admin/allDepositOfSh.do?shareHolderId=${shareHolder.shareHolderId}"
                                            class="btn btn-success"><spring:message code="payment.details.btn"/></a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        var contextPath = '<%= contextPath %>';
        var shareHolderList = JSON.parse('${shareHolderListStr}');
        var depositList = JSON.parse('${depositListStr}');
        var depositListMap = {};

        $.when( _.each(shareHolderList, function (sh, index) {
            if (!_.isUndefined(sh)) {
                depositListMap [sh.shareHolderId] = _.filter(depositList, function (obj) {
                    return sh.shareHolderId == obj.shareHolderId;
                });
            }
        })
        ).done(function() {
           console.log("SMNLOG ::finished ..........");
        });

        $(".printBtn").click(function () {
            makePrintHtml(shareHolderList, depositList, function(html){
                console.log("SMNLOG :: printing ............"+JSON.stringify(html));
                var options = {
                    "mode": "popup",
                    "popClose": true,
                    "extraCss": contextPath + '/styles/print.css',
                    "retainAttr": ["class", "id", "style", "on"],
                    "extraHead": '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>'
                };

                var printDiv = $('<div></div>');
                var data = html;
                var head = '<div style="text-align: center;padding-bottom: 10px; margin-bottom: 30px;"><table style="display: inline-block;"><tbody> <tr> <td> <img style="height: 35px;" src="/images/bmsLogo.png"></td> </tr> <tr> </tr> </tbody></table></div>'

                data = data.replace("undefined", "");
                data += $("#footer").show().html();

                //            $("#footer").hide();
                $(printDiv).append(head);

                console.log("SMNLOG:printDiv:" + printDiv);
                $(printDiv).append(data);

                $(printDiv).printArea(options);
                //            $("#divPrint").printArea( options );
            });
            });


        function makePrintHtml(shareHolderList, depositList, fn) {
            var html = "";

            $.when(
                _.each(shareHolderList, function (sh, index) {
                    if (!_.isUndefined(sh)) {

                        html += '<div class="shareHolderDetailsDiv shareHolderDetailsWrapperDiv">'
                            + '<table class="table detailsInfoTable zeroPadding">'
                            + '<thead>'
                            + '<th colspan="4" class="headerFont">Basic Information</th>'
                            + '</thead>'
                            + '<tbody>'
                            + '<tr>'
                            + '<td>Name :</td>'
                            + '<td>' + sh.name + '</td>'
                            + '<td rowspan="4">'
                            + '<div class="shareHolderPicWrapperDiv">'
                            + '<img src="/admin/getImage.do?imgId=' + sh.photoName + '&sex=' + sh.sex + '&imgSize=5">'
                            + '</div>'
                            + '</td>'
                            + '</tr>'
                            + '<tr>'
                            + "<td>Father's Name :</td>"
                            + '<td>' + sh["fathersName"] + '</td>'
                            + '</tr>'
                            + '<tr>'
                            + "<td>Mother's Name :</td>"
                            + '<td>' + sh["mothersName"] + '</td>'
                            + '<td></td>'
                            + '</tr>'
                            + ' <tr>'
                            + ' <td>National Id :</td>'
                            + ' <td>' + sh.national_id + '</td>'
                            + '<td></td>'
                            + ' </tr>'
                            + ' <tr>'
                            + ' <td>E-mail :</td>'
                            + ' <td>' + sh.email + '</td>'
                            + '<td></td>'
                            + ' </tr>'
                            + ' <tr>'
                            + ' <td>Contact Number :</td>'
                            + ' <td>' + sh.mobile + '</td>'
                            + '<td></td>'
                            + ' </tr>'
                            + ' <tr>'
                            + ' <td>No of installment :</td>'
                            + ' <td>' + sh.noOfInstallation + '</td>'
                            + '<td></td>'
                            + ' </tr>'
                            + ' <tr>'
                            + ' <td>Total deposited amount :</td>'
                            + ' <td>' + sh.amount + '</td>'
                            + '<td></td>'
                            + ' </tr>'
                            + '</tbody>'
                            + ' </table>'
                            + ' </div>';

                        var depositList = depositListMap[sh.shareHolderId];

                        html +=  '<div class="col-lg-7" style="margin: 20px 0px;">'
                            + '<table class="table customTable">'
                            + '<thead>'
                            + ' <tr>'
                            + '<th class="headerFont">SL#</th>'
                            + '<th class="headerFont">Payment Method</th>'
                            + '<th class="headerFont">Reference No.</th>'
                            + '<th class="headerFont">Installment Name</th>'
                            + '<th class="headerFont">Added By</th>'
                            + '<th class="headerFont">Date</th>'
                            + '<th class="headerFont">Amount</th>'
                            + '</tr>'
                            + '</thead>'
                            + '<tbody>';

                        _.each(depositList, function (deposit, index) {
                            html += '<tr>'
                                + '<td>'+ (index+1) +'</td>'
                                + '<td>'+ deposit.method +'</td>'
                                + '<td>'+ deposit.referenceNo +'</td>'
                                + '<td>'+ deposit.installmentName +'</td>'
                                + '<td>'+ deposit.createdBy +'</td>'
                                + '<td>'+ (new Date(deposit.date)).toString(Recenseo.Tar.GLOBAL_DATE_FORMAT_FOR_JS) +'</td>'
                                + '<td>'+ deposit.amount +'</td>'
                                + '</tr>'
                        });

                        html +=  + '<tr>'
                            + '<td colspan="6" style="font-weight: 600; text-align: right;">Total Deposited Amount :&nbsp;</td>'
                            + '<td>'+ sh.amount +'</td>'
                            + '</tr>'
                            + '</tbody>'
                            + '</table>'
                            + '</div>';
                    }
                })
            ).done(function() {
                if(typeof fn == 'function'){
                    fn(html);
                }
            });
        }
    });
</script>

