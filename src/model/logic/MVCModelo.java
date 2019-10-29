package model.logic;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.omg.PortableInterceptor.AdapterStateHelper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;



import model.data_structures.DoublyLinkedList;
import model.data_structures.HashTableLinearProbing;
import model.data_structures.HashTableSeparateChaining;
import model.data_structures.Nodo;
import model.data_structures.NodoRedVial;
import model.data_structures.TravelTime;
import model.data_structures.UBERTrip;
import model.data_structures.ZonaUber;
import model.data_structures.ZonasUber;


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
	private ZonasUber datosZona;
	private HashTableLinearProbing datosRedVial;



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
	@SuppressWarnings("unchecked")
	public int[] cargar() throws IOException, NoExisteException
	{	


		/**
		 * Se crea un arreglo de enteros para reportar 
		 * 1. El n�mero de viajes que se cargaron de cada archivo CSV
		 * 2. El n�mero de zonas que se cargaron del archivo JSON
		 * 3. El n�mero de nodos (esquinas) de la malla vial del archivo TXT
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


	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int cargarCSV(String ruta, int trimestre, String tipoDate) throws IOException
	{
		int cantidadViajesCargados = 0;
		CSVReader reader;
		FileReader fr = new FileReader(ruta);
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

	public int cargarJSON(String ruta) throws FileNotFoundException
	{
		JsonReader reader;
		try
		{
			reader = new JsonReader(new FileReader(ruta));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonObject jobj = elem.getAsJsonObject();
			JsonArray todas = jobj.getAsJsonArray("features");
			
			
			
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int cargarTXT(String ruta) throws IOException 
	{
		FileReader fr = new FileReader(ruta);
		BufferedReader br = new BufferedReader(fr);
		String linea= br.readLine();
		while(linea!=nul)
		{
			String[] partes = linea.split(",");
			NodoRedVial nuevo = new NodoRedVial(partes[0], partes[1], partes[2]);
			Nodo nuevoNodo = new Nodo<NodoRedVial>(nuevo);
			String Key = partes[0];
			datosRedVial.put(Key, (Comparable) nuevoNodo);
			linea = br.readLine();
		}

		return 0;
	}




}