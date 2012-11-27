import sys
import getopt
from cm_api.api_client import ApiResource
from cm_api.endpoints import role_config_groups
from cm_api.endpoints.types import ApiList

# List all RCGs and with roles and configs.
def list_all_rcgs(api):
  for c in api.get_all_clusters():
    for s in c.get_all_services():
      for r in s.get_all_role_config_groups():
        print r
        for role in r.get_all_roles():
          print " *" + str(role)        
        config = r.get_config()
        print "config: " + str(config)

def main():
  print "== Test =="
  api = ApiResource('localhost', 7180, 'admin', 'admin') 
  
  api.delete_cluster("cluster1")
  api.delete_host("host1")  

  host1 = api.create_host("host1", "host1", "1.1.1.1")
  print host1  

  cluster = api.create_cluster("cluster1", 1)
  print cluster

  service = cluster.create_service("hdfs1", "HDFS")
  print service

  # Create RCGs.
  group_namenode = service.create_role_config_group("group-namenode",
      "group-namenode-displayname", "NAMENODE")
  print "CREATED GROUP: " + str(group_namenode)

  group_datanode = service.create_role_config_group("group-datanode",
       "group-datanode-displayname", "DATANODE")
  print "CREATED GROUP: " + str(group_datanode)

  # Create roles.
  role_namenode = service.create_role("role-namenode", "NAMENODE", "host1")
  print "CREATED ROLE:" + str(role_namenode)
  role_datanode = service.create_role("role-datanode", "DATANODE", "host1")
  print "CREATED ROLE:" + str(role_datanode)
  
  # List all RCGs.
  list_all_rcgs(api)

  g = service.get_role_config_group("group-namenode")
  print "FOUND GROUP: " + str(g)

  # Move roles.
  moved_roles = group_namenode.move_roles(["role-namenode"])
  print "MOVED ROLE:" + str(moved_roles)
  moved_roles = group_datanode.move_roles(["role-datanode"])
  print "MOVED ROLE:" + str(moved_roles)

  r = service.get_role("role-namenode")
  print "ROLE: " + str(r) + " GROUP REF: " \
      + str(r.roleConfigGroupRef.roleConfigGroupName)

  # Get full configs.
  updated_cfg = group_namenode.update_config({"max_log_size" : 200})
  print updated_cfg
  cfg = group_namenode.get_config("FULL")
  if (cfg["max_log_size"] == updated_cfg["max_log_size"]):
    print "OK."

  # Move roles back to base group.
  moved_roles = role_config_groups.move_roles_to_base_role_config_group(
      group_namenode._get_resource_root(),
      "hdfs1", ["role-namenode", "role-datanode"], "cluster1")
  print "MOVED ROLES:" + str(moved_roles)

  # List all RCGs.
  list_all_rcgs(api)

  # Delete the roles.
  g = service.delete_role("role-namenode")
  print "DELETED GROUP: " + str(g)
  g = service.delete_role("role-datanode")
  print "DELETED GROUP: " + str(g)

  # Delete the groups.
  service.delete_role_config_group("group-namenode")
  service.delete_role_config_group("group-datanode")

  # List all RCGs.
  list_all_rcgs(api)

if __name__ == "__main__":
  main()
