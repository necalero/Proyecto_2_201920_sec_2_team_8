package controller;

import java.util.Scanner;

import model.logic.MVCModelo;
import view.MVCView;

public class Controller {

	/* Instancia del Modelo*/
	private MVCModelo modelo;

	/* Instancia de la Vista*/
	private MVCView view;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new MVCView();
		modelo = new MVCModelo();
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option)
			{
			case 1:
				view.printMessage("Se estan cargando los viajes: ");
				modelo = new MVCModelo();
				
				
				try
				{
					modelo.cargar();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;

			case 2:
				
			case 3:
				
			case 4:
				
			case 5:
				
			case 6:
				
			case 7:
				
			case 8:
			
			case 9:
				
			case 10:
				
			case 11:
			
			default: 
				view.printMessage("--------- \n Opcion Invalida !! \n---------");
				break;
				
			}
		}
		lector.close();

	}	
}
