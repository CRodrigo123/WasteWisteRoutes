# Waste Wise Routes — Backend API REST

Plataforma para la gestión de residuos sólidos en Lima Metropolitana.  
Desarrollada con **Spring Boot 3.2 + MySQL + Google Maps API**.

---

## Tecnologías

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL 8
- Google Maps Geocoding & Distance Matrix API
- Desplegado en Railway

---

## Requisitos locales

- Java 17 instalado
- MySQL 8 corriendo localmente
- Maven instalado

---

## Configuración local

### 1. Crear la base de datos en MySQL

```sql
CREATE DATABASE wastewiseroutes CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar credenciales

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wastewiseroutes?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
google.maps.api.key=TU_API_KEY_DE_GOOGLE
```

### 3. Ejecutar

```bash
mvn spring-boot:run
```

La API corre en: `http://localhost:8080/grupo3/WasteWiseRoutes`

---

## Endpoints disponibles

| Entidad            | GET (listar) | GET (por id) | POST (crear) | PUT (actualizar) | DELETE (eliminar) |
|--------------------|---|---|---|---|---|
| Empresa            | /Empresa | /Empresa/{id} | /Empresa | /Empresa/{id} | /Empresa/{id} |
| Vehiculo           | /Vehiculo | /Vehiculo/{id} | /Vehiculo | /Vehiculo/{id} | /Vehiculo/{id} |
| Ruta               | /Ruta | /Ruta/{id} | /Ruta | /Ruta/{id} | /Ruta/{id} |
| RutaDetalle        | /RutaDetalle | /RutaDetalle/{id} | /RutaDetalle | /RutaDetalle/{id} | /RutaDetalle/{id} |
| Ubicacion          | /Ubicacion | /Ubicacion/{id} | /Ubicacion | /Ubicacion/{id} | /Ubicacion/{id} |
| Usuario            | /Usuario | /Usuario/{id} | /Usuario | /Usuario/{id} | /Usuario/{id} |
| Reporte            | /Reporte | /Reporte/{id} | /Reporte | /Reporte/{id} | /Reporte/{id} |
| Evidencia          | /Evidencia | /Evidencia/{id} | /Evidencia | /Evidencia/{id} | /Evidencia/{id} |
| Notificacion       | /Notificacion | /Notificacion/{id} | /Notificacion | /Notificacion/{id} | /Notificacion/{id} |
| ReporteEstadistico | /ReporteEstadistico | /ReporteEstadistico/{id} | /ReporteEstadistico | /ReporteEstadistico/{id} | /ReporteEstadistico/{id} |

### Google Maps (servicio externo)

| Endpoint | Descripción | Parámetros |
|---|---|---|
| GET /maps/geocodificar | Convierte dirección a coordenadas | `?direccion=Av. Javier Prado Lima` |
| GET /maps/geocodificar-inverso | Convierte coordenadas a dirección | `?latitud=-12.08&longitud=-77.03` |
| GET /maps/distancia | Calcula distancia entre dos puntos | `?origen=Miraflores&destino=SJL` |

---

## Despliegue en Railway

### Variables de entorno requeridas en Railway:

| Variable | Valor |
|---|---|
| `MYSQL_URL` | URL de tu base de datos MySQL en Railway |
| `MYSQL_USER` | Usuario de la BD |
| `MYSQL_PASSWORD` | Contraseña de la BD |
| `GOOGLE_MAPS_API_KEY` | Tu API Key de Google Maps |
| `PORT` | Railway la asigna automáticamente |

### Pasos:

1. Sube el proyecto a GitHub
2. En Railway → New Project → Deploy from GitHub repo
3. Agrega un plugin MySQL en Railway
4. Configura las variables de entorno arriba
5. Railway buildea y despliega automáticamente

---

## Integrantes

- Gian Marco Chávez Lopez — U202319880
- Rodrigo Cuba Soldevilla — U202313664
- Renzo Jesus Cañari Blas — U202218244
- Esteban Kamil Lopez Quiroz — U202320612

**Curso:** Arquitectura de Aplicaciones Web — UPC  
**Startup:** Nature's Next
