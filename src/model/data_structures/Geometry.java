package model.data_structures;

public class Geometry {
	private String type;
	private double[][] coordinates;
	
	public Geometry(String pType, short pTama�o)
	{
		type = pType;
		coordinates = new double[2][pTama�o/2];
	}
	
	public void setCoordinate(short i, short j,double pCoordinate)
	{
		coordinates[i][j] = pCoordinate;
	}
	
}
