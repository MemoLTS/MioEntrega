# start-services.ps1

$BasePath = Split-Path -Parent $MyInvocation.MyCommand.Path

function Start-Service {
    param(
        [string]$Nombre,
        [string]$Carpeta
    )

    $Proyecto = Join-Path $BasePath $Carpeta
    $Target = Join-Path $Proyecto "target"

    if (!(Test-Path $Proyecto)) {
        Write-Host "[$Nombre] No existe la carpeta: $Proyecto" -ForegroundColor Red
        return
    }

    if (!(Test-Path $Target)) {
        Write-Host "[$Nombre] No existe la carpeta target. Debes compilar primero." -ForegroundColor Yellow
        return
    }

    $Jar = Get-ChildItem $Target -Filter "*.jar" |
        Where-Object { $_.Name -notlike "*.jar.original" } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1

    if ($null -eq $Jar) {
        Write-Host "[$Nombre] No se encontró ningún .jar en:" -ForegroundColor Red
        Write-Host "          $Target"
        return
    }

    Write-Host "[$Nombre] Iniciando $($Jar.Name)..." -ForegroundColor Green

    Start-Process powershell -ArgumentList @(
        "-NoExit",
        "-Command",
        "cd '$Proyecto'; java -jar '$($Jar.FullName)'"
    )
}

# ================================
# Gateway
# ================================

Start-Service "Gateway" "GatewayEcoMarket"

Start-Sleep -Seconds 5

# ================================
# Juan Pablo Jofre
# ================================

Start-Service "Usuario" "Usuario"
Start-Service "Tienda" "Tienda"
Start-Service "Soporte" "Soporte"

# ================================
# Guillermo Toledo
# ================================

Start-Service "Monitor" "Monitor"
Start-Service "Inventario" "Inventario"
Start-Service "Catalogo" "Catalogo"
Start-Service "Backup" "Backups"

# ================================
# Cristóbal Merino
# ================================

Start-Service "Proveedor" "Proveedor"
Start-Service "Pedido" "Pedido"
Start-Service "Envio" "Envios"
Start-Service "Ruta" "RutaYSeguimiento"
Start-Service "Factura" "Facturacion"
Start-Service "Pago" "Pagos"

Write-Host ""
Write-Host "Todos los servicios fueron procesados." -ForegroundColor Cyan