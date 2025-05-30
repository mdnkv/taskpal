import {Component, input, output} from '@angular/core';

import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';

import {WorkspaceUser} from '../../models/workspace.models';

@Component({
  selector: 'app-workspace-card',
  imports: [MatCardModule, MatButtonModule],
  templateUrl: './workspace-card.component.html',
  styleUrl: './workspace-card.component.css'
})
export class WorkspaceCardComponent {

  workspaceUser = input.required<WorkspaceUser>()
  onSetActive = output<string>()

  setActiveWorkspace(){
    this.onSetActive.emit(this.workspaceUser().id!)
  }

}
