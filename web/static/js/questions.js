function putQuestions(){
  $("#question_list").empty()

  $.getJSON("/questions/" + course_id, function(data){

    let i = 0;
    $.each(data, function(){
      ALLANSWERS = ""

      template = "<div class=\'card\' id=\'questions_list\'>"
      template += "<div class=\'card-header\' id=\'" + i + "\'>"
      template += "<h5 class=\'mb-0\'><button class=\btn btn-link\' data-toggle=\'collapse\'"
      template += "data-target=\'#collapseOne\' aria-expanded=\'true\' aria-controls=\'collapseOne\'>"
      template += "QUESTION </button></h5></div>"
      template += "<div id=\'collapseOne\' class=\'collapse show\' aria-labelledby=\'" + i + "\' data-parent=\'#accordion\'>"
      template += "<div class=\'card-body\'>"

      template = template.replace('QUESTION', data[i]['content'])
      
      $.getJSON("/answers/" + data[i]['id'], function(answers){
        let j = 0;
        $.each(answers, function(){
          ans = "<div class=\'card\'><div class=\'card-body\'> TEXTANSWER</div></div>"
          ans = ans.replace('TEXTANSWER', answers[i]['content'])
          ALLANSWERS += ans
          j = j+1;
        });
      });

      template += ALLANSWERS + "</div></div></div>"

      $("#all_questions").append(template);
      i = i+1;
    });
  });
}