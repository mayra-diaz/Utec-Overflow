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



"""
------------------------------------------------------
                      CRUD USERS
------------------------------------------------------
"""

@app.route('/users', methods=['POST'])
def create_user():
    # c = json.loads(request.data)
    c = json.loads(request.form['values'])
    username = c['username']
    password = c['password']
    name = c['name']
    fullname = c['fullname']
    user = entities.User(
        username=username,
        name=name,
        fullname=fullname,
        password=password
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
            datetime.now() - cache[key_users]['datetime']).total_seconds() < 10) or lock_users.locked():
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
    global contador
    users = []
    if (key_users in cache and (
            datetime.now() - cache[key_users]['datetime']).total_seconds() < 10) or lock_users.locked():
        users = cache[key_users]['data']
        print('Using cache', contador)
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



"""
------------------------------------------------------
                    CRUD QUESTIONS
------------------------------------------------------
"""


"""
------------------------------------------------------
                    CRUD ANSWERS
------------------------------------------------------
"""


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
