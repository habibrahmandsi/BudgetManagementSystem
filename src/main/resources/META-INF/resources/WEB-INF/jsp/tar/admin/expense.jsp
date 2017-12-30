<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="expense.list.title"/></title>

<script type="text/javascript" src="<c:url value="/scripts/bootstrapValidator.min.js"/>"></script>

<!-- /.row -->
<div class="row">
    <form:form method="post" id="expenseForm" commandName="expense">
        <form:hidden path="id"/>
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <spring:message code="expense.new.btn"/> Form
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-4"></div>
                        <div class="col-lg-4">
                           <div class="form-group">
                                <label><spring:message code="expense.form.item"/><span
                                        class="required">*</span></label>
                                <form:select path="expenseItem.id" cssClass="form-control">
                                    <option value=""> -- Select Any One --</option>
                                    <c:forEach items="${expenseItemList}" var="pg">
                                        <c:choose>
                                            <c:when test="${pg.id == expense.expenseItem.id}">
                                                <option value="${pg.id}" selected>${pg.name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${pg.id}">${pg.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="deposit.amount"/><span
                                        class="required">*</span></label>
                                <form:input path="amount" cssClass="form-control"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="expense.form.description"/><span
                                        class="required">*</span></label>
                                <form:input path="description" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-lg-4">
                        </div>
                        <!-- /.row (nested) -->
                    </div>
                    <div class="row">
                        <div class="col-lg-4"></div>
                        <div class="col-lg-2">
                            <button type="reset" class="btn btn-danger btn-block">Cancel</button>
                        </div>
                        <div class="col-lg-2">
                            <button type="submit" class="btn btn-success btn-block">Submit</button>
                        </div>
                        <div class="col-lg-4"></div>
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            <!-- /.col-lg-12 -->
        </div>
    </form:form>
    <!-- /.row -->
</div>
<!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

<script>
    $(document).ready(function () {
        var contextPath = '<%= contextPath %>';


        /* initial form validation declaration */
        console.log('initiliaze validation....');

        formValidation();
    });

    function formValidation() {
        $("#expenseForm").bootstrapValidator({
            fields: {
                "expenseItem.id": {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                },
                amount: {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                }
            }
        });
    }
</script>