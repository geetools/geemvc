package com.cb.examples.jpajsp.geeticket.model;

public class TestBean {
    private String strVal = null;

    private Integer intVal = null;

    private Integer[] intValues = null;

    public TestBean(String strVal, Integer intVal) {
	this.strVal = strVal;
	this.intVal = intVal;
    }

    public TestBean(String strVal, Integer intVal, Integer... intValues) {
	this.strVal = strVal;
	this.intVal = intVal;
	this.intValues = intValues;
    }

    public String getStrVal() {
	return strVal;
    }

    public void setStrVal(String strVal) {
	this.strVal = strVal;
    }

    public Integer getIntVal() {
	return intVal;
    }

    public void setIntVal(Integer intVal) {
	this.intVal = intVal;
    }

    public Integer[] getIntValues() {
	return intValues;
    }

    public void setIntValues(Integer[] intValues) {
	this.intValues = intValues;
    }

    @Override
    public String toString() {
	return "TestBean [strVal=" + strVal + ", intVal=" + intVal + "]";
    }
}
