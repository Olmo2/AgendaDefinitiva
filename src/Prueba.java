import java.util.HashMap;
import java.util.Map;

public class Prueba {

	public static void main(String[] args) {
		Map<String, String> agenda = new HashMap<>();
		agenda.put("olmo","123456789");
		agenda.put("ana","569832147");
		agenda.put("adrian","459876231");
		
		agenda.forEach((k,v) -> System.out.println("Key: " + k + ": Value: " + v));
		

	}

}
