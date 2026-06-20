import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-mapas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './mapas.component.html',
  styleUrl: './mapas.component.css'
})
export class MapasComponent {
  direccion = '';
  resultadoGeo: any = null;
  lat: any = '';
  lng: any = '';
  resultadoInverso: string | null = null;
  origen = '';
  destino = '';
  resultadoDistancia: any = null;
  cargando = '';
  mensaje = '';

  constructor(private api: ApiService) {}

  buscarCoordenadas(): void {
    this.cargando = 'geo';
    this.mensaje = '';
    this.resultadoGeo = null;
    this.api.geocodificar(this.direccion).subscribe({
      next: (data) => {
        const r = data.results?.[0];
        if (r) {
          this.resultadoGeo = {
            direccion: r.formatted_address,
            latitud: r.geometry.location.lat,
            longitud: r.geometry.location.lng
          };
        } else {
          this.mensaje = 'Google Maps no encontró esa dirección. Intenta agregando el distrito.';
        }
        this.cargando = '';
      },
      error: () => {
        this.mensaje = 'Error al consultar Google Maps. Verifica la API key en el backend.';
        this.cargando = '';
      }
    });
  }

  buscarDireccion(): void {
    this.cargando = 'inverso';
    this.mensaje = '';
    this.resultadoInverso = null;
    this.api.geocodificarInverso(this.lat, this.lng).subscribe({
      next: (data) => {
        const r = data.results?.[0];
        this.resultadoInverso = r ? r.formatted_address : null;
        if (!r) this.mensaje = 'No se encontró una dirección para esas coordenadas.';
        this.cargando = '';
      },
      error: () => {
        this.mensaje = 'Error al consultar Google Maps. Verifica la API key en el backend.';
        this.cargando = '';
      }
    });
  }

  calcularDistancia(): void {
    this.cargando = 'distancia';
    this.mensaje = '';
    this.resultadoDistancia = null;
    this.api.distancia(this.origen, this.destino).subscribe({
      next: (data) => {
        const e = data.rows?.[0]?.elements?.[0];
        if (e?.status === 'OK') {
          this.resultadoDistancia = { distancia: e.distance.text, duracion: e.duration.text };
        } else {
          this.mensaje = 'No se pudo calcular la ruta entre esos puntos.';
        }
        this.cargando = '';
      },
      error: () => {
        this.mensaje = 'Error al consultar Google Maps. Verifica la API key en el backend.';
        this.cargando = '';
      }
    });
  }

  guardarComoUbicacion(): void {
    this.api.crear('Ubicacion', {
      latitud: this.resultadoGeo.latitud,
      longitud: this.resultadoGeo.longitud,
      direccion: this.resultadoGeo.direccion
    }).subscribe({
      next: () => { this.mensaje = 'Ubicación guardada en la base de datos.'; },
      error: () => { this.mensaje = 'No se pudo guardar la ubicación.'; }
    });
  }
}
