# Übung 3: Paketmanager, Lizenzen, transitive Abhängigkeiten und SBOM

Beispielprojekt in **Java** mit **IntelliJ IDEA** und dem Paketmanager (Werkzeug, das Bibliotheken automatisch herunterlädt und einbindet) **Maven**.

> **Stand der Dokumentation:** Projekt gebaut, Programm getestet, Abhängigkeitsbaum, Lizenzliste (`THIRD-PARTY.txt`) und SBOM (`bom.json`) erzeugt und unten eingetragen.
> Offen: `bom.json` in den rancher.io-Viewer laden und Screenshot machen.

## Inhalt des Projekts

| Datei | Zweck |
|---|---|
| `pom.xml` | Maven-Konfiguration (POM = Project Object Model): Pakete und CycloneDX-Plugin |
| `src/main/java/de/uebung/Main.java` | Kleines Programm, das alle 3 Pakete benutzt |
| `README.md` | Diese Dokumentation |
| `bom.json` | SBOM im CycloneDX-Format (Kopie aus `target/`) |
| `THIRD-PARTY.txt` | Lizenzliste aller 11 Pakete (Kopie aus `target/`) |

## Schnellstart: fertige Startknöpfe

Oben rechts in IntelliJ gibt es ein Auswahlfeld neben dem grünen ▶. Dort sind vier Einträge vorbereitet:

| Eintrag | Was passiert | Ergebnis |
|---|---|---|
| **A1 Main starten** | Programm mit allen 3 Paketen ausführen | JSON-Ausgabe in der Konsole |
| **A2 Lizenzen** | Lizenzliste aller Pakete erzeugen | `target/generated-sources/license/THIRD-PARTY.txt` |
| **A3 Abhaengigkeiten** | `mvn dependency:tree` | Abhängigkeitsbaum in der Konsole |
| **A4 SBOM erzeugen** | `mvn cyclonedx:makeBom` | `target/bom.json` |

## Einrichtung in IntelliJ

