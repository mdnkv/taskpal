import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {WorkspaceUserService} from '../../../workspaces/services/workspace-user.service';

@Component({
  selector: 'app-dashboard-view',
  imports: [],
  templateUrl: './dashboard-view.component.html',
  styleUrl: './dashboard-view.component.css'
})
export class DashboardViewComponent implements OnInit {

  workspaceUserService: WorkspaceUserService = inject(WorkspaceUserService)
  router: Router = inject(Router)

  ngOnInit() {
    this.workspaceUserService.getActiveWorkspaceUser().subscribe({
      next: result => {
        console.log(result)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        if (err.status == 404){
          this.router.navigateByUrl('/workspaces/create')
        }
      }
    })
  }

}
