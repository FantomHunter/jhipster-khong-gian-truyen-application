import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDownload, Download } from '../download.model';
import { DownloadService } from '../service/download.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IResourceDownload } from 'app/entities/resource-download/resource-download.model';
import { ResourceDownloadService } from 'app/entities/resource-download/service/resource-download.service';

@Component({
  selector: 'jhi-download-update',
  templateUrl: './download-update.component.html',
})
export class DownloadUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  resourceDownloadsSharedCollection: IResourceDownload[] = [];

  editForm = this.fb.group({
    id: [],
    downloadDate: [],
    user: [],
    resource: [],
  });

  constructor(
    protected downloadService: DownloadService,
    protected userService: UserService,
    protected resourceDownloadService: ResourceDownloadService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ download }) => {
      if (download.id === undefined) {
        const today = dayjs().startOf('day');
        download.downloadDate = today;
      }

      this.updateForm(download);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const download = this.createFromForm();
    if (download.id !== undefined) {
      this.subscribeToSaveResponse(this.downloadService.update(download));
    } else {
      this.subscribeToSaveResponse(this.downloadService.create(download));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackResourceDownloadById(index: number, item: IResourceDownload): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDownload>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(download: IDownload): void {
    this.editForm.patchValue({
      id: download.id,
      downloadDate: download.downloadDate ? download.downloadDate.format(DATE_TIME_FORMAT) : null,
      user: download.user,
      resource: download.resource,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, download.user);
    this.resourceDownloadsSharedCollection = this.resourceDownloadService.addResourceDownloadToCollectionIfMissing(
      this.resourceDownloadsSharedCollection,
      download.resource
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.resourceDownloadService
      .query()
      .pipe(map((res: HttpResponse<IResourceDownload[]>) => res.body ?? []))
      .pipe(
        map((resourceDownloads: IResourceDownload[]) =>
          this.resourceDownloadService.addResourceDownloadToCollectionIfMissing(resourceDownloads, this.editForm.get('resource')!.value)
        )
      )
      .subscribe((resourceDownloads: IResourceDownload[]) => (this.resourceDownloadsSharedCollection = resourceDownloads));
  }

  protected createFromForm(): IDownload {
    return {
      ...new Download(),
      id: this.editForm.get(['id'])!.value,
      downloadDate: this.editForm.get(['downloadDate'])!.value
        ? dayjs(this.editForm.get(['downloadDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      resource: this.editForm.get(['resource'])!.value,
    };
  }
}
