import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';

import {WorkspaceUser} from '../models/workspace.models';

@Injectable({
  providedIn: 'root'
})
export class WorkspaceUserService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getActiveWorkspaceUser(): Observable<WorkspaceUser> {
    return this.http.get<WorkspaceUser>(`${this.serverUrl}/workspaces/users/active`)
  }

  setActiveWorkspace(id: string): Observable<WorkspaceUser>{
    return this.http.post<WorkspaceUser>(`${this.serverUrl}/workspaces/users/active/${id}`, {})
  }

  getWorkspaces(): Observable<WorkspaceUser[]>{
    return this.http.get<WorkspaceUser[]>(`${this.serverUrl}/workspaces/users/my`)
  }

}
