from flask import Flask, render_template, request, session, Response, redirect
from database import connector
from model import entities
import json
import time
import threading

# OTHERS
from datetime import datetime

db = connector.Manager()
engine = db.createEngine()

app = Flask(__name__)



"""
------------------------------------------------------
                     CACHE
------------------------------------------------------
"""
cache = {}

key_users = "users"
lock_users = threading.Lock()

key_questions = "questions"
lock_questions = threading.Lock()

key_answers = "answers"
lock_answers = threading.Lock()

key_courses = "courses"
lock_courses = threading.Lock()



"""
------------------------------------------------------
                      CRUD USERS
------------------------------------------------------
"""

@app.route('/users', methods=['POST'])
def create_user_form():
    # c = json.loads(request.data)
    c = json.loads(request.form['values'])
    user = entities.User(
        username=c['username'],
        name=c['name'],
        fullname=c['fullname'],
        password=c['password']
    )
    session = db.getSession(engine)
    session.add(user)
    session.commit()
    session.close()
    r_msg = {'msg': 'UserCreated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/users/<id>', methods=['GET'])
def get_user(id):
    if (key_users in cache and (
            datetime.now() - cache[key_users]['datetime']).total_seconds() < 5) or lock_users.locked():
        users = cache[key_users]['data']
    else:
        lock_users.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.User)
        session.close()
        users = dbResponse[:]
        now = datetime.now()
        cache[key_users] = {'data': users, 'datetime': now}
        lock_users.release()
        print('Using db')
    """
    # Without cache
    db_session = db.getSession(engine)
    users = db_session.query(entities.User).filter(entities.User.id == id)
    session.close()
    """
    final_user = []
    for user in users:
        if user['id'] == id:
            final_user.append(user)
            break

    for user in final_user:
        js = json.dumps(final_user, cls=connector.AlchemyEncoder)
        return Response(js, status=200, mimetype='application/json')
    message = {'status': 404, 'message': 'Not Found'}
    return Response(json.dumps(message), status=404, mimetype='application/json')


@app.route('/users', methods=['GET'])
def get_users():
    users = []
    if (key_users in cache and (
            datetime.now() - cache[key_users]['datetime']).total_seconds() < 5) or lock_users.locked():
        users = cache[key_users]['data']
    else:
        lock_users.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.User)
        session.close()
        users = dbResponse[:]
        now = datetime.now()
        cache[key_users] = {'data': users, 'datetime': now}
        lock_users.release()
        print('Using db')

    return Response(json.dumps(users, cls=connector.AlchemyEncoder), mimetype='application/json')


@app.route('/users', methods=['PUT'])
def update_user():
    session = db.getSession(engine)
    id = request.form['key']
    user = session.query(entities.User).filter(entities.User.id == id).first()
    c = json.loads(request.form['values'])

    for key in c.keys():
        setattr(user, key, c[key])

    session.add(user)
    session.commit()
    session.close()

    r_msg = {'msg': 'User updated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/users', methods=['DELETE'])
