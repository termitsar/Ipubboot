package com.example.config;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.example.service.ForTest;


@RunWith(Parameterized.class)
public class TestRunWith {


	    public int firstParameter;
	      public int secondParameter;
	      public int expectedResult;

	  public TestRunWith(int firstParameter, int secondParameter, int expectedResult) {
	           this.firstParameter = firstParameter;
	           this.secondParameter = secondParameter;
	           this.expectedResult = expectedResult;
	  }

	  @Test
	    public void checkCalculator() {
		       ForTest calculator = new ForTest();
	           int result = calculator.calA(firstParameter, secondParameter);
	           assertTrue("���������(" + result + ") �� ����� " + expectedResult, result == expectedResult);
	  }

	  @Parameterized.Parameters
	     public static Collection<Object[]> getTestData() {
	           return Arrays.asList(new Object[][]{
	         {2, 2, 4},
	         {2, 0, 0},
	         {-1, 2, -2},
	         {0, 2, 0}
	     });
	  }

	}
	
	
