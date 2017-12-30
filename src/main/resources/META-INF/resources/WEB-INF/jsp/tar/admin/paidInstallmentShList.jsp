<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="expense.list.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
            <div class="col-lg-4"></div>
            <div class="col-lg-4" style="text-align: center; font-size: 15px;">
                <label class="nonBoldFont totalDepositAmount"> </label>
            </div>
            <div class="col-lg-4"></div>
            </div>
        </div>

        <div class="row" style="margin-top: 50px;">
                <div class="col-lg-12">
                    <table class="table customTable">
                        <thead>
                        <tr>
                            <th class="headerFont">SL#</th>
                            <th class="headerFont"></th>
                            <th class="headerFont">Name</th>
                            <th class="headerFont">Father's Name</th>
                            <th class="headerFont">Mother's Name</th>
                            <th class="headerFont">Installment Name</th>
                            <th class="headerFont">Payment method</th>
                            <th class="headerFont">Reference No.</th>
                            <th class="headerFont">Created By</th>
                            <th class="headerFont">Amount</th>
                        </tr>
                        </thead>
                        <tbody id="tableBody">
                         <c:forEach items="${depositList}" var="obj" varStatus="idx">
                             <tr>
                                 <td>${idx.index+1}</td>
                                 <td> <img class="roundImage" style="height: 50px !important; width: 50px !important;" src="/admin/getImage.do?imgId=${obj.photoName}&sex=${obj.sex}&imgSize=2"/></td>
                                 <td>${obj.name}</td>
                                 <td>${obj.fathersName}</td>
                                 <td>${obj.mothersName}</td>
                                 <td>${obj.installmentName}</td>
                                 <td>${obj.method}</td>
                                 <td>${obj.referenceNo}</td>
                                 <td>${obj.createdBy}</td>
                                 <td>${obj.amount}</td>
                             </tr>
                         </c:forEach>
                        <tr>
                            <td colspan="9" style="font-weight: 600; text-align: right;">Total Deposited Amount :&nbsp;</td>
                            <td class="totalDepositAmountTd"></td>
                        </tr>
                        </tbody>
                    </table>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">
    $(function () {
        var disabledRowId = [];
        var total = 0;
        $(".customTable tbody tr").each(function(){
            total += +$(this).find("td:last-child").html();

        })
        console.log("SMNLOG ::total:"+total);
        $(".totalDepositAmount").html('Total deposited amount: <font style="font-weight: 600;">'+total.toFixed(2)+'</font> tk.')
        $(".totalDepositAmountTd").html(total.toFixed(2))
    });
</script>