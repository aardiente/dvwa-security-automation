\# OWASP Security Automation Framework



A Java-based security testing framework designed to automate vulnerability assessments, execute security challenges, and analyze intentionally vulnerable web applications.



The project integrates \*\*Selenium WebDriver, TestNG, Docker, OWASP ZAP, and Ollama\*\* to provide a foundation for automated security testing and vulnerability research.



\---



\## Overview



This framework automates security testing against intentionally vulnerable applications, with a focus on identifying and demonstrating common web application vulnerabilities.



The project is under active development, with ongoing improvements to challenge coverage, security reporting, and automated testing capabilities.



\### Supported Applications



| Application | Description |

|---|---|

| \*\*OWASP Juice Shop\*\* | Modern, intentionally vulnerable web application covering OWASP security challenges. |

| \*\*DVWA\*\* | Damn Vulnerable Web Application, designed for practicing and evaluating common web vulnerabilities. |



\## Features



\- \*\*Automated Security Challenges\*\* — Execute vulnerability-focused tests against supported applications.

\- \*\*OWASP ZAP Integration\*\* — Capture HTTP traffic and generate security assessment reports.

\- \*\*Docker Environment Management\*\* — Run vulnerable applications in isolated container environments.

\- \*\*Automated Screenshots\*\* — Capture visual evidence during test execution.

\- \*\*TestNG Integration\*\* — Organize, execute, and validate automated security tests.

\- \*\*Page Object Model (POM)\*\* — Maintain reusable application-specific page interactions.

\- \*\*Ollama Integration\*\* — Development area for local AI and chatbot security testing.

\- \*\*Logging and Reporting\*\* — Generate execution logs, screenshots, and security reports.



\## Technology Stack



| Technology | Purpose |

|---|---|

| Java | Primary programming language |

| Selenium WebDriver | Browser automation |

| TestNG | Test execution and assertions |

| Maven | Dependency management and builds |

| Docker / Docker Desktop | Containerized testing environments |

| OWASP ZAP | Web application security scanning and traffic analysis |

| Ollama | Local AI model integration |



\---



\## Prerequisites



Before executing the automation framework, ensure that the following are available:



\- Java Development Kit (JDK)

\- Docker Desktop

\- IntelliJ IDEA or another Java IDE

\- Maven or an IDE with Maven integration



\*\*Docker Desktop must be running and properly configured before executing tests that depend on containerized environments.\*\*



\## Docker Desktop Configuration



\### 1. Enable WSL Integration (Windows)



1\. Open \*\*Docker Desktop\*\*.

2\. Navigate to \*\*Settings → Resources → WSL Integration\*\*.

3\. Enable \*\*Integration with my default WSL distro\*\*.

4\. Apply the changes.



\### 2. Configure the Docker Engine



Navigate to \*\*Settings → Docker Engine\*\*.



Ensure your Docker daemon configuration includes the following settings:



```json

{

&#x20; "builder": {

&#x20;   "gc": {

&#x20;     "defaultKeepStorage": "20GB",

&#x20;     "enabled": true

&#x20;   }

&#x20; },

&#x20; "experimental": false,

&#x20; "min-api-version": "1.24"

}

```



Apply and restart Docker Desktop if prompted.



> \*\*Note:\*\* These are the Docker daemon settings used for this project. Review and merge them with your existing configuration rather than overwriting unrelated settings. Compatibility may vary by Docker version.



\---



\## Running the Tests



\### Using IntelliJ IDEA



1\. Open the project in IntelliJ IDEA.

2\. Ensure Docker Desktop is running.

3\. Allow Maven to resolve the project dependencies.

4\. Navigate to the desired TestNG test class.

5\. Right-click the test class or method and select \*\*Run\*\*.



\### Using Maven



If Maven is installed and available in your system PATH:



```bash

mvn test

```



Test execution depends on the project's configured Maven and TestNG setup.



\---



\## Reports and Test Evidence



The framework generates several types of output to help analyze test execution and security findings.



\### OWASP ZAP Reports



OWASP ZAP is \*\*partially integrated\*\* into the framework.



When ZAP reporting is enabled and invoked, an HTML security assessment report is generated at:



```text

Automation/target/zap-report.html

```



The report can be opened in a web browser to review detected security findings.



\### Automated Screenshots



Screenshots are captured during automated test execution and stored in:



```text

Automation/target/screenshots/

```



Each screenshot uses the corresponding TestNG test method name.



Example:



```text

screenshots/

├── testSqlInjectionLoginBypass.png

├── testDomXssChallenge.png

├── testConfidentialDocumentChallenge.png

├── testExposedMetricsChallenge.png

└── testScoreBoardChallenge.png

```



Existing screenshots with the same filename are overwritten on subsequent runs.



\### Execution Logs



Application and automation logs are generated in:



```text

Automation/logs/

```



These logs can assist with debugging Selenium interactions, analyzing execution failures, and reviewing automated test activity.



\---



\## Project Development Roadmap



\- \[ ] Expand OWASP Juice Shop challenge automation coverage.

\- \[ ] Expand DVWA vulnerability testing.

\- \[ ] Complete OWASP ZAP integration and improve vulnerability reporting.

\- \[ ] Introduce automated API security testing.

\- \[ ] Develop Ollama-powered chatbot security testing.

\- \[ ] Create comprehensive HTML documentation for the automation framework.



\---



\## Security Disclaimer



This project is intended for \*\*educational purposes, security research, and authorized penetration testing\*\*.



The included applications are intentionally vulnerable and should be deployed only within controlled testing environments.



Do not use this framework against systems without explicit authorization.



\---



\*\*Project Status:\*\* Active Development

