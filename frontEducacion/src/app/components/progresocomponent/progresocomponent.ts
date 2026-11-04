import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-progresocomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './progresocomponent.html',
  styleUrl: './progresocomponent.css',
})
export class Progresocomponent {}
