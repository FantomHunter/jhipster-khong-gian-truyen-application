jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DownloadService } from '../service/download.service';
import { IDownload, Download } from '../download.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IResourceDownload } from 'app/entities/resource-download/resource-download.model';
import { ResourceDownloadService } from 'app/entities/resource-download/service/resource-download.service';

import { DownloadUpdateComponent } from './download-update.component';

describe('Download Management Update Component', () => {
  let comp: DownloadUpdateComponent;
  let fixture: ComponentFixture<DownloadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let downloadService: DownloadService;
  let userService: UserService;
  let resourceDownloadService: ResourceDownloadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DownloadUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DownloadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DownloadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    downloadService = TestBed.inject(DownloadService);
    userService = TestBed.inject(UserService);
    resourceDownloadService = TestBed.inject(ResourceDownloadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const download: IDownload = { id: 456 };
      const user: IUser = { id: 20704 };
      download.user = user;

      const userCollection: IUser[] = [{ id: 48452 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ download });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ResourceDownload query and add missing value', () => {
      const download: IDownload = { id: 456 };
      const resource: IResourceDownload = { id: 41880 };
      download.resource = resource;

      const resourceDownloadCollection: IResourceDownload[] = [{ id: 10595 }];
      jest.spyOn(resourceDownloadService, 'query').mockReturnValue(of(new HttpResponse({ body: resourceDownloadCollection })));
      const additionalResourceDownloads = [resource];
      const expectedCollection: IResourceDownload[] = [...additionalResourceDownloads, ...resourceDownloadCollection];
      jest.spyOn(resourceDownloadService, 'addResourceDownloadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ download });
      comp.ngOnInit();

      expect(resourceDownloadService.query).toHaveBeenCalled();
      expect(resourceDownloadService.addResourceDownloadToCollectionIfMissing).toHaveBeenCalledWith(
        resourceDownloadCollection,
        ...additionalResourceDownloads
      );
      expect(comp.resourceDownloadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const download: IDownload = { id: 456 };
      const user: IUser = { id: 16523 };
      download.user = user;
      const resource: IResourceDownload = { id: 63726 };
      download.resource = resource;

      activatedRoute.data = of({ download });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(download));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.resourceDownloadsSharedCollection).toContain(resource);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Download>>();
      const download = { id: 123 };
      jest.spyOn(downloadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ download });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: download }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(downloadService.update).toHaveBeenCalledWith(download);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Download>>();
      const download = new Download();
      jest.spyOn(downloadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ download });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: download }));
      saveSubject.complete();

      // THEN
      expect(downloadService.create).toHaveBeenCalledWith(download);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Download>>();
      const download = { id: 123 };
      jest.spyOn(downloadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ download });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(downloadService.update).toHaveBeenCalledWith(download);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackResourceDownloadById', () => {
      it('Should return tracked ResourceDownload primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackResourceDownloadById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
