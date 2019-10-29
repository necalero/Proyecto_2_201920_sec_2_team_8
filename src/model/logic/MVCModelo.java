package model.logic;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;



import model.data_structures.Geometry;
import model.data_structures.HashTableLinearProbing;
import model.data_structures.Nodo;
import model.data_structures.NodoRedVial;
import model.data_structures.Properties;
import model.data_structures.UBERTrip;
import model.data_structures.ZonaUber;
import model.data_structures.BSTRojoNegro.ArbolesRYN;



/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private HashTableLinearProbing datosHora;
	private HashTableLinearProbing datosDia;
	private HashTableLinearProbing datosMes;
	private HashTableLinearProbing datosRedVial;
	private ArbolesRYN zonasUber;



	//-----------------------------------------------------------------

	/**
	 * Constructor del modelo del mundo
	 */
	public MVCModelo()
	{
	}



	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 * @throws IOException 
	 * @throws NoExisteException 
	 */
	public int[] cargar() throws IOException, NoExisteException
	{	

		
		try {
			/**
			 * Se crea un arreglo de enteros para reportar 
			 * 1. El número de viajes que se cargaron de cada archivo CSV
			 * 2. El número de zonas que se cargaron del archivo JSON
			 * 3. El número de nodos (esquinas) de la malla vial del archivo TXT
			 */
			int[] resultados = new int[3];

					//Se utilizan metodos auxiliares para cargar los archivos de cada tipo
					int cantidadViajesCargados = 0; 
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-1-WeeklyAggregate.csv",1,"weekly");
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-2-WeeklyAggregate.csv",2,"weekly");
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-1-All-MonthlyAggregate.csv",1,"monthly");
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-2-All-MonthlyAggregate.csv",2,"monthly");
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-1-All-HourlyAggregate.csv",1,"hourly");
					cantidadViajesCargados += cargarCSV("./data/bogota-cadastral-2018-2-All-HourlyAggregate.csv",2,"hourly");


					int cantidadZonasCargadas = cargarJSON("./data/bogota-cadastral.json");
					int cantidadNodosCargados = cargarTXT("./data/Nodes_of_red_vial-wgs84_shp.txt");

					resultados[0] = cantidadViajesCargados;
					resultados[1] = cantidadZonasCargadas;
					resultados[2] = cantidadNodosCargados;

					return resultados;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("No sirvió :(");
			e.printStackTrace();
			
		}
		return null;


	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int cargarCSV(String ruta, int trimestre, String tipoDate) throws IOException
	{
		int cantidadViajesCargados = 0;
		CSVReader reader;
		File arc = new File(ruta);
		FileReader fr = new FileReader(arc);
		reader = new CSVReader(fr);
		reader.readNext();
		String [] nextLine=reader.readNext();

		while (nextLine != null) 
		{
			String key = trimestre+"-"+nextLine[0]+"-"+nextLine[1];
			if(tipoDate.equals("weekly"))
			{
				datosDia.put(key, (Comparable) new Nodo(new UBERTrip(nextLine[0],nextLine[1],nextLine[2],nextLine[3],nextLine[4],nextLine[5], nextLine[6], tipoDate)));
			}
			else if(tipoDate.equals("hourly"))
			{
				datosHora.put(key, (Comparable) new Nodo(new UBERTrip(nextLine[0],nextLine[1],nextLine[2],nextLine[3],nextLine[4],nextLine[5], nextLine[6], tipoDate)));
			}
			else if(tipoDate.equals("monthly"))
			{
				datosDia.put(key, (Comparable) new Nodo(new UBERTrip(nextLine[0],nextLine[1],nextLine[2],nextLine[3],nextLine[4],nextLine[5], nextLine[6], tipoDate)));
			}

			nextLine = reader.readNext();
			cantidadViajesCargados++;
		}
		reader.close();


		return cantidadViajesCargados;
	}

	@SuppressWarnings("unchecked")
	public int cargarJSON(String ruta) throws FileNotFoundException
	{
		JsonReader reader;
		try
		{
			Gson gson = new Gson();
			reader = new JsonReader(new FileReader(ruta));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonObject jobj = elem.getAsJsonObject();
			JsonArray todas = jobj.getAsJsonArray("features");
			zonasUber = new ArbolesRYN<>();
			//zonasUber = new ZonaUber[todas.size()];
			for(int i = 0; i< todas.size(); i++)
			{
				JsonObject actual = todas.get(i).getAsJsonObject();
				String typeFeature = actual.getAsJsonObject("type").getAsString();
				JsonObject geometry = actual.getAsJsonObject("geometry");
				String typeGeom = geometry.getAsJsonObject("type").getAsString();
				JsonArray coordinatesJSON = geometry.getAsJsonArray("coordinates");
				String rawNumbers = coordinatesJSON.getAsString().replace("[", "").replace("]", "");
				String[] coordinates = rawNumbers.split(",");
				Geometry nuevaGeometry = new Geometry(typeGeom, coordinates);
				JsonObject propertiesJSON = actual.getAsJsonObject("properties");
				Properties properties = gson.fromJson(propertiesJSON, Properties.class);
				String key = actual.getAsJsonObject("properties").getAsJsonObject("DISPLAY_NAME").getAsString();
				zonasUber.put(key, new ZonaUber(typeFeature, nuevaGeometry, properties));
				

			}
			return zonasUber.size();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int cargarTXT(String ruta) throws IOException 
	{
		FileReader fr = new FileReader(ruta);
		BufferedReader br = new BufferedReader(fr);
		String linea= br.readLine();
		int contador = 0;
		while(linea!=null)
		{
			String[] partes = linea.split(",");
			NodoRedVial nuevo = new NodoRedVial(partes[0], partes[1], partes[2]);
			Nodo nuevoNodo = new Nodo<NodoRedVial>(nuevo);
			String Key = partes[0];
			datosRedVial.put(Key, (Comparable) nuevoNodo);
			contador++;
			linea = br.readLine();
		}
		br.close();
		return contador;
	}
	
	/**
	 * Obtener las N letras más frecuentes por las que comienza el nombre de una zona
	 */
	public String[] reqFunc1A(short N)
	{
		return null;
	}
	/**
	 * Buscar los nodos que delimitan las zonas por Localización Geográfica (latitud, longitud)
	 */
	public String[] reqFunc2A(double pLatitud, double pLongitud)
	{
		return null;
	}
	/**
	 * Buscar los tiempos promedio de viaje que están en un rango y que son del primer trimestre del 2018.
	 */
	public String[] reqFunc3A(double pMin, double pMax, short N)
	{
		return null;
	}
	/**
	 * Buscar los N zonas que están más al norte.
	 */
	public String[] reqFunc1B(short N)
	{
		return null;
	}
	/**
	 * Buscar nodos de la malla vial por Localización Geográfica (latitud, longitud).
	 */
	public String[] reqFunc2B(double pLatitud, double pLongitud)
	{
		return null;
	}
	/**
	 * Buscar los tiempos de espera que tienen una desviación estándar en un rango dado y que son del primer trimestre del 2018
	 */
	public String[] reqFunc3B(double pLimiteBajoDesviacionEstandar, double pLimiteAltoDesviacionEstandar)
	{
		return null;
	}
	/**
	 * Retornar todos los tiempos de viaje promedio que salen de una zona dada y a una hora dada
	 */
	public String[] reqFunc1C(int pIdZonaSalida, double pHora)
	{
		return null;
	}
	/**
	 * Retornar todos los tiempos de viaje que llegan de una zona dada y en un rango de horas
	 */
	public String[] reqFunc2C(int idZonaLlegada, double pHoraMin, double pHoraMax)
	{
		return null;
	}
	/**
	 * Obtener las N zonas priorizadas por la mayor cantidad de nodos que definen su frontera.
	 */
	public String[] reqFunc3C(int N)
	{
		return null;
	}
	/**
	 * Gráfica ASCII - Porcentaje de datos faltantes para el primer semestre 2018.
	 */
	public String[][] reqFunc4C(int N)
	{
		return null;
	}
	



}