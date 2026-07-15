# build-all.ps1
# Compila (empaqueta) todos los microservicios UNA sola vez usando el Maven Wrapper
# incluido en cada carpeta (mvnw.cmd) -- no necesitas tener Maven instalado.
# Corre esto cada vez que cambies código; después usa start-services.ps1 para arrancar
# todo sin volver a compilar.

$modulos = @(
    "GatewayEcoMarket",
    "Usuario",
    "Tienda",
    "Soporte",
    "Monitor",
    "Inventario",
    "Catalogo",
    "Backups",
    "Proveedor",
    "Pedido",
    "Envios",
    "RutaYSeguimiento",
    "Facturacion",
    "Pagos"
)

foreach ($m in $modulos) {
    if (-not (Test-Path $m)) {
        Write-Host "[$m] Carpeta no encontrada, se omite." -ForegroundColor Yellow
        continue
    }
    Write-Host "== Compilando $m ==" -ForegroundColor Cyan
    Push-Location $m
    & ".\mvnw.cmd" -q -DskipTests package
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[$m] FALLÓ la compilación." -ForegroundColor Red
    } else {
        Write-Host "[$m] OK" -ForegroundColor Green
    }
    Pop-Location
}
