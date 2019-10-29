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
	
	/**---------------------------------------
	 * Getters & setters
	 * ---------------------------------------
	 */

	public Properties darProperties() {
		return properties;
	}


	public void setProperties(Properties properties) {
		this.properties = properties;
	}


	public Geometry darGeometry() {
		return geometry;
	}


	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	
	
	
}
