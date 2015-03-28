import random

from base import *
from db import *

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
        amount = self.request.get('amount')
        result = braintree.Transaction.sale({
            "amount": amount,
            "payment_method_nonce": nonce,
        })
        info(result)


class DataPlansHandler(ApiHandler):
    def handle(self):
        op = self.request.get('op')
        op = int(op)

        if op == 1:
            plans = phone_data.plans[0:11]
        elif op == 2:
            plans = phone_data.plans[11:14]
        else:
            plans = phone_data.plans[14:]

        return [
            {
                'id': plan[0],
                'name': plan[1],
                'call': plan[2],
                'sms': plan[3],
                'monthly': plan[4],
                'quota': plan[5],
            }
            for plan in plans
        ]


class AvailablePhonesHandler(ApiHandler):
    def handle(self):
        res = []
        os = [o for o in Offer.query()]
        for p in phone_data.phones:
            el = {
                'id': p[0],
                'name': p[1],
                'img': p[2],
            }

            price = 9e9
            base_prices = phone_data.pp[p[0]-1]
            for o in os:
                harga = base_prices[json.loads(o.plan)['id']-1] + o.profit
                if harga < price:
                    price = harga

            el['price'] = price

            res.append(el)

        return res


class PhonesToSellHandler(ApiHandler):
    def handle(self):
        plan_id = int(self.request.get('plan_id'))
        res = [
            {
                'id': p[0],
                'name': p[1],
                'img': p[2],
                'price_old': phone_data.pp[p[0]-1][plan_id-1],
            }
            for p in phone_data.phones
        ]

        res = sorted(res, key=lambda x: -x['price_old'])

        return res


class SellersHandler(ApiHandler):
    def handle(self):
        phone_id = int(self.request.get('phone_id'))
        base_prices = phone_data.pp[phone_id-1]

        areas = ['Toa Payoh', 'Bishan', 'Clementi', 'Jurong East', 'Sembawang', 'Changi', 'Tampines', 'Jurong West', 'Bukit Batok', 'Bukit Timah', 'Choa Chu Kang', 'Paya Lebar', 'Yishun', 'Kranji', 'Harborfront', 'Yio Chu Kang', 'Queenstown']

        random.seed(phone_id)

        res = {
            'sellers': [
                {
                    'area': random.choice(areas),
                    'price': base_prices[json.loads(o.plan)['id']-1] + o.profit,
                    'user': o.user,
                }
                for o in Offer.query()
            ]
        }

        res['sellers'] = sorted(res['sellers'], key=lambda x: x['price'])

        return res


class MyOffersHandler(ApiHandler):
    def handle(self):
        res = []
        for o in Offer.query().filter(Offer.user == self.user):
            res.append({
                'id': o.key.id(),
                'exp': o.exp,
                'op': o.op,
                'plan': json.loads(o.plan),
                'profit': o.profit,
            })

        return res


class MyOfferDeleteHandler(ApiHandler):
    def handle(self):
        offer_id = self.request.get('offer_id')
        o = Offer.get_by_id(int(offer_id))
        if o:
            o.key.delete()

        return True


class MyOfferAddHandler(ApiHandler):
    def handle(self):
        plan = self.request.get('plan')
        exp = self.request.get('exp')
        op = int(self.request.get('op'))
        profit = int(self.request.get('profit'))

        o = Offer(user=self.user, plan=plan, exp =exp, op=op, profit=profit)
        o.put()

        return {
            'success': True,
            'id': o.key.id(),
        }


class MyMeetupsHandler(ApiHandler):
    def handle(self):
        ddd = 'Default Moeljadi, +65 9390 1772, davidmoeljadi@gmail.com'
        return [
            {
                'id': m.key.id(),
                'buyer_user': m.buyer_user,
                'seller_user': m.seller_user,
                'phone': json.loads(m.phone),
                'contacts': phone_data.contacts.get(m.buyer_user, ddd) if self.user == m.seller_user else phone_data.contacts.get(m.seller_user, ddd),
                'your_email': (phone_data.contacts.get(m.seller_user, ddd) if self.user == m.seller_user else phone_data.contacts.get(m.buyer_user, ddd)).split(',')[2].strip(),
                'youare': 1 if self.user == m.seller_user else 2,
                'status': m.status,
            }
            for m in list(Meetup.query().filter(Meetup.buyer_user == self.user)) + list(Meetup.query().filter(Meetup.seller_user == self.user)) if m.status != 3
        ]


class MeetupUpdateStatusHandler(ApiHandler):
    def handle(self):
        meetup_id = int(self.request.get('meetup_id'))
        youare = int(self.request.get('youare'))

        m = Meetup.get_by_id(meetup_id)
        if not m:
            return False

        m.status |= youare
        m.put()

        return True


class MeetupAddHandler(ApiHandler):
    def handle(self):
        seller_user = self.request.get('seller_user')
        phone = self.request.get('phone')

        m = Meetup(seller_user = seller_user, buyer_user = self.user, status = 0, phone = phone)
        m.put()

        return {
            'id': m.key.id(),
        }



app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/client_token/?', ClientTokenHandler),
    ('/payment-method-nonce/?', PaymentMethodNonceHandler),
    ('/data/plans/?', DataPlansHandler),
    ('/my_offers/?', MyOffersHandler),
    ('/my_offer_delete/?', MyOfferDeleteHandler),
    ('/my_offer_add/?', MyOfferAddHandler),
    ('/available_phones/?', AvailablePhonesHandler),
    ('/phones_to_sell/?', PhonesToSellHandler),
    ('/sellers/?', SellersHandler),
    ('/my_meetups/?', MyMeetupsHandler),
    ('/meetup_update_status/?', MeetupUpdateStatusHandler),
    ('/meetup_add/?', MeetupAddHandler),
], debug=True)
