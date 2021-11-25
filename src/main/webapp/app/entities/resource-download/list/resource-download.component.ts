import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResourceDownload } from '../resource-download.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ResourceDownloadService } from '../service/resource-download.service';
import { ResourceDownloadDeleteDialogComponent } from '../delete/resource-download-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-resource-download',
  templateUrl: './resource-download.component.html',
})
export class ResourceDownloadComponent implements OnInit {
  resourceDownloads: IResourceDownload[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected resourceDownloadService: ResourceDownloadService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.resourceDownloads = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.resourceDownloadService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IResourceDownload[]>) => {
          this.isLoading = false;
          this.paginateResourceDownloads(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.resourceDownloads = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IResourceDownload): number {
    return item.id!;
  }

  delete(resourceDownload: IResourceDownload): void {
    const modalRef = this.modalService.open(ResourceDownloadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resourceDownload = resourceDownload;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateResourceDownloads(data: IResourceDownload[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.resourceDownloads.push(d);
      }
    }
  }
}
