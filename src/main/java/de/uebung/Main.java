package de.uebung;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Kleines Beispiel, das alle drei eingebundenen Pakete benutzt.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Guava: unveraenderliche Liste
        ImmutableList<String> pakete = ImmutableList.of("guava", "commons-lang3", "jackson-databind");

        // Commons Lang3: String-Hilfen
        String titel = StringUtils.capitalize("uebung 3: paketmanager und sbom");

        // Jackson: Objekt als JSON ausgeben
        Map<String, Object> daten = new LinkedHashMap<>();
        daten.put("titel", titel);
        daten.put("anzahlPakete", pakete.size());
        daten.put("pakete", pakete);

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(daten);
        System.out.println(json);
    }
}
