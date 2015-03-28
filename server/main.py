import random
from base import *


if IS_DEVELOPMENT_SERVER:
    from google.appengine.tools.devappserver2.python import sandbox

    sandbox._WHITE_LIST_C_MODULES += ['_ssl', '_socket']

    import sys
    import stdlib_socket

    socket = sys.modules['socket'] = stdlib_socket

import braintree
from logging import *
import phone_data

braintree.Configuration.configure(braintree.Environment.Sandbox,
                                  merchant_id="vz2wmfx5nzd9s8j5",
                                  public_key="4r6q872hwvhhxxpk",
                                  private_key="ce3bbc75aaad16b4781b4df108d9c3cb")


class MainHandler(webapp2.RequestHandler):
    def get(self):
        self.response.write('Hello world!')


class ClientTokenHandler(ApiHandler):
    def handle(self):
        client_token = braintree.ClientToken.generate({
        })
        return client_token


class PaymentMethodNonceHandler(ApiHandler):
    def handle(self):
        nonce = self.request.get('nonce')
        result = braintree.Transaction.sale({
            "amount": "10.00",
            "payment_method_nonce": nonce,
        })
        info(result)


class DataPlansHandler(ApiHandler):
    def handle(self):
        return [
            {
                'id': plan[0],
                'name': plan[1],
                'call': plan[2],
                'sms': plan[3],
                'monthly': plan[4],
                'quota': plan[5],
            }
            for plan in phone_data.plans
        ]


class AvailablePhonesHandler(ApiHandler):
    def handle(self):
        return [
            {
                'id': p[0],
                'name': p[1],
                'img': p[2],
                'price': random.randint(10, 99)*10,
            }
            for p in phone_data.phones
        ]


class PhonesToSellHandler(ApiHandler):
    def handle(self):
        return [
            {
                'id': p[0],
                'name': p[1],
                'img': p[2],
                'price_old': random.randint(10, 99)*10,
            }
            for p in phone_data.phones
        ]


app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/client_token/?', ClientTokenHandler),
    ('/payment-method-nonce/?', PaymentMethodNonceHandler),
    ('/data/plans/?', DataPlansHandler),
    ('/available_phones/?', AvailablePhonesHandler),
    ('/phones_to_sell/?', PhonesToSellHandler),
], debug=True)
