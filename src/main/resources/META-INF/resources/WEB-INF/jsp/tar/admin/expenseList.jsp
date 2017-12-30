<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>

<script type="text/javascript" src="<c:url value="/scripts/jquery.PrintArea.js"/>"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="expense.list.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
            <div class="col-lg-4"></div>
            <div class="col-lg-4" style="text-align: center; font-size: 15px;">
                <label class="nonBoldFont"> Total expense amount: <font style="font-weight: 600;">${totalExpenseAmount}</font> tk.</label>
            </div>
            <div class="col-lg-4"></div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-8"></div>
            <div class="col-lg-2">
                <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
                    <input type="button" class="addNewExpense btn btn-block btn-primary"
                           value="<spring:message code="expense.new.btn"/>">
                </sec:authorize>
            </div>
            <div class="col-lg-2"><button class="btn btn-success btn-block printBtn">Print</button></div>
            </div>
        </div>
        <div class="row" id="divPrint">
                <div class="col-lg-12">
                    <table class="table customTable">
                        <thead>
                        <tr>
                            <th class="headerFont">SL#</th>
                            <th class="headerFont">Expense Item Name</th>
                            <th class="headerFont">Description</th>
                            <th class="headerFont">Date</th>
                            <th class="headerFont">Created By</th>
                            <th class="headerFont">Amount</th>
                        </tr>
                        </thead>
                        <tbody id="tableBody">
                         <c:forEach items="${expenseList}" var="obj" varStatus="idx">
                             <tr>
                                 <td>${idx.index+1}</td>
                                 <td>${obj.expenseItem.name}</td>
                                 <td>${obj.description}</td>
                                 <td>${obj.date}</td>
                                 <td>${obj.createdBy.username}</td>
                                 <td>${obj.amount}</td>
                             </tr>
                         </c:forEach>
                        <tr>
                            <td colspan="5" style="font-weight: 600; text-align: right;">Total Expense Amount :&nbsp;</td>
                            <td>${totalExpenseAmount}</td>
                        </tr>
                        </tbody>
                    </table>
            </div>
        </div>
    </div>

<script type="text/javascript">
    $(document).ready(function () {
        var contextPath = '<%= contextPath %>';
        var disabledRowId = [];
        var $createBtn = $(".addNewExpense");
        $createBtn.click(function () {
            window.location = "/admin/upsertExpense.do";
        });

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