import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResourceDownloadComponent } from './list/resource-download.component';
import { ResourceDownloadDetailComponent } from './detail/resource-download-detail.component';
import { ResourceDownloadUpdateComponent } from './update/resource-download-update.component';
import { ResourceDownloadDeleteDialogComponent } from './delete/resource-download-delete-dialog.component';
import { ResourceDownloadRoutingModule } from './route/resource-download-routing.module';

@NgModule({
  imports: [SharedModule, ResourceDownloadRoutingModule],
  declarations: [
    ResourceDownloadComponent,
    ResourceDownloadDetailComponent,
    ResourceDownloadUpdateComponent,
    ResourceDownloadDeleteDialogComponent,
  ],
  entryComponents: [ResourceDownloadDeleteDialogComponent],
})
export class ResourceDownloadModule {}
