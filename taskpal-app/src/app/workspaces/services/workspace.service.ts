import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {Workspace} from '../models/workspace.models';

@Injectable({
  providedIn: 'root'
})
export class WorkspaceService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  createWorkspace(payload: Workspace): Observable<Workspace>{
    return this.http.post<Workspace>(`${this.serverUrl}/workspaces/create`, payload)
  }

}
