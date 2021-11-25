import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResourceDownload } from '../resource-download.model';

@Component({
  selector: 'jhi-resource-download-detail',
  templateUrl: './resource-download-detail.component.html',
})
export class ResourceDownloadDetailComponent implements OnInit {
  resourceDownload: IResourceDownload | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceDownload }) => {
      this.resourceDownload = resourceDownload;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
