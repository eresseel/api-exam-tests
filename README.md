## Tesztesetek

| Tesztcsomag | Teszteset | Típus | Kérés | Elvárt eredmény | Osztály |
|---|---|---|---|---|---|
| 1 – Postok listázása | TC-1.1 | Pozitív | `GET /posts` | 200, nem üres body, lista, legalább 1 elem, minden elemben `id`, `title`, `body` | `PostsListTest` |
| 2 – Egy konkrét post | TC-2.1 | Pozitív | `GET /posts/1` | 200, `id` értéke 1 | `SinglePostTest` |
| 2 – Egy konkrét post | TC-2.2 | Negatív | `GET /posts/9999` | 404, üres body vagy `{}` | `SinglePostTest` |
| 3 – Új post | TC-3.1 | Pozitív + logolás | `POST /posts` (`title`, `body`, `userId`) | 201, a válasz tartalmaz `id` mezőt, request logolva, hibánál a response is | `CreatePostTest` |

## Automatizálási és CI elvárások

| Elvárás | Megvalósítás |
|---|---|
| Java, Maven, JUnit 5, RestAssured | `pom.xml` (Java 17 fordítási szint, JUnit 5.10.2, RestAssured 5.4.0, slf4j) |
| Futtatás IDE-ből | IntelliJ IDEA, Eclipse (Maven projektként importálva) |
| Futtatás parancssorból | `mvn clean test` |
| CI push hatására | `.github/workflows/ci.yml` (`push` trigger) |
| Surefire riport artifactként | `actions/upload-artifact` (`target/surefire-reports/`), hiba esetén is feltöltődik |
| Kézi indítás | `workflow_dispatch` (GitHub → Actions → *Run workflow*) |
| Beadás | GitHub repo, BBotlik kollaborátor, a projekt zip-je a HackerRankre |

## Környezeti követelmények

A projekt tiszta Java + Maven, ezért Linuxon, Windowson és macOS-en is ugyanúgy működik.

| Eszköz | Verzió | Megjegyzés |
|---|---|---|
| JDK | 17 vagy újabb | A projekt `release 17`-re fordít, újabb JDK-val (pl. 21, 27) is fut |
| Maven | 3.9 vagy újabb | Az IntelliJ és az Eclipse saját beágyazott Mavennel is működik, parancssorhoz külön kell |
| Git | bármely friss verzió | Verziókövetéshez és a GitHub-feltöltéshez |
| Internetkapcsolat | – | A Maven függőségek letöltéséhez és a `jsonplaceholder.typicode.com` eléréséhez |
| IDE (opcionális) | IntelliJ IDEA vagy Eclipse | Parancssorból IDE nélkül is futtatható |

### macOS (Intel és Apple Silicon)

```bash
# Homebrew telepítése (ha még nincs): https://brew.sh
brew install --cask temurin@17     # vagy: brew install openjdk@17
brew install maven git
```

Ha az `openjdk` Homebrew-csomagot használod, előfordulhat, hogy a `java` nem kerül automatikusan a PATH-ra. Ilyenkor kövesd a `brew info openjdk` kimenetét (symlink vagy `JAVA_HOME` beállítás).

### Linux (Debian/Ubuntu)

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven git
```

Fedora/RHEL: `sudo dnf install java-17-openjdk-devel maven git`.

### Windows

PowerShell-ből (winget):

```powershell
winget install EclipseAdoptium.Temurin.17.JDK
winget install Apache.Maven
winget install Git.Git
```

Ha a `winget` nem elérhető, a telepítők letölthetők kézzel is: [Temurin JDK](https://adoptium.net), [Apache Maven](https://maven.apache.org/download.cgi), [Git](https://git-scm.com). A Maven zip-et kicsomagolás után add hozzá a PATH-hoz, és állítsd be a `JAVA_HOME` környezeti változót a JDK mappájára. Telepítés után nyiss új terminált.

### Telepítés ellenőrzése (minden operációs rendszeren)

```bash
java -version
mvn -v
git --version
```

A `mvn -v` kimenetében a `Java version` sor a telepített JDK-t mutassa (17 vagy újabb).

## Futtatás

### Parancssorból

```bash
git clone <repo-url>
cd api-exam-tests
mvn clean test
```

Sikeres futás végén: `BUILD SUCCESS` és `Tests run: 4, Failures: 0, Errors: 0`. Egyetlen tesztosztály futtatása:

```bash
mvn -Dtest=SinglePostTest test
```

A Surefire riportok a `target/surefire-reports/` mappában keletkeznek.

### IntelliJ IDEA

1. *File → Open* → a projekt `pom.xml` fájlja → *Open as Project*.
2. *File → Project Structure → Project → SDK*: JDK 17 vagy újabb (ha nincs, az *Add SDK → Download JDK* letölti).
3. Jobb klikk a `src/test/java` mappán → *Run 'All Tests'*.

### Eclipse

1. *File → Import → Maven → Existing Maven Projects* → a projekt mappája.
2. *Project → Properties → Java Build Path*: a JRE legyen 17 vagy újabb. Apple Silicon Macen az aarch64-es Eclipse-et töltsd le.
3. Jobb klikk a projekten → *Run As → JUnit Test*.

### GitHub Actions (CI)

A workflow a `push`-ra automatikusan lefut Ubuntu környezetben, JDK 17-tel (Temurin). Kézi indítás: *Actions → API Tests CI → Run workflow*. A Surefire riport a futás oldalán, az *Artifacts* szekcióban tölthető le (`surefire-reports`).
