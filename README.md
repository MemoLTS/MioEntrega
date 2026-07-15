Documentación de APIs
Arquitectura
Servicio
Base URL
Inventario
/api/v1/inventario
Catálogo
/api/v1/catalogo
Monitor
/api/v1/monitor
Backups
/api/v1/backup

Todos los servicios exponen APIs REST utilizando JSON.

swagger
http://localhost:8080/swagger-ui/index.html

API Inventario
Permite administrar productos, stock y registros de inventario.
Base URL
/api/v1/inventario
Funcionalidades
Registrar productos
Actualizar productos
Eliminar productos
Consultar productos
Administrar stock
Consultar registros (logs)

API Catálogo
Permite consultar los productos disponibles para los clientes.
Base URL
/api/v1/catalogo
Endpoints
Obtener catálogo completo
GET /api/v1/catalogo/ver
Descripción
Obtiene todos los productos del catálogo.

Obtener productos disponibles
GET /api/v1/catalogo/ver/disponibles
Devuelve únicamente productos con stock disponible.

Buscar por categoría
GET /api/v1/catalogo/PorCategoria/{idCategoria}
Parámetros
Nombre
Tipo
idCategoria
Long

Respuesta
[
  {
    "id":1,
    "nombre":"Leche",
    "precio":1200
  }
]

Buscar por nombre
GET /api/v1/catalogo/PorNombre/{nombre}
Ejemplo
GET /api/v1/catalogo/PorNombre/Leche

Buscar por nombre de categoría
GET /api/v1/catalogo/PorNombreCategoria/{nombreCategoria}
Ejemplo
GET /api/v1/catalogo/PorNombreCategoria/LACTEOS

API Backups
Administra la creación y configuración de respaldos de la base de datos.
Base URL
/api/v1/backup
Crear respaldo manual
POST /api/v1/backup
Descripción
Genera un respaldo manual.
Respuesta
{
  "id":1,
  "tipo":"MANUAL",
  "fecha":"2026-07-14T18:00:00"
}

Listar respaldos
GET /api/v1/backup
Devuelve todos los respaldos registrados.

Obtener último respaldo
GET /api/v1/backup/ultimo
Obtiene el respaldo más reciente.

Activar respaldo automático
PUT /api/v1/backup/activar

Desactivar respaldo automático
PUT /api/v1/backup/desactivar

Obtener configuración
GET /api/v1/backup/config
Devuelve la configuración actual del servicio de respaldo.

API Monitor
Permite monitorear el estado de los microservicios.
Base URL
/api/v1/monitor

Registrar servicio
POST /api/v1/monitor/servicio
Ejemplo
{
    "nombre":"Inventario",
    "url":"http://localhost:8090"
}

Documentación de APIs
Descripción General
El sistema está compuesto por varios microservicios Spring Boot que se comunican mediante REST.
API
Función
Inventario
Administración de productos, categorías y stock
Catálogo
Consulta de productos disponibles
Monitor
Estado y monitoreo de microservicios
Backups
Gestión y configuración de respaldos


API Inventario
Base URL
/api/v1/inventario
Categorías
Crear categoría
POST /categorias
Crea una nueva categoría.
Body
{
    "nombre":"Lácteos"
}
Respuesta
201 Created

Listar categorías
GET /categorias
Obtiene todas las categorías.
Respuesta
[
   {
      "id":1,
      "nombre":"Lácteos"
   }
]

Obtener categoría
GET /categorias/{id}
Ejemplo
GET /categorias/3

Actualizar categoría
PUT /categorias/{id}
Body
{
    "nombre":"Bebidas"
}

Eliminar categoría
DELETE /categorias/{id}
Respuesta
204 No Content

Logs
Listar logs
GET /logs
Devuelve todos los registros de auditoría.

Registrar log
POST /logs
Body
{
    "accion":"Actualizar Stock",
    "usuario":"Administrador"
}

Productos
En el controlador también aparecen endpoints para productos (el archivo continúa después del fragmento mostrado), incluyendo operaciones típicas como:
listar productos
obtener producto por ID
registrar producto
actualizar producto
eliminar producto
administrar stock
Estos puedo documentarlos completos revisando el resto del controlador.

API Catálogo
Base URL
/api/v1/catalogo
Ver catálogo
GET /ver
Obtiene todos los productos.

Ver productos disponibles
GET /ver/disponibles
Retorna únicamente productos disponibles para la venta.

Buscar por categoría
GET /PorCategoria/{idCategoria}
Ejemplo
GET /PorCategoria/2

Buscar por nombre
GET /PorNombre/{nombre}
Ejemplo
GET /PorNombre/Leche

Buscar por nombre de categoría
GET /PorNombreCategoria/{nombreCategoria}
Ejemplo
GET /PorNombreCategoria/Lácteos

API Monitor
Base URL
/api/v1/monitor
Registrar servicio
POST /servicio
Registra un nuevo microservicio para ser monitoreado.

Obtener estado de todos los servicios
GET /
Respuesta
[
   {
      "nombre":"Inventario",
      "estado":"ONLINE"
   }
]

Obtener estado de un servicio
GET /{nombre}
Ejemplo
GET /Inventario

Historial general
GET /historial
Devuelve el historial de monitoreo.

Historial de un servicio
GET /historial/{nombre}

Estadísticas
GET /estadisticas
Entrega estadísticas del monitoreo.

Ejecutar monitoreo manual
POST /ejecutar
Respuesta
Monitoreo ejecutado correctamente

API Backups
Base URL
/api/v1/backup
Crear respaldo
POST /
Genera un respaldo manual de la base de datos.

Listar respaldos
GET /
Obtiene todos los respaldos registrados.

Último respaldo
GET /ultimo
Obtiene el último respaldo generado.

Activar respaldos automáticos
PUT /activar

Desactivar respaldos automáticos
PUT /desactivar

Obtener configuración
GET /config
Devuelve la configuración actual del sistema de respaldos.

Tecnologías utilizadas
Java 21
Spring Boot
Spring Web
Spring Data JPA
Hibernate
Maven
MySQL
OpenAPI / Swagger (en Inventario se observan anotaciones de documentación)
Arquitectura basada en microservicios
