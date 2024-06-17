import {Component, Input, OnInit} from '@angular/core';
import {AuthorizationService} from '../auth/service/authorization.service';
import {UserData} from '../auth/shared/user-data-type';

@Component({
  selector: 'navigation-board',
  templateUrl: './navigation-board.component.html',
  styleUrls: ['./navigation-board.component.scss']
})
export class NavigationBoardComponent implements OnInit {
  @Input() user!: UserData;

  isLoggedIn$ = this.authorizationService.isLoggedIn$;

  constructor(
    private authorizationService: AuthorizationService
  ) {
  }

  ngOnInit(): void {
    this.authorizationService.getUser().subscribe((user) => {
      this.user = user || new UserData('', '', '', '', '', '', '', 0, 0);
    });
  }

  logout(): void {
    this.authorizationService.logout();
  }
}
