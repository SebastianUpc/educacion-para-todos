import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-evaluacioncomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './evaluacioncomponent.html',
  styleUrl: './evaluacioncomponent.css',
})
export class Evaluacioncomponent {}
