import webapp2
import json
import jinja2
import os
from logging import *
from datetime import *

import os

IS_DEVELOPMENT_SERVER = os.environ['SERVER_SOFTWARE'].startswith('Development')


class ApiHandler(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-type'] = 'application/json'
        result = self.handle()
        self.response.write(json.dumps(result, indent=2))

    def post(self):
        self.get()

    def handle(self):
        return {'success': False, 'message': 'not implemented'}


class TemplateHandler(webapp2.RequestHandler):
    jinja_env = None

    def _initJinja(self, template_dir):
        self.jinja_env = jinja2.Environment(
            loader=jinja2.FileSystemLoader(os.path.join(os.path.dirname(__file__), template_dir)),
            extensions=['jinja2.ext.with_']
        )

        def format_datetime(value, format=None):
            if not value: return ''
            if format == None:
                format = '%d %b %Y, %H:%M'
            return datetime.strftime(value, format)

        self.jinja_env.filters['datetime'] = format_datetime

    def _render_str(self, template, **params):
        t = self.jinja_env.get_template(template)
        return t.render(params)

    def render(self, template_dir, template, **kw):
        self._initJinja(template_dir)
        self.response.write(self._render_str(template, **kw))

    def param_int(self, name, default=0):
        v = self.request.get(name)
        if v is None:
            return default
        try:
            return int(v)
        except ValueError:
            return default
