# Waste Wise Routes — Frontend (Angular 17)

Aplicación web en **Angular 17** integrada con la API REST de Waste Wise Routes (Spring Boot).
Interfaz responsive siguiendo las Style Guidelines del proyecto (Poppins/Roboto, verde #1B5E20, azul #25659A).

## Requisitos
- Node.js 18+
- Angular CLI 17  →  `npm install -g @angular/cli@17`
- El backend corriendo en `http://localhost:8080`

## Cómo ejecutar
```bash
npm install
ng serve
```
Abre en `http://localhost:4200`

El proxy (`proxy.conf.json`) redirige `/api/*` → `http://localhost:8080/grupo3/WasteWiseRoutes/*`,
así que no hay problemas de CORS en desarrollo.

## Qué incluye
- **Panel general**: métricas en vivo + tabla de últimos reportes.
- **CRUD de las 10 entidades**: Empresa, Vehículo, Ruta, Detalle de Ruta, Ubicación, Usuario, Reporte, Evidencia, Notificación, Reporte Estadístico. Listar, crear, editar, eliminar, con desplegables para relaciones.
- **Google Maps (servicio externo)**: geocodificación, geocodificación inversa, cálculo de distancias. Las coordenadas se pueden guardar como Ubicación.
- **Responsive**: menú lateral colapsable en móvil.

## Build de producción
```bash
ng build
```
Genera `dist/wastewise-frontend/`. Antes de desplegar, edita `src/environments/environment.prod.ts`
con la URL de tu backend en Railway.

## Integrantes
- Gian Marco Chávez Lopez — U202319880
- Rodrigo Cuba Soldevilla — U202313664
- Renzo Jesus Cañari Blas — U202218244
- Esteban Kamil Lopez Quiroz — U202320612

Curso: Arquitectura de Aplicaciones Web — UPC
