# Design choices

## Datomic memory store

The Datomic database uses a memory store just for easiness of setup.
After all, this is just for demonstrational purposes.

The [io.rkn/conformity](https://github.com/rkneufeld/conformity)
library has been used to ensure idempotency of operations when
transacting the Datomic schema and sample data.

## Error handling

No error handling has been implemented for lack of time. A good choice
for implementing validation and error reporting is clojure.spec and
maybe [phraser](https://github.com/alexanderkiel/phrase) for producing
nice error messages for the end user.

## Lacinia resolvers wrt Datmoic queries

Each Lacinia resolver makes a query to the Datomic db. This is an ok
solution when the Lacinia server is also a Datomic peer. Not that good
when querying Datomic as
a [datomic client](http://docs.datomic.com/clojure-client/index.html).

The Big Kahuna is translating Graphql queries to Datomic pull queries.
That would allow to fetch all attributes in one go with one single
pull request instead of quering the database whenever an attribute
needs to be resolved. This is by the way mitigated by Datomic because
of the good data locality it exposes for related datoms. The Datomic's
peer caching strategy should kick in and avoid network round trips in
many cases.

Anyway, the "Graphql->Datomic pull request" translation layer would
allow to traverse the entity graph without making multiple (and in
many cases costly) queries on big databases.
