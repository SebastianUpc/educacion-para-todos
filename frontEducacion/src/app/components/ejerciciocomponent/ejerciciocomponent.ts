import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-ejerciciocomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './ejerciciocomponent.html',
  styleUrl: './ejerciciocomponent.css',
})
export class Ejerciciocomponent {}
