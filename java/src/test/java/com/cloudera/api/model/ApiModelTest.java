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

import static org.junit.Assert.*;

import com.cloudera.api.ApiErrorMessage;
import com.cloudera.api.ApiObjectMapper;
import com.cloudera.api.ApiUtils;
import com.cloudera.api.model.ApiHBaseSnapshot.Storage;
import com.cloudera.api.model.ApiHdfsReplicationArguments.ReplicationStrategy;
import com.cloudera.api.model.ApiRole.ZooKeeperServerMode;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Test;

public class ApiModelTest {
  private final static String TEXT_ENCODING = "UTF-8";

  static String objectToXml(Object object)
      throws JAXBException, UnsupportedEncodingException {
    JAXBContext jc = JAXBContext.newInstance(object.getClass());
    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(object, baos);
    return baos.toString(TEXT_ENCODING);
  }

  static <T> T xmlToObject(String text, Class<T> type)
      throws JAXBException, UnsupportedEncodingException,
      IllegalAccessException, InstantiationException {
    JAXBContext jc = JAXBContext.newInstance(type);
    Unmarshaller um = jc.createUnmarshaller();
    ByteArrayInputStream bais =
        new ByteArrayInputStream(text.getBytes(TEXT_ENCODING));
    Object res = um.unmarshal(bais);
    return type.cast(res);
  }

  static String objectToJson(Object object) throws IOException {
    ApiObjectMapper objectMapper = new ApiObjectMapper();
    return objectMapper.writeValueAsString(object);
  }

  static <T> T jsonToObject(String text, Class<T> type)
      throws IOException {
    ApiObjectMapper objectMapper = new ApiObjectMapper();
    return objectMapper.readValue(text, type);
  }


  private static <T> void checkJson(Class<? extends T> type, T object)
      throws IOException {
    String jsonText = objectToJson(object);
    System.out.println(jsonText);

    T objectFromJson = jsonToObject(jsonText, type);
    compareObjects(object, objectFromJson);
  }

  private static <T> void checkXML(Class<? extends T> type, T object)
          throws JAXBException, UnsupportedEncodingException,
          InstantiationException, IllegalAccessException {
    String xmlText = objectToXml(object);
    System.out.println(xmlText);

    T objectFromXml = xmlToObject(xmlText, type);
    compareObjects(object, objectFromXml);
  }

  private static boolean isApiType(Class<?> type) {
    return type.getAnnotation(XmlRootElement.class) != null;
  }

  private static String getMethodName(Method m) {
    return String.format("%s::%s()", m.getDeclaringClass().getName(), m.getName());
  }

  private static void compareChildObjects(Method getter, Class<?> type,
      Object expected, Object deserialized) {
    if (isApiType(type)) {
      if (expected != null) {
        assertNotNull("Missing deserialized value for getter " + getMethodName(getter),
            deserialized);
        compareObjects(expected, deserialized);
      }
    } else {
      assertNotNull("Missing expected value for getter " + getMethodName(getter),
          expected);
      assertNotNull("Missing deserialized value for getter " + getMethodName(getter),
          deserialized);
      String errorMessage =
          String.format("Values for getter '%s' don't match: '%s' != '%s'.",
                        getMethodName(getter), expected, deserialized);
      if (type.isArray()) {
        assertTrue(errorMessage, ArrayUtils.isEquals(expected, deserialized));
      } else {
        assertEquals(errorMessage, expected, deserialized);
      }
    }
  }

