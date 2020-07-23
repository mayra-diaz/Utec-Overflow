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
    bigTemplate += "</div><div><select id=\'cicloSelect\' name=\'ciclo1\' onclick=goToQuestions(this.value);\'>"
    bigTemplate += "<option disabled selected>Elegir un curso</option>"

    bigEndTemplate = "</select> </div> </div> </div> </div>"

    course_div = "<option value=\'GOTOID\'>COURSENAME</option>"
 
    $.getJSON("/courses", function(data){
        let i = 1;
        let prev = 0;
        let rowc = 0;
        let template = ""
        $.each(data, function(){
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
        });
      });
    }

function goToQuestions(course_id){
    console.log("Holi")
    $("#all_content").empty()

    $("#title").append("<h1>PREGUNTAS</h1>")

    container = "<div class=\'card\' id=\'all_questions\'></div>"
    $("#all_content").append(container);
    putQuestions(course_id)
}

function putQuestions(course_id){
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
