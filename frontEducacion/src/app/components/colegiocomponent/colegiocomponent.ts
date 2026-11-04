import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-colegiocomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './colegiocomponent.html',
  styleUrl: './colegiocomponent.css',
})
export class Colegiocomponent {}
