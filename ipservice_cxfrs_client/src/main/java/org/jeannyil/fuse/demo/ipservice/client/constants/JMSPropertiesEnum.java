package org.jeannyil.fuse.demo.ipservice.client.constants;

public enum JMSPropertiesEnum {
    PROCESSEDFILE("ProcessedFile"),
    PROCESSEDPARAMETERS("ProcessedParameters");

    private String jmsProperty;

    /**
     * Constructor
     * @param jmsProperty
     */
    private JMSPropertiesEnum(String jmsProperty) {
        this.setJmsProperty(jmsProperty);
    }

    /**
     * @return the jmsProperty
     */
    public String getJmsProperty() {
        return jmsProperty;
    }

    /**
     * @param jmsProperty the JMS property to set
     */
    public void setJmsProperty(String jmsProperty) {
        this.jmsProperty = jmsProperty;
    }

    /**
     * Override the toString() method
     */
    @Override
    public String toString() {
        return getJmsProperty();
    }
}
