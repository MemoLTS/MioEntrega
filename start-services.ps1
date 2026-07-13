# Gateway
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Gateway; mvn spring-boot:run"

Start-Sleep -Seconds 5

# ─── Juan Pablo Jofre ────────────────────────────────────────────────
# Usuario
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Usuario; mvn spring-boot:run"

# Tienda
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Tienda; mvn spring-boot:run"

# Soporte
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Soporte; mvn spring-boot:run"

# ─── Guillermo Toledo ────────────────────────────────────────────────
# Monitor
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Monitor; mvn spring-boot:run"

# Inventario
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Inventario; mvn spring-boot:run"

# Catalogo
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Catalogo; mvn spring-boot:run"

# Backup
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Backups; mvn spring-boot:run"

# ─── Cristobal Merino ────────────────────────────────────────────────
# Proveedor
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Proveedor; mvn spring-boot:run"

# Pedido
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Pedido; mvn spring-boot:run"

# Envio
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Envio; mvn spring-boot:run"

# Ruta
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Ruta; mvn spring-boot:run"

# Factura
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Factura; mvn spring-boot:run"

# Pago
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd Pago; mvn spring-boot:run"
