import { Routes } from '@angular/router';

import {DashboardViewComponent} from './dashboard/views/dashboard-view/dashboard-view.component';
import {CreateWorkspaceViewComponent} from './workspaces/views/create-workspace-view/create-workspace-view.component';
import {WorkspacesViewComponent} from './workspaces/views/workspaces-view/workspaces-view.component';

export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardViewComponent
  },
  {
    path: 'workspaces/create',
    component: CreateWorkspaceViewComponent
  },
  {
    path: 'workspaces',
    component: WorkspacesViewComponent
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/dashboard'
  }
];
