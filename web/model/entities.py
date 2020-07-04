from sqlalchemy import Column, Integer, String, Sequence, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from database import connector

class User(connector.Manager.Base):
    __tablename__ = 'users'
    id = Column(Integer, Sequence('user_id_seq'), primary_key=True)
    name = Column(String(50))
    fullname = Column(String(50))
    password = Column(String(12))
    username = Column(String(12))


class Courses(connector.Manager.Base):
    __tablename__ = 'courses'
    id = Column(Integer, Sequence('message_id_seq'), primary_key=True)
    course_id = Column(String(10))
    name = Column(String(50))
    semester = Column(Integer)


class Questions(connector.Manager.Base):
    __tablename__ = 'questions'
    id = Column(Integer, Sequence('message_id_seq'), primary_key=True)
    content = Column(String(500))
    sent_on = Column(DateTime(timezone=True))
    #sent_on = Column(default = datetime.now())
    user_id = Column(Integer, ForeignKey('users.id'))
    user = relationship(User, foreign_keys=[user_id])
    course_id = Column(Integer, ForeignKey('courses.id'))
    course = relationship(Courses, foreign_keys=[course_id])


class Answers(connector.Manager.Base):
    __tablename__ = 'answers'
    id = Column(Integer, Sequence('message_id_seq'), primary_key=True)
    content = Column(String(500))
    sent_on = Column(DateTime(timezone=True))
    #sent_on = Column(default = datetime.now())
    user_id = Column(Integer, ForeignKey('users.id'))
    user = relationship(User, foreign_keys=[user_id])
    question_id = Column(Integer, ForeignKey('questions.id'))
    question_course_id = Column(Integer, ForeignKey('questions.course_id'))
    question = relationship(Questions, foreign_keys=[question_id])
