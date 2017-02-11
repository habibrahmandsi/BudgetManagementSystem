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
                <label class="nonBoldFont"> Total expense amount: <font style="font-weight: 600;">${totalExpenseAmount}</font> tk.</label>
            </div>
            <div class="col-lg-4"></div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="pull-right">
                    <input type="button" class="addNewExpense btn btn-primary"
                           value="<spring:message code="expense.new.btn"/>">
                </div>
            </div>
        </div>
        <div class="row">
                <div class="col-lg-12">
                    <table class="table customTable">
                        <thead>
                        <tr>
                            <th class="headerFont">SL#</th>
                            <th class="headerFont">Expense Item Name</th>
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
                                 <td>${obj.date}</td>
                                 <td>${obj.createdBy.username}</td>
                                 <td>${obj.amount}</td>
                             </tr>
                         </c:forEach>
                        <tr>
                            <td colspan="4" style="font-weight: 600; text-align: right;">Total Expense Amount :&nbsp;</td>
                            <td>${totalExpenseAmount}</td>
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
        var $createBtn = $(".addNewExpense");
        $createBtn.click(function () {
            window.location = "/admin/upsertExpense.do";
        });

    });
</script>