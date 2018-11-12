curl -XPUT -H "Content-Type: application/json" "https://nexus.example.com/v1/orgs/myorg/myproject" -d \
'{
  "name": "example project creation",
  "base": "https://nexus.example.com/v1/projects/myorg/myproject",
  "prefixMappings": [
    {
      "prefix": "person",
      "namespace": "http://example.com/some/person"
    },
    {
      "prefix": "schemas",
      "namespace": "https://bluebrain.github.io/nexus/schemas/"
    },
    {
      "prefix": "ex",
      "namespace": "http://example.com/"
    }
  ]
}'