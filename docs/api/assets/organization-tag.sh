curl -XPUT -H "Content-Type: application/json" "https://nexus.example.com/v1/orgs/myorg/tags?rev=2" -d \
'{
  "tag": "mytag",
  "rev": 1
}'