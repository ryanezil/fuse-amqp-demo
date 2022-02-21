= AMQP Fuse Integration route

This demo has been tested on Red Hat Fuse 6.3 (2.4.0.redhat-630446)

== Build & Deploy

1. Build the project

```bash
> mvn clean install
```


2. Configure Dependencies

Open Fuse console and exec:

```bash
> features:install camel-amqp
> osgi:install -s wrap:mvn:org.apache.qpid/qpid-amqp-1-0-client-jms/0.32
> osgi:install -s wrap:mvn:org.apache.qpid/qpid-amqp-1-0-client/0.32
> osgi:install -s wrap:mvn:org.apache.qpid/qpid-amqp-1-0-common/0.32
```

3. Configure AMQ

The Camel context is configured with the following parameters:

```xml
<!-- OPENWIRE connection -->
<bean class="org.apache.activemq.ActiveMQConnectionFactory" id="connectionFactory">
    <property name="brokerURL" value="tcp://127.0.0.1:61616"/>
    <property name="userName" value="amq-demo-user"/>
    <property name="password" value="password"/>
</bean>
```

and

```xml
<!-- AMQP connection -->
<bean class="org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl">
    <constructor-arg index="0" value="localhost"/>
    <constructor-arg index="1" value="5672"/>
    <constructor-arg index="2" value="amq-demo-user"/>
    <constructor-arg index="3" value="password"/>
    <property name="topicPrefix" value="topic://"/>
</bean>
```

You'll need to change them using your own values.

4. Deploy into Fuse

```bash
osgi:install -s mvn:com.mycompany/camel-spring/1.0.0-SNAPSHOT
```

The output log when the route is running should be something similar to:

```
2022-02-21 18:23:10,650 | INFO  | [incomingOrders] | consume-order-to-file | 232 - org.apache.camel.camel-core - 2.17.0.redhat-630446 | AMQP consumer - Storing order in file order4.xml ...
2022-02-21 18:23:10,652 | INFO  | [incomingOrders] | consume-order-to-file | 232 - org.apache.camel.camel-core - 2.17.0.redhat-630446 | Done: processing order4.xml
```

== Troubleshooting

=== Check required dependencies by the bundle:

```bash
> osgi:headers karaf-springdsl-fuse63

karaf-springdsl-fuse63 (405)
----------------------------
Created-By = Apache Maven Bundle Plugin
Manifest-Version = 1.0
Bnd-LastModified = 1645467692823
Build-Jdk = 1.8.0_312
Built-By = ryanezil
Tool = Bnd-3.2.0.201605172007

Bundle-License = https://www.apache.org/licenses/LICENSE-2.0.html
Bundle-ManifestVersion = 2
Bundle-SymbolicName = karaf-springdsl-fuse63
Bundle-Version = 1.0.0.SNAPSHOT
Bundle-Name = karaf-springdsl-fuse63
Bundle-Description = Messaging Spring Example

Require-Capability =
	osgi.ee;filter:=(&(osgi.ee=JavaSE)(version=1.8))

Export-Package =
	com.mycompany;uses:="org.apache.camel,org.apache.camel.builder";version=1.0.0
Import-Package =
	org.apache.activemq,
	org.apache.camel;version="[2.17,3)",
	org.apache.camel.builder;version="[2.17,3)",
	org.apache.camel.component.jms;version="[2.17,3)",
	org.apache.camel.model;version="[2.17,3)",
	org.apache.qpid.amqp_1_0.jms.impl

```

=== Check wired packages/dependencies to the bundle:

Use the same ID (in this example, 405) returned by the above command

```bash
 packages:imports 405
activemq-osgi (219): org.apache.activemq; version=5.11.0.redhat-630446
camel-core (232): org.apache.camel.model; version=2.17.0.redhat-630446
camel-core (232): org.apache.camel.builder; version=2.17.0.redhat-630446
camel-core (232): org.apache.camel; version=2.17.0.redhat-630446
camel-jms (244): org.apache.camel.component.jms; version=2.17.0.redhat-630446
wrap_mvn_org.apache.qpid_qpid-amqp-1-0-client-jms_0.32 (399): org.apache.qpid.amqp_1_0.jms.impl; version=0.0.0
```


