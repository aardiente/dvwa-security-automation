
# OWASP Security Automation Framework

A Java-based security automation framework for testing intentionally vulnerable web applications using Selenium WebDriver, TestNG, Docker Testcontainers, and OWASP ZAP.

The framework provides automated security testing, data-driven payload execution, vulnerability detection, and HTML security reports for DVWA and OWASP Juice Shop.

**Language:** Java 17  
**Build Tool:** Maven  
**Status:** Active Development

> **Important:** This is a security-testing framework. DVWA tests intentionally fail when a vulnerability or security finding is detected. A failed assertion can therefore represent a successful vulnerability detection rather than broken automation.

## Applications Under Test

| Application | Automated Coverage |
|---|---|
| DVWA (Damn Vulnerable Web Application) | Reflected XSS and SQL Injection |
| OWASP Juice Shop | Authentication bypass, DOM XSS, exposed resources, administration access, and additional security challenges |

### DVWA

Security testing includes:

- Reflected Cross-Site Scripting (XSS) at LOW, MEDIUM, and HIGH security levels.
- SQL Injection testing at LOW security.
- Excel-driven security payloads.
- Security-level verification using DVWA's `security` cookie.
- Intentional TestNG assertion failures when vulnerabilities are detected.

### OWASP Juice Shop

Challenge-based testing includes:

- Authentication bypass.
- DOM-based XSS.
- Exposed confidential documents.
- Administrative functionality.
- Security policy discovery.
- Additional OWASP Juice Shop challenges.

Unlike DVWA vulnerability tests, Juice Shop challenge tests generally pass when the expected challenge is successfully completed.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Selenium WebDriver 4.49.0 | Browser automation |
| TestNG 7.10.2 | Test execution and assertions |
| Maven | Dependency management |
| Docker | Container runtime |
| Testcontainers 1.20.1 | Automated application and ZAP container management |
| OWASP ZAP | Security proxy and passive vulnerability scanning |
| Apache POI | Excel payload management |
| SLF4J / Log4j 2 | Logging |
| Page Object Model | Separation of page interactions and test logic |

## Framework Features

- Automated Docker environments for vulnerable applications.
- Selenium Page Object Model architecture.
- Data-driven security testing using Excel.
- Configurable headed and headless Chrome execution.
- Explicit waits for browser synchronization.
- Optional SlowMo mode for demonstrations.
- Configurable display duration for JavaScript alerts and security results.
- OWASP ZAP traffic interception and passive scanning.
- Separate HTML reports for DVWA and Juice Shop.
- Screenshot capture for selected Juice Shop tests.
- Intentional assertion failures for confirmed security findings.

## Prerequisites

Before running the framework, install:

1. Java Development Kit (JDK 17 or later).
2. Maven or IntelliJ IDEA with Maven integration.
3. Docker Desktop with a running Docker engine.
4. Google Chrome.

Docker images and Maven dependencies are downloaded automatically when needed.

DVWA, Juice Shop, and OWASP ZAP are managed through Testcontainers and do not need to be started manually.

## Installation

Clone the repository:

```bash
git clone <repository-url>
cd <repository-directory>
```

Open the project in IntelliJ IDEA or another Java IDE.

Allow Maven to resolve dependencies.

Verify Docker Desktop is running before executing tests.

## Running Tests

The project includes three TestNG suite configurations.

### DVWA

```bash
mvn test -Dsurefire.suiteXmlFiles=testng-dvwa.xml
```

Runs the DVWA SQL Injection and Reflected XSS tests.

### OWASP Juice Shop

```bash
mvn test -Dsurefire.suiteXmlFiles=testng-juiceshop.xml
```

Runs the configured Juice Shop challenge tests.

### Complete Test Suite

```bash
mvn test -Dsurefire.suiteXmlFiles=testng-all.xml
```

Runs both application suites sequentially.

Individual test classes and methods can also be executed directly through IntelliJ IDEA.

## Browser Configuration

Browser settings are managed through:

`src/test/resources/config.properties`

### Headless Configuration

