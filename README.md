# lacinia-example

Example app using Lacinia GraphQL library

## Usage

Start web service at port 3000:

```sh
lein ring server-headless
```

Then send POST requests to `/graphql` with GraphQL query data:

```sh
curl -XPOST localhost:3000/graphql -d '{hero {id name}}'

curl -XPOST localhost:3000/graphql -d '{hero(episode: NEWHOPE) {id name}}'

curl -XPOST localhost:3000/graphql -d '{hero(episode: EMPIRE) {id name}}'

curl -XPOST localhost:3000/graphql -d '{hero(episode: JEDI) {id name}}'

curl -XPOST localhost:3000/graphql -d '{hero {id name friends {name}}}'

curl -XPOST localhost:3000/graphql -d '{hero(episode: NEWHOPE) {id name friends {name}}}'

curl -XPOST localhost:3000/graphql -d '{human(id: "1000") {id name}}'

curl -XPOST localhost:3000/graphql -d '{human {id name}}'

curl -XPOST localhost:3000/graphql -d '{humans {id name}}'

curl -XPOST localhost:3000/graphql -d '{droids {id name}}'

curl -XPOST localhost:3000/graphql -d 'mutation {createHuman(id: "10006" name: "Kylo Ren") {id name}}'
```

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
