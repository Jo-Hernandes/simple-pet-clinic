
from flask import Flask
from flask_restful import Resource, Api, reqparse
from paths import Pets, Settings

app = Flask(__name__)
api = Api(app)
    
api.add_resource(Pets, '/pets')  # '/users' is our entry point for Users
api.add_resource(Settings, '/settings') 

if __name__ == '__main__':
    app.run()  # run our Flask app