def delete_user():
    id = request.form['key']
    session = db.getSession(engine)
    user = session.query(entities.User).filter(entities.User.id == id).one()
    session.delete(user)
    session.commit()
    session.close()

    r_msg = {'msg': 'User deleted'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/authenticate', methods = ['POST'])
def authenticate():
    c = json.loads(request.data)
    username = c['username']
    password = c['password']
    print(c['username'])
    db_session = db.getSession(engine)
    respuesta = db_session.query(entities.User).filter(
            entities.User.username == username).filter(
            entities.User.password == password)
    db_session.close()
    users = respuesta[:]
    if len(users) > 0:
        session['logged'] = json.dumps(users[0], cls=connector.AlchemyEncoder)
        print("Logged")
        return Response(session['logged'], status=201)
    print("Failed")
    return Response(json.dumps("error in login", cls=connector.AlchemyEncoder), status=404)




@app.route('/new_user', methods=['POST'])
def create_user():
    c = json.loads(request.data)
    user = entities.User(
        username=c['username'],
        name=c['name'],
        fullname=c['fullname'],
        password=c['password']
    )
    session = db.getSession(engine)
    session.add(user)
    session.commit()
    session.close()
    print("User Created")
    r_msg = {'msg': 'UserCreated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/logout', methods = ['GET'])
def logoutapp():
    session.clear()
    return Response(json.dumps("exit", cls=connector.AlchemyEncoder),status= 200)    



"""
------------------------------------------------------
                    CRUD QUESTIONS
------------------------------------------------------
"""

@app.route('/questions', methods=['POST'])
def create_question_form():
    # c = json.loads(request.data)
    c = json.loads(request.form['values'])
    question = entities.Questions(
        content=c['content'],
        sent_on=datetime.now(),
        user_id=c['user_id'],
        course_id=c['course_id']
    )
    session = db.getSession(engine)
    session.add(question)
    session.commit()
    session.close()
    r_msg = {'msg': 'QuestionCreated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/questions/<course_id>', methods=['GET'])
def get_questions_by_course(course_id):
    if (key_questions in cache and (
            datetime.now() - cache[key_questions]['datetime']).total_seconds() < 5) or lock_questions.locked():
        questions = cache[key_questions]['data']
    else:
        lock_questions.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Questions)
        session.close()
        questions = dbResponse[:]
        now = datetime.now()
        cache[key_questions] = {'data': questions, 'datetime': now}
        lock_questions.release()
        print('Using db')
    """
    # Without cache
    db_session = db.getSession(engine)
    questions = db_session.query(entities.Questions).filter(entities.Questions.course_id == course_id)
    session.close()
    """
    questions_requested = []
    for question in questions:
        if question.course_id == int(course_id):
            copy = {}
            copy['id'] = question.id
            copy['content'] = question.content
            copy['sent_on'] = question.sent_on.strftime("%m/%d/%Y  %H:%M:%S")
            copy['user_id'] = question.user_id
            copy['user'] = question.user
            copy['course_id'] = question.course_id
            copy['course'] = question.course
            questions_requested.append(copy)

    js = json.dumps(questions_requested, cls=connector.AlchemyEncoder)
    return Response(js, status=200, mimetype='application/json')
    #message = {'status': 404, 'msg': 'Not Found'}
    #return Response(json.dumps(message), status=404, mimetype='application/json')


@app.route('/questions', methods=['GET'])
def get_questions():
    questions = []
    if (key_questions in cache and (
            datetime.now() - cache[key_questions]['datetime']).total_seconds() < 5) or lock_questions.locked():
        questions = cache[key_questions]['data']
    else:
        lock_questions.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Questions)
        session.close()
        questions = dbResponse[:]
        now = datetime.now()
        cache[key_questions] = {'data': questions, 'datetime': now}
        lock_questions.release()
        print('Using db')

    response = []
    for question in questions:
        copy = {}
        copy['id'] = question.id
        copy['content'] = question.content
        copy['sent_on'] = question.sent_on.strftime("%m/%d/%Y  %H:%M:%S")
        copy['user_id'] = question.user_id
        copy['user'] = question.user
        copy['course_id'] = question.course_id
        copy['course'] = question.course
        response.append(copy)
    return Response(json.dumps(response, cls=connector.AlchemyEncoder), mimetype='application/json')


@app.route('/questions', methods=['PUT'])
def update_questions():
    session = db.getSession(engine)
    id = request.form['key']
    question = session.query(entities.Questions).filter(entities.Questions.id == id).first()
    c = json.loads(request.form['values'])

    for key in c.keys():
        setattr(question, key, c[key])

    session.add(question)
    session.commit()
    session.close()

    r_msg = {'msg': 'QuestionUpdated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/question', methods=['DELETE'])
