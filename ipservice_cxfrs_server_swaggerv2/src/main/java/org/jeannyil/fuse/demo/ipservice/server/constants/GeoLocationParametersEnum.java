package org.jeannyil.fuse.demo.ipservice.server.constants;

public enum GeoLocationParametersEnum {
	IP("ip"),
	OUTPUT("output");
	
	private String inputParameter;
	
	/**
	 * Constructor
	 * @param inputParameter
	 */
	private GeoLocationParametersEnum(String inputParameter) {
		this.setInputParameter(inputParameter);
	}

	/**
	 * @return the inputParameter
	 */
	public String getInputParameter() {
		return inputParameter;
	}

	/**
	 * @param inputParameter the inputParameter to set
	 */
	public void setInputParameter(String inputParameter) {
		this.inputParameter = inputParameter;
	}
	
	/**
	 * Override the toString() method
	 */
	@Override
	public String toString() {
		return getInputParameter();
	}

}
