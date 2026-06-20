import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  modo: 'login' | 'registro' = 'login';
  cargando = false;
  mensajeError = '';

  formulario = {
    nombre: '',
    correo: '',
    password: '',
    tipo: 'CLIENTE'
  };

  constructor(private auth: AuthService, private router: Router) {}

  enviar(): void {
    this.cargando = true;
    this.mensajeError = '';

    const peticion = this.modo === 'login'
      ? this.auth.login({ correo: this.formulario.correo, password: this.formulario.password })
      : this.auth.registrar({
          nombre: this.formulario.nombre,
          correo: this.formulario.correo,
          password: this.formulario.password,
          tipo: this.formulario.tipo
        });

    peticion.subscribe({
      next: () => {
        this.cargando = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.cargando = false;
        this.mensajeError = err?.error?.message || 'No se pudo iniciar sesión. Revisa tus datos.';
      }
    });
  }

  cambiarModo(): void {
    this.mensajeError = '';
    this.modo = this.modo === 'login' ? 'registro' : 'login';
  }
}
