package model.data_structures;

public class ZonasUber {
	private String type;
	private ZonaUber[] features;
	
	public ZonasUber(String pType, short pTamaño)
	{
		type = pType;
		features = new ZonaUber[pTamaño];
	}

	public void añadirZona(short i, ZonaUber pZona)
	{
		features[i] = pZona;
	}
	
	public ZonaUber darZona(short i)
	{
		return features[i];
	}
	
}
