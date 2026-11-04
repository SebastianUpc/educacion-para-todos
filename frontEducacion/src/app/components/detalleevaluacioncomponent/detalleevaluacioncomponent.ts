import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-detalleevaluacioncomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './detalleevaluacioncomponent.html',
  styleUrl: './detalleevaluacioncomponent.css',
})
export class Detalleevaluacioncomponent {}
