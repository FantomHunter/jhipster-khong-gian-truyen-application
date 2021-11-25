import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResourceDownload } from '../resource-download.model';
import { ResourceDownloadService } from '../service/resource-download.service';

@Component({
  templateUrl: './resource-download-delete-dialog.component.html',
})
export class ResourceDownloadDeleteDialogComponent {
  resourceDownload?: IResourceDownload;

  constructor(protected resourceDownloadService: ResourceDownloadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourceDownloadService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
