# API tesztprojekt – JSONPlaceholder /posts

## Összefoglaló

A projekt a JSONPlaceholder `/posts` végpontját teszteli Java 17, Maven, JUnit 5 és RestAssured segítségével, három tesztcsomaggal (TC-1.1, TC-2.1, TC-2.2, TC-3.1), a POST teszt a requestet és hiba esetén a response-t is logolja. A tesztek futtathatók IDE-ből és `mvn clean test` paranccsal, a GitHub Actions CI push-ra és manuálisan is lefut, a Surefire riportot artifactként tölti fel. Az előírt feladatok mindegyikét sikerült megvalósítani, nem maradt kihagyott elem; a fejlesztés gitflow szerint történt, a verziószám SemVer szerint a `version.txt`-ben található.

---

*Az alábbi szakaszok kiegészítő információk.*

## Kézi ellenőrzés (curl)

A tesztek előtt az API működését így ellenőriztem kézzel:

```bash
curl -i https://jsonplaceholder.typicode.com/posts/1
curl -i https://jsonplaceholder.typicode.com/posts/9999
curl -i -X POST https://jsonplaceholder.typicode.com/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"projektfeladat","body":"api házivizsga","userId":1}'
```

Elvárt válasz: az első `200` és `"id": 1`, a második `404` és `{}`, a harmadik `201` és egy generált `id` (pl. `101`). Windows PowerShellben `curl.exe`-t használj, és a parancsot egy sorban írd.

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

## Környezeti követelmények

A projekt tiszta Java + Maven, ezért Linuxon, Windowson és macOS-en is ugyanúgy működik.

| Eszköz | Verzió | Megjegyzés |
|---|---|---|
| JDK | 17 vagy újabb | A projekt `release 17`-re fordít, újabb JDK-val is fut |
| Maven | 3.9 vagy újabb | Az IDE-k beágyazott Mavenje is megfelel, parancssorhoz külön kell |
| Git | bármely friss verzió | Verziókövetéshez és a GitHub-feltöltéshez |
| Internetkapcsolat | – | Maven függőségek és a `jsonplaceholder.typicode.com` eléréséhez |

Telepítés:

```bash
# macOS (Homebrew)
brew install --cask temurin@17
brew install maven git

# Linux (Debian/Ubuntu)
sudo apt update && sudo apt install -y openjdk-17-jdk maven git
```

```powershell
# Windows (PowerShell, winget)
winget install EclipseAdoptium.Temurin.17.JDK
winget install Apache.Maven
winget install Git.Git
```

Ellenőrzés: `java -version`, `mvn -v`, `git --version`.

## Futtatás

```bash
git clone <repo-url>
cd api-exam-tests
mvn clean test
```

Sikeres futás végén: `BUILD SUCCESS` és `Tests run: 4, Failures: 0, Errors: 0`. Egy osztály futtatása: `mvn -Dtest=SinglePostTest test`. A Surefire riportok a `target/surefire-reports/` mappában keletkeznek.

- **IntelliJ IDEA:** *File → Open* → `pom.xml` → *Open as Project*, SDK: JDK 17+, jobb klikk a `src/test/java` mappán → *Run 'All Tests'*.
- **Eclipse:** *File → Import → Maven → Existing Maven Projects*, majd jobb klikk a projekten → *Run As → JUnit Test*.
- **GitHub Actions:** push-ra automatikusan fut, kézzel az *Actions → API Tests CI → Run workflow* gombbal. A riport az *Artifacts* szekcióban a `surefire-reports` néven érhető el.