Recommended for automated verification:

```properties
browser.headless=true
browser.viewport.width=1920
browser.viewport.height=1080
browser.implicitWaitSeconds=0

browser.slowMo.enabled=false
browser.slowMo.delayMs=250
browser.showResultsMs=3000
```

### Presentation Configuration

Recommended when demonstrating browser interactions and vulnerability findings:

```properties
browser.headless=false
browser.viewport.width=1920
browser.viewport.height=1080
browser.implicitWaitSeconds=0

browser.slowMo.enabled=true
browser.slowMo.delayMs=250
browser.showResultsMs=3000
```

### Configuration Options

| Property | Description |
|---|---|
| `browser.headless` | Enables or disables headless Chrome. |
| `browser.viewport.width` | Browser window width. |
| `browser.viewport.height` | Browser window height. |
| `browser.implicitWaitSeconds` | Selenium implicit wait duration. |
| `browser.slowMo.enabled` | Enables presentation mode. |
| `browser.slowMo.delayMs` | Delay applied to supported browser interactions. |
| `browser.showResultsMs` | Duration that selected results and JavaScript alerts remain visible. |

`browser.showResultsMs` only applies when SlowMo is enabled.

SlowMo is intended for demonstrations and does not replace Selenium's explicit waits.

## DVWA Security Testing

### Reflected XSS

The Reflected XSS tests use payloads from:

`src/test/java/data/xss_payloads.xlsx`

Payloads are tested against three security levels:

- LOW
- MEDIUM
- HIGH

Selenium submits each payload and checks whether a JavaScript alert executes.

**Test behavior:**

| Result | TestNG Outcome |
|---|---|
| JavaScript alert executes | FAIL — XSS vulnerability detected |
| No alert detected | PASS — No alert-based execution confirmed |
| Browser or setup exception | ERROR/FAIL — Automation issue |

Successful XSS execution intentionally triggers an assertion failure.

The active DVWA security level is verified using its `security` cookie.

### SQL Injection

SQL injection payloads are supplied through:

`src/test/java/data/sqli_payloads.xlsx`

Testing is performed against DVWA's LOW-security SQL injection page.

The framework compares normal database responses with injected responses to identify potential security findings.

Security findings include:

- SQL injection that changes database query results.
- Unauthorized database information disclosure.
- SQL or MariaDB error disclosure.
- SQL-like input accepted by the application, reported separately from confirmed injection execution.

**Test behavior:**

| Result | TestNG Outcome |
|---|---|
| Confirmed SQL injection | FAIL |
| Credential or unauthorized data disclosure | FAIL |
| SQL/MariaDB error disclosure | FAIL |
| Unsafe SQL input accepted, according to configured assertions | FAIL |
| No detected finding | PASS |

A database error is reported as a security finding but does not independently prove successful SQL injection.

Similarly, reflected payload text does not independently prove that the database executed injected SQL commands.

## OWASP Juice Shop Testing

Juice Shop tests automate selected security challenges using Selenium and the Page Object Model.

Challenge completion is verified through the expected application behavior or challenge status.

Unlike DVWA's vulnerability-detection tests, Juice Shop tests generally use positive assertions: the test passes when the expected challenge behavior is demonstrated.

Additional experimental tests may exist in the source tree but are not included in the default Juice Shop TestNG suite.

## OWASP ZAP Integration

OWASP ZAP runs inside a Docker container during test execution.

Selenium sends browser traffic through the configured ZAP proxy, allowing ZAP to inspect application requests and responses.

The framework uses passive scanning to identify potential security issues.

### Generated Reports

| Application | Report Location |
|---|---|
| DVWA | `target/zap-dvwa.html` |
| OWASP Juice Shop | `target/zap-juiceshop.html` |

Reports are generated during suite teardown.

Open the HTML files in a browser to review the detected alerts.

ZAP findings and Selenium assertions provide separate sources of security evidence.

The default framework does not perform a ZAP active scan.

## Screenshots and Logging

Selected tests capture screenshots as execution evidence.

Generated files are stored under:

