import { Routes } from '@angular/router';
import { PanelComponent } from './views/panel/panel.component';
import { EntidadComponent } from './views/entidad/entidad.component';
import { MapasComponent } from './views/mapas/mapas.component';
import { LoginComponent } from './views/login/login.component';
import { MisDatosComponent } from './views/mis-datos/mis-datos.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: PanelComponent, canActivate: [authGuard] },
  { path: 'gestion/:recurso', component: EntidadComponent, canActivate: [authGuard, adminGuard] },
  { path: 'mis/:tipo', component: MisDatosComponent, canActivate: [authGuard] },
  { path: 'geocodificador', component: MapasComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
