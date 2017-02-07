/**
 * Created by habib on 31/10/2016.
 */

var rowDisplayGlobal = 5;

$(document).ajaxError(function (event, jqxhr) {
    if (Recenseo.Tar.HTTP_UNAUTHORISED_ERROR_CODE == jqxhr.status) {
        console.log("AJAX ERROR:: Session timeout...");
        location.reload();
    }
});

$(document).on('click','.close',function () {
    $("#allMessage").hide();
});

function commonDataTableInit(tableIdOrCss, url, columns, sortArr, callBack) {
    return $(tableIdOrCss).dataTable({
        "aLengthMenu": [[5, 10, 20, -1], [5, 10, 20, 'All']],
        "iDisplayLength": rowDisplayGlobal,
        "dom": '<"top"f>rt<"bottom"ip><"clear">',
        "sPaginationType": "simple_numbers", // you can also give here 'simple','simple_numbers','full','full_numbers'
        "oLanguage": {
            "sSearch": "Search:"
        },
        "ajax": url,
        "processing": true,
        "serverSide": true,
        "searching": true,
        "fnDrawCallback":function(){
            if(typeof callBack == 'function'){
                callBack();
            }
        },
        "aoColumns": columns,
        "aaSorting": sortArr //[[ 0, "asc" ],[ 1, "desc" ]] // Sort by first column descending
    });
}

function ConvertFormToJSON(form){
    var array = jQuery(form).serializeArray();
    var json = {};

    jQuery.each(array, function() {
        json[this.name] = this.value || '';
    });

    return json;
}
