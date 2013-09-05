// Licensed to Cloudera, Inc. under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  Cloudera, Inc. licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.cloudera.api.model;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.fail;

/**
 * This is a test class for checking the behavior of JAX-B annotation
 */
public class JaxbAnnotationBehavior {
  private static final String TEST_STRING = "This is a test string";

  public static interface Valuable {
    public String getValue();
  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.NONE)
  public static class AnnotatedGetterNoSetterAccessNone implements Valuable {
    private String value;

    public AnnotatedGetterNoSetterAccessNone() {
    }

    public AnnotatedGetterNoSetterAccessNone(String value) {
      this.value = value;
    }

    @XmlElement
    public String getValue() {
      return this.value;
    }

  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.PROPERTY)
  public static class GetterNoSetterAccessProperty implements Valuable {
    private String value;

    public GetterNoSetterAccessProperty() {
    }

    public GetterNoSetterAccessProperty(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }

  }

  @XmlRootElement
  public static class AnnotatedGetterNoSetterAccessDefault implements Valuable {
    private String value;

    public AnnotatedGetterNoSetterAccessDefault() {
    }

    public AnnotatedGetterNoSetterAccessDefault(String value) {
      this.value = value;
    }

    @XmlElement
    public String getValue() {
      return this.value;
    }

  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.NONE)
  public static class AnnotatedGetterWithSetterAccessNone implements Valuable {
    private String value;

    public AnnotatedGetterWithSetterAccessNone() {
    }

    public AnnotatedGetterWithSetterAccessNone(String value) {
      this.value = value;
    }

    @XmlElement
    public String getValue() {
      return this.value;
    }

    public void setValue(String value) {
      this.value = value;
    }

  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.NONE)
  public static class AnnotatedFieldAccessNone implements Valuable {

    @XmlElement
    private String value;

    public AnnotatedFieldAccessNone() {
    }

    public AnnotatedFieldAccessNone(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

  }

  private static <T> void testObject(String test, Class<? extends T> type,
                                     T object) {
    System.out.println(test);
    String objXML = null;
    String objJSON = null;
    try {
      objXML = ApiModelTest.objectToXml(object);
      objJSON = ApiModelTest.objectToJson(object);
    } catch (JAXBException e) {
      e.printStackTrace();
      return;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return;
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    if (objXML.contains(TEST_STRING)) {
      System.out.println("\tObject->XML contained the value.");
    } else {
      System.out.println("\tObject->XML DID NOT contain the value.");
    }
    if (objJSON.contains(TEST_STRING)) {
      System.out.println("\tObject->JSON contained the value.");
    } else {
      System.out.println("\tObject->JSON DID NOT contain the value.");
    }

    T objFromXML = null;
    T objFromJSON = null;

    try {
      objFromXML = ApiModelTest.xmlToObject(objXML, type);
      objFromJSON = ApiModelTest.jsonToObject(objJSON, type);
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (UnrecognizedPropertyException e) {
      System.out.println("\tJSON->Obj threw an UnrecognizedPropertyException");
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (objFromXML == null) {
      System.out.println("\tXML->Obj returned a null object");
    } else {
      String xmlValue = ((Valuable)objFromXML).getValue();
      if (xmlValue == null) {
        System.out.println("\tXML->Obj has a NULL value");
      } else if (xmlValue.equals(TEST_STRING)) {
        System.out.println("\tXML->Obj has the value.");
      } else {
        fail("XML->Obj has an unexpected value=" + xmlValue);
      }
    }

    if (objFromJSON == null) {
      System.out.println("\tJSON->Obj returned a null object");
    } else {
      String jsonValue = ((Valuable)objFromJSON).getValue();
      if (jsonValue == null) {
        System.out.println("\tJSON->Obj has a NULL value");
      } else if (jsonValue.equals(TEST_STRING)) {
        System.out.println("\tJSON->Obj has the value.");
      } else {
        fail("JSON->Obj has an unexpected value=" + jsonValue);
      }
    }

  }

  public static void main(String []args) {
    testObject("Annotated Getter no Setter Access=NONE",
               AnnotatedGetterNoSetterAccessNone.class,
               new AnnotatedGetterNoSetterAccessNone(TEST_STRING));
    testObject("Annotated Getter no Setter Access=Default (PUBLIC_MEMBER)",
               AnnotatedGetterNoSetterAccessDefault.class,
               new AnnotatedGetterNoSetterAccessDefault(TEST_STRING));
    testObject("Annotated Getter with Setter Access=NONE",
               AnnotatedGetterWithSetterAccessNone.class,
               new AnnotatedGetterWithSetterAccessNone(TEST_STRING));
    testObject("Annotated field with Access=NONE",
               AnnotatedFieldAccessNone.class,
               new AnnotatedFieldAccessNone(TEST_STRING));
    testObject("Getter no setter Access=PROPERTY",
               GetterNoSetterAccessProperty.class,
               new GetterNoSetterAccessProperty(TEST_STRING));
  }

}
