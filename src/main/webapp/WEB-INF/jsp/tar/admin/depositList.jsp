<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<link type="text/css" rel="stylesheet" href="<c:url value="/styles/datatable/datatables.bootstrap.css"/>"/>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/datatables.bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/datatable/dataTables.responsive.js"/>"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <spring:message code="deposit.list.title"/>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
                <div class="pull-right">
                    <input type="button" class="addNewDeposit btn btn-primary"
                           value="<spring:message code="deposit.new.btn"/>">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <table id="depositListTable" class="table table-striped table-hover dataTable">
                </table>
            </div>
        </div>
        <div class="depositDetailsList"></div>
    </div>

</div>

<script type="text/javascript">
    $(function () {
        var disabledRowId = [];
        var $createBtn = $(".addNewDeposit");
        $createBtn.click(function () {
            window.location = "/admin/user";
        });

        // datatable initialization
        var url = "/admin/getDeposits.do";
        var sort = [[1, "asc"]];
        var columns = [
            {
                "sTitle": "Photo", "mData": null, "bSortable": false, "render": function (data) {
                return ' <img class="roundImage" src="/admin/getImage.do?imgId='+data.photoName+'&sex='+data.sex+'"/>'
            }
            },
            {
                "sTitle": "Name", "mData": null, "bSortable": true, "render": function (data) {
                return '<a href="javascript:void(0)" class="userProjectDetails cursorPointer" data-id="'+data.id+'">'+data.name+'</a>';
            }
            },
            {"sTitle": "Father's Name", "mData": "fathersName", "bSortable": true},
            {"sTitle": "Mother's Name", "mData": "mothersName", "bSortable": true},
            {"sTitle": "Mobile No.", "mData": "mobile", "bSortable": true},
            {"sTitle": "Deposit Amount", "mData": "amount", "bSortable": true},
            {"sTitle": "Payment method", "mData": "mobile", "bSortable": true},
            {"sTitle": "Reference No.", "mData": "mobile", "bSortable": true},
            {"sTitle": "Added By", "mData": "mobile", "bSortable": true},
            {"sTitle": "", "mData": "mobile", "bSortable": true},
            {
                "sTitle": "Action icons", "mData": null, "bSortable": false, "render": function (data) {
                var html = '<a class="editUser" title="Edit Share Holder" href="/admin/upsertShareHolder.do?shareHolderId=' + data.id + '"><img class="icon" src="/images/edit-icon.png"></a>';
                html += '<a class="editUser" title="Details info of Share Holder" href="/admin/shareHolderDetails.do?shareHolderId=' + data.id + '"><img class="icon" src="/images/details-icon.png"></a>';
                html += '<a class="deleteUser" title="Delete user" href="/admin/deleteUser?userId=' + data.id + '"><img class="icon" src="/images/delete-icon.png"></a>';
                return html;
            }
            }
        ];

        commonDataTableInit("#depositListTable", url, columns, sort, function(){
        });

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