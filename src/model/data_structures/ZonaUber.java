package model.data_structures;

public class ZonaUber 
{
	/**---------------------------------------
	 * Atributos
	 * ---------------------------------------
	 */
	
	private Geometry geometry;
	private Properties properties;
	
	
	/**---------------------------------------
	 * Constructor
	 * ---------------------------------------
	 */
	public ZonaUber(Geometry geometry, Properties properties)
	{
		
		this.setGeometry(geometry);
		this.setProperties(properties);
		
	}


	public Properties getProperties() {
		return properties;
	}


	public void setProperties(Properties properties) {
		this.properties = properties;
	}


	public Geometry getGeometry() {
		return geometry;
	}


	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	
	
	
}
