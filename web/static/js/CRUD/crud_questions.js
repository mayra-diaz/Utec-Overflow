$(function(){
    var url_questions = '/questions'
    var url_users = "/users"
    var url_courses = "/courses"
    $("#grid").dxDataGrid({
        dataSource: DevExpress.data.AspNet.createStore({
            key: "id",
            loadUrl: url_questions,
            insertUrl: url_questions,
            updateUrl: url_questions,
            deleteUrl: url_questions,
            onBeforeSend: function(method, ajaxOptions) {
                ajaxOptions.xhrFields = { withCredentials: true };
            }
        }),

        editing: {
            allowUpdating: true,
            allowDeleting: true,
            allowAdding: true
        },

        remoteOperations: {
          sorting: true,
          paging: true
        },

        paging: {
            pageSize: 12
        },

        pager: {
            showPageSizeSelector: false,
            allowedPageSizes: [8, 12, 20]
        },

        columns: [{
            dataField: "id",
            dataType: "number",
            allowEditing: false
        }, {
            dataField: "user_id",
            caption: "user",
            lookup: {
              dataSource: DevExpress.data.AspNet.createStore({
                key: "id",
                loadUrl: url_users,
                onBeforeSend: function (method, ajaxOptions){
                  ajaxOptions.xhrFields = { withCredentials: true };
                }
              }),
              valueExpr: "id",
              displayExpr:"username"
            }
        }, {
          dataField: "course_id",
          caption: "course",
          lookup: {
            dataSource: DevExpress.data.AspNet.createStore({
              key: "id",
              loadUrl: url_courses,
              onBeforeSend: function (method, ajaxOptions){
                ajaxOptions.xhrFields = { withCredentials: true };
              }
            }),
            valueExpr: "id",
            displayExpr:"name"
          }
        }, {
          dataField: "sent_on",
          dataType: "date",
          format: "MM/dd/yyy hh:mm",
          allowEditing: false
        }, {
            dataField: "content"
        },],
    }).dxDataGrid("instance");
});
