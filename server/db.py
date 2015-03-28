from google.appengine.ext import ndb


class Offer(ndb.Model):
    user = ndb.StringProperty()
    plan = ndb.TextProperty()
    exp = ndb.TextProperty()
    op = ndb.IntegerProperty()
    profit = ndb.IntegerProperty()


class Meetup(ndb.Model):
    seller_user = ndb.StringProperty()
    buyer_user = ndb.StringProperty()
    status = ndb.IntegerProperty()
    phone = ndb.TextProperty()


