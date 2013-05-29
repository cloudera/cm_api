# Licensed to Cloudera, Inc. under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  Cloudera, Inc. licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import datetime
import unittest
from cm_api.endpoints.types import *
from cm_api_tests import utils

class Child(BaseApiObject):
  def _get_attributes(self):
    return { 'value' : None }

class Parent(BaseApiObject):
  def _get_attributes(self):
    return {
      'child'     : Attr(Child),
      'children'  : Attr(Child),
      'date'      : Attr(datetime.datetime),
      'readOnly'  : ROAttr(),
    }

class Dummy(BaseApiObject):
  _ATTRIBUTES = {
    'foo' : None,
    'bar' : None,
  }

class TestBaseApiObject(unittest.TestCase):

  def test_props(self):
    obj = Parent(None)
    obj.child = Child(None)
    obj.children = [ ]
    obj.date = datetime.datetime.now()

    # Setting read-only attribute.
    with self.assertRaises(AttributeError):
      obj.readOnly = False

    # Setting unknown attribute.
    with self.assertRaises(AttributeError):
      obj.unknown = 'foo'

  def test_serde(self):
    JSON = '''
      {
        "child" : { "value" : "string1" },
        "children" : [
          { "value" : 1 },
          { "value" : "2" }
        ],
        "date" : "2013-02-12T12:17:15.831765Z",
        "readOnly" : true
      }
    '''
    obj = utils.deserialize(JSON, Parent)
    self.assertIsInstance(obj.child, Child)
    self.assertEqual('string1', obj.child.value)
    self.assertIsInstance(obj.children, list)
    self.assertEqual(2, len(obj.children))
    self.assertEqual(1, obj.children[0].value)
    self.assertEqual('2', obj.children[1].value)
    self.assertIsInstance(obj.date, datetime.datetime)
    self.assertEqual(2013, obj.date.year)
    self.assertEqual(2, obj.date.month)
    self.assertEqual(12, obj.date.day)
    self.assertEqual(12, obj.date.hour)
    self.assertEqual(17, obj.date.minute)
    self.assertEqual(15, obj.date.second)
    self.assertEqual(831765, obj.date.microsecond)
    self.assertTrue(obj.readOnly)

    JSON = '''
      {
        "children" : [ ]
      }
    '''
    obj = utils.deserialize(JSON, Parent)
    self.assertEquals([], obj.children)

  def test_init(self):
    obj = Parent(None)
    self.assertTrue(hasattr(obj, 'child'))
    self.assertTrue(hasattr(obj, 'readOnly'))

    obj = Parent(None, date=datetime.datetime.now())
    self.assertIsInstance(obj.date, datetime.datetime)

    self.assertRaises(AttributeError, Parent, None, readOnly=True)

  def test_empty_property(self):
    dummy = Dummy(None)
    dummy.foo = 'foo'
    json = dummy.to_json_dict()
    self.assertEqual('foo', json['foo'])
    self.assertFalse(json.has_key('bar'))
