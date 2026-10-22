# OWASP Security Automation Framework

A Java-based security testing framework designed to automate vulnerability assessments, execute security challenges, and analyze intentionally vulnerable web applications.

Built using **Selenium WebDriver, TestNG, Docker, OWASP ZAP, and Ollama**.

> **Project Status:** Active Development

---

## Supported Applications

### OWASP Juice Shop
An intentionally vulnerable web application designed to demonstrate modern web security vulnerabilities.

- Automated security challenge execution and verification
- SQL injection and cross-site scripting (XSS) testing
- Authentication and access control testing
- Automated screenshots of security test execution
- Challenge completion tracking through the Score Board

### DVWA (Damn Vulnerable Web Application)
A deliberately vulnerable PHP/MySQL application used for security testing and vulnerability research.

- Automated vulnerability testing
- Multiple security difficulty levels
- Selenium-driven attack simulation
- Data-driven testing using external payload files

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Primary programming language |
| Selenium WebDriver | Browser automation |
| TestNG | Test execution and assertions |
| Maven | Dependency management and builds |
| Docker | Containerized testing environments |
| OWASP ZAP | HTTP traffic interception and security reporting |
| Ollama | Local AI and chatbot security testing (in development) |

---

## Features

- **Security Challenge Automation:** Execute vulnerability-focused tests against Juice Shop and DVWA.
- **Page Object Model (POM):** Separate application interactions from test execution logic.
- **Docker Integration:** Manage isolated testing environments.
- **OWASP ZAP Integration:** Capture application traffic and generate independent security reports.
- **Automated Screenshots:** Capture visual evidence using TestNG test method names.
- **TestNG Suite Management:** Execute DVWA and Juice Shop tests independently or together.
- **Execution Logging:** Record Selenium interactions and automation errors.
- **Headless Browser Testing:** Run automated tests without displaying a browser window.
- **AI Security Testing:** Experimental Ollama integration for chatbot testing.

---

## Prerequisites

Ensure the following are installed:

- Java Development Kit (JDK)
- IntelliJ IDEA or another Java IDE
- Maven or an IDE with Maven integration
- Docker Desktop
- Chrome or another supported browser

**Docker Desktop must be running before executing container-dependent tests.**

---

## Docker Configuration

### 1. Enable WSL Integration (Windows)

Open Docker Desktop and navigate to:

**Settings → Resources → WSL Integration**

Enable:

`Integration with my default WSL distro`

Apply the changes.

### 2. Configure Docker Engine

Navigate to:

**Settings → Docker Engine**

The project has been developed using the following configuration:

```json
{
  "builder": {
    "gc": {
      "defaultKeepStorage": "20GB",
      "enabled": true
    }
  },
  "experimental": false,
  "min-api-version": "1.24"
}
```

Review these settings for compatibility with your Docker version. Preserve any existing configuration your environment requires.

---

## Running Tests

Tests can be executed individually or through TestNG suite configurations.

### IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Ensure Docker Desktop is running.
3. Allow Maven to resolve the project dependencies.
4. Navigate to the desired TestNG test class or suite XML file.
5. Right-click and select **Run**.

### TestNG Suite Configurations

| Configuration | Description |
|---|---|
| `testng-dvwa.xml` | Runs the DVWA security tests |
| `testng-juiceshop.xml` | Runs the OWASP Juice Shop challenge tests |
| `testng-all.xml` | Executes both application suites sequentially |

### Running the Entire Framework

Execute:

`testng-all.xml`

The intended execution sequence is:

1. Start the DVWA Docker environment and OWASP ZAP.
2. Execute the DVWA security tests.
3. Generate the DVWA security report.
4. Shut down the DVWA and ZAP environments.
5. Start the Juice Shop Docker environment and a fresh ZAP session.
6. Execute the Juice Shop security challenges.
7. Capture the final Juice Shop Score Board screenshot.
8. Generate the Juice Shop security report.
9. Shut down the remaining containers.

This sequence is designed to keep the applications' ZAP traffic histories separate.

### Maven

From the project root:

```bash
mvn test
```

Maven executes tests according to the configured Surefire/TestNG setup. To run a specific suite through Maven, the corresponding suite configuration must be selected in the build configuration or command-line options.

---

## OWASP ZAP Integration

OWASP ZAP is integrated into the automation framework to intercept HTTP traffic, analyze web application behavior, and generate security assessment reports.

### Independent Application Reports

The framework supports separate ZAP reports for DVWA and Juice Shop.

| Application | Report |
|---|---|
| DVWA | `zap-dvwa.html` |
| OWASP Juice Shop | `zap-juiceshop.html` |

Reports are generated in:

```text
Automation/
└── target/
    ├── zap-dvwa.html
    └── zap-juiceshop.html
```

Each report can be opened in a web browser.

### How Reporting Works

The framework uses `ZapManager` to manage OWASP ZAP and its Selenium proxy connections.

During test execution:

1. ZAP starts in its Docker environment.
2. Selenium browser traffic is routed through the ZAP proxy.
3. ZAP records intercepted HTTP requests and responses.
4. Passive scanning analyzes the captured traffic.
5. At suite completion, the framework waits for pending passive scans to finish.
6. An HTML report is saved using the corresponding application's filename.

The two applications run in separate ZAP sessions when using the combined TestNG suite configuration.

### Reporting Limitations

OWASP ZAP integration currently focuses on **HTTP interception, passive scanning, and HTML report generation**.

- Active vulnerability scanning is not automatically performed.
- Direct Java HTTP requests that bypass the ZAP proxy are not included in its traffic history.
- Findings depend on the requests executed during testing.
- A completed Juice Shop challenge does not necessarily produce a corresponding ZAP alert.
- Reports are generated when the relevant ZAP initialization and reporting lifecycle completes successfully.

Additional API security testing and scanning functionality are planned.

---

## Automated Screenshots

The framework includes a reusable `ScreenshotHelper` utility.

Screenshots are saved under:

```text
Automation/target/screenshots/
```

Each screenshot is named after its corresponding TestNG method.

Example:

```text
screenshots/
├── testSqlInjectionLoginBypass.png
├── testDomXssChallenge.png
├── testConfidentialDocumentChallenge.png
├── testExposedMetricsChallenge.png
└── testScoreBoardChallenge.png
```

Existing screenshots with matching filenames are overwritten during subsequent test runs.

### Juice Shop Score Board Verification

The `testScoreBoardChallenge()` method runs last in the Juice Shop challenge suite using TestNG priority `1000`.

Its final screenshot provides a visual overview of completed security challenges.

---

## Execution Logs

Automation logs are generated under:

```text
Automation/logs/
```

These logs help with:

- Debugging Selenium interactions
- Reviewing browser navigation
- Analyzing automation failures
- Investigating test execution behavior

**Note:** Generated logs, screenshots, and reports should remain excluded from Git tracking through `.gitignore`.

---

## Development Roadmap

- [ ] Automate all feasible OWASP Juice Shop challenges
- [ ] Expand DVWA security testing coverage
- [x] Implement independent DVWA and Juice Shop ZAP report generation
- [ ] Expand ZAP scanning and security reporting
- [ ] Implement additional API security testing
- [ ] Complete Ollama chatbot integration
- [ ] Develop comprehensive HTML documentation for framework classes and utilities

---

## Security Disclaimer

This project is intended for **educational purposes, authorized penetration testing, and vulnerability research**.

The applications used in this framework are intentionally vulnerable and should only be deployed in controlled testing environments.

Do not use this framework against systems without explicit authorization.

---

**OWASP Security Automation Framework — Active Development**