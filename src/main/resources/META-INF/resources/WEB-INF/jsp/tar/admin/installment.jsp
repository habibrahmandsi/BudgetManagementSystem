<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="installment.header"/></title>

<script type="text/javascript" src="<c:url value="/scripts/bootstrapValidator.min.js"/>"></script>

<!-- /.row -->
<div class="row">
    <form:form method="post" id="installmentForm" commandName="installment">
        <form:hidden path="id"/>
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <spring:message code="installment.new.btn"/> Form
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-4"></div>
                        <div class="col-lg-4">
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.name"/><span
                                        class="required">*</span></label>
                                <form:input path="name" cssClass="form-control"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="installment.form.active.from"/><span
                                        class="required">*</span></label>
                                <form:input path="activeFrom" cssClass="form-control"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="installment.form.active.from"/><span
                                        class="required">*</span></label><br>
                                <form:checkbox path="active"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="deposit.amount"/><span
                                        class="required">*</span></label>
                                <form:input path="amountToPay" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-lg-4"></div>
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
        installmentFormValidation();
    });

    function installmentFormValidation() {
        $("#installmentForm").bootstrapValidator({
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                },
                amountToPay: {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    },
                    numeric: {
                        message: "must be numeric"
                    }
                }
            }
        });
    }

</script>