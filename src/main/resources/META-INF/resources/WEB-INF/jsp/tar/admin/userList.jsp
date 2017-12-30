<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<link type="text/css" rel="stylesheet" href="<c:url value="/styles/datatable/datatables.bootstrap.css"/>"/>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/dataTables.responsive.js"/>"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="user.list.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-1"></div>
            <div class="col-lg-10">
                <div class="pull-right">
                    <input type="button" class="createUser btn btn-primary"
                           value="<spring:message code="user.form.create.title"/>">
                </div>
            </div>
            <div class="col-lg-1"></div>

        </div>
        <div class="row">
            <div class="col-lg-1"></div>
            <div class="col-lg-10">
                <table id="datatableUserList" class="table table-striped table-hover dataTable">
                </table>
            </div>
            <div class="col-lg-1"></div>
        </div>
        <div class="userDetailsList"></div>
    </div>

</div>

<script type="text/javascript">
    $(function () {
        var disabledRowId = [];
        var $createBtn = $(".createUser");
        $createBtn.click(function () {
//            window.location = "/admin/user";
            Recenseo.Tar.addUser();
        })
        var userId = +'${userId}';
        console.log("userId:: "+userId);

        // datatable initialization
        var url = "/admin/getUsers";
        var sort = [[0, "asc"]];
        var columns = [

            {
                "sTitle": "Name", "mData": null, "bSortable": true, "render": function (data) {
                return '<a href="javascript:void(0)" class="userProjectDetails cursorPointer" data-id="'+data.id+'">'+data.firstname + " " + data.lastname+'</a>';
            }
            },
            {"sTitle": "Username", "mData": "username", "bSortable": true},
            {"sTitle": "Email Address", "mData": "email", "bSortable": true},
            {
                "sTitle": "Active", "mData": null, "bSortable": true, "render": function (data) {
                if(data.enabled == false && _.indexOf(disabledRowId, data.id) == -1)
                    disabledRowId.push(data.id);

                return data.enabled == true ? '<input type="checkbox" checked>' : '<input type="text" value="'+data.id+'" style="display:none;"><input type="checkbox">';
            }
            },
            {
                "sTitle": "Action icons", "mData": null, "bSortable": false, "render": function (data) {
                var html = '<a href="javascript:void(0)" onclick="Recenseo.Tar.addUser(' + data.id + ')" class="editUser" title="Edit user" href="/admin/user?userId=' + data.id + '"><img class="icon" src="/images/edit-icon.png"></a>';
                html += '<a class="manageProject" title="Manage Project" href="/admin/manageUserProject?userId=' + data.id + '"><img class="icon" src="/images/manage-project-icon.png"></a>';

                if (data.enabled == true) {
                    html += '<a class="disableUser" title="Disable user" href="/admin/userStatusChange?userId=' + data.id
                            + '&isEnable=false"><img class="icon" src="/images/disabled-icon.png"></a>';
                } else {
                    html += '<a class="enableUser" title="Enable user" href="/admin/userStatusChange?userId=' + data.id
                            + '&isEnable=true"><img class="icon" src="/images/enabled-icon.png"></a>';
                }

                html += '<a class="deleteUser" title="Delete user" href="/admin/deleteUser?userId=' + data.id + '"><img class="icon" src="/images/delete-icon.png"></a>';
                return html;
            }
            }
        ];

        commonDataTableInit("#datatableUserList", url, columns, sort, function(){
            // If user id is greater than 0 than keep selected userId for project detais
            if(userId > 0){
                $(document).find('[data-id='+userId+']').trigger('click');
            }
            console.log("SMNLOG:: "+JSON.stringify(disabledRowId));
            $.each(disabledRowId, function(index, val){
                $(document).find("#datatableUserList tbody tr td").find("input[value='"+val+"']").closest("tr").addClass("disabled");
            });
        });

        $(".dataTables_filter").find("input").focus();

        $(document).on('click', '.disableUser', function () {
            return confirm('Are you sure to disable this user?')
        });

        $(document).on('click', '.enableUser', function () {
            return confirm('Are you sure to enable this user?')
        });

        $(document).on('click', '.deleteUser', function () {
            return confirm('Are you sure to delete this user?')
        });

        $(document).on('click', '.userProjectDetails', function () {
            var usetId = $(this).attr('data-id');
            $(document).find("a.userProjectDetails").removeClass("userSelected");
            $(this).addClass("userSelected");

            Recenseo.Tar._blockUI("User Project List showing...");
            $.get('/admin/userProjectList', {userId:usetId}, function(data) {
                if (!data.error) {
                    $(".userDetailsList").html(data);
                    Recenseo.Tar._unblockUI();
                } else {
                    //TODO: Handle exception here
                    Recenseo.Tar._unblockUI();
                }
            });
        });
    });
</script>