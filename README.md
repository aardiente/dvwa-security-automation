

\# OWASP Security Automation Framework



A Java-based security automation framework for testing intentionally vulnerable web applications using \*\*Selenium, TestNG, Docker, OWASP ZAP, and Ollama\*\*.



The goal is to automate security challenges, demonstrate common attack vectors, collect testing evidence, and generate vulnerability assessment reports.



> \*\*Project Status:\*\* Active Development



\---



\## Supported Applications



\### OWASP Juice Shop

An intentionally vulnerable web application designed to demonstrate modern web security vulnerabilities.



\- Automated security challenge execution

\- SQL injection and cross-site scripting (XSS) testing

\- Authentication and access control testing

\- Challenge completion verification

\- Automated screenshots and reporting



\### DVWA (Damn Vulnerable Web Application)

A deliberately vulnerable PHP/MySQL application used for security testing and vulnerability research.



\- Automated vulnerability testing

\- Multiple vulnerability categories and difficulty levels

\- Selenium-driven security testing



\---



\## Technology Stack



| Technology | Purpose |

|------------|---------|

| Java | Primary programming language |

| Selenium WebDriver | Browser automation |

| TestNG | Test execution and assertions |

| Maven | Dependency management |

| Docker | Containerized test environments |

| OWASP ZAP | Security scanning and HTTP traffic analysis |

| Ollama | Local AI and chatbot testing (in development) |



\---



\## Features



\- \*\*Automated Security Challenges:\*\* Execute vulnerability-focused tests against Juice Shop and DVWA.

\- \*\*Page Object Model:\*\* Separate application interactions from test logic.

\- \*\*Docker Integration:\*\* Manage isolated vulnerable application environments.

\- \*\*OWASP ZAP Integration:\*\* Capture HTTP traffic and produce security assessment reports.

\- \*\*Screenshot Capture:\*\* Save test evidence automatically using TestNG method names.

\- \*\*Execution Logging:\*\* Record Selenium activity and errors.

\- \*\*Headless Testing:\*\* Execute browser automation without displaying the browser window.

\- \*\*AI Testing:\*\* Experimental Ollama integration for chatbot security testing.



\---



\## Prerequisites



Ensure the following are installed:



\- Java Development Kit (JDK)

\- IntelliJ IDEA or another Java IDE

\- Maven or an IDE with Maven integration

\- Docker Desktop

\- Chrome or another supported browser



\*\*Docker Desktop must be running before executing container-dependent tests.\*\*



\---



\## Docker Configuration



\### 1. Enable WSL Integration (Windows)



Open Docker Desktop and navigate to:



\*\*Settings → Resources → WSL Integration\*\*



Enable:



`Integration with my default WSL distro`



Apply the changes.



\### 2. Configure Docker Engine



Navigate to:



\*\*Settings → Docker Engine\*\*



The project has been developed using the following Docker configuration:



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



Review these settings for compatibility with your Docker version. Preserve any existing settings your environment requires.



\---



\## Running Tests



\### IntelliJ IDEA



1\. Open the project in IntelliJ.

2\. Ensure Docker Desktop is running.

3\. Allow Maven to load the project dependencies.

4\. Navigate to the desired TestNG class.

5\. Right-click the test class and select \*\*Run\*\*.



\### Maven



From the project root:



```bash

mvn test

```



This executes the tests configured for the Maven build.



Individual tests can also be executed directly through IntelliJ.



\---



\## Reports and Logging



\### OWASP ZAP Reports



OWASP ZAP is partially integrated into the framework.



When ZAP reporting is enabled and executed, an HTML report is generated at:



```text

Automation/target/zap-report.html

```



Open the file in a browser to inspect security findings.



\### Automated Screenshots



Screenshots are stored in:



```text

Automation/target/screenshots/

```



Each screenshot is named after its TestNG test method.



Examples:



```text

testSqlInjectionLoginBypass.png

testDomXssChallenge.png

testConfidentialDocumentChallenge.png

testExposedMetricsChallenge.png

testScoreBoardChallenge.png

```



Screenshots with matching filenames are overwritten during subsequent test runs.



The Juice Shop Score Board test runs last to capture the final challenge completion status.



\### Execution Logs



Logs are generated in:



```text

Automation/logs/

```



These logs provide information about browser interactions, automation execution, and errors.



\*\*Note:\*\* Generated reports, logs, and screenshots should remain excluded from Git tracking through `.gitignore`.



\---



\## Development Roadmap



\- \[ ] Automate all feasible OWASP Juice Shop challenges

\- \[ ] Expand DVWA security testing coverage

\- \[ ] Complete OWASP ZAP integration

\- \[ ] Implement additional API security testing

\- \[ ] Complete Ollama chatbot integration

\- \[ ] Expand automated vulnerability reporting

\- \[ ] Develop HTML documentation for framework classes and utilities



\---



\## Security Disclaimer



This project is intended for \*\*educational purposes, authorized security testing, and vulnerability research\*\*.



The applications used in this framework are intentionally vulnerable and should only be deployed in controlled testing environments.



Do not use this framework against systems without explicit authorization.



\---



\*\*OWASP Security Automation Framework — Active Development\*\*



