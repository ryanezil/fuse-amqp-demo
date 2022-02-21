package com.mycompany;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		
		/*
		 *  A route for generating a random order every 10 seconds
		 *  It publishes the order into a JMS Queue using JmsComponent configured
		 *  with 'org.apache.activemq.ActiveMQConnectionFactory' (hence, using Openwire)
		 */
		from("timer:order?period=10000")
		.routeId("generate-order")
		.bean("orderGenerator","generateOrder")
		.setHeader("Exchange.FILE_NAME", method("orderGenerator","generateFileName"))
		.to("jmsComponent:queue:incomingOrders?transacted=true");


		/*
		 *  A route consuming Orders using AMQP Component
		 */		
		from("jmsComponentAmqp:queue:incomingOrders?concurrentConsumers=1&maxConcurrentConsumers=5")
		.streamCaching("true")
		.routeId("consume-order-to-file")
		.log(LoggingLevel.INFO, "AMQP consumer - Storing order in file ${file:name} ...")
		.to("file:work-demo/output")
		.log("Done: processing ${file:name}");		
	}
}
