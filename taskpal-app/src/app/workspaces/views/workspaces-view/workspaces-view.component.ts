import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {MatButtonModule} from '@angular/material/button';

import {WorkspaceUser} from '../../models/workspace.models';
import {WorkspaceUserService} from '../../services/workspace-user.service';
import {WorkspaceCardComponent} from '../../components/workspace-card/workspace-card.component';

@Component({
  selector: 'app-workspaces-view',
  imports: [RouterLink, WorkspaceCardComponent, MatButtonModule],
  templateUrl: './workspaces-view.component.html',
  styleUrl: './workspaces-view.component.css'
})
export class WorkspacesViewComponent implements OnInit{

  workspaces: WorkspaceUser[] = []

  workspaceUserService: WorkspaceUserService = inject(WorkspaceUserService)

  ngOnInit() {
    this.workspaceUserService.getWorkspaces().subscribe({
      next: result => {
        this.workspaces = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  onSetActive(id: string){
    this.workspaceUserService.setActiveWorkspace(id).subscribe({
      next: result => {
        console.log(result)
        this.workspaces = this.workspaces.map(workspace => {
          if (workspace.id == id){
            workspace.active = true
          } else {
            workspace.active = false
          }
          return workspace
        })
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

}