  /**
   * Compares two objects by recursively comparing all of the fields that have
   * JAX-B annotations (@XmlElement and @XmlElementWrapper). This check assumes
   * that all getters are annotated and public, as is our convention.
   * <p/>
   * The test requires that all non-API-object properties of the instances
   * being compared are properly set. The assumption is that there is at least
   * one test for each API type (explicitly or implicitly through another
   * object's test), so that in the end everything is covered.
   */
  private static void compareObjects(Object expected, Object deserialized) {
    try {
      for (Method m : expected.getClass().getMethods()) {
        String methodName = getMethodName(m);

        XmlElement elem = m.getAnnotation(XmlElement.class);
        if (elem != null) {
          compareChildObjects(m, m.getReturnType(),
              m.invoke(expected), m.invoke(deserialized));
          continue;
        }

        XmlElementWrapper wrapper = m.getAnnotation(XmlElementWrapper.class);
        if (wrapper != null) {
          assertTrue(Collection.class.isAssignableFrom(m.getReturnType()));
          assertTrue("Unexpected generic return in " + methodName,
              m.getGenericReturnType() instanceof ParameterizedType);

          Type memberType =
            ((ParameterizedType) m.getGenericReturnType())
                .getActualTypeArguments()[0];
          assertTrue("Unexpected generic argument in " + methodName,
              memberType instanceof Class);

          Collection<?> expectedItems = (Collection<?>) m.invoke(expected);
          Collection<?> deserializedItems = (Collection<?>) m.invoke(deserialized);
          if (!isApiType((Class<?>) memberType)) {
            assertNotNull("No expected items for getter " + m.getName(),
                expectedItems);
          }
          if (expectedItems != null) {
            assertNotNull("No deserialized items for getter " + methodName,
                deserializedItems);
            assertEquals("Mismatched item count in values for getter " + methodName,
                expectedItems.size(), deserializedItems.size());

            Iterator<?> ex = expectedItems.iterator();
            Iterator<?> ds = deserializedItems.iterator();
            while (ex.hasNext()) {
              compareChildObjects(m, (Class<?>) memberType, ex.next(), ds.next());
            }
          }
        }
      }
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  public static <T> void checkJsonXML(T object)
      throws IOException, JAXBException, IllegalAccessException,
      InstantiationException {
    checkXML(object.getClass(), object);
    checkJson(object.getClass(), object);
  }

  @Test
  public void testApiCluster() throws Exception {
    ApiClusterRef ref = new ApiClusterRef("cluster1");
    checkJsonXML(ref);

    ApiCluster cluster = newCluster();
    checkJsonXML(cluster);
  }

  @Test
  public void testApiConfig() throws Exception {
    ApiConfig cfg = new ApiConfig("name", "value", true, "default", "display",
        "description", "relatedName", ApiConfig.ValidationState.OK,
        "validationMessage");
    cfg.setValidationWarningsSuppressed(true);
    cfg.setSensitive(false);
    checkJsonXML(cfg);

    ApiServiceConfig svcCfg = new ApiServiceConfig();
    svcCfg.add(cfg);
    ApiRoleTypeConfig rtCfg = new ApiRoleTypeConfig();
    rtCfg.add(cfg);
    svcCfg.setRoleTypeConfigs(Arrays.asList(rtCfg));
    checkJsonXML(svcCfg);
  }

  @Test
  public void testApiHost()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    checkJsonXML(newHost());
  }

  @Test
  public void testApiHealthCheck() throws Exception {
    ApiHealthCheck healthCheck = new ApiHealthCheck("checkName",
                                                    ApiHealthSummary.GOOD,
                                                    "Dummy Health explanation.",
                                                    false);
    checkJsonXML(healthCheck);
  }

  @Test
  public void testApiRole()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    ApiRole role = new ApiRole();
    role.setCommissionState(ApiCommissionState.COMMISSIONED);
    role.setConfigStale(false);
    role.setConfigStalenessStatus(ApiConfigStalenessStatus.FRESH);
    role.setHaStatus(ApiRole.HaStatus.ACTIVE);
    role.setHealthChecks(createHealthChecks());
    role.setHealthSummary(ApiHealthSummary.GOOD);
    role.setEntityStatus(ApiEntityStatus.GOOD_HEALTH);
    role.setHostRef(new ApiHostRef("myhost"));
    role.setMaintenanceMode(true);
    role.setMaintenanceOwners(createMaintenanceOwners());
    role.setName("myname");
    role.setRoleState(ApiRoleState.STARTED);
    role.setRoleUrl("http://localhost:7180");
    role.setServiceRef(new ApiServiceRef("peer", "mycluster", "myservice"));
    role.setType("mytype");
    role.setZooKeeperServerMode(ZooKeeperServerMode.REPLICATED_LEADER);
    checkJsonXML(role);
  }

  @Test
  public void testApiService()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    ApiService service = new ApiService();
    service.setClusterRef(new ApiClusterRef("mycluster"));
    service.setConfigStale(false);
    service.setConfigStalenessStatus(ApiConfigStalenessStatus.FRESH);
    service.setClientConfigStalenessStatus(ApiConfigStalenessStatus.FRESH);
    service.setDisplayName("mydisplayname");
    service.setHealthChecks(createHealthChecks());
    service.setHealthSummary(ApiHealthSummary.GOOD);
    service.setEntityStatus(ApiEntityStatus.GOOD_HEALTH);
    service.setMaintenanceMode(true);
    service.setMaintenanceOwners(createMaintenanceOwners());
    service.setName("myname");
    service.setServiceState(ApiServiceState.STARTED);
    service.setServiceUrl("http://foo:7180");
    service.setRoleInstancesUrl("http://foo:7180/instances");
    service.setType("mytype");

    ApiRoleConfigGroup rcg = new ApiRoleConfigGroup();
    rcg.setName("rcg");
    rcg.setDisplayName("display");
    rcg.setRoleType("role");
    rcg.setBase(true);
    rcg.setServiceRef(new ApiServiceRef("p1", "c1", "s1"));
    rcg.setConfig(new ApiConfigList());
    service.setRoleConfigGroups(Arrays.asList(rcg));

