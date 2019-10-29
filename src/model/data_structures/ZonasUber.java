package model.data_structures;

public class ZonasUber {
	private String type;
	private ZonaUber[] features;
	
	public ZonasUber(String pType, short pTama�o)
	{
		type = pType;
		features = new ZonaUber[pTama�o];
	}

	public void a�adirZona(short i, ZonaUber pZona)
	{
		features[i] = pZona;
	}
	
	public ZonaUber darZona(short i)
	{
		return features[i];
	}
	
}
