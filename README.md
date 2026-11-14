**Übung 4 (Git und GitHub)** ist [weiter unten](#übung-4-git-und-github) dokumentiert.

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

---

# Übung 4: Git und GitHub

Repository: <https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM>
Lokaler Ordner: `~/IdeaProjects/GitHub/Uebung3_SBOM`. Git-Client: Git (Apple Command Line Tools) und die GitHub-Integration von IntelliJ.

## Ablauf im Überblick

| Teilaufgabe | Was gemacht wurde | Nachweis |
|---|---|---|
| 1 | Skills-Kurs „Introduction to GitHub" | eigener Kurs-Durchlauf |
| 2 | Repository auf GitHub angelegt, `.gitignore`-Vorlage **Maven** gewählt | [Initial commit](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/commits/main) |
| 3 | Git installiert, Repository geklont | – |
| 4 | Projekt aus Übung 3 in den Klon verschoben, Commit und Push | [Dateien auf GitHub](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM) |
| 5–6 | Issue #1, daraus Branch `1-main-ausgabe-erweitern`, **3 Commits**, PR #2 | [Issue #1](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/issues/1), [PR #2](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/2) |
| 7 | Zwei Branches **ohne Konflikt** (verschiedene Dateien): `CHANGELOG.md` und `PAKETE.md` | [PR #5](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/5), [PR #6](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/6) |
| 8 | Zwei Branches **mit Konflikt** (gleiche Zeile in `Main.java`), Konflikt gelöst | [PR #9](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/9), [PR #10](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/10) |
| S2 | main geschützt, direkter Push getestet und abgelehnt | Einstellungen → Branches |
| S3 | `CODEOWNERS` angelegt | [PR #12](https://github.com/4bd5wwy62s-netizen/Uebung3_SBOM/pull/12) |

## Die Datei `.gitignore`

**Kurzantwort zum Vortragen:** Die `.gitignore` listet Dateien und Ordner, die Git **nicht** versionieren soll. Das sind vor allem erzeugte Dateien (z. B. `target/` mit den kompilierten Klassen) und persönliche IDE-Einstellungen (`.idea/`). GitHub bietet beim Anlegen eines Repositorys fertige Vorlagen pro Sprache bzw. Werkzeug an. Für dieses Projekt passt die Vorlage **Maven**.

**Mehr Informationen für Rückfragen**
- Warum? Erzeugte Dateien lassen sich jederzeit neu bauen, blähen das Repository auf und führen zu unnötigen Konflikten. Persönliche Einstellungen und Pfade gehören nicht in ein gemeinsames Repository.
- Die Maven-Vorlage ignoriert `target/` und Hilfsdateien von Maven-Releases. Ich habe `.idea/`, `*.iml` und `.DS_Store` ergänzt (IntelliJ und macOS).
- Muster: `target/` = ganzer Ordner, `*.iml` = alle Dateien mit dieser Endung, `#` = Kommentar.

## Teilaufgabe 8: Merge-Konflikt

- Branch `7-titel-uebung4` änderte den Titel in `Main.java` zu „uebung 3 und 4: …".
- Branch `8-titel-grossbuchstaben` änderte **dieselbe Zeile** zu `StringUtils.upperCase(…)`.
- PR #9 wurde zuerst gemergt. Danach meldete GitHub für PR #10: *This branch has conflicts that must be resolved*.
- Lösung lokal: `git merge origin/main` im Branch. Git markierte die Stelle:
  ```
  <<<<<<< HEAD
          String titel = StringUtils.upperCase("Uebung 3: Paketmanager und SBOM");
  =======
          String titel = StringUtils.capitalize("uebung 3 und 4: paketmanager, sbom und git");
  >>>>>>> origin/main
  ```
- Ich habe beide Änderungen kombiniert (neuer Text **und** Großbuchstaben), die Markierungen entfernt, committet und gepusht. Danach war PR #10 mergebar.
- Hinweis: Konflikte lassen sich auch direkt auf GitHub („Resolve conflicts") oder in IntelliJ (**Git → Resolve Conflicts**, Drei-Spalten-Ansicht) lösen.

## Sondersituation 1: Die drei Merge-Optionen

**Kurzantwort zum Vortragen**

| Option | Was passiert | Verlauf auf main | Im Projekt benutzt |
|---|---|---|---|
| **Create merge commit** | Alle Commits des Branches bleiben erhalten, dazu ein zusätzlicher Merge-Commit | Verzweigt, Branch bleibt sichtbar | PR #2, #9, #10 |
| **Squash and merge** | Alle Commits des Branches werden zu **einem** Commit zusammengefasst | Linear, 1 Commit pro PR | PR #5 (2 Commits → 1) |
| **Rebase and merge** | Die Commits werden einzeln **hinter** main gehängt (neu geschrieben, neue IDs), ohne Merge-Commit | Linear, alle Einzel-Commits | PR #6 (2 Commits) |

**Mehr Informationen für Rückfragen**
- *Merge commit* zeigt am ehrlichsten, wann was zusammengeführt wurde. Der Verlauf wird bei vielen Branches aber unübersichtlich.
- *Squash* hält main sauber („ein PR = ein Commit"). Die Zwischenschritte gehen auf main verloren, bleiben aber im PR sichtbar.
- *Rebase* ergibt einen geraden Verlauf mit allen Einzel-Commits. Die Commit-IDs ändern sich (bei PR #6 z. B. `1c7ed00` → `7f21c26`).
- Mit `git log --oneline --graph` sieht man die Unterschiede direkt.

## Sondersituation 2: Direktes Pushen auf main verbieten

**Kurzantwort zum Vortragen:** Unter **Settings → Branches → Add branch protection rule** (bzw. *Rules → Rulesets*) für `main` die Option **Require a pull request before merging** aktivieren. Mit **Do not allow bypassing the above settings** gilt die Regel auch für Administratoren, also auch für mich als Besitzer.

**Test:** Ein direkter Push auf main wurde abgelehnt:
```
remote: error: GH006: Protected branch update failed for refs/heads/main.
remote: - Changes must be made through a pull request.
 ! [remote rejected] main -> main (protected branch hook declined)
```
Hinweis: Bei kostenlosen Konten ist Branch-Schutz nur in **öffentlichen** Repositorys verfügbar.

## Sondersituation 3 (optional): CODEOWNERS

**Kurzantwort zum Vortragen:** Die Datei `.github/CODEOWNERS` legt fest, wer für welche Dateien verantwortlich ist. Mit `* @4bd5wwy62s-netizen` bin ich Codeowner für alles. GitHub fordert mich bei jedem Pull Request automatisch als Reviewer an. Mit der Option **Require review from Code Owners** darf ein PR erst nach meiner Freigabe gemergt werden.

**Noch zu tun (mit Kommilitone)**
1. **Settings → Collaborators → Add people** → GitHub-Namen des Kommilitonen einladen.
2. **Settings → Branches → Regel für main bearbeiten**: *Require approvals* auf **1** setzen und **Require review from Code Owners** anhaken → **Save changes**.
3. Kommilitone: Branch erstellen, Änderung committen, Pull Request stellen.
4. Ich: Im PR **Files changed → Review changes → Approve → Submit review**, dann **Merge**.

Wichtig: Schritt 2 erst einschalten, wenn der Kommilitone bereit ist. Eigene PRs kann man sich nicht selbst freigeben, sie wären dann blockiert.

## Screenshots für die Abgabe
- [ ] Repository-Startseite mit allen Dateien (Teilaufgabe 4)
- [ ] Issue #1 mit verknüpftem Branch (rechte Spalte *Development*) (Teilaufgabe 5)
- [ ] PR #2 mit den 3 Commits (Reiter *Commits*) (Teilaufgabe 6)
- [ ] PR #10: Konfliktmeldung bzw. gelöster Konflikt (Teilaufgabe 8)
- [ ] *Insights → Network*: Verlauf mit allen Branches
- [ ] *Settings → Branches*: Schutzregel für main (Sondersituation 2)
