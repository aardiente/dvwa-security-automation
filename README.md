This project relies on an instance of DockerDesktop running and configured.
- For windows: WSL Integration
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