    checkJsonXML(service);
  }

  @Test
  public void testApiUser() throws Exception {
    checkJsonXML(newUser());
  }

  private ApiEvent newEvent() {
    Map<String, List<String>> attributes =  Maps.newHashMap();
    attributes.put("foo", Arrays.asList("one"));
    attributes.put("bar", Arrays.asList("a", "b"));
    attributes.put("baz", Lists.<String>newArrayList());
    return new ApiEvent(UUID.randomUUID().toString(),
                        new Date(), new Date(),
                        ApiEventCategory.HEALTH_EVENT,
                        ApiEventSeverity.CRITICAL,
                        "Event content",
                        true, attributes);
  }

  @Test
  public void testApiEvent() throws Exception {
    ApiEvent event = newEvent();
    checkJsonXML(event);
  }

  @Test
  public void testEventQueryResult() throws Exception {
    List<ApiEvent> events = Arrays.asList(newEvent(), newEvent());
    ApiEventQueryResult result = new ApiEventQueryResult(
        events.size(), events);
    checkJsonXML(result);
  }

  @Test
  public void testLists() throws Exception {
    for (ApiListBase<?> lst : Arrays.<ApiListBase<?>>asList(
        new ApiActivityList(Lists.<ApiActivity>newArrayList()),
        new ApiAuditList(Lists.<ApiAudit>newArrayList()),
        new ApiClusterList(Lists.<ApiCluster>newArrayList()),
        new ApiCommandList(Lists.<ApiCommand>newArrayList()),
        new ApiConfigList(Lists.<ApiConfig>newArrayList()),
        new ApiHostList(Lists.<ApiHost>newArrayList()),
        new ApiHostRefList(Lists.<ApiHostRef>newArrayList()),
        new ApiMetricList(Lists.<ApiMetric>newArrayList()),
        new ApiNameserviceList(Lists.<ApiNameservice>newArrayList()),
        new ApiParcelList(Lists.<ApiParcel>newArrayList()),
        new ApiReplicationCommandList(Lists.<ApiReplicationCommand>newArrayList()),
        new ApiReplicationScheduleList(Lists.<ApiReplicationSchedule>newArrayList()),
        new ApiRoleList(Lists.<ApiRole>newArrayList()),
        new ApiRoleNameList(Lists.<String>newArrayList()),
        new ApiRoleTypeList(Lists.<String>newArrayList()),
        new ApiServiceList(Lists.<ApiService>newArrayList()),
        new ApiSnapshotCommandList(Lists.<ApiSnapshotCommand>newArrayList()),
        new ApiSnapshotPolicyList(Lists.<ApiSnapshotPolicy>newArrayList()),
        new ApiUserList(Lists.<ApiUser>newArrayList()),
        new ApiDashboardList(Lists.<ApiDashboard>newArrayList()),
        new ApiImpalaQueryAttributeList(Lists.<ApiImpalaQueryAttribute>newArrayList()),
        new ApiYarnApplicationAttributeList(
            Lists.<ApiYarnApplicationAttribute>newArrayList())
    )) {
      checkJsonXML(lst);

      // Check that the encoded property matches the convention for our lists.
      String json = objectToJson(lst);

      ApiObjectMapper mapper = new ApiObjectMapper();
      @SuppressWarnings("unchecked")
      Map<String, Object> map = mapper.readValue(json, Map.class);
      assertTrue("List " + lst.getClass().getName() + " has wrong 'items' property.",
          map.containsKey(ApiListBase.ITEMS_ATTR));
    }
  }

  @Test
  public void testActivity() throws Exception {
    ApiActivity activity = new ApiActivity("activityId",
                                           "activityName",
                                           ApiActivityType.MR,
                                           ApiActivityStatus.STARTED,
                                           "parentActivityId",
                                           new Date(12345),
                                           new Date(23456),
                                           "userName",
                                           "groupName",
                                           "/tmp/inputDir",
                                           "/tmp/outputDir",
                                           "com.my.mapper",
                                           "com.my.combiner",
                                           "com.my.reducer",
                                           "schedulerQueueName",
                                           "mypriority");
    checkJsonXML(activity);
  }

  @Test
  public void testAudit() throws Exception {
    ApiAudit audit = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(250912898047L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKey2", "extraValue2"));
    checkJsonXML(audit);
  }

  @Test
  public void testAuditsExtraValuesEqual() throws Exception {
    ApiAudit audit1 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKey2", "extraValue2"));

    ApiAudit audit2 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey2", "extraValue2", "extraKey1", "extraValue1"));

    assertTrue(audit1.equals(audit2));
  }

  @Test
  public void testAuditsExtraValuesNotEqual() throws Exception {
    ApiAudit audit1 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKey2", "extraValue2"));

    // different number of extra values
    ApiAudit audit2 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1"));

    assertFalse(audit1.equals(audit2));

    // different keys
    audit2 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKeyX", "extraValue2"));

    assertFalse(audit1.equals(audit2));

    // different values
    audit2 = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKey2", "extraValueX"));

    assertFalse(audit1.equals(audit2));
  }

  @Test
  public void testExtraValuesMap() {
    ApiAudit audit = new ApiAudit("service",
        "myName",
        "imp",
        "command",
        "1.2.3.4",
        "resource",
        true,
        new Date(1L),
        "select foo from bar",
        ImmutableMap.of("extraKey1", "extraValue1", "extraKey2", "extraValue2"));

    Map<String, String> expectedExtraValues = Maps.newHashMap();
    expectedExtraValues.put("extraKey1", "extraValue1");
    expectedExtraValues.put("extraKey2", "extraValue2");

    assertEquals(expectedExtraValues, audit.getServiceValues());
  }

  @Test
  public void testConfig() throws Exception {
    ApiConfig config = new ApiConfig("name", "value", false, "defaultValue",
                                     "displayName", "description",
                                     "relatedName",
                                     ApiConfig.ValidationState.OK,
                                     "validationMessage");
    config.setValidationWarningsSuppressed(false);
    config.setSensitive(true);
    checkJsonXML(config);
  }

  @Test
  public void testEcho() throws Exception {
    ApiEcho echo = new ApiEcho("Hello... Hello... Hello...");
    checkJsonXML(echo);
  }

  @Test
  public void testMetric() throws Exception {
    ApiMetric metric = new ApiMetric("name", "context", "unit",
                                     Lists.<ApiMetricData>newArrayList(),
                                     "displayName", "description");
    metric.setData(Arrays.asList(new ApiMetricData(new Date(0), 123.45)));
    checkJsonXML(metric);
  }

  @Test
  public void testNameservice() throws Exception {
    ApiNameservice ns = new ApiNameservice(
        "foo-ns",
        new ApiRoleRef("foo", "bar", "baz"),
        new ApiRoleRef("fooFC", "barFC", "bazFC"),
        new ApiRoleRef("foo2", "bar2", "baz2"),
        new ApiRoleRef("foo2FC", "bar2FC", "baz2FC"),
        new ApiRoleRef("foo3", "bar3", "baz3"),
        Arrays.asList("mount1", "mount2"));
    ns.setHealthSummary(ApiHealthSummary.GOOD);
    checkJsonXML(ns);
  }

  @Test
  public void testHdfsHaArguments() throws Exception {
    ApiHdfsHaArguments haArgs = new ApiHdfsHaArguments();
    haArgs.setNameservice("ns1");
    haArgs.setActiveName("ann");
    haArgs.setActiveSharedEditsPath("/foo");
    haArgs.setStandByName("sbn");
    haArgs.setStandBySharedEditsPath("/bar");
    haArgs.setStartDependentServices(false);
    haArgs.setDeployClientConfigs(true);
    haArgs.setEnableQuorumStorage(true);
    checkJsonXML(haArgs);

    ApiHdfsDisableHaArguments dhaArgs = new ApiHdfsDisableHaArguments();
    dhaArgs.setActiveName("ann");
    dhaArgs.setSecondaryName("snn");
    dhaArgs.setStartDependentServices(false);
    dhaArgs.setDeployClientConfigs(true);
    checkJsonXML(dhaArgs);

    ApiHdfsFailoverArguments afoArgs = new ApiHdfsFailoverArguments();
    afoArgs.setNameservice("ns1");
    afoArgs.setZooKeeperService(new ApiServiceRef("p1", "c1", "s1"));
    afoArgs.setActiveFCName("fc1");
    afoArgs.setStandByFCName("fc2");
    checkJsonXML(afoArgs);
  }

  @Test
  public void testCmPeer() throws Exception {
    ApiCmPeer peer = new ApiCmPeer();
    peer.setName("peer1");
    peer.setUrl("url1");
    peer.setUsername("user1");
    peer.setPassword("password1");
    peer.setType(ApiCmPeerType.REPLICATION);
    peer.setClouderaManagerCreatedUser(true);
    checkJsonXML(peer);
  }

  @Test
  public void testDeployment() throws Exception {
    ApiDeployment deployment = new ApiDeployment();
    deployment.setUsers(Arrays.asList(newUser()));
    deployment.setClusters(Arrays.asList(newCluster()));
    deployment.setTimestamp(new Date(62239));
    deployment.setHosts(Arrays.asList(newHost()));

    ApiVersionInfo versionInfo = new ApiVersionInfo();
    versionInfo.setVersion("alpha 1");
    versionInfo.setSnapshot(true);
    versionInfo.setBuildUser("alice");
    versionInfo.setBuildTimestamp("today");
    versionInfo.setGitHash("hash");
    deployment.setVersionInfo(versionInfo);
    checkJsonXML(deployment);
  }

  @Test
  public void testHdfsReplication() throws Exception {
    ApiHdfsReplicationArguments hdfsArgs = newHdfsReplicationArguments();
    ApiReplicationSchedule hdfsInfo =
      new ApiReplicationSchedule(20L, new Date(1234), new Date(5678), 10,
          ApiScheduleInterval.MONTH, true);
    hdfsInfo.setHdfsArguments(hdfsArgs);
    hdfsInfo.setNextRun(new Date(12345));
    hdfsInfo.setActive(true);

    ApiReplicationCommand cmd = new ApiReplicationCommand();
    fillInCommand(cmd);

    ApiHdfsReplicationResult result = newHdfsReplicationResult();
    cmd.setHdfsResult(result);

    hdfsInfo.setHistory(Arrays.asList(cmd));
    checkJsonXML(hdfsInfo);
  }

  @Test
  public void testApiParcel() throws Exception {
    ApiParcel parcel = new ApiParcel();
    parcel.setProduct("productName");
    parcel.setVersion("version");
    parcel.setStage("DOWNLOADING");
    parcel.setClusterRef(new ApiClusterRef("cluster1"));

    ApiParcelState state = new ApiParcelState();
    state.setProgress(1L);
    state.setTotalProgress(2L);
    state.setCount(3L);
    state.setTotalCount(4L);
    state.setWarnings(Arrays.asList("warning"));
    state.setErrors(Arrays.asList("error"));
    parcel.setState(state);

    checkJsonXML(parcel);
  }

  @Test
  public void testHiveReplication() throws Exception {
    ApiHiveReplicationArguments args = new ApiHiveReplicationArguments();
    args.setSourceService(new ApiServiceRef("p1", "c1", "s1"));
    args.setTableFilters(Arrays.asList(new ApiHiveTable("db1", "table1")));
    args.setExportDir("foo");
    args.setForce(true);
    args.setReplicateData(true);
    args.setReplicateImpalaMetadata(true);

    ApiHdfsReplicationArguments hdfsArgs = newHdfsReplicationArguments();
    args.setHdfsArguments(hdfsArgs);

    checkJsonXML(args);

    ApiReplicationSchedule sch = new ApiReplicationSchedule(20L,
        new Date(1234), new Date(5678), 10, ApiScheduleInterval.MONTH, true);
    sch.setHiveArguments(args);
    sch.setActive(true);
    checkJsonXML(sch);

    ApiHiveReplicationResult res = new ApiHiveReplicationResult();
    res.setTables(Arrays.asList(new ApiHiveTable("db1", "table1")));
    res.setTableCount(1);
    res.setImpalaUDFs(Arrays.asList(new ApiImpalaUDF("db1", "func1")));
    res.setImpalaUDFCount(1);
    res.setHiveUDFs(Arrays.asList(new ApiHiveUDF("db2", "func2")));
    res.setHiveUDFCount(1);
    res.setErrors(Arrays.asList(new ApiHiveReplicationError("db1", "table1",
        "func1(STRING)", "func2(STRING)", "error1")));
    res.setErrorCount(1);
    res.setDryRun(true);
    res.setPhase("foo");
    res.setRunAsUser("foo");

    ApiHdfsReplicationResult hdfsRes = newHdfsReplicationResult();
    res.setDataReplicationResult(hdfsRes);

    checkJsonXML(res);

    ApiReplicationCommand cmd = new ApiReplicationCommand();
    fillInCommand(cmd);
    cmd.setHiveResult(res);
    checkJsonXML(cmd);
  }

  @Test
  public void testImpalaQuery() throws Exception {
    ApiImpalaQuery query = new ApiImpalaQuery(
        "queryId", "statement", "QUERY", "FINISHED", new Date(), new Date(),
        1L, Maps.<String, String>newHashMap(), "user", "hostId", true, "db",
        Duration.standardDays(1));
    checkJsonXML(query);

    ApiImpalaQueryResponse response = new ApiImpalaQueryResponse(
        ImmutableList.<ApiImpalaQuery>of(), ImmutableList.<String>of());
    checkJsonXML(response);

    ApiImpalaCancelResponse cancelResponse =
        new ApiImpalaCancelResponse("warning");
    checkJsonXML(cancelResponse);

    ApiImpalaQueryDetailsResponse detailsResponse =
        new ApiImpalaQueryDetailsResponse("details");
    checkJsonXML(detailsResponse);
  }

  @Test
  public void testHBaseScheduledSnapshots() throws Exception {
    ApiSnapshotPolicy policy = createPolicy();
    ApiHBaseSnapshotPolicyArguments hbaseArgs =
        new ApiHBaseSnapshotPolicyArguments(Arrays.asList("table1", "table2"),
            Storage.LOCAL);
    policy.setHBaseArguments(hbaseArgs);
    checkJsonXML(policy);

    ApiHBaseSnapshotResult result = new ApiHBaseSnapshotResult();
    result.setProcessedTableCount(1);
    result.setProcessedTables(Arrays.asList("t1"));
    result.setUnprocessedTableCount(2);
    result.setUnprocessedTables(Arrays.asList("t2", "t3"));
    result.setCreatedSnapshotCount(1);
    result.setCreatedSnapshots(
        Arrays.asList(
            new ApiHBaseSnapshot("s1", "t1", new Date(0), Storage.LOCAL)));
    result.setDeletedSnapshotCount(2);
    result.setDeletedSnapshots(
        Arrays.asList(
            new ApiHBaseSnapshot("s2", "t1", new Date(0), Storage.LOCAL),
            new ApiHBaseSnapshot("s3", "t2", new Date(0), Storage.LOCAL)));
    result.setCreationErrorCount(1);
    result.setCreationErrors(
        Arrays.asList(
            new ApiHBaseSnapshotError("t1", "s1", Storage.LOCAL, "error 1")));
    result.setDeletedSnapshotCount(1);
    result.setDeletionErrors(
        Arrays.asList(
            new ApiHBaseSnapshotError("t2", "s3", Storage.LOCAL, "error 2")));
    result.setDeletionErrorCount(1);

    ApiSnapshotCommand cmd = new ApiSnapshotCommand();
    fillInCommand(cmd);
    cmd.setHBaseResult(result);

    checkJsonXML(cmd);
  }

  @Test
  public void testHdfsScheduledSnapshots() throws Exception {
    ApiSnapshotPolicy policy = createPolicy();

    ApiHdfsSnapshotPolicyArguments hdfsArgs =
        new ApiHdfsSnapshotPolicyArguments(Arrays.asList("/a", "/b"));
    policy.setHdfsArguments(hdfsArgs);

    checkJsonXML(policy);

    ApiHdfsSnapshotResult result = new ApiHdfsSnapshotResult();
    result.setProcessedPathCount(1);
    result.setProcessedPaths(Arrays.asList("/a"));
    result.setUnprocessedPathCount(2);
    result.setUnprocessedPaths(Arrays.asList("/b", "/c"));
    result.setCreatedSnapshotCount(1);
    result.setCreatedSnapshots(
        Arrays.asList(new ApiHdfsSnapshot("/a", "s1", "/a/.snapshot/s1", new Date(0))));
    result.setDeletedSnapshotCount(2);
    result.setDeletedSnapshots(
        Arrays.asList(new ApiHdfsSnapshot("/a", "s2", "/a/.snapshot/s2", new Date(0)),
            new ApiHdfsSnapshot("/b", "s3", "/b/.snapshot/s3", new Date(0))));
    result.setCreationErrorCount(1);
    result.setCreationErrors(
        Arrays.asList(new ApiHdfsSnapshotError(
            "/a", "s3", "error 1")));
    result.setDeletedSnapshotCount(1);
    result.setDeletionErrors(
        Arrays.asList(new ApiHdfsSnapshotError(
            "/b", "s4", "error 2")));
    result.setDeletionErrorCount(1);

    ApiSnapshotCommand cmd = new ApiSnapshotCommand();
    fillInCommand(cmd);
    cmd.setHdfsResult(result);

    checkJsonXML(cmd);
  }

  @Test
  public void testApiDashboards() throws Exception {
    ApiDashboard view = new ApiDashboard("name", "json");
    checkJsonXML(view);
    ApiDashboardList viewResponse = new ApiDashboardList(ImmutableList.of(view));
    checkJsonXML(viewResponse);
  }

  @Test
  public void testImpalaQueryAttributes() throws Exception {
    ApiImpalaQueryAttribute attribute = new ApiImpalaQueryAttribute();
    attribute.setName("name");
    attribute.setType("STRING");
    attribute.setDisplayName("displayName");
    attribute.setSupportsHistograms(true);
    attribute.setDescription("description");
    checkJsonXML(attribute);
    ApiImpalaQueryAttributeList list = new ApiImpalaQueryAttributeList(
        ImmutableList.of(attribute));
    checkJsonXML(list);
  }

  @Test
  public void testYarnApplicationAttributes() throws Exception {
    ApiYarnApplicationAttribute attribute = new ApiYarnApplicationAttribute();
    attribute.setName("name");
    attribute.setType("STRING");
    attribute.setDisplayName("displayName");
    attribute.setSupportsHistograms(true);
    attribute.setDescription("description");
    checkJsonXML(attribute);
    ApiYarnApplicationAttributeList list = new ApiYarnApplicationAttributeList(
        ImmutableList.of(attribute));
    checkJsonXML(list);
  }

  private ApiSnapshotPolicy createPolicy() {
    ApiSnapshotPolicy policy = new ApiSnapshotPolicy("policy1",
        "some description", 10, 20, 30, 40, 50);
    policy.setMinuteOfHour((byte) 15);
    policy.setHourOfDay((byte) 12);
    policy.setDayOfWeek((byte) 3);
    policy.setDayOfMonth((byte) 31);
    policy.setMonthOfYear((byte) 6);
    policy.setHoursForHourlySnapshots(Arrays.asList((byte) 4, (byte) 8));
    policy.setPaused(false);

    return policy;
  }

  @Test
  public void testYarnApplication() throws Exception {
    ApiMr2AppInformation mr2Information = new ApiMr2AppInformation("jobState");
    ApiYarnApplication application = new ApiYarnApplication(
        "appId", "appName", new Date(), new Date(), "user", "pool",
        "FINISHED", 80.0, mr2Information, Maps.<String, String>newHashMap(),
        ImmutableList.of("foo"), 1234L, 5678L,
        123, 1, 3, 1d, 1.5d, 2d, 3d, 4d, 5d);
    checkJsonXML(application);

    ApiYarnApplicationResponse response = new ApiYarnApplicationResponse(
        ImmutableList.<ApiYarnApplication>of(), ImmutableList.<String>of());
    checkJsonXML(response);

    ApiYarnKillResponse cancelResponse =
        new ApiYarnKillResponse("warning");
    checkJsonXML(cancelResponse);
  }

  @Test
  public void testClusterVersion() {
    assertEquals(ApiClusterVersion.CDH4,
        ApiClusterVersion.fromString("CDH4"));
    assertEquals(ApiClusterVersion.UNKNOWN,
        ApiClusterVersion.fromString("invalidVersion"));
  }

  @Test
  public void testApiErrorMessage() throws Exception {
    Throwable cause = new Throwable("'cause.");
    Throwable t = new Throwable("To err is human...", cause);
    checkJsonXML(new ApiErrorMessage(t));
  }

  @Test
  public void testApiTimeSeriesData() throws Exception {
    ApiTimeSeriesData data = new ApiTimeSeriesData();
    Date now = ApiUtils.newDateFromMillis(Instant.now().getMillis());
    // Test no aggregate statistics.
    data.setTimestamp(now);
    data.setValue(3.14);
    data.setType("something");
    checkJsonXML(data);

    // Test with aggregate statistics but no cross entity metadata.
    ApiTimeSeriesAggregateStatistics aggStats =
        new ApiTimeSeriesAggregateStatistics();
    aggStats.setCount(42);
    aggStats.setMax(3.14);
    aggStats.setMaxTime(now);
    aggStats.setMean(3.13);
    aggStats.setMin(3.11);
    aggStats.setMinTime(now);
    aggStats.setSampleTime(now);
    aggStats.setSampleValue(3.12);
    aggStats.setStdDev(0.1);
    data.setAggregateStatistics(aggStats);
    checkJsonXML(data);

    ApiTimeSeriesCrossEntityMetadata xEntityMetadata =
        new ApiTimeSeriesCrossEntityMetadata();
    xEntityMetadata.setMaxEntityDisplayName("maxEntityDisplayName");
    xEntityMetadata.setMinEntityDisplayName("minEntityDisplayName");
    xEntityMetadata.setMaxEntityName("maxEntityName");
    xEntityMetadata.setMinEntityName("minEntityName");
    xEntityMetadata.setNumEntities(3.14);
    aggStats.setCrossEntityMetadata(xEntityMetadata);
    checkJsonXML(data);
  }

  private List<ApiHealthCheck> createHealthChecks() {
    return ImmutableList.of(
        new ApiHealthCheck("TEST1",
                           ApiHealthSummary.GOOD,
                           "Dummy Health explanation.",
                           false),
        new ApiHealthCheck("TEST2",
                           ApiHealthSummary.CONCERNING,
                           "Dummy Health explanation.",
                           false));
  }

  private List<ApiEntityType> createMaintenanceOwners() {
    return ImmutableList.of(ApiEntityType.CLUSTER, ApiEntityType.SERVICE);
  }

  private ApiHdfsReplicationArguments newHdfsReplicationArguments() {
    ApiHdfsReplicationArguments hdfsArgs = new ApiHdfsReplicationArguments(
        new ApiServiceRef("p1", "c1", "s1"), "a", "b", "mr1", 5, "admin");
    hdfsArgs.setSchedulerPoolName("pool");
    hdfsArgs.setBandwidthPerMap(200);
    hdfsArgs.setAbortOnError(true);
    hdfsArgs.setRemoveMissingFiles(true);
    hdfsArgs.setPreserveReplicationCount(true);
    hdfsArgs.setPreserveBlockSize(true);
    hdfsArgs.setPreservePermissions(true);
    hdfsArgs.setLogPath("log1");
    hdfsArgs.setSkipChecksumChecks(true);
    hdfsArgs.setSkipTrash(true);
    hdfsArgs.setPreserveXAttrs(true);
    hdfsArgs.setReplicationStrategy(ReplicationStrategy.DYNAMIC);
    hdfsArgs.setExclusionFilters(Lists.newArrayList("/a/.*", "/b/.*"));
    return hdfsArgs;
  }

  private ApiHdfsReplicationResult newHdfsReplicationResult() {
    ApiHdfsReplicationResult result = new ApiHdfsReplicationResult();
    result.setProgress(42);
    result.setCounters(Arrays.asList(
        new ApiHdfsReplicationCounter("g1", "n1", 1L),
        new ApiHdfsReplicationCounter("g2", "n2", 2L)));
    result.setNumFilesCopied(10);
    result.setNumBytesCopied(100);
    result.setNumFilesSkipped(20);
    result.setNumBytesSkipped(200);
    result.setNumFilesDeleted(30);
    result.setNumFilesCopyFailed(40);
    result.setNumBytesCopyFailed(400);
    result.setSetupError("error");
    result.setSnapshottedDirs(Arrays.asList("/user/a"));
    result.setFailedFiles(Arrays.asList("path1"));
    result.setRunAsUser("systest");
    return result;
  }

  private void fillInCommand(ApiCommand cmd) {
    cmd.setId(1L);
    cmd.setName("command");
    cmd.setStartTime(new Date(0));
    cmd.setEndTime(new Date(0));
    cmd.setActive(true);
    cmd.setSuccess(false);
    cmd.setResultMessage("message");
    cmd.setResultDataUrl("url");
    cmd.setCanRetry(false);
  }

  private ApiCluster newCluster() {
    ApiCluster cluster = new ApiCluster();
    cluster.setMaintenanceOwners(Arrays.asList(ApiEntityType.CLUSTER));
    cluster.setMaintenanceMode(true);
    cluster.setName("mycluster");
    cluster.setDisplayName("mycluster-displayName");
    cluster.setUuid("abcd-efg-hijk-lmnop");
    cluster.setClusterUrl("http://some-url:7180/cmf/clusterRedirect/mycluster");
    cluster.setHostsUrl("http://some-url:7180/cmf/clusterRedirect/mycluster/hosts");
    cluster.setVersion(ApiClusterVersion.CDH4);
    cluster.setFullVersion("4.1.2");
    cluster.setEntityStatus(ApiEntityStatus.GOOD_HEALTH);
    return cluster;
  }

  private ApiHost newHost() {
    List<ApiRoleRef> roleRefs = Arrays.asList(
        new ApiRoleRef("clusterName", "serviceName", "roleName"));

    ApiHost host = new ApiHost();
    host.setCommissionState(ApiCommissionState.COMMISSIONED);
    host.setHealthChecks(createHealthChecks());
    host.setHealthSummary(ApiHealthSummary.GOOD);
    host.setEntityStatus(ApiEntityStatus.GOOD_HEALTH);
    host.setHostId("myHostId");
    host.setHostUrl("http://foo:7180");
    host.setHostname("myHostname");
    host.setIpAddress("1.1.1.1");
    host.setLastHeartbeat(new Date(0));
    host.setMaintenanceMode(true);
    host.setMaintenanceOwners(Arrays.asList(ApiEntityType.HOST));
    host.setNumCores(4L);
    host.setNumPhysicalCores(4L);
    host.setRackId("/default");
    host.setRoleRefs(roleRefs);
    host.setTotalPhysMemBytes(1234L);
    host.setClusterRef(new ApiClusterRef("clusterName"));
    return host;
  }

  private ApiUser newUser() {
    ApiUser user = new ApiUser();
    user.setName("myuser");
    user.setPassword("correct horse battery staple");
    user.addRole("myrole");
    user.setPwHash("hash");
    user.setPwSalt(1L);
    user.setPwLogin(true);
    return user;
  }

}
