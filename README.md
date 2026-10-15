This project relies on an instance of DockerDesktop running and configured.
- For windows:
    Resources-> WSL Integration-> Enable "Enable integration with my default WSL distro"
- Settings-> Docker Engine -> "Configure the Docker daemon by typing a JSON Docker Daemon configuration file."  ->
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


OWASP Zap is partially integrated into the project.
Once you run the tests a report under the name 'zap-report.html' will be generated in Automation/target
