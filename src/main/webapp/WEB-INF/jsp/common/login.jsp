<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<form action="/login" method="post" onsubmit="return validate();">
        <div class="login-wrapper-div">
            <div class="login-div">
                <div id="product">BMS<sup style="font-size:24px">&trade;</sup></div>
			<div>
                <label class="input-label">Username</label>
				<input id="username" type="text" class="style-4" name="username" placeholder="User Name" />
			</div>
			<div>
                <label class="input-label">Password</label>
				<input id="password" type="password" class="style-4" name="password" placeholder="Password" />
            </div>
			<div>
				<input type="submit" value="Sign In" class="button btn-info" />
				<a href="/forgotPassword">Forgot password?</a>
			</div>
			<c:if test="${param.error ne null}">
				<div class="alert-danger">Invalid username and password.</div>
			</c:if>
			<c:if test="${param.logout ne null}">
				<div class="alert-normal">You have been logged out.</div>
			</c:if>

                <div class="alignCenter">
                    <div id="presented">Presented By:</div>
                    <img style="height: 25px;" src="/images/Macrosoft-logo.png">
                </div>
		</div>
		</div>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>

<script type="text/javascript" src="<c:url value="/scripts/jquery.js"/>" ></script>
<script>
	function validate() {
		try {
			var $user = $('#username');
			$user.val(($user.val() || '').trim().toLowerCase());
			if ($user.val().trim() == "") {
				alert("Please enter your user name.\n");
				return false;
			}
			if ($("#password").val().trim() == "") {
				alert("Please enter your password.\n");
				return false;
			}
		} catch (ignore) {
		}
		return true;
	}
	$(document).ready(function() {
		$('#username').change(function() {
			this.value = this.value.toLowerCase();
		}).focus();
	});
</script>