import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';

import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';

import {WorkspaceService} from '../../services/workspace.service';
import {Workspace} from '../../models/workspace.models';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-workspace-view',
  imports: [ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatSlideToggleModule],
  templateUrl: './create-workspace-view.component.html',
  styleUrl: './create-workspace-view.component.css'
})
export class CreateWorkspaceViewComponent {

  formBuilder: FormBuilder = inject(FormBuilder)
  workspaceService: WorkspaceService = inject(WorkspaceService)
  router: Router = inject(Router)

  createWorkspaceForm: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
    personal: [true, [Validators.required]]
  })

  submit(){
    const workspace: Workspace = {
      name: this.createWorkspaceForm.get('name')?.value,
      personal: this.createWorkspaceForm.get('personal')?.value
    }

    this.workspaceService.createWorkspace(workspace).subscribe({
      next: result => {
        console.log(result)
        // Clear the form
        this.createWorkspaceForm.reset()
        // Navigate to the workspaces view
        this.router.navigateByUrl('/workspaces')
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
      }
    })

  }

}