```text
target/
├── screenshots/
├── zap-dvwa.html
├── zap-juiceshop.html
└── surefire-reports/
```

Logs may also be generated in the configured logging directory.

Generated reports, screenshots, and logs are intended as local execution artifacts.

## Project Structure

```text
.
├── pom.xml
├── README.md
├── testng-dvwa.xml
├── testng-juiceshop.xml
├── testng-all.xml
└── src/
    └── test/
        ├── java/
        │   ├── automation/
        │   │   ├── AppConfig.java
        │   │   ├── SlowMoListener.java
        │   │   └── ZapManager.java
        │   ├── pom/
        │   │   ├── Dvwa/
        │   │   └── JuiceShop/
        │   ├── tests/
        │   │   ├── Dvwa/
        │   │   └── JuiceShop/
        │   ├── data/
        │   │   ├── sqli_payloads.xlsx
        │   │   └── xss_payloads.xlsx
        │   └── utilities/
        └── resources/
            ├── config.properties
            └── log4j2.xml
```

### Directory Responsibilities

| Directory | Responsibility |
|---|---|
| `automation/` | Browser configuration, Selenium listeners, and ZAP management. |
| `pom/Dvwa/` | DVWA Page Object Model classes. |
| `pom/JuiceShop/` | Juice Shop Page Object Model classes. |
| `tests/Dvwa/` | DVWA vulnerability tests. |
| `tests/JuiceShop/` | Juice Shop challenge tests. |
| `data/` | Excel-based security payloads. |
| `utilities/` | Shared testing utilities and helpers. |
| `resources/` | Runtime configuration and logging configuration. |

## Troubleshooting

### Selenium Element Errors

If Selenium reports:

```text
Node with given id does not belong to the document
```

The browser may have navigated or refreshed while Selenium was interacting with an element from the previous document.

Avoid retaining WebElement references across navigation and use fresh element locators.

### DVWA Security-Level Issues

If security-level configuration fails:

1. Verify the application login completed successfully.
2. Confirm the security-setting form was submitted.
3. Check the DVWA `security` cookie.
4. Confirm the requested security level is active before proceeding.

### Intentional Assertion Failures

Messages such as:

```text
Visual Verification Confirmed: XSS
java.lang.AssertionError:
Targeted XSS exploit successfully fired
```

indicate a successful vulnerability detection.

These failures are expected behavior for DVWA security tests.

### JavaScript Alert Timeouts

If Selenium reports that it was waiting for an alert, the payload may not have triggered an observable JavaScript alert within the configured wait.

This is not equivalent to a confirmed vulnerability finding.

### Selenium CDP Version Warnings

Chrome and Selenium may report differences between supported Chrome DevTools Protocol versions.

These warnings do not automatically indicate a failed test.

Investigate them if DevTools-dependent browser functionality stops working.

### Docker or ZAP Startup Failures

Verify:

- Docker Desktop is running.
- Required container images are available.
- Docker has sufficient resources.
- The proxy container started successfully.

## Future Improvements

- [x] Selenium Page Object Model implementation.
- [x] Docker Testcontainers integration.
- [x] Excel-driven vulnerability payloads.
- [x] Automated DVWA and Juice Shop testing.
- [x] Configurable headless execution.
- [x] Presentation and SlowMo settings.
- [x] OWASP ZAP passive scanning.
- [x] Separate HTML security reports.
- [x] Screenshot evidence.
- [ ] Expand DVWA vulnerability coverage.
- [ ] Add additional Juice Shop challenges.
- [ ] Improve SQL injection verification.
- [ ] Improve distinction between security findings and automation failures in CI reporting.
- [ ] Expand ZAP scanning capabilities.
- [ ] Standardize experimental chatbot testing.

## Responsible Use

This framework is intended for educational purposes, controlled security laboratories, and explicitly authorized penetration testing.

DVWA and OWASP Juice Shop are intentionally vulnerable applications and should not be exposed to untrusted networks.

Only perform security testing against applications you own or have permission to assess.

---

**OWASP Security Automation Framework — Active Development**
