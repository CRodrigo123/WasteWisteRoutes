import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { ApiService } from '../../services/api.service';
import { AuthService, UsuarioSesion } from '../../services/auth.service';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './panel.component.html',
  styleUrl: './panel.component.css'
})
export class PanelComponent implements OnInit {
  cargando = true;
  errorConexion = false;
  totales: Record<string, number> = {};
  reportesRecientes: any[] = [];
  rutasMias: any[] = [];
  notificacionesMias: any[] = [];
  usuario: UsuarioSesion | null = null;

  tarjetasAdmin = [
    { recurso: 'Reporte', etiqueta: 'Reportes ciudadanos' },
    { recurso: 'Ruta', etiqueta: 'Rutas registradas' },
    { recurso: 'Vehiculo', etiqueta: 'Vehículos en flota' },
    { recurso: 'Usuario', etiqueta: 'Usuarios' },
    { recurso: 'Empresa', etiqueta: 'Empresas' },
    { recurso: 'Ubicacion', etiqueta: 'Puntos georreferenciados' }
  ];

  tarjetasCliente = [
    { recurso: 'Reporte', etiqueta: 'Mis reportes' },
    { recurso: 'Ruta', etiqueta: 'Mis rutas' },
    { recurso: 'Notificacion', etiqueta: 'Mis notificaciones' }
  ];

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit(): void {
    this.usuario = this.auth.getUsuarioActual();
    if (this.auth.esAdmin()) {
      this.cargarPanelAdmin();
    } else {
      this.cargarPanelCliente();
    }
  }

  cargarPanelAdmin(): void {
    const peticiones = this.tarjetasAdmin.map(t => this.api.listar(t.recurso));
    forkJoin(peticiones).subscribe({
      next: (resultados) => {
        this.tarjetasAdmin.forEach((t, i) => { this.totales[t.recurso] = resultados[i].length; });
        this.reportesRecientes = [...resultados[0]].slice(-5).reverse();
        this.cargando = false;
      },
      error: () => {
        this.errorConexion = true;
        this.cargando = false;
      }
    });
  }

  cargarPanelCliente(): void {
    forkJoin({
      reportes: this.api.misReportes(),
      rutas: this.api.misRutas(),
      notificaciones: this.api.misNotificaciones()
    }).subscribe({
      next: ({ reportes, rutas, notificaciones }) => {
        this.totales['Reporte'] = reportes.length;
        this.totales['Ruta'] = rutas.length;
        this.totales['Notificacion'] = notificaciones.length;
        this.reportesRecientes = [...reportes].slice(-5).reverse();
        this.rutasMias = rutas;
        this.notificacionesMias = notificaciones.filter(n => !n.leido).slice(0, 5);
        this.cargando = false;
      },
      error: () => {
        this.errorConexion = true;
        this.cargando = false;
      }
    });
  }

  formatearFecha(f: string): string {
    return f ? new Date(f).toLocaleString('es-PE') : '—';
  }

  clasePorEstado(e: string): string {
    return 'etiqueta-estado estado-' + String(e || 'pendiente').toLowerCase().replace(/_/g, '-');
  }
}
