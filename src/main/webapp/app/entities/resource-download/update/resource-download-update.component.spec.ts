jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResourceDownloadService } from '../service/resource-download.service';
import { IResourceDownload, ResourceDownload } from '../resource-download.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ResourceDownloadUpdateComponent } from './resource-download-update.component';

describe('ResourceDownload Management Update Component', () => {
  let comp: ResourceDownloadUpdateComponent;
  let fixture: ComponentFixture<ResourceDownloadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceDownloadService: ResourceDownloadService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ResourceDownloadUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ResourceDownloadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceDownloadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceDownloadService = TestBed.inject(ResourceDownloadService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const resourceDownload: IResourceDownload = { id: 456 };
      const product: IProduct = { id: 46009 };
      resourceDownload.product = product;

      const productCollection: IProduct[] = [{ id: 63251 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resourceDownload });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resourceDownload: IResourceDownload = { id: 456 };
      const product: IProduct = { id: 85549 };
      resourceDownload.product = product;

      activatedRoute.data = of({ resourceDownload });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(resourceDownload));
      expect(comp.productsSharedCollection).toContain(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResourceDownload>>();
      const resourceDownload = { id: 123 };
      jest.spyOn(resourceDownloadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceDownload });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceDownload }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceDownloadService.update).toHaveBeenCalledWith(resourceDownload);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResourceDownload>>();
      const resourceDownload = new ResourceDownload();
      jest.spyOn(resourceDownloadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceDownload });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceDownload }));
      saveSubject.complete();

      // THEN
      expect(resourceDownloadService.create).toHaveBeenCalledWith(resourceDownload);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ResourceDownload>>();
      const resourceDownload = { id: 123 };
      jest.spyOn(resourceDownloadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceDownload });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceDownloadService.update).toHaveBeenCalledWith(resourceDownload);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
