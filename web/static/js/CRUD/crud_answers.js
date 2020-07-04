$(function(){
    var url_answers = '/answers'
    var url_questions = '/questions'
    var url_users = "/users"
    $("#grid").dxDataGrid({
        dataSource: DevExpress.data.AspNet.createStore({
            key: "id",
            loadUrl: url_answers,
            insertUrl: url_answers,
            updateUrl: url_answers,
            deleteUrl: url_answers,
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
              loadUrl: url_questions,
              onBeforeSend: function (method, ajaxOptions){
                ajaxOptions.xhrFields = { withCredentials: true };
              }
            }),
            valueExpr: "id",
            displayExpr:"question_course_id"
          }
        }, {
          dataField: "question_id",
          caption: "question",
          lookup: {
            dataSource: DevExpress.data.AspNet.createStore({
              key: "id",
              loadUrl: url_questions,
              onBeforeSend: function (method, ajaxOptions){
                ajaxOptions.xhrFields = { withCredentials: true };
              }
            }),
            valueExpr: "id",
            displayExpr:"content"
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
