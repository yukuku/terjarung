import webapp2
from base import *

class MainHandler(webapp2.RequestHandler):
    def get(self):
        self.response.write('Hello world!')


class ClientTokenHandler(ApiHandler):
    def handle(self):



app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('client_token/?', ClientTokenHandler)
], debug=True)
