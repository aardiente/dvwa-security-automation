This project relies on an instance of DockerDesktop running and configured.
- For windows:
    Resources-> WSL Integration-> Enable "Enable integration with my default WSL distro"
- Settings-> Docker Engine ->
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
