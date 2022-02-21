package com.mycompany;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.CamelContext;

/**
 * Generate random order
 */
public class OrderGenerator {

    private AtomicInteger count = new AtomicInteger(1);
    private AtomicInteger bodyCounter = new AtomicInteger(1);

   
    public String generateOrder(CamelContext camelContext) {
    	   	
    	return new String("This is the content of the Order with number  #" + bodyCounter.getAndIncrement() + ": used as body file.");
    }

    public String generateFileName() {
        return "order" + count.getAndIncrement() + ".xml";
    }
}
