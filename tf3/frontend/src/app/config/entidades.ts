export interface CampoConfig {
  clave: string;
  etiqueta: string;
  tipo: 'texto' | 'textoLargo' | 'numero' | 'fecha' | 'correo' | 'password' | 'opciones' | 'relacion' | 'booleano';
  requerido?: boolean;
  opciones?: string[];
  recurso?: string;
  mostrar?: string;
  ocultarEnTabla?: boolean;
}

export interface EntidadConfig {
  titulo: string;
  singular: string;
  campos: CampoConfig[];
}

export const ENTIDADES: Record<string, EntidadConfig> = {
  Empresa: {
    titulo: 'Empresas',
    singular: 'empresa',
    campos: [
      { clave: 'nombre', etiqueta: 'Nombre', tipo: 'texto', requerido: true },
      { clave: 'ruc', etiqueta: 'RUC', tipo: 'texto', requerido: true },
      { clave: 'telefono', etiqueta: 'Teléfono', tipo: 'texto' },
      { clave: 'direccion', etiqueta: 'Dirección', tipo: 'textoLargo' }
    ]
  },
  Vehiculo: {
    titulo: 'Vehículos',
    singular: 'vehículo',
    campos: [
      { clave: 'placa', etiqueta: 'Placa', tipo: 'texto', requerido: true },
      { clave: 'capacidad', etiqueta: 'Capacidad (toneladas)', tipo: 'numero' },
      { clave: 'estado', etiqueta: 'Estado', tipo: 'opciones', opciones: ['ACTIVO', 'MANTENIMIENTO', 'INACTIVO'] },
      { clave: 'empresa', etiqueta: 'Empresa', tipo: 'relacion', recurso: 'Empresa', mostrar: 'nombre' }
    ]
  },
  Ruta: {
    titulo: 'Rutas',
    singular: 'ruta',
    campos: [
      { clave: 'fecha', etiqueta: 'Fecha', tipo: 'fecha' },
      { clave: 'estado', etiqueta: 'Estado', tipo: 'opciones', opciones: ['PROGRAMADA', 'EN_CURSO', 'COMPLETADA', 'CANCELADA'] },
      { clave: 'empresa', etiqueta: 'Empresa', tipo: 'relacion', recurso: 'Empresa', mostrar: 'nombre' },
      { clave: 'vehiculo', etiqueta: 'Vehículo', tipo: 'relacion', recurso: 'Vehiculo', mostrar: 'placa' }
    ]
  },
  RutaDetalle: {
    titulo: 'Detalles de Ruta',
    singular: 'detalle de ruta',
    campos: [
      { clave: 'orden', etiqueta: 'Orden de visita', tipo: 'numero', requerido: true },
      { clave: 'ruta', etiqueta: 'Ruta', tipo: 'relacion', recurso: 'Ruta', mostrar: 'id' },
      { clave: 'ubicacion', etiqueta: 'Ubicación', tipo: 'relacion', recurso: 'Ubicacion', mostrar: 'direccion' }
    ]
  },
  Ubicacion: {
    titulo: 'Ubicaciones',
    singular: 'ubicación',
    campos: [
      { clave: 'latitud', etiqueta: 'Latitud', tipo: 'numero', requerido: true },
      { clave: 'longitud', etiqueta: 'Longitud', tipo: 'numero', requerido: true },
      { clave: 'direccion', etiqueta: 'Dirección', tipo: 'textoLargo' }
    ]
  },
  Usuario: {
    titulo: 'Usuarios',
    singular: 'usuario',
    campos: [
      { clave: 'nombre', etiqueta: 'Nombre', tipo: 'texto', requerido: true },
      { clave: 'correo', etiqueta: 'Correo', tipo: 'correo', requerido: true },
      { clave: 'password', etiqueta: 'Contraseña', tipo: 'password', ocultarEnTabla: true },
      { clave: 'tipo', etiqueta: 'Tipo', tipo: 'opciones', opciones: ['CLIENTE', 'EMPRESA', 'CONDUCTOR', 'ADMINISTRADOR'] }
    ]
  },
  Reporte: {
    titulo: 'Reportes Ciudadanos',
    singular: 'reporte',
    campos: [
      { clave: 'descripcion', etiqueta: 'Descripción', tipo: 'textoLargo', requerido: true },
      { clave: 'estado', etiqueta: 'Estado', tipo: 'opciones', opciones: ['PENDIENTE', 'EN_ATENCION', 'RESUELTO'] },
      { clave: 'usuario', etiqueta: 'Usuario', tipo: 'relacion', recurso: 'Usuario', mostrar: 'nombre' },
      { clave: 'ubicacion', etiqueta: 'Ubicación', tipo: 'relacion', recurso: 'Ubicacion', mostrar: 'direccion' },
      { clave: 'empresa', etiqueta: 'Empresa asignada', tipo: 'relacion', recurso: 'Empresa', mostrar: 'nombre' }
    ]
  },
  Evidencia: {
    titulo: 'Evidencias',
    singular: 'evidencia',
    campos: [
      { clave: 'urlImagen', etiqueta: 'URL de la imagen', tipo: 'texto', requerido: true },
      { clave: 'reporte', etiqueta: 'Reporte', tipo: 'relacion', recurso: 'Reporte', mostrar: 'id' }
    ]
  },
  Notificacion: {
    titulo: 'Notificaciones',
    singular: 'notificación',
    campos: [
      { clave: 'mensaje', etiqueta: 'Mensaje', tipo: 'textoLargo', requerido: true },
      { clave: 'leido', etiqueta: 'Leído', tipo: 'booleano' },
      { clave: 'usuario', etiqueta: 'Usuario', tipo: 'relacion', recurso: 'Usuario', mostrar: 'nombre' }
    ]
  },
  ReporteEstadistico: {
    titulo: 'Reportes Estadísticos',
    singular: 'reporte estadístico',
    campos: [
      { clave: 'fechaInicio', etiqueta: 'Fecha inicio', tipo: 'fecha', requerido: true },
      { clave: 'fechaFin', etiqueta: 'Fecha fin', tipo: 'fecha', requerido: true },
      { clave: 'totalReportes', etiqueta: 'Total de reportes', tipo: 'numero' },
      { clave: 'ahorroCombustible', etiqueta: 'Ahorro de combustible (L)', tipo: 'numero' },
      { clave: 'empresa', etiqueta: 'Empresa', tipo: 'relacion', recurso: 'Empresa', mostrar: 'nombre' }
    ]
  }
};

export const GRUPOS_MENU = [
  { nombre: 'Operaciones', recursos: ['Reporte', 'Ruta', 'RutaDetalle', 'Ubicacion'] },
  { nombre: 'Flota y empresas', recursos: ['Empresa', 'Vehiculo'] },
  { nombre: 'Comunidad', recursos: ['Usuario', 'Notificacion', 'Evidencia'] },
  { nombre: 'Análisis', recursos: ['ReporteEstadistico'] }
];
