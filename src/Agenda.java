import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class Agenda extends TreeMap<String,String>{
	static String token=null;
	static Writer out=null ;

	public static void main(String[] args) throws IOException {
		Map<String, String> agenda = new HashMap<>();
		agenda.put("jose", "123456789");
		agenda.put("maria", "000000009");
	
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
						
						if(agenda.containsKey(nombre)) {
						String oldTlf = agenda.get(nombre);
						
						if(token.equals(oldTlf)) {
							System.out.println("Eres muy tonto " + oldTlf + " y " + token + " Son iguales..."  );
						}
						else {
							System.out.println("El Número de " + nombre + " Ha sido actualizado de: " + oldTlf + " a: " + token);
						}
						agenda.replace(nombre, token);
						}else
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
						token = s.skip("([a-zA-Z:])*/([A-z0-9-_+]+/)*([A-z0-9]+.(txt))").match().group();
						try {
							 out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(token), "UTF-8"));
						
							// Escribimos linea a linea en el fichero
							
								agenda.forEach((k,v) -> {
									try {
										out.write(k +"-"+ v + System.lineSeparator());
									} catch (IOException e) {
										
										e.printStackTrace();
									}
								});
								estado=5;

						} catch (UnsupportedEncodingException | FileNotFoundException ex2) {
							System.out.println("Error: " + ex2.getMessage());
							estado=5;
						} finally {
							try {
								out.close();
							} catch (IOException ex3) {
								System.out.println("Mensaje error cierre fichero: " + ex3.getMessage());
							}
						}
					
								
					break;

				// se valida la sintaxis de 'cargar:ruta'
				case 11:
					//^([a-zA-Z:])*\\([A-z0-9-_+]+\\)*([A-z0-9]+.(txt))$
					try{
						token = s.skip("([a-zA-Z:])*/([A-z0-9-_+]+/)*([A-z0-9]+.(txt))").match().group();
						System.out.println(token);
						File fichero = new File(token);
					
					BufferedReader s2 = new BufferedReader(new FileReader(fichero));

					try {
						// Leemos el contenido del fichero
//						System.out.println("... Leemos el contenido del fichero ...");
						

						// Leemos linea a linea el fichero
						String linea ="";
						while ((linea = s2.readLine()) != null) {
							 	// Guardamos la linea en un String
							
								String[] parts = linea.split("-");
								String part1 = parts[0]; //nombre
								String part2 = parts[1]; // telefono
						
								agenda.put(part1, part2);
								System.out.println(agenda);
							
							// Imprimimos la linea
						}

					} catch (Exception ex) {
						System.out.println("Error: " + ex.getMessage());
					} finally {
						// Cerramos el fichero tanto si la lectura ha sido correcta o no
						try {
							if (s2 != null)
								s2.close();
						} catch (Exception ex2) {
							System.out.println("Error: " + ex2.getMessage());
							estado=5;
						}
					}
					}catch(FileNotFoundException |NoSuchElementException e){
						System.out.println("Error404: " + e.getMessage());
					}
					estado=5;
					break;
				}
			}
			
		} while (!fin);
}
}


	