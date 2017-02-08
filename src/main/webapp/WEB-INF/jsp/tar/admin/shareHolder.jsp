<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="shareHolder.header"/></title>

<script src="<%= contextPath %>/resources/webcam/jquery.webcam.js" type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/common/shareHolder.js" type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/validation/bootstrapValidator.min.js"></script>


<!-- /.row -->
<div class="row">
    <form:form method="post" id="shareHolderForm" commandName="shareHolder" enctype="multipart/form-data">
        <form:hidden path="id"/>
        <form:hidden path="photoPath"/>
        <form:hidden path="photoName"/>
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Share Holder Entry Form
                </div>
                <div class="panel-body">

                    <div class="row">
                        <div class="col-lg-3"></div>
                        <div class="col-lg-3">
                                <%--${share Holder.educationInfoList}--%>
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.name"/><span class="required">*</span></label>
                                <form:input path="name" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.father.name"/></label>
                                <form:input path="fathersName" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.mother.name"/></label>
                                <form:input path="mothersName" cssClass="form-control"/>
                            </div>

                            <div class="form-group spouseName">
                                <label><spring:message code="shareHolder.form.spouse.name"/></label>
                                <form:input path="spouseName" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.religion"/></label>
                                <form:select path="religion" cssClass="form-control">
                                    <form:option value="Islam">Islam</form:option>
                                    <form:option value="Hinduism">Hinduism</form:option>
                                    <form:option value="Buddhism">Buddhism</form:option>
                                    <form:option value="Christianity">Christianity</form:option>
                                    <form:option value="Other">Other</form:option>
                                </form:select>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.sex"/></label>
                                <form:select path="sex" cssClass="form-control">
                                    <form:option value="male">Male</form:option>
                                    <form:option value="female">Female</form:option>
                                    <form:option value="others">Others</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="col-lg-3">
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.nationalIdNo"/></label>
                                <form:input path="nationalId" cssClass="form-control"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.email"/><span class="required"></span></label>
                                <form:input path="email" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.cellNumber"/><span class="required"></span></label>
                                <form:input path="mobile" cssClass="form-control"/>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.present.address"/></label>
                                <form:textarea path="currentAddress" cssClass="form-control" cssStyle="min-height:75px;"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.permanent.address"/></label>
                                <form:textarea path="permanentAddress" cssClass="form-control" cssStyle="min-height:75px;"/>
                            </div>
                            <form:hidden path="binaryFileData" id="binaryFileData"/>
                        </div>
                        <div class="col-lg-3">
                            <div id="photoUploadDiv" style="text-align:center; padding-left: 0px; padding-right: 0px;"
                                 class="col-lg-12">
                                <input path="fileData" name="fileData" type="file" id="fileData" class="hidden"/>
                                <%--<img id="photoPreview" src="/images/employeeIcon.png"--%>
                                <img id="photoPreview" src="/admin/getImage.do?imgId=${shareHolder.photoName}&imgSize=8"
                                     alt="your image"
                                     class="previewPhoto"/>
                            </div>
                            <div class="col-lg-12">
                                <div class="col-lg-3"></div>
                                <div class="col-lg-6">
                                    <button class="btn btn-block btn-warning imgUploadBtn" type="button">
                                        <spring:message
                                                code="button.change"/></button>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                        </div>

                    </div>

                    <div class="row col-lg-12">
                        <div class="col-lg-4"></div>
                        <div class="col-lg-2">
                            <button class="btn btn-block btn-danger" type="reset"><spring:message
                                    code="button.cancel"/></button>
                        </div>
                        <div class="col-lg-2">
                            <button class="btn btn-block btn-success" type="submit"><spring:message
                                    code="button.save"/></button>
                        </div>
                        <div class="col-lg-4"></div>
                    </div>
                    <!-- /.panel-body -->
                <!-- /.panel -->
            </div>

            <!-- /.col-lg-12 -->
        </div>
        </div>
    </form:form>
    <!-- /.row -->
</div>
<!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

<script>
    $(document).ready(function () {
        var contextPath = '<%= contextPath %>';
        var empId = '${shareHolder.id}';

        $(document).on("click",".crossBtn", function () {
            $(this).closest("div.input-group").find("input").val("");
        });

        $(".imgUploadBtn").click(function () {
            $("#fileData").trigger("click");
        });

        $("#fileData").change(function () {
            readURL(this);
        });
    });

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#photoPreview').attr('src', e.target.result);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }

</script>