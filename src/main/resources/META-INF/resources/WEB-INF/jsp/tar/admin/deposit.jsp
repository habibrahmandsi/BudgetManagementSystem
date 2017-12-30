<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="shareHolder.header"/></title>


<script type="text/javascript" src="<c:url value="/scripts/jquery.typeahead.min.js"/>"></script>
<link type="text/css" rel="stylesheet" href="<c:url value="/styles/jquery.typeahead.css"/>"/>
<script type="text/javascript" src="<c:url value="/scripts/bootstrapValidator.min.js"/>"></script>

<!-- /.row -->
<div class="row">
    <form:form method="post" id="depositForm" commandName="deposit">
        <form:hidden path="id"/>
        <form:input path="shareHolder.id" cssClass="hidden shareHolderId"/>
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <spring:message code="deposit.new.btn"/> Form
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label><spring:message code="shareHolder.form.userName"/><span
                                        class="required">*</span></label>

                                <%--<div class="col-lg-12 noLeftRightPadding">--%>
                                    <div class="form-group">
                                    <div class="typeahead-container">
                                        <div class="typeahead-field">
                                     <span class="typeahead-query">
                                     <input class="shareHolderName form-control" name="name" type="search" autofocus="true"
                                            autocomplete="off">
                                     </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                           <div class="form-group">
                                <label><spring:message code="installment.new.btn"/><span
                                        class="required">*</span></label>
                                <form:select path="installment.id" cssClass="form-control">
                                    <option value=""> -- Select Any One --</option>
                                    <c:forEach items="${installmentList}" var="pg">
                                        <c:choose>
                                            <c:when test="${pg.id == deposit.installment.id}">
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
                                <label><spring:message code="deposit.payment.method"/><span
                                        class="required">*</span></label>
                                <form:select path="method" cssClass="form-control">
                                    <option value=""> -- Select Any One --</option>
                                    <form:options items="${paymentMethodList}" itemLabel="label" itemValue="name"/>
                                </form:select>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="deposit.reference.no"/><span
                                        class="required">*</span></label>
                                <form:input path="referenceNo" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="deposit.amount"/><span
                                        class="required">*</span></label>
                                <form:input path="amount" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="col-lg-6 detailsDiv">
                        </div>
                        <!-- /.row (nested) -->
                    </div>
                    <div class="row">
                        <div class="col-lg-3"></div>
                        <div class="col-lg-3">
                            <button type="reset" class="btn btn-danger btn-block">Cancel</button>
                        </div>
                        <div class="col-lg-3">
                            <button type="submit" class="btn btn-success btn-block">Submit</button>
                        </div>
                        <div class="col-lg-3"></div>
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
        makeTabularAutoCompleteForUser(".shareHolderName", '/admin/getShareHolderForAutoComplete.do', "name", function (data) {
            obj = data;
            $(".shareHolderId").val(obj.id);
            console.log("SMNLOG:employee:" + JSON.stringify(obj));
            reDrawShareHolderDetails(obj);

        });

        /* initial form validation declaration */
        console.log('initiliaze validation....');

        depositFormValidation();
    });

    function depositFormValidation() {
        $("#depositForm").bootstrapValidator({
            fields: {
                name: {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                },
                method: {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                },
                referenceNo: {
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
                },
                "installment.id": {
                    validators: {
                        notEmpty: {
                            message: "Not Empty"
                        }
                    }
                }
            }
        });
    }
    function makeTabularAutoCompleteForUser(inputIdOrClass, url, searchColName, callback) {
        $(inputIdOrClass).typeahead({
            minLength: 1,
            maxItem: 10,
            order: "asc",
            dynamic: true,
            group: [true, '<table  class="table headerSuggestions table-striped"><thead>'
            + '<tr>'
            + '<td style="width:55px;">Photo</td>'
            + '<td style="width:100px;">Name</td>'
            + '<td style="width:80px;">Fathers Name</td>'
            + '<td style="width:80px;">Mothers Name</td>'
            + '<td style="width:100px;">Mobile No</td>'
            + '</tr></thead><tbody>'],
            delay: 0,
            backdrop: {
                "background-color": "#fff"
            },
            template: '<table  class="table table-striped"><tbody>'
            + '<tr id="{{id}}">'
            + '<td style="width:55px">'
            + '<img class="roundImage" src="/admin/getImage.do?imgId={{photoName}}&sex={{sex}}"/>'
            + '</td>'
            + '<td style="width:100px">'
            + '{{name}}'
            + '</td>'
            + '<td style="width:80px">'
            + '{{fathersName}}'
            + '</td>'
            + '<td style="width:80px">'
            + '{{mothersName}}'
            + '</td>'
            + '<td style="width:100px">'
            + '{{mobile}}'
            + '</td>'
            + '</tr>'
            + '</tbody>'
            + '</table>',
            source: {
                user: {
                    display: searchColName,
                    data: [{
                        "id": 1,
                        "photoPath": "",
                        "photoName": "",
                        "name": "",
                        "sex": "",
                        "fathersName": "",
                        "mothersName": "",
                        "nationalId": "",
                        "mobile": ""
                    }],
                    url: [{
                        type: "GET",
                        url: url,

                        data: {
                            sSearch: "{{query}}"
                        },
                        callback: {
                            done: function (data) {
                                /*for (var i = 0; i < data.data.employeeList.length; i++) {
                                    if (typeof data.data.employeeList[i].current_designation == 'undefined'
                                        || data.data.employeeList[i].current_designation == null) {
                                        data.data.employeeList[i].current_designation = ' ';
                                        //console.log("SMNLOG:inside 2:"+data.data.employeeList[i].current_designation);
                                    }
                                    if (typeof data.data.employeeList[i].employee_unique_id == 'undefined'
                                        || data.data.employeeList[i].employee_unique_id == null || data.data.employeeList[i].employee_unique_id.length < 1) {
                                        data.data.employeeList[i].employee_unique_id = ' ';
                                        //console.log("SMNLOG:inside 3:"+data.data.employeeList[i].mobile);
                                    }
                                    console.log("SMNLOG:" + data.data.employeeList[i].photo_name);
                                    if (typeof data.data.employeeList[i].photo_name == 'undefined'
                                        || data.data.employeeList[i].photo_name == null || data.data.employeeList[i].photo_name.length < 1) {
                                        data.data.employeeList[i].photo_name = '';
                                        //console.log("SMNLOG:inside 3:"+data.data.employeeList[i].mobile);
                                    }

                                }*/
                                return data;
                            }
                        }
                    }, "data.shareHolderList"]
                }
            },
            callback: {
                onClick: function (node, a, item, event) {
                    // You can do a simple window.location of the item.href
                    if (typeof callback === 'function') {
                        callback(item);
                    }

                },
                onClickAfter: function (node, a, item, event) {
                    $(".addLineItem").focus();
                },
                onSendRequest: function (node, query) {
                    //console.log('request is sent, perhaps add a loading animation?')
                },
                onReceiveRequest: function (node, query) {
                    //console.log('request is received, stop the loading animation?')
                }
            },
            debug: true
        });
    }


    function reDrawShareHolderDetails(empObject){
        var html = '<div class="shareHolderDetailsDiv shareHolderDetailsWrapperDiv">'
            +'<table class="table detailsInfoTable">'
            +'<thead>'
            +'<th colspan="4" class="headerFont">Basic Information</th>'
            +'</thead>'
            +'<tbody>'
            +'<tr>'
            +'<td>Name :</td>'
            +'<td>'+empObject.name+'</td>'
            +'<td rowspan="4">'
            +'<div class="shareHolderPicWrapperDiv">'
            +'<img src="/admin/getImage.do?imgId='+empObject.photoName+'&sex='+empObject.sex+'&imgSize=5">'
            +'</div>'
            +'</td>'
            +'</tr>'
            +'<tr>'
            +"<td>Father's Name :</td>"
            +'<td>'+empObject["fathersName"]+'</td>'
            +'</tr>'
            +'<tr>'
            +"<td>Mother's Name :</td>"
            +'<td>'+empObject["mothersName"]+'</td>'
            +'<td></td>'
            +'</tr>'
            +'<tr>'
            +' <td>Gender :</td>'
            +'<td>'+empObject.sex+'</td>'
            +' <td></td>'
            +' </tr>'
            +' <tr>'
            +' <td>National Id :</td>'
            +' <td>'+empObject.national_id+'</td>'
            +'<td></td>'
            +' </tr>'
            +' <tr>'
            +' <td>E-mail :</td>'
            +' <td>'+empObject.email+'</td>'
            +'<td></td>'
            +' </tr>'
            +' <tr>'
            +' <td>Contact Number :</td>'
            +' <td>'+empObject.mobile+'</td>'
            +'<td></td>'
            +' </tr>'
            +'</tbody>'
            +' </table>'
            +' </div>';

        $(".detailsDiv").html(html);
    }
</script>