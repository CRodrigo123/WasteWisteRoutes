import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ENTIDADES, GRUPOS_MENU } from './config/entidades';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  menuAbierto = false;
  entidades = ENTIDADES;
  grupos = GRUPOS_MENU;

  constructor(public auth: AuthService) {}

  cerrarSesion(): void {
    this.menuAbierto = false;
    this.auth.logout();
  }
}
