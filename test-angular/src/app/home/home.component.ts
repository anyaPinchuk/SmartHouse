import { Component, OnInit } from '@angular/core';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    $(document).ready(function(){
      $('.slider').slider();
    });
//     $('.slider').slider('pause');
// // Start slider
//     $('.slider').slider('start');
// // Next slide
//     $('.slider').slider('next');
// // Previous slide
//     $('.slider').slider('prev');
  }

}