1. **File | Open** und den Ordner `Uebung3_SBOM` auswählen. Bei der Frage „Trust Project" auf **Trust Project** klicken.
2. IntelliJ erkennt die `pom.xml` automatisch. Falls nicht: Rechtsklick auf `pom.xml` → **Add as Maven Project**.
3. In der Maven-Ansicht (rechts am Rand, Symbol „m") auf **Reload All Maven Projects** klicken. Maven lädt jetzt die Pakete aus **Maven Central** (dem öffentlichen Paketlager für Java).
4. Als JDK (Java Development Kit) ist Version 11 oder neuer nötig: **File | Project Structure | Project | SDK**.
5. `Main.java` öffnen und über das grüne Dreieck starten. Erwartet wird eine kleine JSON-Ausgabe in der Konsole.

---

## Aufgabe 1: Beliebte Pakete recherchieren und installieren

**Kurzantwort zum Vortragen**
Ich habe drei verbreitete Java-Pakete gewählt und über Maven installiert: **Guava** (Hilfsklassen für Collections), **Apache Commons Lang3** (Hilfen für Strings und Objekte) und **Jackson Databind** (JSON verarbeiten). Installieren heißt bei Maven: Das Paket wird als `<dependency>` in die `pom.xml` eingetragen, und Maven lädt es samt Abhängigkeiten herunter.

**Mehr Informationen für Rückfragen**
- Die Pakete werden über ihre Koordinaten identifiziert: `groupId` (Herausgeber), `artifactId` (Paketname), `version`.
- Recherche der Beliebtheit: Auf [mvnrepository.com](https://mvnrepository.com) steht bei jedem Paket die Zahl der Nutzer („Used By") und die Lizenz.
- Alle Pakete stammen aus Maven Central. Das lokale Lager auf dem Rechner ist `~/.m2/repository`.
- Alternative Paketmanager: Gradle für Java, npm für JavaScript, pip für Python.

| Paket | Koordinaten | Version |
|---|---|---|
| Guava | `com.google.guava:guava` | 33.4.0-jre |
| Commons Lang3 | `org.apache.commons:commons-lang3` | 3.17.0 |
| Jackson Databind | `com.fasterxml.jackson.core:jackson-databind` | 2.18.2 |

Neuere Versionen findest du auf mvnrepository.com. Wenn du sie änderst, danach wieder **Reload All Maven Projects** klicken.

---

## Aufgabe 2: Lizenzen der installierten Pakete einsehen

**Kurzantwort zum Vortragen**
Die Lizenz steht in der `pom.xml` des jeweiligen Pakets im Abschnitt `<licenses>`. Ich sehe sie auf mvnrepository.com, in der erzeugten SBOM (Software Bill of Materials, Stückliste aller Bibliotheken) und mit einem Maven-Plugin als Textliste. Die drei gewählten Pakete stehen nach Angabe ihrer Projekte unter der **Apache License 2.0** (erlaubt kommerzielle Nutzung, verlangt Lizenzhinweis).

**So prüfst du es selbst**
- **Maven-Plugin (Textliste):** Im Terminal von IntelliJ im Projektordner ausführen:
  ```
  mvn org.codehaus.mojo:license-maven-plugin:2.4.0:add-third-party
  ```
  Ergebnis: `target/generated-sources/license/THIRD-PARTY.txt`.
- **SBOM:** Die `bom.json` aus Aufgabe 4 enthält pro Bibliothek das Feld `licenses`.
- **IntelliJ direkt:** Schau im Maven-Fenster unter **Dependencies** und im **Dependency Analyzer** (Rechtsklick auf `pom.xml` → *Maven* → *Show Dependencies* oder Reiter *Dependency Analyzer* in der `pom.xml`). Ob IntelliJ dort die Lizenz anzeigt, hängt von Version und Plugins ab. Trage das Ergebnis unten ein.

**Ergebnis (nach Ausführung eintragen)**

| Paket | Lizenz laut Plugin/SBOM |
|---|---|
| guava | Apache License 2.0 |
| commons-lang3 | Apache License 2.0 |
| jackson-databind | Apache License 2.0 |

Die vollständige Liste liegt in `THIRD-PARTY.txt` im Projektordner.

**Mehr Informationen für Rückfragen**
- Warum Lizenzen prüfen? Manche Lizenzen (z. B. GPL) verlangen, eigenen Quellcode offenzulegen. Das ist für Firmenprojekte wichtig (Compliance, also Regelkonformität).
- Die Angabe im Paket stammt vom Herausgeber und ist nicht automatisch geprüft.

---

## Aufgabe 3: Transitive Abhängigkeiten einsehen

**Kurzantwort zum Vortragen**
Transitive Abhängigkeiten sind Bibliotheken, die meine Pakete selbst benötigen und die Maven automatisch mitlädt. In IntelliJ sehe ich sie in der Maven-Ansicht unter **Dependencies**, im **Dependency Analyzer** und als Diagramm. Im Terminal zeigt `mvn dependency:tree` den ganzen Baum. Die Lizenzen der indirekten Pakete stehen in der SBOM und in `THIRD-PARTY.txt`.

**So prüfst du es selbst**
- IntelliJ: Rechtsklick auf `pom.xml` → **Maven | Show Dependencies** (Diagramm), oder in der `pom.xml` unten den Reiter **Dependency Analyzer**.
- Terminal:
  ```
  mvn dependency:tree
  ```
- Lizenzen aller Pakete (auch indirekte): `THIRD-PARTY.txt` aus Aufgabe 2 oder `bom.json` aus Aufgabe 4.

**Ergebnis (Ausgabe von `mvn dependency:tree` hier einfügen)**
```
de.uebung:uebung3-sbom:jar:1.0-SNAPSHOT
+- com.google.guava:guava:jar:33.4.0-jre:compile
|  +- com.google.guava:failureaccess:jar:1.0.2:compile
|  +- com.google.guava:listenablefuture:jar:9999.0-empty-to-avoid-conflict-with-guava:compile
|  +- com.google.code.findbugs:jsr305:jar:3.0.2:compile
|  +- org.checkerframework:checker-qual:jar:3.43.0:compile
|  +- com.google.errorprone:error_prone_annotations:jar:2.36.0:compile
|  \- com.google.j2objc:j2objc-annotations:jar:3.0.0:compile
+- org.apache.commons:commons-lang3:jar:3.17.0:compile
\- com.fasterxml.jackson.core:jackson-databind:jar:2.18.2:compile
   +- com.fasterxml.jackson.core:jackson-annotations:jar:2.18.2:compile
   \- com.fasterxml.jackson.core:jackson-core:jar:2.18.2:compile
```

3 direkte und 8 transitive Abhängigkeiten, zusammen 11 Pakete.

| Transitive Abhängigkeit | Mitgebracht von | Lizenz |
|---|---|---|
| failureaccess 1.0.2 | guava | Apache 2.0 |
| listenablefuture 9999.0-empty… | guava | Apache 2.0 |
| jsr305 3.0.2 | guava | Apache 2.0 |
| **checker-qual 3.43.0** | guava | **MIT** |
| error_prone_annotations 2.36.0 | guava | Apache 2.0 |
| j2objc-annotations 3.0.0 | guava | Apache 2.0 |
| jackson-annotations 2.18.2 | jackson-databind | Apache 2.0 |
| jackson-core 2.18.2 | jackson-databind | Apache 2.0 |

**Auffälligkeit für die Präsentation:** Alle direkten Pakete stehen unter Apache 2.0, aber die transitive Abhängigkeit `checker-qual` steht unter der **MIT-Lizenz**. Das sieht man nur, wenn man auch die indirekten Pakete prüft. MIT ist ebenfalls eine freizügige Lizenz und verlangt nur den Lizenzhinweis.

**Mehr Informationen für Rückfragen**
- Direkt = steht in meiner `pom.xml`. Transitiv = wird von einem direkten Paket mitgebracht.
- Beispiel zur Orientierung: Jackson Databind benötigt `jackson-core` und `jackson-annotations`. Guava bringt kleine Zusatzbibliotheken mit. Die genaue Liste hängt von der Version ab und muss mit `dependency:tree` geprüft werden.
- Warum wichtig? Auch indirekte Pakete können Sicherheitslücken oder andere Lizenzen enthalten.

---

## Aufgabe 4: SBOM mit CycloneDX erzeugen und anzeigen

**Kurzantwort zum Vortragen**
Eine SBOM ist eine Stückliste der Software: Sie listet alle verwendeten Bibliotheken mit Version und Lizenz. **CycloneDX** ist ein Standardformat dafür (von OWASP, dem Open Worldwide Application Security Project). Ich erzeuge sie mit dem CycloneDX-Maven-Plugin über `mvn cyclonedx:makeBom`. Das Ergebnis `target/bom.json` lade ich in den SBOM-Viewer von rancher.io.

**Schritte**
1. Im Terminal von IntelliJ im Projektordner:
   ```
   mvn cyclonedx:makeBom
   ```
   Alternativ in der Maven-Ansicht: **Plugins | cyclonedx | cyclonedx:makeBom** doppelklicken.
2. Ergebnis: `target/bom.json` und `target/bom.xml`. Das JSON-Format liegt damit schon vor, die Umwandlung im CycloneDX Web Tool ist normalerweise nicht nötig. Falls du nur eine XML-Datei hast, konvertiere sie dort zu JSON.
3. Öffne den SBOM-Viewer von rancher.io (Link im Aufgabenblatt) und lade `target/bom.json` hoch.
4. Mach einen Screenshot der Ansicht für die Abgabe.

**Mehr Informationen für Rückfragen**
- Formate: CycloneDX und SPDX sind die zwei gängigen SBOM-Standards.
- Nutzen: Bei einer neuen Sicherheitslücke (CVE, Common Vulnerabilities and Exposures) sieht man sofort, ob die betroffene Bibliothek im Produkt steckt.
- Wenn `mvn` im Terminal fehlt: IntelliJ bringt ein eigenes Maven mit, nutze dann die Maven-Ansicht. Oder installiere es mit `brew install maven`.

**Ergebnis (Screenshot oder Notiz)**
- [x] `bom.json` erzeugt (CycloneDX 1.6, 11 Komponenten). Eine Kopie liegt im Projektordner als `bom.json`.
- [ ] In den rancher.io-Viewer geladen und Screenshot gemacht
- Anzahl der Komponenten laut Viewer: (erwartet: 11)

---

## Quellen
- [Maven Central Repository Search / mvnrepository.com](https://mvnrepository.com)
- [CycloneDX Maven Plugin](https://github.com/CycloneDX/cyclonedx-maven-plugin)
- [CycloneDX Spezifikation](https://cyclonedx.org)
