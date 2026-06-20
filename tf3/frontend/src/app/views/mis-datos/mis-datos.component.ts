import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription, forkJoin } from 'rxjs';
import { ApiService } from '../../services/api.service';

interface ColumnaTabla {
  clave: string;
  etiqueta: string;
}

@Component({
  selector: 'app-mis-datos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mis-datos.component.html',
  styleUrl: './mis-datos.component.css'
})
export class MisDatosComponent implements OnInit, OnDestroy {
  tipo = 'reportes';
  titulo = 'Mis datos';
  descripcion = '';
  cargando = true;
  mensajeError = '';
  registros: any[] = [];
  detallesRuta: any[] = [];
  modalReporte = false;
  editandoReporteId: number | null = null;
  formularioReporte = { descripcion: '' };

  columnas: ColumnaTabla[] = [];

  private sub?: Subscription;

  constructor(private route: ActivatedRoute, private api: ApiService) {}

  ngOnInit(): void {
    this.sub = this.route.paramMap.subscribe(params => {
      this.tipo = params.get('tipo') || 'reportes';
      this.configurarVista();
      this.cargar();
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  configurarVista(): void {
    const vistas: Record<string, { titulo: string; descripcion: string; columnas: ColumnaTabla[] }> = {
      reportes: {
        titulo: 'Mis reportes',
        descripcion: 'Reportes asociados únicamente a tu usuario.',
        columnas: [
          { clave: 'id', etiqueta: 'ID' },
          { clave: 'descripcion', etiqueta: 'Descripción' },
          { clave: 'estado', etiqueta: 'Estado' },
          { clave: 'fecha', etiqueta: 'Fecha' },
          { clave: 'ubicacion.direccion', etiqueta: 'Ubicación' },
          { clave: 'empresa.nombre', etiqueta: 'Empresa asignada' },
          { clave: 'ruta.id', etiqueta: 'Ruta' }
        ]
      },
      rutas: {
        titulo: 'Mis rutas',
        descripcion: 'Rutas vinculadas a tus reportes asignados.',
        columnas: [
          { clave: 'id', etiqueta: 'ID' },
          { clave: 'fecha', etiqueta: 'Fecha' },
          { clave: 'estado', etiqueta: 'Estado' },
          { clave: 'empresa.nombre', etiqueta: 'Empresa' },
          { clave: 'vehiculo.placa', etiqueta: 'Vehículo' }
        ]
      },
      notificaciones: {
        titulo: 'Mis notificaciones',
        descripcion: 'Mensajes enviados únicamente a tu usuario.',
        columnas: [
          { clave: 'id', etiqueta: 'ID' },
          { clave: 'mensaje', etiqueta: 'Mensaje' },
          { clave: 'leido', etiqueta: 'Leído' },
          { clave: 'fecha', etiqueta: 'Fecha' }
        ]
      },
      evidencias: {
        titulo: 'Mis evidencias',
        descripcion: 'Evidencias vinculadas a tus reportes.',
        columnas: [
          { clave: 'id', etiqueta: 'ID' },
          { clave: 'urlImagen', etiqueta: 'Imagen' },
          { clave: 'reporte.id', etiqueta: 'Reporte' }
        ]
      }
    };

    const config = vistas[this.tipo] || vistas['reportes'];
    this.titulo = config.titulo;
    this.descripcion = config.descripcion;
    this.columnas = config.columnas;
  }

  cargar(): void {
    this.cargando = true;
    this.mensajeError = '';
    this.registros = [];
    this.detallesRuta = [];

    if (this.tipo === 'reportes') {
      this.api.misReportes().subscribe(this.manejarRespuesta());
      return;
    }

    if (this.tipo === 'rutas') {
      forkJoin({ rutas: this.api.misRutas(), detalles: this.api.misDetallesRuta() }).subscribe({
        next: ({ rutas, detalles }) => {
          this.registros = rutas;
          this.detallesRuta = detalles;
          this.cargando = false;
        },
        error: () => this.mostrarError()
      });
      return;
    }

    if (this.tipo === 'notificaciones') {
      this.api.misNotificaciones().subscribe(this.manejarRespuesta());
      return;
    }

    if (this.tipo === 'evidencias') {
      this.api.misEvidencias().subscribe(this.manejarRespuesta());
      return;
    }

    this.api.misReportes().subscribe(this.manejarRespuesta());
  }

  abrirNuevoReporte(): void {
    this.editandoReporteId = null;
    this.formularioReporte = { descripcion: '' };
    this.modalReporte = true;
  }

  abrirEdicionReporte(reporte: any): void {
    this.editandoReporteId = reporte.id;
    this.formularioReporte = { descripcion: reporte.descripcion || '' };
    this.modalReporte = true;
  }

  guardarReporte(): void {
    this.mensajeError = '';
    const datos = { descripcion: this.formularioReporte.descripcion };
    const peticion = this.editandoReporteId
      ? this.api.actualizarMiReporte(this.editandoReporteId, datos)
      : this.api.crearMiReporte(datos);

    peticion.subscribe({
      next: () => {
        this.modalReporte = false;
        this.cargar();
      },
      error: () => {
        this.mensajeError = 'No se pudo guardar el reporte. Revisa la descripción e inténtalo nuevamente.';
      }
    });
  }

  eliminarReporte(id: number): void {
    if (!confirm('¿Seguro que deseas eliminar este reporte?')) return;
    this.api.eliminarMiReporte(id).subscribe({
      next: () => this.cargar(),
      error: () => {
        this.mensajeError = 'No se pudo eliminar el reporte.';
      }
    });
  }

  marcarLeida(id: number): void {
    this.api.marcarNotificacionLeida(id).subscribe({
      next: () => this.cargar(),
      error: () => {
        this.mensajeError = 'No se pudo marcar la notificación como leída.';
      }
    });
  }

  valor(registro: any, ruta: string): string {
    const partes = ruta.split('.');
    let valor = registro;
    for (const parte of partes) {
      valor = valor?.[parte];
    }

    if (valor === null || valor === undefined || valor === '') return '—';
    if (typeof valor === 'boolean') return valor ? 'Sí' : 'No';
    if (ruta.toLowerCase().includes('fecha')) return this.formatearFecha(valor);
    if (ruta === 'ruta.id' || ruta === 'reporte.id') return `#${valor}`;
    return String(valor);
  }

  formatearFecha(fecha: string): string {
    return fecha ? new Date(fecha).toLocaleString('es-PE') : '—';
  }

  clasePorEstado(estado: string): string {
    return 'etiqueta-estado estado-' + String(estado || 'pendiente').toLowerCase().replace(/_/g, '-');
  }

  private manejarRespuesta() {
    return {
      next: (datos: any[]) => {
        this.registros = datos;
        this.cargando = false;
      },
      error: () => this.mostrarError()
    };
  }

  private mostrarError(): void {
    this.mensajeError = 'No se pudo cargar la información. Verifica tu sesión o vuelve a iniciar sesión.';
    this.cargando = false;
  }
}
