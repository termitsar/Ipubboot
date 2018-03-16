package com.example.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.service.ForTest;


public class MyTest {
	
	@Test
    public void testCalA() throws Exception {
		ForTest calculate = new ForTest();
        int n = calculate.calA(2, 2);
 
        assertEquals(4, n);
        
    }

}
