import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Menucomponent } from '../menucomponent/menucomponent';

@Component({
  selector: 'app-temacomponent',
  imports: [Menucomponent, RouterOutlet],
  templateUrl: './temacomponent.html',
  styleUrl: './temacomponent.css',
})
export class Temacomponent {}
