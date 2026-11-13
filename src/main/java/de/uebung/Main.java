package de.uebung;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
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
        String titel = StringUtils.capitalize("uebung 3 und 4: paketmanager, sbom und git");

        // Jackson: Objekt als JSON ausgeben
        Map<String, Object> daten = new LinkedHashMap<>();
        daten.put("titel", titel);
        daten.put("anzahlPakete", pakete.size());
        daten.put("pakete", pakete);
        daten.put("javaVersion", System.getProperty("java.version"));
        // Guava: Liste zu einem Text verbinden
        daten.put("paketText", Joiner.on(", ").join(pakete));
        daten.put("zeitstempel", LocalDateTime.now().withNano(0).toString());

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(daten);
        System.out.println(json);
    }
}
