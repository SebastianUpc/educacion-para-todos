import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-accesodenegadocomponent',
  imports: [Menucomponent, MatButtonModule, RouterLink],
  templateUrl: './accesodenegadocomponent.html',
  styleUrl: './accesodenegadocomponent.css',
})
export class Accesodenegadocomponent {}
