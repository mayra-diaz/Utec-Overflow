
//Tengo que pasar el course_id 
function get_all_questions(course_id){
  get_course_title(course_id);
  $("#questions").empty();
  $.getJSON("/questions/" + course_id, function(data){

    let i = 0;
    $.each(data, function(){
      template = '<span id="IndividualQuestion">content</span>';
      template = template.replace('content', data[i]['content']);
      console.log(template);
      $("#questions").append(template);
      i = i+1;
    });

  });
}

function get_course_title(id){
  $.getJSON("/courses/" + id, function(data){
    console.log(data);
    $("cursoTitulo").empty();
    template = '<a>name</a>';
    template = template.replace('name', data['name']);
    console.log(template);
    $("#cursoTitulo").append(template);
  });
}
