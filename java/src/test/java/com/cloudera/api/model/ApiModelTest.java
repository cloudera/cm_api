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

import com.cloudera.api.ApiObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiModelTest {
  private final static String TEXT_ENCODING = "UTF-8";

  public static String objectToXml(Object object)
      throws JAXBException, UnsupportedEncodingException {
    JAXBContext jc = JAXBContext.newInstance(object.getClass());
    Marshaller m = jc.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(object, baos);
    return baos.toString(TEXT_ENCODING);
  }

  public static <T> T xmlToObject(String text, Class<T> type)
      throws JAXBException, UnsupportedEncodingException,
      IllegalAccessException, InstantiationException {
    JAXBContext jc = JAXBContext.newInstance(type);
    Unmarshaller um = jc.createUnmarshaller();
    ByteArrayInputStream bais =
        new ByteArrayInputStream(text.getBytes(TEXT_ENCODING));
    Object res = um.unmarshal(bais);
    return type.cast(res);
  }

  public static String objectToJson(Object object) throws IOException {
    ApiObjectMapper objectMapper = new ApiObjectMapper();
    return objectMapper.writeValueAsString(object);
  }

  public static <T> T jsonToObject(String text, Class<T> type)
      throws IOException {
    ApiObjectMapper objectMapper = new ApiObjectMapper();
    return objectMapper.readValue(text, type);
  }

  private <T> void checkJson(Class<? extends T> type, T object, boolean encodeOnly)
      throws IOException {
    String jsonText = objectToJson(object);
    System.out.println(jsonText);
    if (!encodeOnly) {
      T objectFromJson = jsonToObject(jsonText, type);
      assertEquals("Object did not encode/decode JSON correctly",
                   object, objectFromJson);
    }
  }

  /**
   * Checks that the encoded JSON for the object has all the expected
   * properties.
   * <p>
   * Note that due to a Jackson bug (http://jira.codehaus.org/browse/JACKSON-791),
   * only non-null properties are serialized, no matter what the object mapper's
   * configuration says.
   */
  public static void checkJsonProperties(Object object, String... properties)
      throws IOException {
    ApiObjectMapper mapper = new ApiObjectMapper();

    String jsonText = mapper.writeValueAsString(object);
    System.out.println(jsonText);

    HashMap decoded = mapper.readValue(jsonText, HashMap.class);

    @SuppressWarnings("unchecked")
    Set<String> jsonProps = new HashSet<String>(decoded.keySet());
    for (String property : properties) {
      assertTrue(property + " not found", jsonProps.remove(property));
    }
    assertTrue(jsonProps.isEmpty());
  }

  private <T> void checkXML(Class<? extends T> type, T object, boolean encodeOnly)
      throws JAXBException, UnsupportedEncodingException,
      InstantiationException, IllegalAccessException {
    String xmlText = objectToXml(object);
    System.out.println(xmlText);
    if (!encodeOnly) {
      T objectFromXml = xmlToObject(xmlText, type);
      assertEquals("Object did not encode/decode XML correctly",
                   object, objectFromXml);
    }
  }

  private <T> void checkJsonXML(Class<? extends T> type, T object)
      throws IOException, JAXBException, IllegalAccessException,
      InstantiationException {
    checkJson(type, object, false);
    checkXML(type, object, false);
  }

  private <T> void checkJsonXMLEncoding(Class<? extends T> type, T object)
      throws IOException, JAXBException, IllegalAccessException,
      InstantiationException {
    checkJson(type, object, true);
    checkXML(type, object, true);
  }

  @Test
  public void testApiCluster() throws Exception {
    ApiCluster clusterV1 = new ApiCluster();
    clusterV1.setName("mycluster");
    clusterV1.setVersion(ApiClusterVersion.CDH4);
    checkJsonXML(clusterV1.getClass(), clusterV1);
    checkJsonProperties(clusterV1, "name", "version");

    ApiClusterRef ref = new ApiClusterRef(clusterV1.getName());
    checkJsonXMLEncoding(ref.getClass(), ref);

    ApiCluster clusterV2 = new ApiCluster();
    clusterV2.setMaintenanceOwners(Arrays.asList(ApiEntityType.CLUSTER));
    clusterV2.setMaintenanceMode(true);
    clusterV2.setName("mycluster");
    clusterV2.setVersion(ApiClusterVersion.CDH4);
    assertTrue(clusterV2.getMaintenanceMode());
    checkJsonProperties(clusterV2, "name", "version", "maintenanceMode",
                        "maintenanceOwners");
  }

  @Test
  public void testApiConfig() throws Exception {
    ApiConfig cfg = new ApiConfig("name", "value");
    checkJsonXML(cfg.getClass(), cfg);
    ApiConfigList configList = new ApiConfigList(Arrays.asList(cfg));
    checkJsonXML(configList.getClass(), configList);

    ApiServiceConfig svcCfg = new ApiServiceConfig();
    svcCfg.add(cfg);
    ApiRoleTypeConfig rtCfg = new ApiRoleTypeConfig();
    rtCfg.add(cfg);
    svcCfg.setRoleTypeConfigs(Arrays.asList(rtCfg));
    checkJsonXML(svcCfg.getClass(), svcCfg);

    cfg = new ApiConfig("name", "value", true, "default", "display",
        "description", "relatedName", ApiConfig.ValidationState.OK, "validationMessage");
    checkJsonProperties(cfg, "name", "value", "required", "default",
        "displayName", "description", "relatedName", "validationState",
        "validationMessage");
  }

  @Test
  public void testApiHost()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    List<ApiRoleRef> roleRefs = Lists.newArrayList();
    roleRefs.add(new ApiRoleRef("clusterName", "serviceName", "roleName"));
    ApiHost host = new ApiHost();
    host.setHostId("myHostId");
    host.setHostname("myHostname");
    host.setIpAddress("1.1.1.1");
    host.setRackId("/default");
    host.setLastHeartbeat(new Date());
    host.setHealthSummary(ApiHealthSummary.GOOD);
    host.setHealthChecks(null);
    host.setRoleRefs(roleRefs);
    host.setHostUrl("http://foo:7180");
    checkJsonXMLEncoding(host.getClass(), host);
    ApiHostList hostList = new ApiHostList(Arrays.asList(host));
    checkJsonXMLEncoding(hostList.getClass(), hostList);

    // check v2 attributes
    host.setMaintenanceOwners(Arrays.asList(ApiEntityType.HOST));
    host.setMaintenanceMode(true);
    host.setCommissionState(ApiCommissionState.COMMISSIONED);
    assertTrue(host.getMaintenanceMode());
    checkJsonProperties(host, "hostId", "ipAddress", "hostname", "rackId",
                        "lastHeartbeat", "healthSummary", "roleRefs",
                        "hostUrl", "maintenanceMode", "maintenanceOwners", "commissionState");
  }

  @Test
  public void testApiHealthCheck() throws Exception {
    ApiHealthCheck healthCheck = new ApiHealthCheck("checkName",
                                                    ApiHealthSummary.GOOD);
    checkJsonXMLEncoding(healthCheck.getClass(), healthCheck);
    checkJsonProperties(healthCheck, "name", "summary");
  }

  @Test
  public void testHost() throws Exception {
    ApiHost host = new ApiHost();
    host.setHostId("hostId");
    host.setIpAddress("1.1.1.1");
    host.setHostname("hostname");
    host.setRackId("/default");
    host.setLastHeartbeat(new Date(0));
    host.setHealthSummary(ApiHealthSummary.GOOD);
    host.setHealthChecks(Lists.<ApiHealthCheck>newArrayList());
    host.setRoleRefs(Lists.<ApiRoleRef>newArrayList());
    host.setHostUrl("http://foo:7180");
    checkJsonXMLEncoding(host.getClass(), host);
    checkJsonProperties(host, "healthChecks", "healthSummary", "hostId",
                        "hostname", "ipAddress", "lastHeartbeat",
                        "rackId", "roleRefs", "hostUrl");
  }

  @Test
  public void testApiRole()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    List<ApiHealthCheck> healthChecks = Lists.newArrayList();
    healthChecks.add(new ApiHealthCheck("TEST1",
                                        ApiHealthSummary.GOOD));
    healthChecks.add(new ApiHealthCheck("TEST2",
                                        ApiHealthSummary.CONCERNING));
    ApiRole role = new ApiRole();
    role.setName("myname");
    role.setType("mytype");
    role.setServiceRef(new ApiServiceRef("mycluster", "myservice"));
    role.setHostRef(new ApiHostRef("myhost"));
    role.setRoleState(ApiRoleState.STARTED);
    role.setHealthSummary(ApiHealthSummary.GOOD);
    role.setHealthChecks(healthChecks);
    role.setConfigStale(false);
    role.setHaStatus(ApiRole.HaStatus.ACTIVE);
    role.setRoleUrl("http://localhost:7180");
    checkJsonXMLEncoding(role.getClass(), role);
    checkJsonProperties(role, "name", "type", "hostRef", "serviceRef",
                        "roleState", "healthSummary", "configStale",
                        "healthChecks", "haStatus", "roleUrl");

    ApiRole roleV2 = new ApiRole();
    roleV2.setName("myname");
    roleV2.setType("mytype");
    roleV2.setServiceRef(new ApiServiceRef("mycluster", "myservice"));
    roleV2.setHostRef(new ApiHostRef("myhost"));
    roleV2.setRoleState(ApiRoleState.STARTED);
    roleV2.setHealthSummary(ApiHealthSummary.GOOD);
    roleV2.setHealthChecks(healthChecks);
    roleV2.setConfigStale(false);
    roleV2.setHaStatus(ApiRole.HaStatus.ACTIVE);
    roleV2.setRoleUrl("http://localhost:7180");
    roleV2.setMaintenanceOwners(Arrays.asList(ApiEntityType.CLUSTER));
    roleV2.setMaintenanceMode(true);
    roleV2.setCommissionState(ApiCommissionState.COMMISSIONED);
    checkJsonXMLEncoding(roleV2.getClass(), role);
    checkJsonProperties(roleV2, "name", "type", "hostRef", "serviceRef",
                        "roleState", "healthSummary", "configStale",
                        "healthChecks", "haStatus", "roleUrl",
                        "maintenanceMode", "maintenanceOwners", "commissionState");
  }

  @Test
  public void testApiService()
      throws JAXBException, IOException, IllegalAccessException,
      InstantiationException {
    List<ApiHealthCheck> healthChecks = Lists.newArrayList();
    healthChecks.add(new ApiHealthCheck("TEST1",
                                        ApiHealthSummary.GOOD));
    healthChecks.add(new ApiHealthCheck("TEST2",
                                        ApiHealthSummary.CONCERNING));
    ApiService service = new ApiService();
    service.setName("myname");
    service.setType("mytype");
    service.setClusterRef(new ApiClusterRef("mycluster"));
    service.setServiceState(ApiServiceState.STARTED);
    service.setHealthSummary(ApiHealthSummary.GOOD);
    service.setHealthChecks(healthChecks);
    service.setConfigStale(false);
    service.setServiceUrl("http://foo:7180");
    checkJsonXMLEncoding(service.getClass(), service);
    checkJsonProperties(service, "name", "type", "clusterRef",
                        "serviceState", "healthSummary", "configStale",
                        "healthChecks", "serviceUrl");

    ApiService serviceV2 = new ApiService();
    serviceV2.setName("myname");
    serviceV2.setType("mytype");
    serviceV2.setClusterRef(new ApiClusterRef("mycluster"));
    serviceV2.setServiceState(ApiServiceState.STARTED);
    serviceV2.setHealthSummary(ApiHealthSummary.GOOD);
    serviceV2.setHealthChecks(healthChecks);
    serviceV2.setConfigStale(false);
    serviceV2.setServiceUrl("http://foo:7180");
    serviceV2.setMaintenanceOwners(Arrays.asList(ApiEntityType.CLUSTER,
        ApiEntityType.SERVICE));
    serviceV2.setMaintenanceMode(true);
    assertTrue(serviceV2.getMaintenanceMode());
    serviceV2.setDisplayName("mydisplayname");
    checkJsonProperties(serviceV2, "name", "type", "clusterRef", "serviceState",
                        "healthSummary", "configStale", "healthChecks",
                        "serviceUrl", "maintenanceMode", "maintenanceOwners",
                        "displayName");
  }

  @Test
  public void testApiUser() throws Exception {
    ApiUser user = new ApiUser();
    user.setName("myuser");
    user.addRole("myrole");
    checkJsonXML(user.getClass(), user);
    ApiUserList userList = new ApiUserList(Arrays.asList(user));
    checkJsonXML(userList.getClass(), userList);
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
    checkJsonXMLEncoding(event.getClass(), event);
    checkJsonProperties(event, "id", "content", "timeOccurred",
                        "timeReceived", "category", "severity",
                        "alert", "attributes");
  }

  @Test
  public void testEventQueryResult() throws Exception {
    List<ApiEvent> events = Arrays.asList(newEvent(), newEvent());
    ApiEventQueryResult result = new ApiEventQueryResult(
        events.size(), events);
    checkJsonXMLEncoding(result.getClass(), result);
    checkJsonProperties(result, "items", "totalResults");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testLists() throws Exception {
    for (Object o : Arrays.asList(
        new ApiActivityList(Lists.<ApiActivity>newArrayList()),
        new ApiClusterList(Lists.<ApiCluster>newArrayList()),
        new ApiConfigList(Lists.<ApiConfig>newArrayList()),
        new ApiCommandList(),
        new ApiHostList(Lists.<ApiHost>newArrayList()),
        new ApiMetricList(Lists.<ApiMetric>newArrayList()),
        new ApiNameserviceList(Lists.<ApiNameservice>newArrayList()),
        new ApiRoleList(Lists.<ApiRole>newArrayList()),
        new ApiRoleNameList(Lists.<String>newArrayList()),
        new ApiRoleTypeList(Lists.<String>newArrayList()),
        new ApiServiceList(Lists.<ApiService>newArrayList()),
        new ApiUserList(Lists.<ApiUser>newArrayList()),
        new ApiHostRefList(Lists.<ApiHostRef>newArrayList())
    )) {
      checkJsonXMLEncoding(o.getClass(), o);
      checkJsonProperties(o, "items");
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
    checkJsonXMLEncoding(activity.getClass(), activity);
    checkJsonProperties(activity,
                        "name", "parent", "id", "type", "startTime", "user",
                        "status", "group", "finishTime", "inputDir",
                        "outputDir", "mapper", "reducer", "combiner",
                        "queueName", "schedulerPriority");
  }

  @Test
  public void testConfig() throws Exception {
    ApiConfig config = new ApiConfig("name", "value", false, "defaultValue",
                                     "displayName", "description",
                                     "relatedName",
                                     ApiConfig.ValidationState.OK,
                                     "validationMessage");
    checkJsonXMLEncoding(config.getClass(), config);
    checkJsonProperties(config, "name", "value", "required", "displayName",
                        "description", "relatedName", "validationState",
                        "validationMessage", "default");
  }

  @Test
  public void testEcho() throws Exception {
    ApiEcho echo = new ApiEcho("Hello... Hello... Hello...");
    checkJsonXMLEncoding(echo.getClass(), echo);
    checkJsonProperties(echo, "message");
  }

  @Test
  public void testMetric() throws Exception {
    ApiMetric metric = new ApiMetric("name", "context", "unit",
                                     Lists.<ApiMetricData>newArrayList(),
                                     "displayName", "description");
    checkJsonXMLEncoding(metric.getClass(), metric);
    checkJsonProperties(metric, "context", "description", "displayName",
                        "name", "unit", "data");
  }

  @Test
  public void testNameService() throws Exception {
    ApiNameservice ns = new ApiNameservice(
        "foo-ns",
        new ApiRoleRef("foo", "bar", "baz"),
        new ApiRoleRef("fooFC", "barFC", "bazFC"),
        new ApiRoleRef("foo2", "bar2", "baz2"),
        new ApiRoleRef("foo2FC", "bar2FC", "baz2FC"),
        new ApiRoleRef("foo3", "bar3", "baz3"),
        Arrays.asList("mount1", "mount2"));
    checkJsonXMLEncoding(ns.getClass(), ns);

    checkJsonProperties(ns, "name", "active", "activeFailoverController",
        "standBy", "standByFailoverController", "secondary", "mountPoints");
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
    checkJsonXML(haArgs.getClass(), haArgs);

    ApiHdfsDisableHaArguments dhaArgs = new ApiHdfsDisableHaArguments();
    dhaArgs.setActiveName("ann");
    dhaArgs.setSecondaryName("snn");
    dhaArgs.setStartDependentServices(false);
    dhaArgs.setDeployClientConfigs(true);
    checkJsonXML(dhaArgs.getClass(), dhaArgs);

    ApiHdfsFailoverArguments afoArgs = new ApiHdfsFailoverArguments();
    afoArgs.setNameservice("ns1");
    afoArgs.setZooKeeperService(new ApiServiceRef("c1", "s1"));
    afoArgs.setActiveFCName("fc1");
    afoArgs.setStandByFCName("fc2");
    checkJsonXML(afoArgs.getClass(), afoArgs);
  }

  @Test
  public void testCmPeer() throws Exception {
    ApiCmPeer peer = new ApiCmPeer();
    peer.setName("peer1");
    peer.setUrl("url1");
    peer.setUsername("user1");
    peer.setPassword("password1");
    checkJsonXML(peer.getClass(), peer);
  }

  @Test
  public void testDeployment() throws Exception {
    ApiDeployment deployment = new ApiDeployment();
    ApiUser user = new ApiUser();
    user.setName("foo");
    user.setPassword("secret");
    user.setRoles(Sets.<String>newHashSet());
    deployment.setUsers(Arrays.asList(user));
    ApiCluster cluster = new ApiCluster();
    cluster.setName("mycluster");
    cluster.setVersion(ApiClusterVersion.CDH3);
    deployment.setClusters(Arrays.asList(cluster));
    deployment.setTimestamp(new Date(62239));
    ApiHost host = new ApiHost();
    host.setHostId("myHostid");
    deployment.setHosts(Arrays.asList(host));
    ApiVersionInfo versionInfo = new ApiVersionInfo();
    deployment.setVersionInfo(versionInfo);
    ApiService mgmtService = new ApiService();
    mgmtService.setType("MGMT");
    mgmtService.setName("myMgmtService");
    deployment.setManagementService(mgmtService);
    deployment.setAllHostsConfig(new ApiConfigList(Arrays.asList(
        new ApiConfig("foo", "bar"))));

    ApiCmPeer peer = new ApiCmPeer();
    peer.setName("peer1");
    peer.setUrl("url1");
    peer.setUsername("user1");
    peer.setPassword("password1");
    deployment.setPeers(Arrays.asList(peer));

    checkJsonXML(deployment.getClass(), deployment);
  }

  @Test
  public void testHdfsReplication() throws Exception {
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

    ApiReplicationSchedule hdfsInfo =
      new ApiReplicationSchedule(20L, new Date(1234), new Date(5678), 10,
          ApiScheduleInterval.MONTH, true);
    hdfsInfo.setHdfsArguments(hdfsArgs);
    hdfsInfo.setNextRun(new Date(12345));

    ApiReplicationCommand cmd = new ApiReplicationCommand();
    cmd.setId(42L);
    cmd.setName("foo");
    cmd.setStartTime(new Date(4321));
    cmd.setEndTime(new Date(8765));
    cmd.setActive(false);
    cmd.setSuccess(true);
    cmd.setResultMessage("done");

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
    cmd.setHdfsResult(result);

    hdfsInfo.setHistory(Arrays.asList(cmd));

    checkJsonXML(hdfsInfo.getClass(), hdfsInfo);
  }

  @Test
  public void testApiParcel() throws Exception {
    ApiClusterRef ref = new ApiClusterRef("clusterName");
    checkJsonXMLEncoding(ref.getClass(), ref);

    ApiParcel parcel = new ApiParcel();
    parcel.setProduct("productName");
    parcel.setVersion("version");
    parcel.setStage("DOWNLOADING");
    parcel.setClusterRef(ref);
    checkJsonXML(parcel.getClass(), parcel);
    checkJsonProperties(parcel, "product", "version", "stage", "clusterRef");

    ApiParcelList list = new ApiParcelList();
    list.setParcels(Arrays.asList(parcel));
    checkJsonXML(list.getClass(), list);
    checkJsonProperties(list, "items");
  }

  @Test
  public void testHiveReplication() throws Exception {
    ApiHiveReplicationArguments args = new ApiHiveReplicationArguments();
    args.setSourceService(new ApiServiceRef("c1", "s1"));
    args.setTableFilters(Arrays.asList(new ApiHiveTable("db1", "table1")));
    args.setExportDir("foo");
    args.setForce(true);
    args.setReplicateData(true);
    ApiHdfsReplicationArguments hdfsArgs = new ApiHdfsReplicationArguments(
        new ApiServiceRef("p1", "c1", "s1"), "a", "b", "mr1", 5, "admin");
    args.setHdfsArguments(hdfsArgs);

    checkJsonXML(args.getClass(), args);

    ApiReplicationSchedule sch = new ApiReplicationSchedule();
    sch.setHiveArguments(args);
    checkJsonXML(sch.getClass(), sch);

    ApiHiveReplicationResult res = new ApiHiveReplicationResult();
    res.setTables(Arrays.asList(new ApiHiveTable("db1", "table1")));
    res.setErrors(Arrays.asList(new ApiHiveReplicationError("db1", "table1", "error1")));
    res.setDryRun(true);

    ApiHdfsReplicationResult hdfsRes = new ApiHdfsReplicationResult();
    hdfsRes.setProgress(42);
    res.setDataReplicationResult(hdfsRes);

    checkJsonXML(res.getClass(), res);

    ApiReplicationCommand cmd = new ApiReplicationCommand();
    cmd.setHiveResult(res);
    checkJsonXML(cmd.getClass(), cmd);
  }

}
