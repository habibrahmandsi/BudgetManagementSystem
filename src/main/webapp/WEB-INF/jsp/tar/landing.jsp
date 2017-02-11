<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="landing.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-4"></div>
                <div class="col-lg-4" style="text-align: center; font-size: 15px;">
                    <label class="nonBoldFont"> Total Deposited amount: <font style="font-weight: 600;">${allDeposit}</font> tk.</label>
                    <label class="nonBoldFont"> Total Expense amount: <font style="font-weight: 600;">${totalExpenseAmount}</font> tk.</label>
                    <label class="nonBoldFont"> Total Available amount: <font style="font-weight: 600;">${totalAvailableAmount}</font> tk.</label>
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
                </c:forEach>
            </div>
        </div>
    </div>
</div>

