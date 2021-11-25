import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResourceDownloadComponent } from '../list/resource-download.component';
import { ResourceDownloadDetailComponent } from '../detail/resource-download-detail.component';
import { ResourceDownloadUpdateComponent } from '../update/resource-download-update.component';
import { ResourceDownloadRoutingResolveService } from './resource-download-routing-resolve.service';

const resourceDownloadRoute: Routes = [
  {
    path: '',
    component: ResourceDownloadComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceDownloadDetailComponent,
    resolve: {
      resourceDownload: ResourceDownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceDownloadUpdateComponent,
    resolve: {
      resourceDownload: ResourceDownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceDownloadUpdateComponent,
    resolve: {
      resourceDownload: ResourceDownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resourceDownloadRoute)],
  exports: [RouterModule],
})
export class ResourceDownloadRoutingModule {}
