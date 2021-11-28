import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResourceDownload, ResourceDownload } from '../resource-download.model';
import { ResourceDownloadService } from '../service/resource-download.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { FormatType } from 'app/entities/enumerations/format-type.model';

@Component({
  selector: 'jhi-resource-download-update',
  templateUrl: './resource-download-update.component.html',
})
export class ResourceDownloadUpdateComponent implements OnInit {
  isSaving = false;
  formatTypeValues = Object.keys(FormatType);

  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    url: [null, [Validators.required]],
    format: [],
    product: [null, Validators.required],
  });

  constructor(
    protected resourceDownloadService: ResourceDownloadService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceDownload }) => {
      this.updateForm(resourceDownload);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceDownload = this.createFromForm();
    if (resourceDownload.id !== undefined) {
      this.subscribeToSaveResponse(this.resourceDownloadService.update(resourceDownload));
    } else {
      this.subscribeToSaveResponse(this.resourceDownloadService.create(resourceDownload));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceDownload>>): void {
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

  protected updateForm(resourceDownload: IResourceDownload): void {
    this.editForm.patchValue({
      id: resourceDownload.id,
      url: resourceDownload.url,
      format: resourceDownload.format,
      product: resourceDownload.product,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      resourceDownload.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IResourceDownload {
    return {
      ...new ResourceDownload(),
      id: this.editForm.get(['id'])!.value,
      url: this.editForm.get(['url'])!.value,
      format: this.editForm.get(['format'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
