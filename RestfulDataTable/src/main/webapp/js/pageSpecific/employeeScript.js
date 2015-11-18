/**
 *
 * @author Ashok
 */
var employeeJs = (function() {
    var dataTable = "#example";
    var _spinnerShown = false;
    var _recount = false;
    var _oTable;
    var myButtons = {
        add: {
            text: 'Add',
            click: function () {
                sendAddEditEmployeeRequest("add");
                $(this).dialog('close');
                fillEmployeeAddEditDialog("");
            }
        },
        edit: {
            text: 'Edit',
            click: function () {
                sendAddEditEmployeeRequest("edit");
                $(this).dialog('close');
                fillEmployeeAddEditDialog("");
            }
        },
        cancel: {
            text: 'Cancel',
            click: function () {
                $(this).dialog('close');
                fillEmployeeAddEditDialog("");
            }
        }
    };

    var uploadEmployee = function (type) {
        $("#dlg-webuploader").dialog("open");
    };

    var downloadEmployee = function () {
        window.location = "";
    };
    
    var openEditOverlay = function(data) {
        var row = $(data).parents('tr');
        var rowData = _oTable.row(row).data();
        var isAlreadySelected = row.hasClass("selected");
        if(!isAlreadySelected) {
            row.addClass("selected");
            $('#employeeAddEditDialog').on("dialogbeforeclose", function() {
                row.removeClass("selected");
            });
        }
        var buttons = [myButtons.edit, myButtons.cancel];        
        fillEmployeeAddEditDialog(rowData);
        $("#employeeAddEditDialog").dialog({title: "Edit Employee"});
        $('#employeeAddEditDialog').dialog('option', 'buttons', buttons);        
        $("#employeeAddEditDialog").dialog("open");       
    };

    var sendAddEditEmployeeRequest = function (type) {
        var data = {
            ids: $("#employeeId").val(),
            firstName: $("#employeeFirstName").val(),
            lastName: $("#employeeLastName").val(),
            position: $("#employeePosition").val(),
            office: $("#employeeOffice").val(),
            startDate: $("#employeeStartDate").val(),
            salary: $("#employeeSalary").val()
        };
        var uri = (type === "add") ? "/RestfulDataTable/action/add" : "/RestfulDataTable/action/edit";
        $.post(uri, data)
            .done(function () {
                _oTable.ajax.reload();
            })
            .fail(function (data) {
                console.log("Error Message: " + data);
            });
    };

    var sendDeleteEmployeeRequest = function () {
        var ids = $("#employeeIdArray").val();
        $.post("/RestfulDataTable/action/delete", {ids : ids})
            .done(function () {
                _oTable.ajax.reload();
            })
            .fail(function (data) {
                console.log("Error Message: " + data);
            });
    };

    var clearEmployeeDeleteDialog = function () {
        $("#employeeIdArray").val("");
    };

    var fillEmployeeAddEditDialog = function(data) {
        if (data === "" || data === null) {
            $("#employeeAddEditForm input").each(function () {
                $(this).val("");
            });
        }
        else {
            data = $.makeArray(data);
            if(data.length === 1) {
                data = data[0];
                $("#employeeId").val(data.id);
                $("#employeeFirstName").val(data.firstName);
                $("#employeeLastName").val(data.lastName);
                $("#employeePosition").val(data.position);
                $("#employeeOffice").val(data.office);
                $("#employeeStartDate").val(data.startDate);
                $("#employeeSalary").val(data.salary);
            }
            else {
                var ids = [data[0].id];
                var isFirstNameSame = true;
                var isLastNameSame = true;
                var isPositionSame = true;
                var isOfficeSame = true;
                var isStartDateSame = true;
                var isSalarySame = true;               
                
                for(var i = 0; i < data.length-1; i++) {
                    if(isFirstNameSame) {
                        if(data[i].firstName !== data[i+1].firstName) {
                            isFirstNameSame = false;
                            $("#employeeFirstName").val("multiple");
                        } 
                    }
                    if(isLastNameSame) {
                        if(data[i].lasttName !== data[i+1].lastName) {
                            isLastNameSame = false;
                            $("#employeeLastName").val("multiple");
                        } 
                    }
                    if(isPositionSame) {
                        if(data[i].position !== data[i+1].position) {
                            isPositionSame = false;
                            $("#employeePosition").val("multiple");
                        } 
                    }
                    if(isOfficeSame) {
                        if(data[i].office !== data[i+1].office) {
                            isOfficeSame = false;
                            $("#employeeOffice").val("multiple");
                        } 
                    }
                    if(isStartDateSame) {
                        if(data[i].startDate !== data[i+1].startDate) {
                            isStartDateSame = false;
                            $("#employeeStartDate").val("multiple");
                        } 
                    }
                    if(isSalarySame) {
                        if(data[i].salary !== data[i+1].salary) {
                            isSalarySame = false;
                            $("#employeeSalary").val("multiple");
                        } 
                    }
                    ids[i+1] = data[i+1].id;
                }
                
                $("#employeeId").val(ids);
                
                if(isFirstNameSame) {                    
                    $("#employeeFirstName").val(data[0].firstName);                     
                }
                if(isLastNameSame) {                    
                    $("#employeeLastName").val(data[0].lastName);                   
                }
                if(isPositionSame) {                    
                    $("#employeePosition").val(data[0].position);                   
                }
                if(isOfficeSame) {                    
                    $("#employeeOffice").val(data[0].office);                     
                }
                if(isStartDateSame) {                    
                    $("#employeeStartDate").val(data[0].startDate);                    
                }
                if(isSalarySame) {                    
                    $("#employeeSalary").val(data[0].salary);                   
                }
            }
        }
    };

    var init = function () {
        /** START OF DATATABLES **/
        _oTable = $(dataTable).DataTable({
            "bServerSide": true,
            "iCookieDuration": 31536000,
            "select": true,
            "sAjaxSource": "/RestfulDataTable/serverProcess",
            //"iDeferLoading": 0,
            "bProcessing": true,
            "bAutoWidth": false,
            "bStateSave": true,
            "sPaginationType": 'full_numbers',
            "aLengthMenu": [[2, 4, 8, 16, 32], [2, 4, 8, 16, 32]],
            "bJQueryUI": true,
            "sDom": '<"dataTables_wrapper"<"T"<"clear"><"H"Bftipl<"adjust">>rt<"F"pl<"exportOptions">>>',
            "aaSorting": [[4, "desc"]],
            "fnStateSave": function (oSettings, oData) {
                localStorage.setItem('DataTables_uid_' + window.location.pathname, JSON.stringify(oData));
            },
            "fnStateLoad": function (oSettings) {
                var data = localStorage.getItem('DataTables_uid_' + window.location.pathname);
                console.log("StateLoaded");
                return JSON.parse(data);
            },
            "fnServerData": function (sSource, aoData, fnCallback, oSettings) {
                if (!_spinnerShown) {
                    _spinnerShown = true;
                    UTIL.showSpinner();
                }
                oSettings.jqXHR = $.get("/RestfulDataTable/serverProcess", aoData, "json")
                    .done(fnCallback)
                    .fail(function(info) {
                        if (info.responseText == "No Data Found.") {
                            UTIL.sessionExpired();
                        }
                        else {
                            UTIL.showError(info.responseText);
                        }
                    });

            },
            "fnServerParams": function (aoData) {
                aoData.push(
                    {"name": "recount", "value": _recount}
                );
            },
            "oLanguage": {
                "sZeroRecords": "No data to display",
                "sSearch": "Filter"
            },
            "buttons": [
                "selectAll",
                "selectNone",
                {
                    text: "New",
                    action: function (e, dt, button, config) {
                        var buttons = [myButtons.add, myButtons.cancel];
                        fillEmployeeAddEditDialog("");
                        $("#employeeAddEditDialog").dialog({title: "Add Employee"});
                        $('#employeeAddEditDialog').dialog('option', 'buttons', buttons);
                        $("#employeeAddEditDialog").dialog("open");
                    }
                },
                {
                    extend: "selected",
                    text: "Edit",
                    action: function (e, dt, button, config) {
                        //Do edit logic here.
                        var buttons = [myButtons.edit, myButtons.cancel];                        
                        fillEmployeeAddEditDialog(dt.rows({selected: true}).data());
                        $("#employeeAddEditDialog").dialog({title: "Edit Employee"});
                        $('#employeeAddEditDialog').dialog('option', 'buttons', buttons);
                        $("#employeeAddEditDialog").dialog("open");                        
                    }
                },
                {
                    extend: "selected",
                    text: "Delete",
                    action: function (e, dt, button, config) {
                        var ids = dt.rows({selected: true}).data().pluck("id");
                        var idArray = $.makeArray(ids);                              
                        $("#employeeIdArray").val(idArray);
                        $("#employeeDeleteDialog").dialog("open");
                    }
                }
            ],
            "aoColumns": [
                {"mData": "id", "bVisible": false},
                {"mData": "firstName", "mRender": function(data, type, full) { return '<a class="bluelink" onclick="employeeJs.openEditOverlay(this)">' + data + '</a>'; } },
                {"mData": "lastName"},
                {"mData": "position"},
                {"mData": "office"},
                {"mData": "startDate"},
                {"mData": "salary"}
            ],
            "fnPreDrawCallback": function (oSettings) {
            },
            "fnDrawCallback": function (oSettings) {
                $.unblockUI();
                _spinnerShown = false;
            }
        });
        /** END OF DATATABLES **/

        var uploadEmployeeObj = $("<a>").addClass("bluelink").attr({"href": "#", "onclick": "employeeJs.uploadEmployee('csv')"}).html("Upload Employees &raquo;");
        var downloadEmployeeObj = $("<a>").addClass("bluelink margin-l-15").attr({"href": "#", "onclick": "employeeJs.downloadEmployee()"}).html("Download Employees &raquo;");
        var reportExportOptions = $("<div>").addClass("reportExportOptions").append(uploadEmployeeObj).append(downloadEmployeeObj);
        $("div.exportOptions").html(reportExportOptions);

        $("#employeeAddEditDialog").dialog({
            autoOpen: false,
            modal: true,
            resizable: false,
            draggable: false,
            minHeight: 110,
            width: 400,
            show: 'fade',
            hide: 'fade'
        });

        $("#employeeDeleteDialog").dialog({
            autoOpen: false,
            modal: true,
            resizable: false,
            draggable: false,
            minHeight: 110,
            width: 400,
            show: 'fade',
            hide: 'fade',
            buttons: [
                {
                    text: 'Yes',
                    click: function () {
                        sendDeleteEmployeeRequest();
                        $(this).dialog('close');
                        clearEmployeeDeleteDialog();
                    }
                },
                {
                    text: 'No',
                    click: function () {
                        $(this).dialog('close');
                        clearEmployeeDeleteDialog();
                    }
                }
            ]
        });

        $("#dlg-webuploader").dialog({
            autoOpen: false,
            modal: true,
            resizable: false,
            draggable: false,
            minHeight: 110,
            width: 400,
            show: 'fade',
            hide: 'fade',
            buttons: [
                {
                    text: 'Upload File',
                    click: function () {
                        $("#webuploaderform").submit();
                    }
                }
            ]
        });
    };

    return {
        init: init,
        uploadEmployee: uploadEmployee,
        downloadEmployee: downloadEmployee,
        openEditOverlay: openEditOverlay
    };
}());