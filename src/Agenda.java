import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class Agenda extends TreeMap<String,String>{
	static String token=null;
	

	public static void main(String[] args) throws IOException {
		Map<String, String> agenda = new HashMap<>();
	
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean fin = false;
		do {
			System.out.print("> ");
			Scanner s = new Scanner(in.readLine());
			int estado = 0;
			
			String nombre = null;
			while (estado != 5) {
				switch (estado) {
				case 0:
					try {
						
						token = s.skip(
								"fin|buscar|borrar|guardar|cargar|[a-zA-ZáéíóúÁÉÍÓÚ]+\\s+([a-zA-ZáéíóúÁÉÍÓÚ]+\\s+)*[a-zA-ZáéíóúÁÉÍÓÚ]+|[a-zA-ZáéíóúÁÉÍÓÚ]+")
								.match().group();
						if (token.equals("fin")) {
							fin=true;
							estado = 5;
						}
						else if (token.equals("buscar"))
							estado = 2;
						else if (token.equals("borrar"))
							estado = 6;
						else if (token.equals("guardar"))
							estado = 8;
						else if (token.equals("cargar"))
							estado = 9;
						else {
							nombre = token;
							estado = 1;
						}
					} catch (NoSuchElementException e) {
						System.out
								.println("Se esperaba 'buscar' , 'fin' , 'borrar' , 'cargar' , 'guardar' o un nombre");
						estado = 5;
					}
					break;
				// Se introduce un nombre
				case 1:
					try {
						s.skip("-");
						estado = 3;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba '-'");
						estado = 5;
					}
					break;
				// se introduce 'buscar'
				case 2:
					try {
						s.skip(":");
						estado = 4;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'");
						estado = 5;
					}
					break;
				// se introduce 'borrar'
				case 6:
					try {
						s.skip(":");
						estado = 7;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'");
						estado = 5;
					}
					break;
				// se introduce 'guardar'
				case 8:
					try {
						s.skip(":");
						estado = 10;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'");
						estado = 5;
					}
					break;
				// se introduce 'cargar'
				case 9:
					try {
						s.skip(":");
						estado = 11;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba ':'");
						estado = 5;
					}
					break;

				// se valida la sintaxis 'nombre-tlf'
				case 3:
					try {
						token = s.skip("\\d{9}").match().group();
						agenda.put(nombre, token);
						estado = 5;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un teléfono");
						estado = 5;
					}
					break;
				// se valida la sintaxis 'buscar:nombre'
				case 4:
					try {
						token = s.skip(
								"[a-zA-ZáéíóúÁÉÍÓÚ]+\\s+([a-zA-ZáéíóúÁÉÍÓÚ]+\\s+)*[a-zA-ZáéíóúÁÉÍÓÚ]+|[a-zA-ZáéíóúÁÉÍÓÚ]+")
								.match().group();
						String telefono = agenda.get(token);
						if (telefono != null)
							System.out.println(token + " -> " + telefono);
						else
							System.out.println(token + " no se encuentra en la agenda");
						estado = 5;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre");
						estado = 5;
					}
					break;
				// se valida la sintaxis de 'borrar:nombre'
				case 7:
					try {
						token = s.skip(
								"[a-zA-ZáéíóúÁÉÍÓÚ]+\\s+([a-zA-ZáéíóúÁÉÍÓÚ]+\\s+)*[a-zA-ZáéíóúÁÉÍÓÚ]+|[a-zA-ZáéíóúÁÉÍÓÚ]+")
								.match().group();
						String telefono = agenda.get(token);
						if (telefono != null) {
							System.out.println("Contacto eliminado: " + token + " -> " + telefono);
							agenda.remove(token);
						} else
							System.out.println(token + " no se encuentra en la agenda");
						estado = 5;
					} catch (NoSuchElementException e) {
						System.out.println("Se esperaba un nombre");
						estado = 5;
					}
					break;
				// se valida la sintaxis de 'guardar:ruta'
					case 10:
								
						break;

				// se valida la sintaxis de 'cargar:ruta'
				case 11:
					File fichero = new File("C:\\Users\\aula9\\Desktop\\prueba.txt");
					BufferedReader s2 = new BufferedReader(new FileReader(fichero));

					try {
						// Leemos el contenido del fichero
//						System.out.println("... Leemos el contenido del fichero ...");
						

						// Leemos linea a linea el fichero
						while (s2.readLine() != null) {
							String linea = s2.readLine(); 	// Guardamos la linea en un String
							
								String[] parts = linea.split("-");
								String part1 = parts[0]; //nombre
								String part2 = parts[1]; // telefono
						
								agenda.put(part1, part2);
								
							
							// Imprimimos la linea
						}

					} catch (Exception ex) {
						System.out.println("Mensaje: " + ex.getMessage());
					} finally {
						// Cerramos el fichero tanto si la lectura ha sido correcta o no
						try {
							if (s2 != null)
								s2.close();
						} catch (Exception ex2) {
							System.out.println("Mensaje 2: " + ex2.getMessage());
						}
					}
					estado=5;
					break;
				}
			}
		} while (!fin);
	}
}


	