def delete_questions():
    id = request.form['key']
    session = db.getSession(engine)
    question = session.query(entities.Questions).filter(entities.Questions.id == id).one()
    session.delete(question)
    session.commit()
    session.close()

    r_msg = {'msg': 'QuestionDeleted'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/new_question', methods=['POST'])
def create_question():
    c = json.loads(request.data)
    question = entities.Questions(
        content=c['content'],
        sent_on=datetime.now(),
        user_id=c['user_id'],
        course_id=c['course_id'],
    )
    session = db.getSession(engine)
    session.add(question)
    session.commit()
    session.close()
    r_msg = {'msg': 'QuestionCreated'}
    return Response(json.dumps(r_msg), status=201)




"""
------------------------------------------------------
                    CRUD ANSWERS
------------------------------------------------------
"""

@app.route('/answers', methods=['POST'])
def create_answer_form():
    # c = json.loads(request.data)
    c = json.loads(request.form['values'])
    answer = entities.Answers(
        content=c['content'],
        sent_on=datetime.now(),
        user_id=c['user_id'],
        question_id=c['question_id']
    )
    session = db.getSession(engine)
    session.add(answer)
    session.commit()
    session.close()
    r_msg = {'msg': 'AnswerCreated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/answers/<question_id>', methods=['GET'])
def get_answers_by_question(question_id):
    if (key_answers in cache and (
            datetime.now() - cache[key_answers]['datetime']).total_seconds() < 5) or lock_answers.locked():
        answers = cache[key_answers]['data']
    else:
        lock_answers.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Answers)
        session.close()
        answers = dbResponse[:]
        now = datetime.now()
        cache[key_answers] = {'data': answers, 'datetime': now}
        lock_answers.release()
        print('Using db')

    """
    # Without cache
    db_session = db.getSession(engine)
    questions = db_session.query(entities.Questions).filter(entities.Questions.course_id == course_id)
    session.close()
    """

    answers_requested = []
    for answer in answers:
        if answer.question_id == int(question_id):
            copy = {}
            copy['id'] = answer.id
            copy['content'] = answer.content
            copy['sent_on'] = answer.sent_on.strftime("%m/%d/%Y  %H:%M:%S")
            copy['user_id'] = answer.user_id
            copy['user'] = answer.user
            copy['question_id'] = answer.question_id
            copy['question_course_id'] = answer.question_course_id
            copy['question'] = answer.question
            answers_requested.append(copy)

    js = json.dumps(answers_requested, cls=connector.AlchemyEncoder)
    return Response(js, status=200, mimetype='application/json')
    #message = {'status': 404, 'msg': 'Not Found'}
    #return Response(json.dumps(message), status=404, mimetype='application/json')


@app.route('/answers', methods=['GET'])
def get_answers():
    answers = []
    if (key_answers in cache and (
            datetime.now() - cache[key_answers]['datetime']).total_seconds() < 5) or lock_answers.locked():
        answers = cache[key_answers]['data']
    else:
        lock_answers.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Answers)
        session.close()
        answers = dbResponse[:]
        now = datetime.now()
        cache[key_answers] = {'data': answers, 'datetime': now}
        lock_answers.release()
        print('Using db')

    response = []
    for answer in answers:
        copy = {}
        copy['id'] = answer.id
        copy['content'] = answer.content
        copy['sent_on'] = answer.sent_on.strftime("%m/%d/%Y  %H:%M:%S")
        copy['user_id'] = answer.user_id
        copy['user'] = answer.user
        copy['question_id'] = answer.question_id
        copy['question_course_id'] = answer.question_course_id
        copy['question'] = answer.question
        response.append(copy)

    return Response(json.dumps(response, cls=connector.AlchemyEncoder), mimetype='application/json')


@app.route('/answers', methods=['PUT'])
def update_answer():
    session = db.getSession(engine)
    id = request.form['key']
    answer = session.query(entities.Answers).filter(entities.Answers.id == id).first()
    c = json.loads(request.form['values'])

    for key in c.keys():
        setattr(answer, key, c[key])

    session.add(answer)
    session.commit()
    session.close()

    r_msg = {'msg': 'AnswerUpdated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/answers', methods=['DELETE'])
