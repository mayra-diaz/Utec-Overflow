function getCourses(){
    $("#all_content").empty()

    $("#title").append("<h1>CURSOS</h1>")

    container = "<div class=\'row course_boxes\' id=\'all_courses\'></div>"
    $("#all_content").append(container);
    putCourses()
}

function putCourses(){
    $("#all_courses").empty()

    bigTemplate = "<div class=\'col-lg-4 course_box\' >";
    bigTemplate += "<div class=\'card\'>";
    bigTemplate += "<img class=\'card-img-top\' src=\'../images/SEMESTERIDNUMBER.png\' id=\imagenesCiclo\'>"
    bigTemplate += "<div class=\'card-body text-center\'>"
    bigTemplate += "<div class=\'card-title\'><a href=\'courses.html\'>Ciclo SNUMBER</a></div>"
    bigTemplate += "<div class=\'card-text\'CURSOS</div>"
    bigTemplate += "</div><div class\'price_box d-flex flex-row align-items-center\'>"
    bigTemplate += "<div class= \'course_author_image\'>"
    bigTemplate += "</div><div><select id=\'cicloSelect\' name=\'ciclo1\' onchange=\'goToQuestions(this.value)\'>"
    bigTemplate += "<option disabled selected>Elegir un curso</option>"

    bigEndTemplate = "</select> </div> </div> </div> </div>"

    course_div = "<option value=\'GOTOID\'>COURSENAME</option>"

    $.getJSON("/courses", function(data){
      console.log(data);
        let i = 1;
        let prev = 0;
        let rowc = 0;
        let template = ""
        $.each(data, function(){
        if (i < 25){
            if (prev != data[i]['semester']){
                template += bigEndTemplate
                $("#all_courses").append(template);
                auxBig = bigTemplate.replace('IDNUMBER', data[i]['semester'])
                auxBig = auxBig.replace('SNUMBER', data[i]['semester'])
                template = auxBig
            }
            aux = course_div.replace('COURSENAME', data[i]['name'])
            aux = aux.replace('GOTOID', data[i]['id'])
            template += aux
            prev = data[i]['semester']
            i = i+1;
            }
        });
      });
    }

function goToQuestions(course_id){
    $("#all_content").empty()
    $("#title").empty()
    $("#title").append("<h1>PREGUNTAS</h1>")

    container = "<div class=\'accordion\' id=\'question_list\'></div>"
    $("#all_content").append(container);
    putQuestions(course_id)
}

function putQuestions(course_id){
    $("#question_list").empty()
    var all_questions_response = [];
    url = "/questions_wa/" + course_id;
    $.getJSON(url, function(questions){
        console.log(questions);
        let pos = 0;
        $.each(questions, function(){
          console.log(questions[pos]['content']);
          template = "<div class=\'card\'\'>"
          template += "<div class=\'card-header\' id=BINDINGQUESTIONID></div></div>"
          template += "<h5 class=\'mb-0\'><button class=\btn btn-link btn-block text-left\' type=\'button\' data-toggle=\'collapse\'"
          template += "data-target=\'#PUTANSWERSHERE\' aria-expanded=\'true\' aria-controls=\'PUTANSWERSHERE\'>"
          template += "QUESTIONCONTENT </button></h5></div>"
          template += "<div id=\'PUTANSWERSHERE\' class=\'collapse show\' aria-labelledby=BINDINGQUESTIONID data-parent=\'#question_list\'>"
          template += "<div class=\'card-body\' id=\'answersANSWERSFORQUESTIONID\'>"

          template = template.replace('QUESTIONCONTENT', questions[pos]['content'])
          template = template.replace('ANSWERSFORQUESTIONID', questions[pos]['id'])

          template = template.replace('BINDINGQUESTIONID', "question"+questions[pos]['id']);
          template = template.replace('BINDINGQUESTIONID', "question"+questions[pos]['id']);
          template = template.replace('PUTANSWERSHERE', "answers_list"+questions[pos]['id']);
          template = template.replace('PUTANSWERSHERE', "answers_list"+questions[pos]['id']);
          template = template.replace('PUTANSWERSHERE', "answers_list"+questions[pos]['id']);

          let j = 0;
          answers = questions[pos]['answers'];
          console.log('answers', questions[pos]['answers']);
          $.each(answers, function(){
             aux += "<div class=\'card\'><div class=\'card-body\'> TEXTANSWER</div></div>";
             aux += aux.replace('TEXTANSWER', answers[j]['content']);
             template += aux
             console.log(template)
             j = j+1;
          });

          template += "</div>"

          console.log(template);
          $("#question_list").append(template);

          pos = pos+1;
        });
    });
  }
