import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription, forkJoin } from 'rxjs';
import { ApiService } from '../../services/api.service';
import { ENTIDADES, EntidadConfig, CampoConfig } from '../../config/entidades';

@Component({
  selector: 'app-entidad',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './entidad.component.html',
  styleUrl: './entidad.component.css'
})
export class EntidadComponent implements OnInit, OnDestroy {
  recurso = '';
  config!: EntidadConfig;
  registros: any[] = [];
  relaciones: Record<string, any[]> = {};
  cargando = true;
  mensajeError = '';
  modalAbierto = false;
  editandoId: number | null = null;
  formulario: Record<string, any> = {};
  confirmandoId: number | null = null;

  private sub?: Subscription;

  constructor(private route: ActivatedRoute, private api: ApiService) {}

  ngOnInit(): void {
    this.sub = this.route.paramMap.subscribe(params => {
      this.recurso = params.get('recurso') || '';
      this.config = ENTIDADES[this.recurso];
      if (this.config) { this.cargar(); }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  get camposTabla(): CampoConfig[] {
    return this.config.campos.filter(c => !c.ocultarEnTabla);
  }

  cargar(): void {
    this.cargando = true;
    this.mensajeError = '';
    this.registros = [];
    this.api.listar(this.recurso).subscribe({
      next: (datos) => {
        this.registros = datos;
        const camposRelacion = this.config.campos.filter(c => c.tipo === 'relacion');
        if (camposRelacion.length === 0) { this.cargando = false; return; }
        forkJoin(camposRelacion.map(c => this.api.listar(c.recurso!))).subscribe({
          next: (resultados) => {
            camposRelacion.forEach((c, i) => { this.relaciones[c.clave] = resultados[i]; });
            this.cargando = false;
          },
          error: () => { this.cargando = false; }
        });
      },
      error: () => {
        this.mensajeError = 'No se pudo cargar la información. Verifica que el backend esté activo.';
        this.cargando = false;
      }
    });
  }

  abrirNuevo(): void {
    this.editandoId = null;
    this.mensajeError = '';
    this.formulario = {};
    this.config.campos.forEach(c => {
      this.formulario[c.clave] = c.tipo === 'booleano' ? false : c.tipo === 'relacion' ? null : '';
    });
    this.modalAbierto = true;
  }

  abrirEdicion(registro: any): void {
    this.editandoId = registro.id;
    this.mensajeError = '';
    this.formulario = {};
    this.config.campos.forEach(c => {
      if (c.tipo === 'relacion') {
        this.formulario[c.clave] = registro[c.clave]?.id ?? null;
      } else {
        this.formulario[c.clave] = registro[c.clave] ?? (c.tipo === 'booleano' ? false : '');
      }
    });
    this.modalAbierto = true;
  }

  guardar(): void {
    this.mensajeError = '';
    const datos: Record<string, any> = {};
    this.config.campos.forEach(c => {
      const valor = this.formulario[c.clave];
      if (c.tipo === 'relacion') {
        datos[c.clave] = valor ? { id: Number(valor) } : null;
      } else if (c.tipo === 'numero') {
        datos[c.clave] = valor === '' || valor === null ? null : Number(valor);
      } else {
        datos[c.clave] = valor === '' ? null : valor;
      }
    });

    const peticion = this.editandoId
      ? this.api.actualizar(this.recurso, this.editandoId, datos)
      : this.api.crear(this.recurso, datos);

    peticion.subscribe({
      next: () => { this.modalAbierto = false; this.cargar(); },
      error: (err) => {
        this.mensajeError = 'No se pudo guardar. Revisa los campos obligatorios e inténtalo de nuevo.';
        console.error(err);
      }
    });
  }

  eliminar(id: number): void {
    this.api.eliminar(this.recurso, id).subscribe({
      next: () => { this.confirmandoId = null; this.cargar(); },
      error: () => {
        this.mensajeError = 'No se pudo eliminar. El registro puede estar vinculado a otros datos.';
        this.confirmandoId = null;
      }
    });
  }

  mostrarCelda(registro: any, campo: CampoConfig): string {
    const valor = registro[campo.clave];
    if (valor === null || valor === undefined || valor === '') return '—';
    if (campo.tipo === 'relacion') return valor[campo.mostrar!] ?? '#' + valor.id;
    if (campo.tipo === 'booleano') return valor ? 'Sí' : 'No';
    return String(valor);
  }

  tipoInput(c: CampoConfig): string {
    const map: Record<string, string> = {
      numero: 'number', fecha: 'date', correo: 'email', password: 'password'
    };
    return map[c.tipo] || 'text';
  }

  clasePorEstado(e: string): string {
    return 'etiqueta-estado estado-' + String(e || '').toLowerCase().replace(/_/g, '-');
  }

  esInputSimple(c: CampoConfig): boolean {
    return ['texto', 'numero', 'fecha', 'correo', 'password'].includes(c.tipo);
  }
}
