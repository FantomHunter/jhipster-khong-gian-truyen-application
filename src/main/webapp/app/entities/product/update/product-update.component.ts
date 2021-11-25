import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { IAuthor } from 'app/entities/author/author.model';
import { AuthorService } from 'app/entities/author/service/author.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { Status } from 'app/entities/enumerations/status.model';
import { FormatType } from 'app/entities/enumerations/format-type.model';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);
  formatTypeValues = Object.keys(FormatType);

  authorsCollection: IAuthor[] = [];
  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    imageUrl: [],
    publishDate: [],
    status: [],
    type: [],
    totalChapter: [],
    author: [],
    categories: [],
  });

  constructor(
    protected productService: ProductService,
    protected authorService: AuthorService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      if (product.id === undefined) {
        const today = dayjs().startOf('day');
        product.publishDate = today;
      }

      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackAuthorById(index: number, item: IAuthor): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  getSelectedCategory(option: ICategory, selectedVals?: ICategory[]): ICategory {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      name: product.name,
      description: product.description,
      imageUrl: product.imageUrl,
      publishDate: product.publishDate ? product.publishDate.format(DATE_TIME_FORMAT) : null,
      status: product.status,
      type: product.type,
      totalChapter: product.totalChapter,
      author: product.author,
      categories: product.categories,
    });

    this.authorsCollection = this.authorService.addAuthorToCollectionIfMissing(this.authorsCollection, product.author);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      ...(product.categories ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.authorService
      .query({ filter: 'product-is-null' })
      .pipe(map((res: HttpResponse<IAuthor[]>) => res.body ?? []))
      .pipe(map((authors: IAuthor[]) => this.authorService.addAuthorToCollectionIfMissing(authors, this.editForm.get('author')!.value)))
      .subscribe((authors: IAuthor[]) => (this.authorsCollection = authors));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, ...(this.editForm.get('categories')!.value ?? []))
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      publishDate: this.editForm.get(['publishDate'])!.value
        ? dayjs(this.editForm.get(['publishDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      type: this.editForm.get(['type'])!.value,
      totalChapter: this.editForm.get(['totalChapter'])!.value,
      author: this.editForm.get(['author'])!.value,
      categories: this.editForm.get(['categories'])!.value,
    };
  }
}
