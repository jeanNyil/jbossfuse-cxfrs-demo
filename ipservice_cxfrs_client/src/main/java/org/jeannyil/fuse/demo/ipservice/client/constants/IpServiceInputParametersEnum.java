package org.jeannyil.fuse.demo.ipservice.client.constants;

public enum IpServiceInputParametersEnum {
	IP("ip_or_hostname"),
	OUTPUT("response_type");
	
	private String inputParameter;
	
	/**
	 * Constructor
	 * @param inputParameter
	 */
	private IpServiceInputParametersEnum(String inputParameter) {
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
