<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <c:choose><c:when test="${user.id > 0}"><spring:message code="user.form.edit.title"/></c:when><c:otherwise><spring:message code="user.form.create.title"/></c:otherwise></c:choose>
    </div>
    <div class="panel-body">
        <div style="width:80%; margin: auto; text-align: left">
            <form:form method="post" id="userForm" commandName="user">
                <form:hidden path="id"/>
                <form:hidden path="enabled"/>
                <div class="formRow">
                    <div class="formLeftDiv">
                        <label class="input-label" ><spring:message code="user.form.email"/></label>
                    </div>
                    <div class="formRightDiv">
                        <form:input path="email" cssClass="form-control"/>
                    </div>
                </div>
                <div class="formRow">
                    <div class="formLeftDiv">
                        <label class="input-label"><spring:message code="user.form.username"/></label>
                    </div>
                    <div class="formRightDiv">
                        <form:input path="username" cssClass="form-control"/>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${user.id > 0}">
                        <form:hidden path="password" cssClass="form-control"/>
                    </c:when>
                    <c:otherwise>
                        <div class="formRow">
                            <div class="formLeftDiv">
                                <label class="input-label" ><spring:message code="user.form.password"/></label>
                            </div>
                            <div class="formRightDiv">
                                <form:password path="password" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="formRow">
                            <div class="formLeftDiv">
                                <label class="input-label" ><spring:message code="user.form.confirm.password"/></label>
                            </div>
                            <div class="formRightDiv">
                                <form:password path="confirmPassword" cssClass="form-control"/>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="formRow">
                    <div class="formLeftDiv">
                        <label class="input-label" ><spring:message code="user.form.firstname"/></label>
                    </div>
                    <div class="formRightDiv">
                        <form:input path="firstName" cssClass="form-control"/>
                    </div>
                </div>
                <div class="formRow">
                    <div class="formLeftDiv">
                        <label class="input-label" ><spring:message code="user.form.lastname"/></label>
                    </div>
                    <div class="formRightDiv">
                        <form:input path="lastName" cssClass="form-control"/>
                    </div>
                </div>
                <div class="formRow">
                    <div class="formLeftDiv">
                        <label class="input-label"><spring:message code="user.form.role"/></label>
                    </div>
                    <div class="formRightDiv">
                        <form:select path="role" cssClass="form-control">
                            <option value="0">------- Select Any -------</option>
                            <form:options items="${roleType}" itemLabel="label" itemValue="name"/>
                        </form:select>
                    </div>
                </div>
                <div class="formRow">
                    <div class="formLeftDiv">
                    </div>
                    <div class="formRightDiv">
                        <div class="col-md-6">
                            <input type="button" class="upsertUser btn btn-primary btn-block"
                                   value="<c:choose><c:when test="${user.id > 0}"><spring:message code="save"/></c:when><c:otherwise><spring:message code="create"/></c:otherwise></c:choose>">
                        </div>
                        <div class="col-md-6">
                            <input type="button" class="cancel btn btn-default btn-block" value="Cancel">
                        </div>
                    </div>
                </div>
                <div style="clear: both;"></div>
            </form:form>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var $upsertUser = $(".upsertUser");
        var $cancel = $(".cancel");
        var $form = $('#userForm');
        var $countDownTimer = $('#countDownTimer');
        var $isAdminUserCreated = '${isAdminUserCreated}';
        var validateJson = {
            rules: {
                email: {
                    required: true,
                    email: true
                },
                username: "required",
                firstName: "required",
                lastName: "required",
                password: {
                    minlength: 8,
                    maxlength: 24
                },
                confirmPassword: {
                    minlength: 8,
                    equalTo: "#password"
                },
                role: {
                    required: true
                }
            },
            messages: {
                email: {
                    required: "Please enter email address.",
                    email: "Invalid E-mail Address."

                },
                username: "Please enter your username.",
                firstName: "Please enter first name.",
                lastName: "Please enter last name.",
                password: {
                    minlength: "Min 8 Characters.",
                    maxlength: "Max 24 Characters."
                },
                confirmPassword: {
                    minlength: "Min 8 Characters.",
                    maxlength: "Max 24 Characters.",
                    equalTo: "Password and Confirm password are not match."
                },
                role: {
                    required: "Select any role."
                }
            }
        };

        $upsertUser.click(function () {
            if ($form.validate(validateJson).form()) {
                $form.submit();
            }
        });

        $cancel.click(function () {
            window.location = "/admin/userList"
        });

        if($isAdminUserCreated && ($isAdminUserCreated == true || $isAdminUserCreated == 'true')){
            var time = 5;
            setInterval(function(){
                        time = time-1;
                        if(time == 0)
                                window.location = "/login";
                        else
                            $countDownTimer.html(" Redirecting to login within <b>"+(time)+"</b> seconds.");
            },1000);
        }
    });
</script>