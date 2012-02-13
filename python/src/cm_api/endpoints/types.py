# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

__docformat__ = "epytext"


class BaseApiObject(object):
  """
  The BaseApiObject helps with (de)serialization from/to JSON.
  To take advantage of it, the derived class needs to define

  RW_ATTR - A list of mutable attributes
  RO_ATTR - A list of immutable attributes

  The derived class's ctor must take all the RW_ATTR as arguments.
  When de-serializing from JSON, all attributes will be set. Their
  names are taken from the keys in the JSON object.
  """
  RO_ATTR = ( )         # Derived classes should define this
  RW_ATTR = ( )         # Derived classes should define this

  def __init__(self, **rw_attrs):
    for k, v in rw_attrs.items():
      if k not in self.RW_ATTR:
        raise ValueError("Unexpected ctor argument '%s' in %s" %
                         (k, self.__class__.__name__))
      setattr(self, k, v)

  @staticmethod
  def ctor_helper(self=None, **kwargs):
    """
    Note that we need a kw arg called `self'. The callers typically just
    pass their locals() to us.
    """
    BaseApiObject.__init__(self, **kwargs)

  def to_json_dict(self):
    dic = { }
    for attr in self.RW_ATTR:
      dic[attr] = getattr(self, attr)
    return dic

  @classmethod
  def from_json_dict(cls, dic):
    rw_dict = { } 
    for k, v in dic.items():
      if k in cls.RW_ATTR:
        rw_dict[k] = v
        del dic[k]
    # Construct object based on RW_ATTR
    obj = cls(**rw_dict)

    # Initialize all RO_ATTR to be None
    for attr in cls.RO_ATTR:
      setattr(obj, attr, None)

    # Now set the RO_ATTR based on the json
    for k, v in dic.items():
      if k in cls.RO_ATTR:
        setattr(obj, k, v)
      else:
        raise KeyError("Unexpected attribute '%s' in %s json" %
                       (k, cls.__name__))
    return obj


class ApiList(object):
  """A list of some api object"""
  def __init__(self, objects, list_key, count=None):
    self.objects = objects
    self.list_key = list_key
    if count is None:
      self.count = len(objects)
    else:
      self.count = count

  def to_json_dict(self):
    return { self.list_key :
            [ x.to_json_dict() for x in self.objects ] }

  def __len__(self):
    return self.objects.__len__()

  def __iter__(self):
    return self.objects.__iter__()

  def __getitem__(self, i):
    return self.objects.__getitem__(i)

  def __getslice(self, i, j):
    return self.objects.__getslice__(i, j)

  @staticmethod
  def from_json_dict(member_cls, dic):
    for k, v in dic.items():
      # The dictionary only has two keys, "count" and the list key
      if k == "count":
        continue
      objects = [ member_cls.from_json_dict(x) for x in v ]
      return ApiList(objects, k, dic['count'])
    else:
      raise ValueError("Cannot find items in list with keys %s" % (dic.keys(),))
