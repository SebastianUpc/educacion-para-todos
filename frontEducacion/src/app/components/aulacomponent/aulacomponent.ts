import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-aulacomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './aulacomponent.html',
  styleUrl: './aulacomponent.css',
})
export class Aulacomponent {}
