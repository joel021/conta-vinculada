import { Component, OnInit } from '@angular/core'
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-top-left-menu',
  templateUrl: './top-left-menu.component.html',
  styleUrls: ['./top-left-menu.component.css'],
})
export class TopLeftMenuComponent implements OnInit {
  displayModal = 'none';
  menu = 'home';
  user : any = null  
  mainNavBar = false;
  pageUrl:string = ''

  constructor(
    private authenticationService: UserService,
    private router: Router
  ) {
    this.pageUrl = router.url
  }

  ngOnInit() {
    if (this.authenticationService.currentUserValue == null) {
      this.router.navigate(['/login'])
    }
    this.user = this.authenticationService.currentUserValue
  }

  logout() {
    this.router.navigate(['/login'])
  }
}
