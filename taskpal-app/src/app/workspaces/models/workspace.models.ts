export interface Workspace {
  id?: string
  name: string
  personal: boolean
}

export interface WorkspaceUser {
  id?: string
  workspace: Workspace
  active: boolean
}
