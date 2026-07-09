# Gateway
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Gateway; mvn spring-boot:run"

Start-Sleep -Seconds 5

# Monitor
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Monitor; mvn spring-boot:run"

# Inventario
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Inventario; mvn spring-boot:run"

# Catalogo
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Catalogo; mvn spring-boot:run"

# Backup
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Backups; mvn spring-boot:run"