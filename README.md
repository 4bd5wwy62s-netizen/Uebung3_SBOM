

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

Die `.gitignore` listet Dateien und Ordner, die Git **nicht** versionieren soll. Das sind vor allem erzeugte Dateien (z. B. `target/` mit den kompilierten Klassen) und persönliche IDE-Einstellungen (`.idea/`). GitHub bietet beim Anlegen eines Repositorys fertige Vorlagen pro Sprache bzw. Werkzeug an. Für dieses Projekt passt die Vorlage **Maven**.

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

| Option | Was passiert | Verlauf auf main | Im Projekt benutzt |
|---|---|---|---|
| **Create merge commit** | Alle Commits des Branches bleiben erhalten, dazu ein zusätzlicher Merge-Commit | Verzweigt, Branch bleibt sichtbar | PR #2, #9, #10 |
| **Squash and merge** | Alle Commits des Branches werden zu **einem** Commit zusammengefasst | Linear, 1 Commit pro PR | PR #5 (2 Commits → 1) |
| **Rebase and merge** | Die Commits werden einzeln **hinter** main gehängt (neu geschrieben, neue IDs), ohne Merge-Commit | Linear, alle Einzel-Commits | PR #6 (2 Commits) |


- *Merge commit* zeigt am ehrlichsten, wann was zusammengeführt wurde. Der Verlauf wird bei vielen Branches aber unübersichtlich.
- *Squash* hält main sauber („ein PR = ein Commit"). Die Zwischenschritte gehen auf main verloren, bleiben aber im PR sichtbar.
- *Rebase* ergibt einen geraden Verlauf mit allen Einzel-Commits. Die Commit-IDs ändern sich (bei PR #6 z. B. `1c7ed00` → `7f21c26`).
- Mit `git log --oneline --graph` sieht man die Unterschiede direkt.

## Sondersituation 2: Direktes Pushen auf main verbieten

Unter **Settings → Branches → Add branch protection rule** (bzw. *Rules → Rulesets*) für `main` die Option **Require a pull request before merging** aktivieren. Mit **Do not allow bypassing the above settings** gilt die Regel auch für Administratoren, also auch für mich als Besitzer.

**Test:** Ein direkter Push auf main wurde abgelehnt:
```
remote: error: GH006: Protected branch update failed for refs/heads/main.
remote: - Changes must be made through a pull request.
 ! [remote rejected] main -> main (protected branch hook declined)
```
Hinweis: Bei kostenlosen Konten ist Branch-Schutz nur in **öffentlichen** Repositorys verfügbar.

## Sondersituation 3 (optional): CODEOWNERS

**Kurzantwort zum Vortragen:** Die Datei `.github/CODEOWNERS` legt fest, wer für welche Dateien verantwortlich ist. Mit `* @4bd5wwy62s-netizen` bin ich Codeowner für alles. GitHub fordert mich bei jedem Pull Request automatisch als Reviewer an. Mit der Option **Require review from Code Owners** darf ein PR erst nach meiner Freigabe gemergt werden.

