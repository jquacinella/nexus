curl -XPUT -H "Content-Type: application/json" "https://nexus.example.com/v1/resolvers/myorg/myproj/nxv:myresolver" -d \
'{
  "@type": [
    "Resolver",
    "InAccount"
  ],
  "resourceTypes": [
    "nxv:Schema"
  ],
  "identities": [
    {
      "realm": "myrealm",
      "group": "some-project"
    }
  ],
  "priority": 50
}'