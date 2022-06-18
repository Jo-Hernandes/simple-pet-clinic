from flask_restful import Resource
import json

class Pets(Resource):
    
    def get(self):
        with open('pets.json', "r") as read_file:
            data = json.load(read_file)
            return data['pets'], 200  # return data and 200 OK code
    
class Settings(Resource):
    
    def get(self):
        with open('config.json', "r") as read_file:
            data = json.load(read_file)
            return data['settings'], 200  # return data and 200 OK code
