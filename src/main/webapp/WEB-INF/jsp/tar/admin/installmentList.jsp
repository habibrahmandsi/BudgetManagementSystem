<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="header.menu.installment.list"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
                <div class="pull-right">
                    <input type="button" class="createInstallment btn btn-primary"
                           value="<spring:message code="header.menu.new.installment"/>">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                    <table class="table customTable">
                        <thead>
                        <tr>
                            <th class="headerFont">SL#</th>
                            <th class="headerFont">Name</th>
                            <th class="headerFont">Is Active</th>
                            <th class="headerFont">Active from</th>
                            <th class="headerFont">Created date</th>
                            <th class="headerFont">Created By</th>
                            <th class="headerFont">Per share Amount</th>
                            <th class="headerFont">Paid share count</th>
                            <th class="headerFont">Total payable</th>
                            <th class="headerFont">Paid Amount</th>
                            <th class="headerFont">Unpaid</th>
                        </tr>
                        </thead>
                        <tbody id="tableBody">
                       <%-- <c:forEach items="${installmentList}" var="obj" varStatus="idx">
                            <tr>
                                <td>${idx.index+1}</td>
                                    &lt;%&ndash;<td>${obj.name}</td>&ndash;%&gt;
                                <td>${obj.name}</td>
                                <td>${obj.active}</td>
                                <td>${obj.activeFrom}</td>
                                <td>${obj.created}</td>
                                <td>${obj.createdBy}</td>
                                <td>${obj.amountToPay}</td>
                            </tr>
                        </c:forEach>--%>
                        <%--<tr>
                            <td colspan="6" style="font-weight: 600; text-align: right;">Total Declared Amount :&nbsp;</td>
                            <td>${totalAmount}</td>
                        </tr>--%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="userDetailsList"></div>
    </div>

</div>

<script type="text/javascript">
    $(document).ready(function() {
        var $createBtn = $(".createInstallment");
        var installmentList = [];
        var depositMap = {};

        <c:forEach items="${depositList}" var="obj" varStatus="idx">
        var deposit = depositMap['${obj.installmentId}'];
        if(!_.isUndefined(deposit)){
            console.log("SMNLOG ::second time found ......."+JSON.stringify(deposit));
            depositMap['${obj.installmentId}'] = {
                "count":deposit.count+1,
                "amount": deposit.amount +(+'${obj.amount}')
            };
        }else{
            depositMap['${obj.installmentId}'] = {
                "count":1,
                "amount": (+'${obj.amount}')
            };
        }
        console.log("SMNLOG ::depositMap :"+JSON.stringify(depositMap));
        </c:forEach>

        <c:forEach items="${installmentList}" var="obj" varStatus="idx">
        var depositMapObj = depositMap['${obj.id}'];
        installmentList.push({
            "id":'${obj.id}',
            "name":'${obj.name}',
            "active":'${obj.active}',
            "activeFrom":'${obj.activeFrom}',
            "created":'${obj.created}',
            "createdBy":'${obj.createdBy}',
            "amountToPay":+'${obj.amountToPay}',
            "totalPaidAmount":+depositMapObj.amount,
            "totalPaidCount":depositMapObj.count
        });
        </c:forEach>

        var tbody = '';
        var topay = 0;
        var totalUnpaid = 0;
        var totalpaid = 0;
        var totalpayable = 0;
            _.each(installmentList, function(object, index){
            console.log("SMNLOG ::"+JSON.stringify(object));
            topay = +(object.amountToPay*(+'${totalShare}')).toFixed(2);
            tbody += '<tr>'
                + '<td>'+(index+1)+'</td>'
                + '<td>'+object.name+'</td>'
                + '<td>'+object.active+'</td>'
                + '<td>'+object.activeFrom+'</td>'
                + '<td>'+object.created+'</td>'
                + '<td>'+object.createdBy+'</td>'
                + '<td>'+object.amountToPay+'</td>'
                + '<td><a href="/admin/paidInstallmentShList.do?installmentId='+object.id+'">'+object.totalPaidCount+'</a></td>'
                + '<td>('+object.amountToPay+' * '+'${totalShare}'+') = '+topay+'</td>'
                + '<td>'+(object.totalPaidAmount).toFixed(2)+'</td>'
                + '<td>'+(topay- object.totalPaidAmount).toFixed(2)+'</td>'
                + '</tr>';
                totalUnpaid += (topay- object.totalPaidAmount);
                totalpayable += topay;
                totalpaid += object.totalPaidAmount;
        });

        tbody += '<tr>'
            + '<td colspan="8" style="text-align: right;">Total</td>'
            + '<td>'+totalpayable+'</td>'
            + '<td>'+totalpaid+'</td>'
            + '<td>'+totalUnpaid+'</td>'
            + '</tr>';

        $("#tableBody").html(tbody);

        $createBtn.click(function () {
           window.location = "/admin/upsertInstallment.do";
        });
      });
</script>