def delete_answer():
    id = request.form['key']
    session = db.getSession(engine)
    answer = session.query(entities.Answers).filter(entities.Answers.id == id).one()
    session.delete(answer)
    session.commit()
    session.close()

    r_msg = {'msg': 'AnswerDeleted'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/new_answer', methods=['POST'])
def create_answer():
    c = json.loads(request.data)
    answer = entities.Answers(
        content=c['content'],
        sent_on=datetime.now(),
        user_id=c['user_id'],
        question_id=c['question_id']
    )
    session = db.getSession(engine)
    session.add(answer)
    session.commit()
    session.close()
    r_msg = {'msg': 'AnswerCreated'}
    return Response(json.dumps(r_msg), status=201)



"""
------------------------------------------------------
                    CRUD COURSES
------------------------------------------------------
"""

@app.route('/courses', methods=['POST'])
def create_course_form():
    # c = json.loads(request.data)
    c = json.loads(request.form['values'])
    course = entities.Courses(
        course_id=c['course_id'],
        name=c['name'],
        semester=c['semester']
    )
    session = db.getSession(engine)
    session.add(course)
    session.commit()
    session.close()
    r_msg = {'msg': 'CourseCreated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/courses/<semester>', methods=['GET'])
def get_course(semester):
    semester = int(semester)
    if (key_courses in cache and (
            datetime.now() - cache[key_courses]['datetime']).total_seconds() < 20) or lock_courses.locked():
        courses = cache[key_courses]['data']
    else:
        lock_courses.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Courses)
        session.close()
        courses = dbResponse[:]
        now = datetime.now()
        cache[key_courses] = {'data': courses, 'datetime': now}
        lock_courses.release()
        print('Using db')

    """
    # Without cache
    db_session = db.getSession(engine)
    questions = db_session.query(entities.Questions).filter(entities.Questions.course_id == course_id)
    session.close()
    """

    courses_requested = []
    for course in courses:
        if course['semester'] == semester:
            courses_requested.append(course)
    if len(courses_requested) > 0:
        data = sorted(data, key=lambda msj: msj.semester) 
        js = json.dumps(courses_requested, cls=connector.AlchemyEncoder)
        return Response(js, status=200, mimetype='application/json')
    message = {'status': 404, 'msg': 'Not Found'}
    return Response(json.dumps(message), status=404, mimetype='application/json')


@app.route('/courses', methods=['GET'])
def get_courses():
    courses = []
    if (key_courses in cache and (
            datetime.now() - cache[key_courses]['datetime']).total_seconds() < 20) or lock_courses.locked():
        courses = cache[key_courses]['data']
    else:
        lock_courses.acquire()
        session = db.getSession(engine)
        dbResponse = session.query(entities.Courses)
        session.close()
        courses = dbResponse[:]
        now = datetime.now()
        cache[key_courses] = {'data': courses, 'datetime': now}
        lock_courses.release()
        print('Using db')

    return Response(json.dumps(courses, cls=connector.AlchemyEncoder), mimetype='application/json')


@app.route('/courses', methods=['PUT'])
def update_course():
    session = db.getSession(engine)
    id = request.form['key']
    course = session.query(entities.Courses).filter(entities.Courses.id == id).first()
    c = json.loads(request.form['values'])

    for key in c.keys():
        setattr(course, key, c[key])

    session.add(course)
    session.commit()
    session.close()

    r_msg = {'msg': 'CourseUpdated'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/courses', methods=['DELETE'])
def delete_course():
    id = request.form['key']
    session = db.getSession(engine)
    course = session.query(entities.Courses).filter(entities.Courses.id == id).one()
    session.delete(course)
    session.commit()
    session.close()

    r_msg = {'msg': 'CourseDeleted'}
    return Response(json.dumps(r_msg), status=201)


@app.route('/new_course', methods=['POST'])
def create_course():
    c = json.loads(request.data)
    course = entities.Courses(
        course_id=c['course_id'],
        name=c['name'],
        semester=c['semester']
    )
    session = db.getSession(engine)
    session.add(course)
    session.commit()
    session.close()
    r_msg = {'msg': 'CourseCreated'}
    return Response(json.dumps(r_msg), status=201)




"""
------------------------------------------------------
                    RENDER TEMPLATE
------------------------------------------------------
"""

# STATIC
@app.route('/')
def home():
    return render_template('html/inicio.html')

# STATIC
@app.route('/static/<content>')
def static_content(content):
    return render_template(content)

if __name__ == '__main__':
    app.secret_key = ".."
    app.run(port=8080, threaded=True, host=('127.0.0.1'))
